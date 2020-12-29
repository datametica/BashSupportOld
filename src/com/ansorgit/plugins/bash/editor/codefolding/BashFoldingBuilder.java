package com.ansorgit.plugins.bash.editor.codefolding;

import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.ansorgit.plugins.bash.lang.parser.BashElementTypes;
import com.google.common.collect.Lists;
import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilder;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiTreeUtil;
import java.util.List;
import org.jetbrains.annotations.NotNull;























public class BashFoldingBuilder
  implements FoldingBuilder, BashElementTypes
{
  private static final TokenSet foldableTokens = TokenSet.create(new IElementType[] { GROUP_COMMAND, CASE_PATTERN_LIST_ELEMENT, GROUP_ELEMENT, LOGICAL_BLOCK_ELEMENT, SUBSHELL_COMMAND });
  private static final TokenSet startLogicalBlockTokens = TokenSet.create(new IElementType[] { BashTokenTypes.THEN_KEYWORD, BashTokenTypes.DO_KEYWORD });
  private static final TokenSet endLogicalBlockTokens = TokenSet.create(new IElementType[] { BashTokenTypes.FI_KEYWORD, BashTokenTypes.DONE_KEYWORD });
  
  @NotNull
  public FoldingDescriptor[] buildFoldRegions(@NotNull ASTNode node, @NotNull Document document) {
    if (node == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "node", "com/ansorgit/plugins/bash/editor/codefolding/BashFoldingBuilder", "buildFoldRegions" }));  if (document == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "document", "com/ansorgit/plugins/bash/editor/codefolding/BashFoldingBuilder", "buildFoldRegions" }));  List<FoldingDescriptor> descriptors = Lists.newArrayList();
    
    appendDescriptors(node, document, descriptors);
    
    if ((FoldingDescriptor[])descriptors.toArray(new FoldingDescriptor[descriptors.size()]) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/codefolding/BashFoldingBuilder", "buildFoldRegions" }));  return descriptors.toArray(new FoldingDescriptor[descriptors.size()]);
  }

  
  public String getPlaceholderText(@NotNull ASTNode node) {
    if (node == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "node", "com/ansorgit/plugins/bash/editor/codefolding/BashFoldingBuilder", "getPlaceholderText" }));  IElementType type = node.getElementType();
    
    if (!isFoldable(node)) {
      return null;
    }
    
    if (type == HEREDOC_CONTENT_ELEMENT) {
      return "...";
    }
    
    if (type == SUBSHELL_COMMAND) {
      return "(...)";
    }
    
    if (isFoldableBlock(node, BashTokenTypes.THEN_KEYWORD)) {
      return "then...fi";
    }
    
    if (isFoldableBlock(node, BashTokenTypes.DO_KEYWORD)) {
      return "do...done";
    }
    
    return "{...}";
  }
  
  private boolean isFoldableBlock(ASTNode node, IElementType keyword) {
    if (node.getElementType() == LOGICAL_BLOCK_ELEMENT) {
      PsiElement prev = PsiTreeUtil.prevVisibleLeaf(node.getPsi());
      return (prev != null && prev
        .getNode().getElementType() == keyword);
    } 
    return false;
  }
  
  public boolean isCollapsedByDefault(@NotNull ASTNode node) {
    if (node == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "node", "com/ansorgit/plugins/bash/editor/codefolding/BashFoldingBuilder", "isCollapsedByDefault" }));  return false;
  }
  
  private static ASTNode appendDescriptors(ASTNode node, Document document, List<FoldingDescriptor> descriptors) {
    if (isFoldable(node)) {
      IElementType type = node.getElementType();
      
      int startLine = document.getLineNumber(node.getStartOffset());
      
      TextRange adjustedFoldingRange = adjustFoldingRange(node);
      int endLine = document.getLineNumber(adjustedFoldingRange.getEndOffset());
      
      if (startLine + minumumLineOffset(type) <= endLine) {
        descriptors.add(new FoldingDescriptor(node, adjustedFoldingRange));
      }
    } 
    
    if (mayContainFoldBlocks(node)) {
      
      ASTNode child = node.getFirstChildNode();
      while (child != null) {
        child = appendDescriptors(child, document, descriptors).getTreeNext();
      }
    } 
    
    return node;
  }
  
  private static TextRange adjustFoldingRange(ASTNode node) {
    if (node.getElementType() == HEREDOC_CONTENT_ELEMENT) {
      int startOffset = node.getStartOffset();

      
      for (ASTNode next = node.getTreeNext(); next != null; next = next.getTreeNext()) {
        IElementType elementType = next.getElementType();
        if (elementType == BashElementTypes.HEREDOC_END_ELEMENT || elementType == BashElementTypes.HEREDOC_END_IGNORING_TABS_ELEMENT) {
          return TextRange.create(startOffset, next.getStartOffset() - 1);
        }
      } 
    } else if (node.getElementType() == LOGICAL_BLOCK_ELEMENT) {

      
      int startOffset = getStartOffset(node);

      
      for (ASTNode next = node.getTreeNext(); next != null; next = next.getTreeNext()) {
        IElementType elementType = next.getElementType();
        if (endLogicalBlockTokens.contains(elementType)) {
          return TextRange.create(startOffset, next.getTextRange().getEndOffset());
        }
      } 
    } 

    
    return node.getTextRange();
  }
  
  private static int getStartOffset(ASTNode node) {
    for (ASTNode prev = node.getTreePrev(); prev != null; prev = prev.getTreePrev()) {
      IElementType elementType = prev.getElementType();
      if (startLogicalBlockTokens.contains(elementType)) {
        return prev.getStartOffset();
      }
    } 
    return node.getStartOffset();
  }
  
  private static boolean mayContainFoldBlocks(ASTNode node) {
    IElementType type = node.getElementType();
    
    return (type != HEREDOC_CONTENT_ELEMENT);
  }
  
  private static boolean isFoldable(ASTNode node) {
    IElementType type = node.getElementType();
    
    if (type == HEREDOC_CONTENT_ELEMENT) {

      
      ASTNode prev = node.getTreePrev();
      if (prev != null && prev.getElementType() == BashTokenTypes.LINE_FEED)
      {
        return true;
      }
    } 
    
    return foldableTokens.contains(type);
  }
  
  private static int minumumLineOffset(IElementType type) {
    return 2;
  }
}
