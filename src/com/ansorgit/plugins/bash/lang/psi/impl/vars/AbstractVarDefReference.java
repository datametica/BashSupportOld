package com.ansorgit.plugins.bash.lang.psi.impl.vars;

import com.ansorgit.plugins.bash.lang.psi.api.BashReference;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.resolve.reference.impl.CachingReference;
import com.intellij.refactoring.rename.BindablePsiReference;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;


















abstract class AbstractVarDefReference
  extends CachingReference
  implements BashReference, BindablePsiReference
{
  protected final BashVarDefImpl bashVarDef;
  
  public AbstractVarDefReference(BashVarDefImpl bashVarDef) {
    this.bashVarDef = bashVarDef;
  }

  
  public String getReferencedName() {
    return this.bashVarDef.getReferenceName();
  }

  
  public PsiElement getElement() {
    return (PsiElement)this.bashVarDef;
  }

  
  public TextRange getRangeInElement() {
    return this.bashVarDef.getAssignmentNameTextRange();
  }

  
  @NotNull
  public String getCanonicalText() {
    if (this.bashVarDef.getReferenceName() == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/vars/AbstractVarDefReference", "getCanonicalText" }));  return this.bashVarDef.getReferenceName();
  }

  
  public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
    this.bashVarDef.setName(newElementName);
    return (PsiElement)this.bashVarDef;
  }

  
  public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException {
    if (element == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "element", "com/ansorgit/plugins/bash/lang/psi/impl/vars/AbstractVarDefReference", "bindToElement" }));  if (isReferenceTo(element)) {
      return (PsiElement)this.bashVarDef;
    }

    
    return handleElementRename(element.getText());
  }

  
  @NotNull
  public Object[] getVariants() {
    if (EMPTY_ARRAY == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/vars/AbstractVarDefReference", "getVariants" }));  return (Object[])EMPTY_ARRAY;
  }
}
