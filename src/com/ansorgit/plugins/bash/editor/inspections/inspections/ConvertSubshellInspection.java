package com.ansorgit.plugins.bash.editor.inspections.inspections;

import com.ansorgit.plugins.bash.editor.inspections.quickfix.SubshellQuickfix;
import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.expression.BashSubshellCommand;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
















public class ConvertSubshellInspection
  extends LocalInspectionTool
{
  @NotNull
  public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, final boolean isOnTheFly) {
    if (holder == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "holder", "com/ansorgit/plugins/bash/editor/inspections/inspections/ConvertSubshellInspection", "buildVisitor" }));  if (new BashVisitor()
      {
        public void visitSubshell(BashSubshellCommand subshellCommand) {
          if (isOnTheFly)
            holder.registerProblem((PsiElement)subshellCommand, "Replace with backquote", new LocalQuickFix[] { (LocalQuickFix)new SubshellQuickfix(subshellCommand) });  } } == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/inspections/inspections/ConvertSubshellInspection", "buildVisitor" }));  return (PsiElementVisitor)new BashVisitor() { public void visitSubshell(BashSubshellCommand subshellCommand) { if (isOnTheFly) holder.registerProblem((PsiElement)subshellCommand, "Replace with backquote", new LocalQuickFix[] { (LocalQuickFix)new SubshellQuickfix(subshellCommand) });  }
         }
      ;
  }
}
