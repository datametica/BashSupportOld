package com.ansorgit.plugins.bash.editor.inspections.inspections;

import com.ansorgit.plugins.bash.lang.LanguageBuiltins;
import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.BashFunctionDefName;
import com.ansorgit.plugins.bash.lang.psi.api.function.BashFunctionDef;
import com.ansorgit.plugins.bash.settings.BashProjectSettings;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

















public class InternalCommandFunctionOverrideInspection
  extends LocalInspectionTool
{
  @NotNull
  public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
    if (holder == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "holder", "com/ansorgit/plugins/bash/editor/inspections/inspections/InternalCommandFunctionOverrideInspection", "buildVisitor" }));  if (new BashVisitor()
      {
        public void visitFunctionDef(BashFunctionDef functionDef) {
          boolean bash4 = BashProjectSettings.storedSettings(holder.getProject()).isSupportBash4();
          
          if (LanguageBuiltins.isInternalCommand(functionDef.getName(), bash4))
          { PsiElement psiElement; BashFunctionDefName bashFunctionDefName = functionDef.getNameSymbol();
            if (bashFunctionDefName == null) {
              psiElement = functionDef.getNavigationElement();
            }
            
            holder.registerProblem(psiElement, "Function overrides internal Bash command", ProblemHighlightType.GENERIC_ERROR_OR_WARNING, new com.intellij.codeInspection.LocalQuickFix[0]); }  } } == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/inspections/inspections/InternalCommandFunctionOverrideInspection", "buildVisitor" }));  return (PsiElementVisitor)new BashVisitor() { public void visitFunctionDef(BashFunctionDef functionDef) { boolean bash4 = BashProjectSettings.storedSettings(holder.getProject()).isSupportBash4(); if (LanguageBuiltins.isInternalCommand(functionDef.getName(), bash4)) { PsiElement psiElement; BashFunctionDefName bashFunctionDefName = functionDef.getNameSymbol(); if (bashFunctionDefName == null) psiElement = functionDef.getNavigationElement();  holder.registerProblem(psiElement, "Function overrides internal Bash command", ProblemHighlightType.GENERIC_ERROR_OR_WARNING, new com.intellij.codeInspection.LocalQuickFix[0]); }
           }
         }
      ;
  }
}
