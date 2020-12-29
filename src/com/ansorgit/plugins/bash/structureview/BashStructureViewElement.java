package com.ansorgit.plugins.bash.structureview;

import com.ansorgit.plugins.bash.lang.psi.api.BashPsiElement;
import com.ansorgit.plugins.bash.lang.psi.api.function.BashFunctionDef;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.NavigationItem;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiNamedElement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Icon;
import org.jetbrains.annotations.NotNull;


















class BashStructureViewElement
  implements StructureViewTreeElement
{
  private final PsiElement myElement;
  
  BashStructureViewElement(PsiElement element) {
    this.myElement = element;
  }
  
  public PsiElement getValue() {
    return this.myElement;
  }
  
  @NotNull
  public ItemPresentation getPresentation() {
    if (this.myElement instanceof NavigationItem) {
      ItemPresentation presentation = ((NavigationItem)this.myElement).getPresentation();
      if (presentation != null) {
        if (presentation == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/structureview/BashStructureViewElement", "getPresentation" }));  return presentation;
      } 
    } 

    
    if (new BashItemPresentation() == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/structureview/BashStructureViewElement", "getPresentation" }));  return new BashItemPresentation();
  }
  
  @NotNull
  public TreeElement[] getChildren() {
    final List<BashPsiElement> childrenElements = new ArrayList<>();
    this.myElement.acceptChildren(new PsiElementVisitor() {
          public void visitElement(PsiElement element) {
            if (BashStructureViewElement.this.isBrowsableElement(element)) {
              childrenElements.add((BashPsiElement)element);
            } else {
              element.acceptChildren(this);
            } 
          }
        });
    
    StructureViewTreeElement[] children = new StructureViewTreeElement[childrenElements.size()];
    for (int i = 0; i < children.length; i++) {
      children[i] = new BashStructureViewElement((PsiElement)childrenElements.get(i));
    }
    
    if (children == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/structureview/BashStructureViewElement", "getChildren" }));  return (TreeElement[])children;
  }
  
  private boolean isBrowsableElement(PsiElement element) {
    return (element instanceof BashFunctionDef && ((BashFunctionDef)element).getNameSymbol() != null);
  }
  
  public void navigate(boolean requestFocus) {
    ((NavigationItem)this.myElement).navigate(requestFocus);
  }
  
  public boolean canNavigate() {
    return ((NavigationItem)this.myElement).canNavigateToSource();
  }
  
  public boolean canNavigateToSource() {
    return ((NavigationItem)this.myElement).canNavigateToSource();
  }
  
  private class BashItemPresentation implements ItemPresentation {
    public String getPresentableText() {
      return ((PsiNamedElement)BashStructureViewElement.this.myElement).getName();
    }
    private BashItemPresentation() {}
    public String getLocationString() {
      return null;
    }
    
    public Icon getIcon(boolean open) {
      return null;
    }
  }
}
