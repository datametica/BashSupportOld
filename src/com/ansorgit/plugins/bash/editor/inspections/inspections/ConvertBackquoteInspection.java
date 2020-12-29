package com.ansorgit.plugins.bash.editor.inspections.inspections;

import com.ansorgit.plugins.bash.editor.inspections.quickfix.BackquoteQuickfix;
import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.BashBackquote;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;


















public class ConvertBackquoteInspection
  extends LocalInspectionTool
{
  @NotNull
  public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder problemsHolder, final boolean isOnTheFly) {
    if (problemsHolder == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "problemsHolder", "com/ansorgit/plugins/bash/editor/inspections/inspections/ConvertBackquoteInspection", "buildVisitor" }));  if (new BashVisitor()
      {
        public void visitBackquoteCommand(BashBackquote backquote) {
          if (isOnTheFly)
            problemsHolder.registerProblem((PsiElement)backquote, "Replace with subshell", new LocalQuickFix[] { (LocalQuickFix)new BackquoteQuickfix(backquote) });  } } == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/inspections/inspections/ConvertBackquoteInspection", "buildVisitor" }));  return (PsiElementVisitor)new BashVisitor() { public void visitBackquoteCommand(BashBackquote backquote) { if (isOnTheFly) problemsHolder.registerProblem((PsiElement)backquote, "Replace with subshell", new LocalQuickFix[] { (LocalQuickFix)new BackquoteQuickfix(backquote) });  }
         }
      ;
  }
}
