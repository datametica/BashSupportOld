package com.ansorgit.plugins.bash.documentation;

import com.intellij.psi.PsiElement;

interface CachableDocumentationSource extends DocumentationSource {
  String findCacheKey(PsiElement paramPsiElement1, PsiElement paramPsiElement2);
}
