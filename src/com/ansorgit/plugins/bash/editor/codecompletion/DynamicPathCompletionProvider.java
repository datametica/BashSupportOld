package com.ansorgit.plugins.bash.editor.codecompletion;

import com.ansorgit.plugins.bash.lang.psi.api.word.BashWord;
import com.ansorgit.plugins.bash.util.BashFiles;
import com.ansorgit.plugins.bash.util.CompletionUtil;
import com.google.common.collect.Sets;
import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.patterns.ElementPattern;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import java.util.List;
import java.util.Set;
import org.jetbrains.annotations.Nullable;





















class DynamicPathCompletionProvider
  extends AbstractBashCompletionProvider
{
  private static final Set<String> homePrefixes = Sets.newHashSet((Object[])new String[] { "$HOME", "~" });
  private static final Set<String> supportedPrefixes = Sets.newHashSet((Object[])new String[] { "$HOME", "~", "." });




  
  void addTo(CompletionContributor contributor) {
    contributor.extend(CompletionType.BASIC, (ElementPattern)(new BashPsiPattern()).withParent(BashWord.class), this);
  }


  
  protected void addBashCompletions(String currentText, CompletionParameters parameters, ProcessingContext context, CompletionResultSet result) {
    PsiElement parentElement = parameters.getPosition().getParent();
    if (parentElement instanceof BashWord) {
      currentText = findCurrentText(parameters, parentElement);
    }
    
    result = result.withPrefixMatcher(currentText);
    
    String usedPrefix = findUsedPrefix(currentText);
    if (usedPrefix == null) {
      return;
    }

    
    String baseDir = findBaseDir(parameters, usedPrefix);
    if (baseDir == null) {
      return;
    }
    
    String relativePath = currentText.substring(usedPrefix.length());
    if (relativePath.startsWith("/")) {
      relativePath = relativePath.substring(1);
    }
    
    List<String> completions = CompletionUtil.completeRelativePath(baseDir, usedPrefix, relativePath);
    result.addAllElements(CompletionProviderUtils.createPathItems(completions));
  }
  
  @Nullable
  private String findBaseDir(CompletionParameters parameters, String usedPrefix) {
    if (homePrefixes.contains(usedPrefix)) {
      return BashFiles.userHomeDir();
    }
    
    PsiDirectory file = parameters.getOriginalFile().getParent();
    return (file != null) ? file.getVirtualFile().getPath() : null;
  }
  
  private String findUsedPrefix(String originalText) {
    String usedPrefix = null;
    for (String prefix : supportedPrefixes) {
      if (originalText.startsWith(prefix)) {
        usedPrefix = prefix;
        
        break;
      } 
    } 
    return usedPrefix;
  }
}
