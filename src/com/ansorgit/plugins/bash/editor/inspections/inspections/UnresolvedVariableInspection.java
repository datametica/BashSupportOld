package com.ansorgit.plugins.bash.editor.inspections.inspections;

import com.ansorgit.plugins.bash.editor.inspections.quickfix.GlobalVariableQuickfix;
import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.BashReference;
import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVar;
import com.ansorgit.plugins.bash.settings.BashProjectSettings;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import java.util.Set;
import org.jetbrains.annotations.NotNull;




















public class UnresolvedVariableInspection
  extends LocalInspectionTool
{
  @NotNull
  public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
    if (holder == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "holder", "com/ansorgit/plugins/bash/editor/inspections/inspections/UnresolvedVariableInspection", "buildVisitor" }));  if (new UnresolvedVariableVisitor(holder) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/inspections/inspections/UnresolvedVariableInspection", "buildVisitor" }));  return (PsiElementVisitor)new UnresolvedVariableVisitor(holder);
  }
  
  private static final class UnresolvedVariableVisitor extends BashVisitor {
    private final ProblemsHolder holder;
    private final Set<String> globalVariableNames;
    
    public UnresolvedVariableVisitor(ProblemsHolder holder) {
      this.holder = holder;
      this.globalVariableNames = BashProjectSettings.storedSettings(holder.getProject()).getGlobalVariables();
    }

    
    public void visitVarUse(BashVar bashVar) {
      if (bashVar.isBuiltinVar() || bashVar.getTextLength() == 0) {
        return;
      }
      
      if (this.globalVariableNames.contains(bashVar.getReferenceName())) {
        return;
      }
      
      BashReference ref = bashVar.getReference();
      if (ref.resolve() == null)
        this.holder.registerProblem((PsiElement)bashVar, "Unresolved variable", ProblemHighlightType.GENERIC_ERROR_OR_WARNING, ref

            
            .getRangeInElement(), new LocalQuickFix[] { (LocalQuickFix)new GlobalVariableQuickfix(bashVar, true) }); 
    }
  }
}
