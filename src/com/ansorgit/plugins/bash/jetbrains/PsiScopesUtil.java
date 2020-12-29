package com.ansorgit.plugins.bash.jetbrains;

import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import org.jetbrains.annotations.NotNull;



























public class PsiScopesUtil
{
  public static boolean walkChildrenScopes(@NotNull PsiElement thisElement, @NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, PsiElement place) {
    if (thisElement == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "thisElement", "com/ansorgit/plugins/bash/jetbrains/PsiScopesUtil", "walkChildrenScopes" }));  if (processor == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "processor", "com/ansorgit/plugins/bash/jetbrains/PsiScopesUtil", "walkChildrenScopes" }));  if (state == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "state", "com/ansorgit/plugins/bash/jetbrains/PsiScopesUtil", "walkChildrenScopes" }));  for (PsiElement child = thisElement.getFirstChild(); child != null; child = child.getNextSibling()) {
      if (child != lastParent && !child.processDeclarations(processor, state, lastParent, place)) {
        return false;
      }
    } 
    
    return true;
  }
}
