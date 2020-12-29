package com.ansorgit.plugins.bash.editor.codecompletion;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.PrefixMatcher;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import java.io.File;
import java.util.List;
import java.util.function.Predicate;
import org.jetbrains.annotations.NotNull;



















abstract class AbstractBashCompletionProvider
  extends CompletionProvider<CompletionParameters>
{
  protected Predicate<File> createFileFilter() {
    return file -> true;
  }




  
  protected final void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet resultWithoutPrefix) {
    if (parameters == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "parameters", "com/ansorgit/plugins/bash/editor/codecompletion/AbstractBashCompletionProvider", "addCompletions" }));  if (resultWithoutPrefix == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "resultWithoutPrefix", "com/ansorgit/plugins/bash/editor/codecompletion/AbstractBashCompletionProvider", "addCompletions" }));  addBashCompletions(findCurrentText(parameters, parameters.getPosition()), parameters, context, resultWithoutPrefix);
  }
  
  protected String findOriginalText(PsiElement element) {
    return element.getText();
  }
  
  protected String findCurrentText(CompletionParameters parameters, PsiElement element) {
    String originalText = findOriginalText(element);
    int elementOffset = parameters.getOffset() - element.getTextOffset();
    
    return (elementOffset >= 0 && elementOffset < originalText.length()) ? originalText
      .substring(0, elementOffset) : originalText;
  }



  
  protected int computeResultCount(List<String> completions, CompletionResultSet result) {
    PrefixMatcher prefixMatcher = result.getPrefixMatcher();
    
    int resultCount = 0;
    
    for (String c : completions) {
      if (prefixMatcher.prefixMatches(c)) {
        resultCount++;
      }
    } 
    
    return resultCount;
  }
  
  abstract void addTo(CompletionContributor paramCompletionContributor);
  
  protected abstract void addBashCompletions(String paramString, CompletionParameters paramCompletionParameters, ProcessingContext paramProcessingContext, CompletionResultSet paramCompletionResultSet);
}
