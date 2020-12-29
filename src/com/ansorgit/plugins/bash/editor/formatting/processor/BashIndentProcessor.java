package com.ansorgit.plugins.bash.editor.formatting.processor;

import com.ansorgit.plugins.bash.editor.formatting.BashBlock;
import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.ansorgit.plugins.bash.lang.parser.BashElementTypes;
import com.intellij.formatting.Indent;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;





















public abstract class BashIndentProcessor
  implements BashElementTypes, BashTokenTypes
{
  private static final TokenSet BLOCKS = TokenSet.create(new IElementType[] { GROUP_COMMAND, GROUP_ELEMENT, CASE_PATTERN_LIST_ELEMENT, LOGICAL_BLOCK_ELEMENT });








  
  @NotNull
  public static Indent getChildIndent(@NotNull BashBlock parent, @Nullable ASTNode prevChildNode, @NotNull ASTNode child) {
    if (parent == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "parent", "com/ansorgit/plugins/bash/editor/formatting/processor/BashIndentProcessor", "getChildIndent" }));  if (child == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "child", "com/ansorgit/plugins/bash/editor/formatting/processor/BashIndentProcessor", "getChildIndent" }));  ASTNode node = parent.getNode();
    IElementType nodeType = node.getElementType();
    PsiElement psiElement = node.getPsi();

    
    if (psiElement instanceof com.ansorgit.plugins.bash.lang.psi.api.BashFile) {
      if (Indent.getNoneIndent() == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/formatting/processor/BashIndentProcessor", "getChildIndent" }));  return Indent.getNoneIndent();
    } 
    
    if (BLOCKS.contains(nodeType)) {
      if (indentForBlock(psiElement, child) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/formatting/processor/BashIndentProcessor", "getChildIndent" }));  return indentForBlock(psiElement, child);
    } 

    
    ASTNode parentNode = node.getTreeParent();
    if (parentNode != null && parentNode.getElementType() == SUBSHELL_COMMAND && 
      parentNode.getTreeParent() != null && parentNode.getTreeParent().getElementType() == FUNCTION_DEF_COMMAND) {
      if (Indent.getNormalIndent() == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/formatting/processor/BashIndentProcessor", "getChildIndent" }));  return Indent.getNormalIndent();
    } 

    
    if (Indent.getNoneIndent() == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/formatting/processor/BashIndentProcessor", "getChildIndent" }));  return Indent.getNoneIndent();
  }








  
  private static Indent indentForBlock(PsiElement psiBlock, ASTNode child) {
    if (LEFT_CURLY.equals(child.getElementType()) || RIGHT_CURLY.equals(child.getElementType())) {
      return Indent.getNoneIndent();
    }
    
    return Indent.getNormalIndent();
  }
}
