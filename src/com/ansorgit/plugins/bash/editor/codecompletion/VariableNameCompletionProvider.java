package com.ansorgit.plugins.bash.editor.codecompletion;

import com.ansorgit.plugins.bash.lang.LanguageBuiltins;
import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVar;
import com.ansorgit.plugins.bash.lang.psi.impl.vars.BashVarCollectorProcessor;
import com.ansorgit.plugins.bash.lang.psi.impl.vars.BashVarVariantsProcessor;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import com.ansorgit.plugins.bash.settings.BashProjectSettings;
import com.ansorgit.plugins.bash.util.BashIcons;
import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.openapi.project.Project;
import com.intellij.patterns.ElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import java.util.Collection;


















class VariableNameCompletionProvider
  extends AbstractBashCompletionProvider
{
  void addTo(CompletionContributor contributor) {
    BashPsiPattern insideVar = (BashPsiPattern)(new BashPsiPattern()).withParent(BashVar.class);
    
    contributor.extend(CompletionType.BASIC, (ElementPattern)insideVar, this);
  }

  
  protected void addBashCompletions(String currentText, CompletionParameters parameters, ProcessingContext context, CompletionResultSet result) {
    PsiElement element = parameters.getPosition();
    
    BashVar varElement = (BashVar)PsiTreeUtil.getContextOfType(element, BashVar.class, false);
    boolean dollarPrefix = (currentText != null && currentText.startsWith("$"));
    boolean insideExpansion = (element.getParent() != null && element.getParent().getParent() instanceof com.ansorgit.plugins.bash.lang.psi.api.vars.BashParameterExpansion);
    if (varElement == null && !dollarPrefix && !insideExpansion) {
      return;
    }
    
    int invocationCount = parameters.getInvocationCount();
    int resultLength = 0;
    
    PsiElement original = parameters.getOriginalPosition();
    BashVar varElementOriginal = (original != null) ? (BashVar)PsiTreeUtil.getContextOfType(original, BashVar.class, false) : null;
    
    if (varElement != null) {
      
      PsiElement originalRef = (varElementOriginal != null) ? (PsiElement)varElementOriginal : original;
      if (originalRef != null) {
        resultLength += addCollectedVariables(original, result, (BashVarCollectorProcessor)new BashVarVariantsProcessor(originalRef, false, true));
      }

      
      resultLength += addCollectedVariables(element, result, (BashVarCollectorProcessor)new BashVarVariantsProcessor((PsiElement)varElement, true, false));
    } else {
      
      if (original != null) {
        resultLength += addCollectedVariables(original, result, (BashVarCollectorProcessor)new BashVarVariantsProcessor(original, false, true));
      }
      resultLength += addCollectedVariables(element, result, (BashVarCollectorProcessor)new BashVarVariantsProcessor(element, false, true));
    } 
    
    if (currentText != null && (dollarPrefix || insideExpansion) && (invocationCount >= 2 || resultLength == 0)) {
      Project project = element.getProject();
      addBuiltInVariables(result, project);
      addGlobalVariables(result, project);
    } else {
      result.addLookupAdvertisement("Press twice for global variables");
    } 
  }
  
  private int addCollectedVariables(PsiElement element, CompletionResultSet result, BashVarCollectorProcessor processor) {
    PsiTreeUtil.treeWalkUp((PsiScopeProcessor)processor, element, (PsiElement)BashPsiUtils.findFileContext(element), ResolveState.initial());
    
    Collection<LookupElement> items = CompletionProviderUtils.createFromPsiItems(processor.getVariables(), BashIcons.VAR_ICON, Integer.valueOf(CompletionGrouping.NormalVar.ordinal()));
    result.addAllElements(items);
    
    return items.size();
  }
  
  private void addGlobalVariables(CompletionResultSet result, Project project) {
    if (BashProjectSettings.storedSettings(project).isAutcompleteGlobalVars()) {
      Collection<LookupElement> globalVars = CompletionProviderUtils.createItems(BashProjectSettings.storedSettings(project).getGlobalVariables(), BashIcons.GLOBAL_VAR_ICON, true, Integer.valueOf(CompletionGrouping.GlobalVar.ordinal()));
      result.addAllElements(globalVars);
    } 
  }
  
  private void addBuiltInVariables(CompletionResultSet result, Project project) {
    if (BashProjectSettings.storedSettings(project).isAutocompleteBuiltinVars()) {
      Collection<LookupElement> shellBuiltIns = CompletionProviderUtils.createItems(LanguageBuiltins.bashShellVars, BashIcons.BASH_VAR_ICON, true, Integer.valueOf(CompletionGrouping.BuiltInVar.ordinal()));
      result.addAllElements(shellBuiltIns);
      
      Collection<LookupElement> bashBuiltIns = CompletionProviderUtils.createItems(LanguageBuiltins.bourneShellVars, BashIcons.BOURNE_VAR_ICON, true, Integer.valueOf(CompletionGrouping.BuiltInVar.ordinal()));
      result.addAllElements(bashBuiltIns);
    } 
  }
}
