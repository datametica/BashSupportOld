package com.ansorgit.plugins.bash.lang.psi.api.vars;

import com.ansorgit.plugins.bash.lang.psi.api.BashPsiElement;
import com.ansorgit.plugins.bash.lang.psi.api.BashReference;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface BashVar extends BashPsiElement, PsiNamedElement {
  boolean isBuiltinVar();
  
  boolean isParameterExpansion();
  
  boolean isParameterReference();
  
  boolean isArrayUse();
  
  @NotNull
  BashReference getReference();
  
  @Nullable
  BashReference getNeighborhoodReference();
  
  String getReferenceName();
  
  boolean isVarDefinition();
  
  int getPrefixLength();
}
