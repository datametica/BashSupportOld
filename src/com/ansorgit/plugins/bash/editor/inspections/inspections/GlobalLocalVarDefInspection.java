package com.ansorgit.plugins.bash.editor.inspections.inspections;

import com.ansorgit.plugins.bash.editor.inspections.quickfix.RemoveLocalQualifierQuickfix;
import com.ansorgit.plugins.bash.lang.LanguageBuiltins;
import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.command.BashCommand;
import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVarDef;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;





















public class GlobalLocalVarDefInspection
  extends LocalInspectionTool
{
  @NotNull
  public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
    if (holder == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "holder", "com/ansorgit/plugins/bash/editor/inspections/inspections/GlobalLocalVarDefInspection", "buildVisitor" }));  if (new BashVisitor()
      {
        public void visitVarDef(BashVarDef varDef) {
          if (varDef.isFunctionScopeLocal())
          { PsiElement context = varDef.getContext();
            
            if (context instanceof BashCommand)
            { BashCommand parentCmd = (BashCommand)context;
              
              if (parentCmd.isVarDefCommand() && LanguageBuiltins.localVarDefCommands.contains(parentCmd.getReferencedCommandName()))
              { boolean isInFunction = false;
                
                PsiElement parent = BashPsiUtils.findEnclosingBlock((PsiElement)varDef);
                while (parent != null && !isInFunction) {
                  isInFunction = parent instanceof com.ansorgit.plugins.bash.lang.psi.api.function.BashFunctionDef;
                  
                  parent = BashPsiUtils.findEnclosingBlock(parent);
                } 
                
                if (!isInFunction)
                { PsiElement problemHolder = varDef.getContext();
                  holder.registerProblem(problemHolder, "'local' must be used in a function", ProblemHighlightType.GENERIC_ERROR, new LocalQuickFix[] { (LocalQuickFix)new RemoveLocalQualifierQuickfix(varDef) }); }  }  }  }  } } == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/inspections/inspections/GlobalLocalVarDefInspection", "buildVisitor" }));  return (PsiElementVisitor)new BashVisitor() { public void visitVarDef(BashVarDef varDef) { if (varDef.isFunctionScopeLocal()) { PsiElement context = varDef.getContext(); if (context instanceof BashCommand) { BashCommand parentCmd = (BashCommand)context; if (parentCmd.isVarDefCommand() && LanguageBuiltins.localVarDefCommands.contains(parentCmd.getReferencedCommandName())) { boolean isInFunction = false; PsiElement parent = BashPsiUtils.findEnclosingBlock((PsiElement)varDef); while (parent != null && !isInFunction) { isInFunction = parent instanceof com.ansorgit.plugins.bash.lang.psi.api.function.BashFunctionDef; parent = BashPsiUtils.findEnclosingBlock(parent); }  if (!isInFunction) { PsiElement problemHolder = varDef.getContext(); holder.registerProblem(problemHolder, "'local' must be used in a function", ProblemHighlightType.GENERIC_ERROR, new LocalQuickFix[] { (LocalQuickFix)new RemoveLocalQualifierQuickfix(varDef) }); }
                 }
               }
             }
           }
         }
      ;
  }
}
