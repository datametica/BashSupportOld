package com.ansorgit.plugins.bash.editor.formatting.noOpModel;

import com.intellij.formatting.Alignment;
import com.intellij.formatting.Block;
import com.intellij.formatting.ChildAttributes;
import com.intellij.formatting.Indent;
import com.intellij.formatting.Spacing;
import com.intellij.formatting.Wrap;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.NotNull;


















public class NoOpBlock
  implements Block
{
  private final ASTNode astNode;
  
  public NoOpBlock(ASTNode astNode) {
    this.astNode = astNode;
  }
  
  @NotNull
  public TextRange getTextRange() {
    if (this.astNode.getTextRange() == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/formatting/noOpModel/NoOpBlock", "getTextRange" }));  return this.astNode.getTextRange();
  }
  
  @NotNull
  public List<Block> getSubBlocks() {
    if (Collections.emptyList() == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/formatting/noOpModel/NoOpBlock", "getSubBlocks" }));  return (List)Collections.emptyList();
  }
  
  public Wrap getWrap() {
    return null;
  }
  
  public Indent getIndent() {
    return null;
  }
  
  public Alignment getAlignment() {
    return null;
  }
  
  public Spacing getSpacing(Block child1, Block child2) {
    return null;
  }
  
  @NotNull
  public ChildAttributes getChildAttributes(int newChildIndex) {
    if (new ChildAttributes(Indent.getNoneIndent(), Alignment.createAlignment()) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/formatting/noOpModel/NoOpBlock", "getChildAttributes" }));  return new ChildAttributes(Indent.getNoneIndent(), Alignment.createAlignment());
  }

  
  public boolean isIncomplete() {
    return true;
  }
  
  public boolean isLeaf() {
    return true;
  }
}
