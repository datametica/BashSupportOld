package com.ansorgit.plugins.bash.editor.inspections.inspections;

import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.BashFunctionDefName;
import com.ansorgit.plugins.bash.lang.psi.api.function.BashFunctionDef;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;
















public class FunctionNamesShouldBeLowerSnakeCaseInspection
  extends LocalInspectionTool
{
  private static class FunctionNameVisitor
    extends BashVisitor
  {
    private static final Pattern LOWER_SNAKE_CASE = Pattern.compile("([a-z_0-9]|::)+");
    private final ProblemsHolder holder;
    
    FunctionNameVisitor(ProblemsHolder holder) {
      this.holder = holder;
    }

    
    public void visitFunctionDef(BashFunctionDef functionDef) {
      String functionName = functionDef.getName();
      if (functionName != null && doesNotMatchPattern(functionName)) {
        PsiElement psiElement; BashFunctionDefName bashFunctionDefName = functionDef.getNameSymbol();
        if (bashFunctionDefName == null) {
          psiElement = functionDef.getNavigationElement();
        }
        this.holder.registerProblem(psiElement, "Function name must fit lower snake case", ProblemHighlightType.WEAK_WARNING, new com.intellij.codeInspection.LocalQuickFix[0]);
      } 
    }
    
    private static boolean doesNotMatchPattern(@NotNull CharSequence functionName) {
      if (functionName == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "functionName", "com/ansorgit/plugins/bash/editor/inspections/inspections/FunctionNamesShouldBeLowerSnakeCaseInspection$FunctionNameVisitor", "doesNotMatchPattern" }));  return !LOWER_SNAKE_CASE.matcher(functionName).matches();
    }
  }


  
  @NotNull
  public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
    if (holder == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "holder", "com/ansorgit/plugins/bash/editor/inspections/inspections/FunctionNamesShouldBeLowerSnakeCaseInspection", "buildVisitor" }));  if (new FunctionNameVisitor(holder) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/inspections/inspections/FunctionNamesShouldBeLowerSnakeCaseInspection", "buildVisitor" }));  return (PsiElementVisitor)new FunctionNameVisitor(holder);
  }
}
