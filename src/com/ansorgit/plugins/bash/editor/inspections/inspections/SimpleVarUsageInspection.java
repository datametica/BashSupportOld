package com.ansorgit.plugins.bash.editor.inspections.inspections;

import com.ansorgit.plugins.bash.editor.inspections.quickfix.ReplaceVarWithParamExpansionQuickfix;
import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVar;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

















public class SimpleVarUsageInspection
  extends LocalInspectionTool
{
  @NotNull
  public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
    if (holder == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "holder", "com/ansorgit/plugins/bash/editor/inspections/inspections/SimpleVarUsageInspection", "buildVisitor" }));  if (new BashVisitor()
      {
        public void visitVarUse(BashVar var) {
          if (var.getTextLength() != 0 && ReplaceVarWithParamExpansionQuickfix.isAvailableAt(var))
            holder.registerProblem((PsiElement)var, "Simple variable usage", new LocalQuickFix[] { (LocalQuickFix)new ReplaceVarWithParamExpansionQuickfix(var) });  } } == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/inspections/inspections/SimpleVarUsageInspection", "buildVisitor" }));  return (PsiElementVisitor)new BashVisitor() { public void visitVarUse(BashVar var) { if (var.getTextLength() != 0 && ReplaceVarWithParamExpansionQuickfix.isAvailableAt(var)) holder.registerProblem((PsiElement)var, "Simple variable usage", new LocalQuickFix[] { (LocalQuickFix)new ReplaceVarWithParamExpansionQuickfix(var) });  }
         }
      ;
  }
}
