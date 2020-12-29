package com.ansorgit.plugins.bash.lang.psi.impl.vars;

import com.ansorgit.plugins.bash.lang.psi.api.BashReference;
import com.ansorgit.plugins.bash.lang.psi.util.BashIdentifierUtil;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiElementFactory;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.resolve.reference.impl.CachingReference;
import com.intellij.refactoring.rename.BindablePsiReference;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;


















abstract class AbstractBashVarReference
  extends CachingReference
  implements BashReference, BindablePsiReference
{
  protected final BashVarImpl bashVar;
  
  public AbstractBashVarReference(BashVarImpl bashVar) {
    this.bashVar = bashVar;
  }

  
  public PsiElement getElement() {
    return (PsiElement)this.bashVar;
  }

  
  public boolean isReferenceTo(PsiElement element) {
    return super.isReferenceTo(element);
  }

  
  public TextRange getRangeInElement() {
    return this.bashVar.getNameTextRange();
  }

  
  @NotNull
  public String getCanonicalText() {
    if (this.bashVar.getReferenceName() == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/vars/AbstractBashVarReference", "getCanonicalText" }));  return this.bashVar.getReferenceName();
  }
  
  public PsiElement handleElementRename(String newName) throws IncorrectOperationException {
    if (!BashIdentifierUtil.isValidNewVariableName(newName)) {
      throw new IncorrectOperationException("Invalid variable name");
    }

    
    if (this.bashVar.getPrefixLength() == 0) {
      return BashPsiUtils.replaceElement((PsiElement)this.bashVar, BashPsiElementFactory.createVariable(this.bashVar.getProject(), newName, true));
    }
    
    return BashPsiUtils.replaceElement((PsiElement)this.bashVar, BashPsiElementFactory.createVariable(this.bashVar.getProject(), newName, false));
  }

  
  public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException {
    if (element == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "element", "com/ansorgit/plugins/bash/lang/psi/impl/vars/AbstractBashVarReference", "bindToElement" }));  return handleElementRename(element.getText());
  }

  
  @NotNull
  public Object[] getVariants() {
    if (EMPTY_ARRAY == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/vars/AbstractBashVarReference", "getVariants" }));  return (Object[])EMPTY_ARRAY;
  }

  
  public String getReferencedName() {
    return this.bashVar.getReferenceName();
  }
}
