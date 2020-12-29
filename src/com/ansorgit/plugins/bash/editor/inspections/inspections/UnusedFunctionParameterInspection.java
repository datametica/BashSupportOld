package com.ansorgit.plugins.bash.editor.inspections.inspections;

import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.BashPsiElement;
import com.ansorgit.plugins.bash.lang.psi.api.command.BashCommand;
import com.ansorgit.plugins.bash.lang.psi.api.function.BashFunctionDef;
import com.ansorgit.plugins.bash.lang.psi.api.loops.BashLoop;
import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVar;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiReference;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import org.jetbrains.annotations.NotNull;

























public class UnusedFunctionParameterInspection
  extends LocalInspectionTool
{
  @NotNull
  public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
    if (holder == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "holder", "com/ansorgit/plugins/bash/editor/inspections/inspections/UnusedFunctionParameterInspection", "buildVisitor" }));  if (new BashVisitor()
      {
        public void visitGenericCommand(BashCommand bashCommand) {
          if (!bashCommand.isFunctionCall()) {
            return;
          }
          
          PsiReference reference = bashCommand.getReference();
          if (reference == null) {
            return;
          }
          
          BashFunctionDef functionDef = (BashFunctionDef)reference.resolve();
          if (functionDef == null) {
            return;
          }

          
          if (isShiftUsedInLoop(functionDef)) {
            return;
          }
          
          Set<String> referencedParameterNames = Sets.newHashSet(Lists.transform(functionDef.findReferencedParameters(), new Function<BashPsiElement, String>() {
                  public String apply(BashPsiElement element) {
                    if (element instanceof BashVar) {
                      return ((BashVar)element).getReference().getReferencedName();
                    }
                    
                    return element.getText();
                  }
                }));

          
          if (referencedParameterNames.contains("*") || referencedParameterNames.contains("@")) {
            return;
          }
          
          List<BashPsiElement> callerParameters = bashCommand.parameters();
          int callerParameterCount = callerParameters.size();
          
          for (int i = 0; i < callerParameterCount; i++) {
            String paramName = String.valueOf(i + 1);
            
            if (!referencedParameterNames.contains(paramName)) {
              holder.registerProblem((PsiElement)callerParameters.get(i), "Unused function parameter", LocalQuickFix.EMPTY_ARRAY);
            }
          } 
        }
        
        boolean isShiftUsedInLoop(final BashFunctionDef function)
        {
          final AtomicBoolean shiftUsed = new AtomicBoolean(false);
          
          BashPsiUtils.visitRecursively((PsiElement)function, new BashVisitor()
              {
                public void visitInternalCommand(BashCommand bashCommand) {
                  if (shiftUsed.get()) {
                    return;
                  }
                  
                  if ("shift".equals(bashCommand.getReferencedCommandName())) {
                    BashLoop loop = (BashLoop)BashPsiUtils.findParent((PsiElement)bashCommand, BashLoop.class);
                    if (loop != null && function.equals(BashPsiUtils.findParent((PsiElement)loop, BashFunctionDef.class, BashFunctionDef.class))) {
                      shiftUsed.set(true);
                    }
                  } 
                }
              });
          
          return shiftUsed.get(); } } == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/inspections/inspections/UnusedFunctionParameterInspection", "buildVisitor" }));  return (PsiElementVisitor)new BashVisitor() { boolean isShiftUsedInLoop(final BashFunctionDef function) { final AtomicBoolean shiftUsed = new AtomicBoolean(false); BashPsiUtils.visitRecursively((PsiElement)function, new BashVisitor() { public void visitInternalCommand(BashCommand bashCommand) { if (shiftUsed.get()) return;  if ("shift".equals(bashCommand.getReferencedCommandName())) { BashLoop loop = (BashLoop)BashPsiUtils.findParent((PsiElement)bashCommand, BashLoop.class); if (loop != null && function.equals(BashPsiUtils.findParent((PsiElement)loop, BashFunctionDef.class, BashFunctionDef.class))) shiftUsed.set(true);  }  } }); return shiftUsed.get(); }

        
        public void visitGenericCommand(BashCommand bashCommand) {
          if (!bashCommand.isFunctionCall())
            return; 
          PsiReference reference = bashCommand.getReference();
          if (reference == null)
            return; 
          BashFunctionDef functionDef = (BashFunctionDef)reference.resolve();
          if (functionDef == null)
            return; 
          if (isShiftUsedInLoop(functionDef))
            return; 
          Set<String> referencedParameterNames = Sets.newHashSet(Lists.transform(functionDef.findReferencedParameters(), new Function<BashPsiElement, String>() {
                  public String apply(BashPsiElement element) {
                    if (element instanceof BashVar)
                      return ((BashVar)element).getReference().getReferencedName(); 
                    return element.getText();
                  }
                }));
          if (referencedParameterNames.contains("*") || referencedParameterNames.contains("@"))
            return; 
          List<BashPsiElement> callerParameters = bashCommand.parameters();
          int callerParameterCount = callerParameters.size();
          for (int i = 0; i < callerParameterCount; i++) {
            String paramName = String.valueOf(i + 1);
            if (!referencedParameterNames.contains(paramName))
              holder.registerProblem((PsiElement)callerParameters.get(i), "Unused function parameter", LocalQuickFix.EMPTY_ARRAY); 
          } 
        } }
      ;
  }
}
