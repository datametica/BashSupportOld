package com.ansorgit.plugins.bash.editor.inspections.inspections;

import com.ansorgit.plugins.bash.editor.inspections.quickfix.EvaluateExpansionQuickfix;
import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.word.BashExpansion;
import com.ansorgit.plugins.bash.settings.BashProjectSettings;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;




















public class EvaluateExpansionInspection
  extends LocalInspectionTool
{
  @NotNull
  public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, final boolean isOnTheFly) {
    if (holder == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "holder", "com/ansorgit/plugins/bash/editor/inspections/inspections/EvaluateExpansionInspection", "buildVisitor" }));  if (new BashVisitor()
      {
        public void visitExpansion(BashExpansion expansion) {
          if (isOnTheFly && expansion.isValidExpansion())
          { boolean bash4 = BashProjectSettings.storedSettings(holder.getProject()).isSupportBash4();
            holder.registerProblem((PsiElement)expansion, "Evaluate expansion", new LocalQuickFix[] { (LocalQuickFix)new EvaluateExpansionQuickfix(expansion, bash4) }); }  } } == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/inspections/inspections/EvaluateExpansionInspection", "buildVisitor" }));  return (PsiElementVisitor)new BashVisitor() { public void visitExpansion(BashExpansion expansion) { if (isOnTheFly && expansion.isValidExpansion()) { boolean bash4 = BashProjectSettings.storedSettings(holder.getProject()).isSupportBash4(); holder.registerProblem((PsiElement)expansion, "Evaluate expansion", new LocalQuickFix[] { (LocalQuickFix)new EvaluateExpansionQuickfix(expansion, bash4) }); }
           }
         }
      ;
  }
}
