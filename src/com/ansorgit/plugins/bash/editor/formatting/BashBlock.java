package com.ansorgit.plugins.bash.editor.formatting;

import com.ansorgit.plugins.bash.editor.formatting.processor.BashSpacingProcessor;
import com.ansorgit.plugins.bash.editor.formatting.processor.BashSpacingProcessorBasic;
import com.ansorgit.plugins.bash.lang.parser.BashElementTypes;
import com.intellij.formatting.Alignment;
import com.intellij.formatting.Block;
import com.intellij.formatting.ChildAttributes;
import com.intellij.formatting.Indent;
import com.intellij.formatting.Spacing;
import com.intellij.formatting.Wrap;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
























public class BashBlock
  implements Block, BashElementTypes
{
  protected final ASTNode myNode;
  protected final Alignment myAlignment;
  protected final Indent myIndent;
  protected final Wrap myWrap;
  protected final CodeStyleSettings mySettings;
  protected List<Block> mySubBlocks = null;
  
  public BashBlock(@NotNull ASTNode node, @Nullable Alignment alignment, @NotNull Indent indent, @Nullable Wrap wrap, CodeStyleSettings settings) {
    this.myNode = node;
    this.myAlignment = alignment;
    this.myIndent = indent;
    this.myWrap = wrap;
    this.mySettings = settings;
  }
  
  @NotNull
  public ASTNode getNode() {
    if (this.myNode == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/formatting/BashBlock", "getNode" }));  return this.myNode;
  }
  
  @NotNull
  public CodeStyleSettings getSettings() {
    if (this.mySettings == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/formatting/BashBlock", "getSettings" }));  return this.mySettings;
  }
  
  @NotNull
  public TextRange getTextRange() {
    if (this.myNode.getTextRange() == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/formatting/BashBlock", "getTextRange" }));  return this.myNode.getTextRange();
  }
  
  @NotNull
  public List<Block> getSubBlocks() {
    if (this.mySubBlocks == null) {
      this.mySubBlocks = BashBlockGenerator.generateSubBlocks(this.myNode, this.myAlignment, this.myWrap, this.mySettings, this);
    }
    if (this.mySubBlocks == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/formatting/BashBlock", "getSubBlocks" }));  return this.mySubBlocks;
  }
  
  @Nullable
  public Wrap getWrap() {
    return this.myWrap;
  }
  
  @Nullable
  public Indent getIndent() {
    return this.myIndent;
  }
  
  @Nullable
  public Alignment getAlignment() {
    return this.myAlignment;
  }







  
  @Nullable
  public Spacing getSpacing(Block child1, Block child2) {
    if (child1 instanceof BashBlock && child2 instanceof BashBlock) {
      Spacing spacing = BashSpacingProcessor.getSpacing((BashBlock)child1, (BashBlock)child2, this.mySettings);
      return (spacing != null) ? spacing : BashSpacingProcessorBasic.getSpacing((BashBlock)child1, (BashBlock)child2, this.mySettings);
    } 
    return null;
  }
  
  @NotNull
  public ChildAttributes getChildAttributes(int newChildIndex) {
    if (getAttributesByParent() == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/formatting/BashBlock", "getChildAttributes" }));  return getAttributesByParent();
  }
  
  private ChildAttributes getAttributesByParent() {
    ASTNode astNode = this.myNode;
    PsiElement psiParent = astNode.getPsi();
    if (psiParent instanceof com.ansorgit.plugins.bash.lang.psi.api.BashFile) {
      return new ChildAttributes(Indent.getNoneIndent(), null);
    }
    
    return new ChildAttributes(Indent.getNoneIndent(), null);
  }

  
  public boolean isIncomplete() {
    return isIncomplete(this.myNode);
  }




  
  public boolean isIncomplete(@NotNull ASTNode node) {
    if (node == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "node", "com/ansorgit/plugins/bash/editor/formatting/BashBlock", "isIncomplete" }));  if (node.getElementType() instanceof com.intellij.psi.tree.ILazyParseableElementType) {
      return false;
    }
    ASTNode lastChild = node.getLastChildNode();
    while (lastChild != null && 
      !(lastChild.getElementType() instanceof com.intellij.psi.tree.ILazyParseableElementType) && (lastChild
      .getPsi() instanceof com.intellij.psi.PsiWhiteSpace || lastChild.getPsi() instanceof com.intellij.psi.PsiComment)) {
      lastChild = lastChild.getTreePrev();
    }
    return (lastChild != null && (lastChild.getPsi() instanceof com.intellij.psi.PsiErrorElement || isIncomplete(lastChild)));
  }
  
  public boolean isLeaf() {
    return (this.myNode.getFirstChildNode() == null);
  }
}
