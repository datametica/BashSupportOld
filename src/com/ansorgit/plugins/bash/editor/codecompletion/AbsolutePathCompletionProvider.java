package com.ansorgit.plugins.bash.editor.codecompletion;

import com.ansorgit.plugins.bash.util.CompletionUtil;
import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.completion.PrefixMatcher;
import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.StandardPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import java.io.File;
import java.util.List;
import java.util.function.Predicate;
import org.jetbrains.annotations.NotNull;


















class AbsolutePathCompletionProvider
  extends AbstractBashCompletionProvider
{
  void addTo(CompletionContributor contributor) {
    contributor.extend(CompletionType.BASIC, (ElementPattern)StandardPatterns.instanceOf(PsiElement.class), this);
  }
  
  protected void addBashCompletions(String currentText, CompletionParameters parameters, ProcessingContext context, CompletionResultSet resultWithoutPrefix) {
    if (currentText == null || !currentText.startsWith("/")) {
      return;
    }
    
    PsiElement parentElement = parameters.getPosition().getParent();
    String parentText = parentElement.getText();
    if (parentText.startsWith("$HOME") || parentText.startsWith("~")) {
      return;
    }
    
    int invocationCount = parameters.getInvocationCount();
    
    CompletionResultSet result = resultWithoutPrefix.withPrefixMatcher(new PathPrefixMatcher(currentText));
    
    Predicate<File> incovationCountPredicate = file -> {

        
        boolean isHidden = (file.isHidden() || file.getName().startsWith("."));
        return ((isHidden && invocationCount >= 2) || (invocationCount >= 1 && !isHidden));
      };
    
    List<String> completions = CompletionUtil.completeAbsolutePath(currentText, createFileFilter().and(incovationCountPredicate));
    result.addAllElements(CompletionProviderUtils.createPathItems(completions));
    
    int validResultCount = computeResultCount(completions, result);
    
    if (validResultCount == 0 && invocationCount == 1) {
      
      List<String> secondCompletions = CompletionUtil.completeAbsolutePath(currentText, createFileFilter());
      result.addAllElements(CompletionProviderUtils.createPathItems(secondCompletions));
    } 
    
    if (invocationCount == 1)
      result.addLookupAdvertisement("Press twice for hidden files"); 
  }
  
  private static class PathPrefixMatcher
    extends PrefixMatcher {
    protected PathPrefixMatcher(String prefix) {
      super(prefix);
    }

    
    public boolean prefixMatches(@NotNull String name) {
      if (name == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "name", "com/ansorgit/plugins/bash/editor/codecompletion/AbsolutePathCompletionProvider$PathPrefixMatcher", "prefixMatches" }));  return name.startsWith(getPrefix());
    }

    
    @NotNull
    public PrefixMatcher cloneWithPrefix(@NotNull String prefix) {
      if (prefix == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "prefix", "com/ansorgit/plugins/bash/editor/codecompletion/AbsolutePathCompletionProvider$PathPrefixMatcher", "cloneWithPrefix" }));  if (new PathPrefixMatcher(prefix) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/codecompletion/AbsolutePathCompletionProvider$PathPrefixMatcher", "cloneWithPrefix" }));  return new PathPrefixMatcher(prefix);
    }
  }
}
