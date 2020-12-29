package com.ansorgit.plugins.bash.editor.inspections.inspections;

import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.expression.BashFiledescriptor;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;






















public class UnknownFiledescriptorInspection
  extends LocalInspectionTool
{
  @NotNull
  public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
    if (holder == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "holder", "com/ansorgit/plugins/bash/editor/inspections/inspections/UnknownFiledescriptorInspection", "buildVisitor" }));  if (new BashVisitor()
      {
        public void visitFiledescriptor(BashFiledescriptor descriptor) {
          Integer asInt = descriptor.descriptorAsInt();
          if (asInt != null && (
            asInt.intValue() < 0 || asInt.intValue() > 9))
            holder.registerProblem((PsiElement)descriptor, BashPsiUtils.rangeInParent((PsiElement)descriptor, (PsiElement)descriptor), "Invalid file descriptor " + asInt, new com.intellij.codeInspection.LocalQuickFix[0]);  } } == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/inspections/inspections/UnknownFiledescriptorInspection", "buildVisitor" }));  return (PsiElementVisitor)new BashVisitor() { public void visitFiledescriptor(BashFiledescriptor descriptor) { Integer asInt = descriptor.descriptorAsInt(); if (asInt != null && (asInt.intValue() < 0 || asInt.intValue() > 9)) holder.registerProblem((PsiElement)descriptor, BashPsiUtils.rangeInParent((PsiElement)descriptor, (PsiElement)descriptor), "Invalid file descriptor " + asInt, new com.intellij.codeInspection.LocalQuickFix[0]);  }
         }
      ;
  }
}
