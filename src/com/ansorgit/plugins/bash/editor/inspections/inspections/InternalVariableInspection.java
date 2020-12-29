package com.ansorgit.plugins.bash.editor.inspections.inspections;

import com.ansorgit.plugins.bash.lang.LanguageBuiltins;
import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVarDef;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

















public class InternalVariableInspection
  extends LocalInspectionTool
{
  @NotNull
  public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
    if (holder == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "holder", "com/ansorgit/plugins/bash/editor/inspections/inspections/InternalVariableInspection", "buildVisitor" }));  if (new BashVisitor()
      {
        public void visitVarDef(BashVarDef varDef) {
          String name = varDef.getName();
          
          if (LanguageBuiltins.readonlyShellVars.contains(name))
            holder.registerProblem((PsiElement)varDef, "Built-in shell variable", LocalQuickFix.EMPTY_ARRAY);  } } == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/inspections/inspections/InternalVariableInspection", "buildVisitor" }));  return (PsiElementVisitor)new BashVisitor() { public void visitVarDef(BashVarDef varDef) { String name = varDef.getName(); if (LanguageBuiltins.readonlyShellVars.contains(name)) holder.registerProblem((PsiElement)varDef, "Built-in shell variable", LocalQuickFix.EMPTY_ARRAY);  }
         }
      ;
  }
}
