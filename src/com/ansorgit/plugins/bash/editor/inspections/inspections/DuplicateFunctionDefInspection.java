package com.ansorgit.plugins.bash.editor.inspections.inspections;

import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.BashFunctionDefName;
import com.ansorgit.plugins.bash.lang.psi.api.function.BashFunctionDef;
import com.ansorgit.plugins.bash.lang.psi.impl.command.BashFunctionProcessor;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import com.google.common.collect.Lists;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.util.PsiTreeUtil;
import java.util.List;
import org.jetbrains.annotations.NotNull;


















public class DuplicateFunctionDefInspection
  extends LocalInspectionTool
{
  @NotNull
  public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
    if (holder == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "holder", "com/ansorgit/plugins/bash/editor/inspections/inspections/DuplicateFunctionDefInspection", "buildVisitor" }));  if (new BashVisitor()
      {
        public void visitFunctionDef(BashFunctionDef functionDef) {
          BashFunctionProcessor p = new BashFunctionProcessor(functionDef.getName(), true);
          
          boolean isOnGlobalLevel = (BashPsiUtils.findNextVarDefFunctionDefScope((PsiElement)functionDef) == null);

          
          PsiElement start = (functionDef.getContext() != null && !isOnGlobalLevel) ? functionDef.getContext() : functionDef.getPrevSibling();
          
          if (start != null)
          { PsiTreeUtil.treeWalkUp((PsiScopeProcessor)p, start, (PsiElement)functionDef.getContainingFile(), ResolveState.initial());
            
            if (p.hasResults())
            { List<PsiElement> results = (p.getResults() != null) ? Lists.newArrayList(p.getResults()) : Lists.newArrayList();
              results.remove(functionDef);
              
              if (!results.isEmpty())
              
              { PsiElement firstFunctionDef = results.get(0);
                for (PsiElement e : results) {
                  if (e.getTextOffset() < firstFunctionDef.getTextOffset()) {
                    firstFunctionDef = e;
                  }
                } 
                
                if (firstFunctionDef.getTextOffset() < functionDef.getTextOffset())
                { BashFunctionDefName nameSymbol = functionDef.getNameSymbol();
                  
                  if (nameSymbol != null)
                  { String message = String.format("The function '%s' is already defined at line %d.", new Object[] { functionDef
                          .getName(), 
                          Integer.valueOf(BashPsiUtils.getElementLineNumber(firstFunctionDef)) });
                    
                    holder.registerProblem((PsiElement)nameSymbol, message, ProblemHighlightType.GENERIC_ERROR_OR_WARNING, new com.intellij.codeInspection.LocalQuickFix[0]); }  }  }  }  }  } } == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/inspections/inspections/DuplicateFunctionDefInspection", "buildVisitor" }));  return (PsiElementVisitor)new BashVisitor() { public void visitFunctionDef(BashFunctionDef functionDef) { BashFunctionProcessor p = new BashFunctionProcessor(functionDef.getName(), true); boolean isOnGlobalLevel = (BashPsiUtils.findNextVarDefFunctionDefScope((PsiElement)functionDef) == null); PsiElement start = (functionDef.getContext() != null && !isOnGlobalLevel) ? functionDef.getContext() : functionDef.getPrevSibling(); if (start != null) { PsiTreeUtil.treeWalkUp((PsiScopeProcessor)p, start, (PsiElement)functionDef.getContainingFile(), ResolveState.initial()); if (p.hasResults()) { List<PsiElement> results = (p.getResults() != null) ? Lists.newArrayList(p.getResults()) : Lists.newArrayList(); results.remove(functionDef); if (!results.isEmpty()) { PsiElement firstFunctionDef = results.get(0); for (PsiElement e : results) { if (e.getTextOffset() < firstFunctionDef.getTextOffset()) firstFunctionDef = e;  }  if (firstFunctionDef.getTextOffset() < functionDef.getTextOffset()) { BashFunctionDefName nameSymbol = functionDef.getNameSymbol(); if (nameSymbol != null) { String message = String.format("The function '%s' is already defined at line %d.", new Object[] { functionDef.getName(), Integer.valueOf(BashPsiUtils.getElementLineNumber(firstFunctionDef)) }); holder.registerProblem((PsiElement)nameSymbol, message, ProblemHighlightType.GENERIC_ERROR_OR_WARNING, new com.intellij.codeInspection.LocalQuickFix[0]); }
                   }
                 }
               }
             }
           }
         }
      ;
  }
}
