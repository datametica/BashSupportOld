package com.ansorgit.plugins.bash.lang.psi.api.vars;

import com.ansorgit.plugins.bash.lang.psi.api.BashPsiElement;
import com.ansorgit.plugins.bash.lang.psi.api.BashReference;
import com.ansorgit.plugins.bash.lang.psi.api.DocumentationAwareElement;
import com.intellij.navigation.NavigationItem;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiReference;
import org.jetbrains.annotations.NotNull;

public interface BashVarDef extends BashPsiElement, PsiNamedElement, PsiNameIdentifierOwner, NavigationItem, DocumentationAwareElement, BashVar {
  String getName();
  
  boolean isArray();
  
  boolean isReadonly();
  
  boolean isCommandLocal();
  
  @NotNull
  PsiElement findAssignmentWord();
  
  boolean isFunctionScopeLocal();
  
  boolean isLocalVarDef();
  
  PsiElement findFunctionScope();
  
  boolean hasAssignmentValue();
  
  @NotNull
  BashReference getReference();
  
  boolean isStaticAssignmentWord();
}
