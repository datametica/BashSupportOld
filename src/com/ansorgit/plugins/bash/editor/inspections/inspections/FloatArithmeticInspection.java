package com.ansorgit.plugins.bash.editor.inspections.inspections;

import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.arithmetic.ArithmeticExpression;
import com.ansorgit.plugins.bash.lang.psi.api.arithmetic.ProductExpression;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;





















public class FloatArithmeticInspection
  extends LocalInspectionTool
{
  @NotNull
  public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
    if (holder == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "holder", "com/ansorgit/plugins/bash/editor/inspections/inspections/FloatArithmeticInspection", "buildVisitor" }));  if (new BashVisitor()
      {
        public void visitArithmeticExpression(ArithmeticExpression expression) {
          if (!expression.isStatic()) {
            return;
          }
          
          if (expression instanceof ProductExpression)
          { ProductExpression product = (ProductExpression)expression;
            
            if (product.hasDivisionRemainder())
              holder.registerProblem((PsiElement)expression, "Integer division with remainder found.", new com.intellij.codeInspection.LocalQuickFix[0]);  }  } } == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/inspections/inspections/FloatArithmeticInspection", "buildVisitor" }));  return (PsiElementVisitor)new BashVisitor() { public void visitArithmeticExpression(ArithmeticExpression expression) { if (!expression.isStatic()) return;  if (expression instanceof ProductExpression) { ProductExpression product = (ProductExpression)expression; if (product.hasDivisionRemainder()) holder.registerProblem((PsiElement)expression, "Integer division with remainder found.", new com.intellij.codeInspection.LocalQuickFix[0]);  }
           }
         }
      ;
  }
}
