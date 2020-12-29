package com.ansorgit.plugins.bash.editor.inspections.inspections;

import com.ansorgit.plugins.bash.editor.inspections.quickfix.EvaluateArithExprQuickfix;
import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.arithmetic.ArithmeticExpression;
import com.ansorgit.plugins.bash.lang.psi.impl.arithmetic.InvalidExpressionValue;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import java.util.List;
import org.jetbrains.annotations.NotNull;




















public class EvaluateArithmeticExpressionInspection
  extends LocalInspectionTool
{
  @NotNull
  public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, final boolean isOnTheFly) {
    if (holder == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "holder", "com/ansorgit/plugins/bash/editor/inspections/inspections/EvaluateArithmeticExpressionInspection", "buildVisitor" }));  if (new BashVisitor()
      {
        public void visitArithmeticExpression(ArithmeticExpression expression) {
          if (!isOnTheFly) {
            return;
          }
          
          List<ArithmeticExpression> subexpressions = expression.subexpressions();
          
          if (subexpressions.isEmpty() && expression.isStatic())
          
          { try {
              expression.computeNumericValue();
            } catch (InvalidExpressionValue e) {
              holder.registerProblem((PsiElement)expression, e.getMessage(), ProblemHighlightType.GENERIC_ERROR, null, LocalQuickFix.EMPTY_ARRAY);
            }  }
          else if (subexpressions.size() > 1 && expression.isStatic())
          { 
            try { long value = expression.computeNumericValue();

              
              ArithmeticExpression parent = expression.findParentExpression();
              if (parent == null || !parent.isStatic()) {
                String template = "Replace '" + expression.getText() + "' with the evaluated result of '" + value + "'";
                holder.registerProblem((PsiElement)expression, template, new LocalQuickFix[] { (LocalQuickFix)new EvaluateArithExprQuickfix(expression) });
              }  }
            catch (InvalidExpressionValue e)
            { boolean errors = false;
              for (int i = 0; i < subexpressions.size() && !errors; i++) {
                try {
                  ((ArithmeticExpression)subexpressions.get(i)).computeNumericValue();
                } catch (InvalidExpressionValue invalidExpressionValue1) {
                  errors = true;
                } 
              } 

              
              if (!errors)
                holder.registerProblem((PsiElement)expression, e.getMessage(), ProblemHighlightType.GENERIC_ERROR, null, LocalQuickFix.EMPTY_ARRAY);  }  }  } } == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/inspections/inspections/EvaluateArithmeticExpressionInspection", "buildVisitor" }));  return (PsiElementVisitor)new BashVisitor() { public void visitArithmeticExpression(ArithmeticExpression expression) { if (!isOnTheFly) return;  List<ArithmeticExpression> subexpressions = expression.subexpressions(); if (subexpressions.isEmpty() && expression.isStatic()) { try { expression.computeNumericValue(); } catch (InvalidExpressionValue e) { holder.registerProblem((PsiElement)expression, e.getMessage(), ProblemHighlightType.GENERIC_ERROR, null, LocalQuickFix.EMPTY_ARRAY); }  } else if (subexpressions.size() > 1 && expression.isStatic()) { try { long value = expression.computeNumericValue(); ArithmeticExpression parent = expression.findParentExpression(); if (parent == null || !parent.isStatic()) { String template = "Replace '" + expression.getText() + "' with the evaluated result of '" + value + "'"; holder.registerProblem((PsiElement)expression, template, new LocalQuickFix[] { (LocalQuickFix)new EvaluateArithExprQuickfix(expression) }); }  } catch (InvalidExpressionValue e) { boolean errors = false; for (int i = 0; i < subexpressions.size() && !errors; i++) { try { ((ArithmeticExpression)subexpressions.get(i)).computeNumericValue(); } catch (InvalidExpressionValue invalidExpressionValue1) { errors = true; }  }  if (!errors) holder.registerProblem((PsiElement)expression, e.getMessage(), ProblemHighlightType.GENERIC_ERROR, null, LocalQuickFix.EMPTY_ARRAY);  }
             }
           }
         }
      ;
  }
}
