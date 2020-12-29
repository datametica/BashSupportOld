package com.ansorgit.plugins.bash.lang.psi.api.heredoc;

import com.ansorgit.plugins.bash.lang.psi.api.BashPsiElement;
import com.intellij.psi.PsiNamedElement;

public interface BashHereDocMarker extends BashPsiElement, PsiNamedElement {
  String getMarkerText();
  
  boolean isIgnoringTabs();
}
