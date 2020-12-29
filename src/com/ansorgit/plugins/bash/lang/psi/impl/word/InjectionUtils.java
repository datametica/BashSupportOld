package com.ansorgit.plugins.bash.lang.psi.impl.word;

import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import java.util.List;
import org.jetbrains.annotations.NotNull;




















public class InjectionUtils
{
  public static boolean walkInjection(PsiElement host, @NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement place, boolean walkOn) {
    if (processor == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "processor", "com/ansorgit/plugins/bash/lang/psi/impl/word/InjectionUtils", "walkInjection" }));  if (state == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "state", "com/ansorgit/plugins/bash/lang/psi/impl/word/InjectionUtils", "walkInjection" }));  if (place == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "place", "com/ansorgit/plugins/bash/lang/psi/impl/word/InjectionUtils", "walkInjection" }));  InjectedLanguageManager injectedLanguageManager = InjectedLanguageManager.getInstance(host.getProject());
    
    List<Pair<PsiElement, TextRange>> injectedPsiFiles = injectedLanguageManager.getInjectedPsiFiles(host);
    if (injectedPsiFiles != null) {
      for (Pair<PsiElement, TextRange> psi_range : injectedPsiFiles)
      {
        walkOn &= ((PsiElement)psi_range.first).processDeclarations(processor, state, lastParent, place);
      }
    }
    
    return walkOn;
  }
}
