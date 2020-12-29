package com.ansorgit.plugins.bash.editor.inspections.inspections;

import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVar;
import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVarDef;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import com.ansorgit.plugins.bash.lang.psi.util.BashResolveUtil;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;



















public class ReadonlyVariableInspection
  extends LocalInspectionTool
{
  @NotNull
  public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
    if (holder == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "holder", "com/ansorgit/plugins/bash/editor/inspections/inspections/ReadonlyVariableInspection", "buildVisitor" }));  if (new BashVisitor()
      {
        public void visitVarDef(BashVarDef visitedVarDef) {
          if (visitedVarDef.isReadonly() || !visitedVarDef.hasAssignmentValue()) {
            return;
          }

          
          String name = visitedVarDef.getName();
          if (name == null) {
            return;
          }
          
          BashResolveUtil.walkVariableDefinitions((BashVar)visitedVarDef, varDef -> { if (varDef != visitedVarDef && !visitedVarDef.isEquivalentTo((PsiElement)varDef) && varDef != null && varDef.isReadonly() && BashPsiUtils.isValidReferenceScope((PsiElement)visitedVarDef, (PsiElement)varDef)) { ReadonlyVariableInspection.this.registerWarning(visitedVarDef, holder); return Boolean.valueOf(false); }  return Boolean.valueOf(true); }); } } == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/inspections/inspections/ReadonlyVariableInspection", "buildVisitor" }));  return (PsiElementVisitor)new BashVisitor() { public void visitVarDef(BashVarDef visitedVarDef) { if (visitedVarDef.isReadonly() || !visitedVarDef.hasAssignmentValue()) return;  String name = visitedVarDef.getName(); if (name == null) return;  BashResolveUtil.walkVariableDefinitions((BashVar)visitedVarDef, varDef -> {
                if (varDef != visitedVarDef && !visitedVarDef.isEquivalentTo((PsiElement)varDef) && varDef != null && varDef.isReadonly() && BashPsiUtils.isValidReferenceScope((PsiElement)visitedVarDef, (PsiElement)varDef)) {
                  ReadonlyVariableInspection.this.registerWarning(visitedVarDef, holder);
                  return Boolean.valueOf(false);
                } 
                return Boolean.valueOf(true);
              }); }
         }
      ;
  }







  
  private void registerWarning(BashVarDef varDef, @NotNull ProblemsHolder holder) {
    if (holder == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "holder", "com/ansorgit/plugins/bash/editor/inspections/inspections/ReadonlyVariableInspection", "registerWarning" }));  holder.registerProblem((PsiElement)varDef, "Change to a read-only variable", LocalQuickFix.EMPTY_ARRAY);
  }
}
