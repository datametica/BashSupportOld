package com.ansorgit.plugins.bash.lang.psi.api;

import com.intellij.psi.PsiElement;

public interface BashKeyword extends BashPsiElement {
  PsiElement keywordElement();
}
