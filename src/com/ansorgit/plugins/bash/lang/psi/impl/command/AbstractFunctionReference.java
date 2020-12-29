package com.ansorgit.plugins.bash.lang.psi.impl.command;

import com.ansorgit.plugins.bash.lang.psi.api.BashReference;
import com.ansorgit.plugins.bash.lang.psi.api.function.BashFunctionDef;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.ElementManipulator;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.resolve.reference.impl.CachingReference;
import com.intellij.refactoring.rename.BindablePsiReference;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;


















abstract class AbstractFunctionReference
  extends CachingReference
  implements BashReference, BindablePsiReference
{
  protected final AbstractBashCommand<?> cmd;
  
  public AbstractFunctionReference(AbstractBashCommand<?> cmd) {
    this.cmd = cmd;
  }

  
  public String getReferencedName() {
    return this.cmd.getReferencedCommandName();
  }

  
  public PsiElement getElement() {
    return (PsiElement)this.cmd;
  }

  
  public TextRange getRangeInElement() {
    return getManipulator().getRangeInElement((PsiElement)this.cmd);
  }
  
  @NotNull
  private ElementManipulator<AbstractBashCommand<?>> getManipulator() {
    ElementManipulator<AbstractBashCommand<?>> manipulator = ElementManipulators.getManipulator((PsiElement)this.cmd);
    if (manipulator == null) {
      throw new IncorrectOperationException("No element manipulator found for " + this.cmd);
    }
    if (manipulator == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/command/AbstractFunctionReference", "getManipulator" }));  return manipulator;
  }

  
  @NotNull
  public String getCanonicalText() {
    String referencedName = this.cmd.getReferencedCommandName();
    if (((referencedName != null) ? referencedName : "") == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/command/AbstractFunctionReference", "getCanonicalText" }));  return (referencedName != null) ? referencedName : "";
  }

  
  public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
    return getManipulator().handleContentChange((PsiElement)this.cmd, newElementName);
  }

  
  public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException {
    if (element == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "element", "com/ansorgit/plugins/bash/lang/psi/impl/command/AbstractFunctionReference", "bindToElement" }));  if (element instanceof BashFunctionDef) {
      return handleElementRename(((BashFunctionDef)element).getName());
    }
    
    throw new IncorrectOperationException("unsupported for element " + element);
  }

  
  @NotNull
  public Object[] getVariants() {
    if (EMPTY_ARRAY == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/command/AbstractFunctionReference", "getVariants" }));  return (Object[])EMPTY_ARRAY;
  }
}
