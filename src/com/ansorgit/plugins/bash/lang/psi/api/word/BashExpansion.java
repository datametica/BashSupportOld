package com.ansorgit.plugins.bash.lang.psi.api.word;

import com.ansorgit.plugins.bash.lang.psi.api.BashPsiElement;

public interface BashExpansion extends BashPsiElement, BashWord {
  boolean isValidExpansion();
}
