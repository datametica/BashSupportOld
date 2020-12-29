package com.ansorgit.plugins.bash.documentation;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;

interface DocumentationSource {
  @Nullable
  String documentation(PsiElement paramPsiElement1, PsiElement paramPsiElement2);
  
  @Nullable
  String documentationUrl(PsiElement paramPsiElement1, PsiElement paramPsiElement2);
}
