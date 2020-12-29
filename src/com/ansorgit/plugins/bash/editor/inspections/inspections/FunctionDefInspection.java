package com.ansorgit.plugins.bash.editor.inspections.inspections;

import com.ansorgit.plugins.bash.editor.inspections.quickfix.FunctionBodyQuickfix;
import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.BashBlock;
import com.ansorgit.plugins.bash.lang.psi.api.function.BashFunctionDef;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;



















public class FunctionDefInspection
  extends LocalInspectionTool
{
  @NotNull
  public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, final boolean isOnTheFly) {
    if (holder == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "holder", "com/ansorgit/plugins/bash/editor/inspections/inspections/FunctionDefInspection", "buildVisitor" }));  if (new BashVisitor()
      {
        public void visitFunctionDef(BashFunctionDef functionDef) {
          if (isOnTheFly)
          { BashBlock body = functionDef.functionBody();
            if (body != null && !body.isCommandGroup())
              holder.registerProblem((PsiElement)body, "Wrap function body", new LocalQuickFix[] { (LocalQuickFix)new FunctionBodyQuickfix(functionDef) });  }  } } == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/inspections/inspections/FunctionDefInspection", "buildVisitor" }));  return (PsiElementVisitor)new BashVisitor() { public void visitFunctionDef(BashFunctionDef functionDef) { if (isOnTheFly) { BashBlock body = functionDef.functionBody(); if (body != null && !body.isCommandGroup()) holder.registerProblem((PsiElement)body, "Wrap function body", new LocalQuickFix[] { (LocalQuickFix)new FunctionBodyQuickfix(functionDef) });  }
           }
         }
      ;
  }
}
