package com.ansorgit.plugins.bash.refactoring;

import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import com.intellij.lang.refactoring.RefactoringSupportProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

























public class BashRefactoringSupport
  extends RefactoringSupportProvider
{
  public boolean isInplaceRenameAvailable(@NotNull PsiElement element, PsiElement context) {
    if (element == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "element", "com/ansorgit/plugins/bash/refactoring/BashRefactoringSupport", "isInplaceRenameAvailable" }));  PsiFile fileContext = BashPsiUtils.findFileContext(element);
    if (context == null || fileContext == null || !fileContext.isEquivalentTo((PsiElement)BashPsiUtils.findFileContext(context))) {
      return false;
    }
    
    return (element instanceof com.ansorgit.plugins.bash.lang.psi.api.vars.BashVarDef || element instanceof com.ansorgit.plugins.bash.lang.psi.api.function.BashFunctionDef || element instanceof com.ansorgit.plugins.bash.lang.psi.api.vars.BashVar || element instanceof com.ansorgit.plugins.bash.lang.psi.api.heredoc.BashHereDocMarker || element instanceof com.ansorgit.plugins.bash.lang.psi.api.BashFileReference);
  }
}
