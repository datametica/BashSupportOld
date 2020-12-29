package com.ansorgit.plugins.bash.documentation;

import com.ansorgit.plugins.bash.lang.psi.api.BashKeyword;
import com.intellij.psi.PsiElement;



















class BashKeywordDocSource
  extends ClasspathDocSource
{
  BashKeywordDocSource() {
    super("/documentation/internal");
  }

  
  String resourceNameForElement(PsiElement element) {
    return ((BashKeyword)element).keywordElement().getText();
  }

  
  boolean isValid(PsiElement element, PsiElement originalElement) {
    return element instanceof BashKeyword;
  }
  
  public String documentationUrl(PsiElement element, PsiElement originalElement) {
    return null;
  }
}
