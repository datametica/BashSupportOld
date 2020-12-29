package com.ansorgit.plugins.bash.editor.codecompletion;

import com.ansorgit.plugins.bash.lang.LanguageBuiltins;
import com.ansorgit.plugins.bash.lang.psi.api.command.BashGenericCommand;
import com.ansorgit.plugins.bash.lang.psi.api.command.BashInternalCommand;
import com.ansorgit.plugins.bash.lang.psi.impl.command.BashFunctionVariantsProcessor;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import com.ansorgit.plugins.bash.settings.BashProjectSettings;
import com.ansorgit.plugins.bash.util.BashIcons;
import com.google.common.collect.Lists;
import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.StandardPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;























class CommandNameCompletionProvider
  extends AbstractBashCompletionProvider
{
  private final BashPathCompletionService completionService;
  
  public CommandNameCompletionProvider(BashPathCompletionService completionService) {
    this.completionService = completionService;
  }

  
  void addTo(CompletionContributor contributor) {
    BashPsiPattern internal = (BashPsiPattern)(new BashPsiPattern()).withParent(BashInternalCommand.class);
    BashPsiPattern generic = (BashPsiPattern)(new BashPsiPattern()).withParent(BashGenericCommand.class);
    ElementPattern<PsiElement> internalOrGeneric = StandardPatterns.or(new ElementPattern[] { (ElementPattern)internal, (ElementPattern)generic });
    
    BashPsiPattern pattern = (BashPsiPattern)(new BashPsiPattern()).withParent(internalOrGeneric);
    
    contributor.extend(CompletionType.BASIC, (ElementPattern)pattern, this);
  }




  
  protected void addBashCompletions(String currentText, CompletionParameters parameters, ProcessingContext context, CompletionResultSet result) {
    if (currentText.startsWith("$")) {
      return;
    }
    
    PsiElement element = parameters.getPosition();
    if (element.getParent() != null && element.getParent().getParent() instanceof com.ansorgit.plugins.bash.lang.psi.api.vars.BashParameterExpansion) {
      return;
    }
    
    int addedItems = 0;
    
    Collection<LookupElement> functions = collectFunctions(element, CompletionGrouping.Function.ordinal());
    result.addAllElements(functions);
    addedItems += functions.size();
    
    int invocationCount = parameters.getInvocationCount();


    
    String lookupPrefix = findCurrentText(parameters, element);
    CompletionResultSet prefixedCommand = result.withPrefixMatcher(lookupPrefix);
    
    BashProjectSettings settings = BashProjectSettings.storedSettings(element.getProject());
    if (settings.isAutocompleteBuiltinCommands() && (invocationCount > 1 || addedItems == 0)) {
      Collection<LookupElement> commands = CompletionProviderUtils.createItems(LanguageBuiltins.commands, BashIcons.GLOBAL_VAR_ICON, true, Integer.valueOf(CompletionGrouping.GlobalCommand.ordinal()));
      addedItems += commands.size();
      
      prefixedCommand.addAllElements(commands);
      
      if (settings.isSupportBash4()) {
        commands = CompletionProviderUtils.createItems(LanguageBuiltins.commands_v4, BashIcons.GLOBAL_VAR_ICON, true, Integer.valueOf(CompletionGrouping.GlobalCommand.ordinal()));
        addedItems += commands.size();
        
        prefixedCommand.addAllElements(commands);
      } 
    } 
    
    if (settings.isAutocompletePathCommands()) {
      List<LookupElement> commands = collectPathCommands(currentText, invocationCount);
      addedItems += commands.size();
      
      prefixedCommand.addAllElements(commands);
    } 


    
    if (invocationCount < 2 && addedItems == 0) {
      result.addLookupAdvertisement("Press twice for all built-in and system-wide commands");
    }
  }
  
  private List<LookupElement> collectPathCommands(String currentText, int invocationCount) {
    if (invocationCount < 2) {
      return Collections.emptyList();
    }



    
    Collection<BashPathCompletionService.CompletionItem> commands = (invocationCount == 2) ? this.completionService.findCommands(currentText) : this.completionService.allCommands();
    
    return (List<LookupElement>)commands.stream()
      .map(completionItem -> LookupElementBuilder.create(completionItem.getFilename()).withCaseSensitivity(!SystemInfo.isWindows).withTypeText(completionItem.getPath(), true))

      
      .collect(Collectors.toCollection(Lists::newLinkedList));
  }
  
  @NotNull
  private Collection<LookupElement> collectFunctions(PsiElement lookupElement, int groupId) {
    BashFunctionVariantsProcessor processor = new BashFunctionVariantsProcessor(lookupElement);
    PsiTreeUtil.treeWalkUp((PsiScopeProcessor)processor, lookupElement, (PsiElement)BashPsiUtils.findFileContext(lookupElement), ResolveState.initial());
    
    if (CompletionProviderUtils.createFromPsiItems(processor.getFunctionDefs(), BashIcons.FUNCTION_ICON, Integer.valueOf(groupId)) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/codecompletion/CommandNameCompletionProvider", "collectFunctions" }));  return CompletionProviderUtils.createFromPsiItems(processor.getFunctionDefs(), BashIcons.FUNCTION_ICON, Integer.valueOf(groupId));
  }
}
