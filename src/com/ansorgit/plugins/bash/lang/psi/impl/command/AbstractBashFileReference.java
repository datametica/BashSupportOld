package com.ansorgit.plugins.bash.lang.psi.impl.command;

import com.ansorgit.plugins.bash.lang.psi.api.BashFile;
import com.ansorgit.plugins.bash.lang.psi.api.BashReference;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiFileUtils;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.ElementManipulator;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.resolve.reference.impl.CachingReference;
import com.intellij.refactoring.rename.BindablePsiReference;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

















abstract class AbstractBashFileReference
  extends CachingReference
  implements BashReference, BindablePsiReference
{
  protected final AbstractBashCommand<?> cmd;
  
  public AbstractBashFileReference(AbstractBashCommand<?> cmd) {
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
    
    if (manipulator == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/command/AbstractBashFileReference", "getManipulator" }));  return manipulator;
  }

  
  @NotNull
  public String getCanonicalText() {
    String referencedName = this.cmd.getReferencedCommandName();
    if (((referencedName != null) ? referencedName : "") == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/command/AbstractBashFileReference", "getCanonicalText" }));  return (referencedName != null) ? referencedName : "";
  }

  
  public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
    return getManipulator().handleContentChange((PsiElement)this.cmd, newElementName);
  }

  
  public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException {
    if (element == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "element", "com/ansorgit/plugins/bash/lang/psi/impl/command/AbstractBashFileReference", "bindToElement" }));  if (!element.isWritable()) {
      throw new IncorrectOperationException("bindToElement not possible for read-only element: " + element);
    }
    
    if (element instanceof PsiFile) {
      
      BashFile bashFile = this.cmd.getContainingFile();
      if (!bashFile.isWritable()) {
        throw new IncorrectOperationException("bindToElement not possible for read-only file: " + bashFile.getName());
      }
      
      String relativeFilePath = BashPsiFileUtils.findRelativeFilePath((PsiFile)bashFile, (PsiFile)element);
      
      return handleElementRename(relativeFilePath);
    } 
    
    throw new IncorrectOperationException("unsupported for element " + element);
  }

  
  @NotNull
  public Object[] getVariants() {
    if (EMPTY_ARRAY == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/command/AbstractBashFileReference", "getVariants" }));  return (Object[])EMPTY_ARRAY;
  }
}
