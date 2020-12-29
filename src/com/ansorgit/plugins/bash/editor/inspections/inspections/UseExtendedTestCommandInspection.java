package com.ansorgit.plugins.bash.editor.inspections.inspections;

import com.ansorgit.plugins.bash.editor.inspections.quickfix.DoubleBracketsQuickfix;
import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.shell.BashConditionalCommand;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;


















public class UseExtendedTestCommandInspection
  extends LocalInspectionTool
{
  @NotNull
  public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
    if (holder == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "holder", "com/ansorgit/plugins/bash/editor/inspections/inspections/UseExtendedTestCommandInspection", "buildVisitor" }));  if (new ExtendedTestCommandVisitor(isOnTheFly, holder) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/inspections/inspections/UseExtendedTestCommandInspection", "buildVisitor" }));  return (PsiElementVisitor)new ExtendedTestCommandVisitor(isOnTheFly, holder);
  }
  
  static class ExtendedTestCommandVisitor
    extends BashVisitor {
    private static final String DESCRIPTION = "Replace with double brackets";
    private final boolean onTheFly;
    private final ProblemsHolder holder;
    
    ExtendedTestCommandVisitor(boolean onTheFly, ProblemsHolder holder) {
      this.onTheFly = onTheFly;
      this.holder = holder;
    }

    
    public void visitConditional(BashConditionalCommand conditionalCommand) {
      if (this.onTheFly) {
        DoubleBracketsQuickfix quickfix = new DoubleBracketsQuickfix(conditionalCommand);
        this.holder.registerProblem((PsiElement)conditionalCommand, "Replace with double brackets", new LocalQuickFix[] { (LocalQuickFix)quickfix });
      } 
    }
  }
}
