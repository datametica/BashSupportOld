package com.ansorgit.plugins.bash.editor.formatting.processor;

import com.ansorgit.plugins.bash.editor.formatting.BashBlock;
import com.ansorgit.plugins.bash.editor.formatting.SpacingUtil;
import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.intellij.formatting.Spacing;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.impl.source.SourceTreeToPsiMap;
import com.intellij.psi.impl.source.tree.CompositeElement;



















public class BashSpacingProcessor
  extends BashVisitor
{
  private static final ThreadLocal<BashSpacingProcessor> mySharedProcessorAllocator = new ThreadLocal<>();
  private static final Logger LOG = Logger.getInstance("#SpacingProcessor");
  private MyBashSpacingVisitor myBashSpacingVisitor;
  
  public BashSpacingProcessor(MyBashSpacingVisitor myBashSpacingVisitor) {
    this.myBashSpacingVisitor = myBashSpacingVisitor;
  }
  
  public static Spacing getSpacing(BashBlock child1, BashBlock child2, CodeStyleSettings settings) {
    return getSpacing(child2.getNode(), settings);
  }
  
  private static Spacing getSpacing(ASTNode node, CodeStyleSettings settings) {
    BashSpacingProcessor spacingProcessor = mySharedProcessorAllocator.get();
    try {
      if (spacingProcessor == null) {
        spacingProcessor = new BashSpacingProcessor(new MyBashSpacingVisitor(node, settings));
        mySharedProcessorAllocator.set(spacingProcessor);
      } else {
        spacingProcessor.myBashSpacingVisitor = new MyBashSpacingVisitor(node, settings);
      } 
      
      spacingProcessor.doInit();
      return spacingProcessor.getResult();
    } finally {
      if (spacingProcessor != null) {
        spacingProcessor.clear();
      }
    } 
  }
  
  private void clear() {
    if (this.myBashSpacingVisitor != null) {
      this.myBashSpacingVisitor.clear();
    }
  }
  
  private Spacing getResult() {
    return this.myBashSpacingVisitor.getResult();
  }
  
  private void doInit() {
    this.myBashSpacingVisitor.doInit();
  }
  
  public void setVisitor(MyBashSpacingVisitor visitor) {
    this.myBashSpacingVisitor = visitor;
  }
  
  private static class MyBashSpacingVisitor extends BashVisitor {
    private Spacing result;
    private CodeStyleSettings mySettings;
    private ASTNode myChild2;
    private ASTNode myChild1;
    private PsiElement myParent;
    
    public MyBashSpacingVisitor(ASTNode node, CodeStyleSettings settings) {
      this.mySettings = settings;
      init(node);
    }
    
    private void init(ASTNode child) {
      if (child == null) {
        return;
      }
      ASTNode treePrev = child.getTreePrev();
      while (treePrev != null && SpacingUtil.isWhiteSpace(treePrev)) {
        treePrev = treePrev.getTreePrev();
      }
      
      if (treePrev == null) {
        init(child.getTreeParent());
      } else {
        this.myChild2 = child;
        this.myChild1 = treePrev;
        CompositeElement parent = (CompositeElement)treePrev.getTreeParent();
        this.myParent = SourceTreeToPsiMap.treeElementToPsi((ASTNode)parent);
      } 
    }

    
    public void clear() {}
    
    public Spacing getResult() {
      return this.result;
    }
    
    public void doInit() {}
  }
}
