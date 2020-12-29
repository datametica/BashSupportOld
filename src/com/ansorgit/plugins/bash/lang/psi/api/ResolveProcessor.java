package com.ansorgit.plugins.bash.lang.psi.api;

import com.intellij.psi.PsiElement;
import com.intellij.psi.scope.PsiScopeProcessor;
import org.jetbrains.annotations.Nullable;

public interface ResolveProcessor extends PsiScopeProcessor {
  @Nullable
  PsiElement getBestResult(boolean paramBoolean, PsiElement paramPsiElement);
  
  @Nullable
  Iterable<PsiElement> getResults();
  
  boolean hasResults();
  
  void reset();
  
  void prepareResults();
}
