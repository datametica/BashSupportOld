package com.ansorgit.plugins.bash.editor.inspections.inspections;

import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.BashString;
import com.ansorgit.plugins.bash.lang.psi.api.arithmetic.ArithmeticExpression;
import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVar;
import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVarDef;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;


















public class SimpleArrayUseInspection
  extends LocalInspectionTool
{
  @NotNull
  public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
    if (holder == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "holder", "com/ansorgit/plugins/bash/editor/inspections/inspections/SimpleArrayUseInspection", "buildVisitor" }));  if (new BashVisitor()
      {
        public void visitVarUse(BashVar var) {
          BashVarDef definition = (BashVarDef)var.getReference().resolve();
          if (definition != null)
          { boolean defIsArray = definition.isArray();
            boolean arrayUse = var.isArrayUse();
            
            if (arrayUse && !defIsArray)
            
            { 
              if (!SimpleArrayUseInspection.isStringLengthExpr(var)) {
                holder.registerProblem((PsiElement)var, "Array use of non-array variable", ProblemHighlightType.WEAK_WARNING, new com.intellij.codeInspection.LocalQuickFix[0]);
              } }
            else if (!arrayUse && defIsArray && 
              
              !BashPsiUtils.hasParentOfType((PsiElement)var, BashString.class, 5) && 
              !BashPsiUtils.hasParentOfType((PsiElement)var, ArithmeticExpression.class, 5))
            
            { holder.registerProblem((PsiElement)var, "Simple use of array variable", ProblemHighlightType.WEAK_WARNING, new com.intellij.codeInspection.LocalQuickFix[0]); }  }  } } == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/inspections/inspections/SimpleArrayUseInspection", "buildVisitor" }));  return (PsiElementVisitor)new BashVisitor() { public void visitVarUse(BashVar var) { BashVarDef definition = (BashVarDef)var.getReference().resolve(); if (definition != null) { boolean defIsArray = definition.isArray(); boolean arrayUse = var.isArrayUse(); if (arrayUse && !defIsArray) { if (!SimpleArrayUseInspection.isStringLengthExpr(var)) holder.registerProblem((PsiElement)var, "Array use of non-array variable", ProblemHighlightType.WEAK_WARNING, new com.intellij.codeInspection.LocalQuickFix[0]);  } else if (!arrayUse && defIsArray && !BashPsiUtils.hasParentOfType((PsiElement)var, BashString.class, 5) && !BashPsiUtils.hasParentOfType((PsiElement)var, ArithmeticExpression.class, 5)) { holder.registerProblem((PsiElement)var, "Simple use of array variable", ProblemHighlightType.WEAK_WARNING, new com.intellij.codeInspection.LocalQuickFix[0]); }
             }
           }
         }
      ;
  }
  
  private static boolean isStringLengthExpr(BashVar var) {
    PsiElement prevLeaf = PsiTreeUtil.prevLeaf((PsiElement)var);
    if (prevLeaf != null && prevLeaf.getNode().getElementType() == BashTokenTypes.PARAM_EXPANSION_OP_HASH) {
      PsiElement nextLeaf = PsiTreeUtil.nextLeaf((PsiElement)var);
      return (nextLeaf == null || nextLeaf.getNode().getElementType() != BashTokenTypes.LEFT_SQUARE);
    } 
    
    return false;
  }
}
