package com.ansorgit.plugins.bash.lang.psi.api;

import com.intellij.psi.PsiReference;

public interface BashReference extends PsiReference {
  String getReferencedName();
}
