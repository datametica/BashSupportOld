package com.ansorgit.plugins.bash.editor.inspections.inspections;

import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.codeInspection.ex.UnfairLocalInspectionTool;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;



















public class UnusedFunctionDefInspection
  extends LocalInspectionTool
  implements UnfairLocalInspectionTool
{
  public static final String ID = "BashUnusedFunction";
  
  @NotNull
  public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
    if (holder == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "holder", "com/ansorgit/plugins/bash/editor/inspections/inspections/UnusedFunctionDefInspection", "buildVisitor" }));  if (new BashVisitor() {  } == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/inspections/inspections/UnusedFunctionDefInspection", "buildVisitor" }));  return (PsiElementVisitor)new BashVisitor() {
      
      };
  }
}
