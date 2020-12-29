package com.ansorgit.plugins.bash.lang.psi.api.word;

import com.ansorgit.plugins.bash.lang.psi.api.BashCharSequence;
import com.ansorgit.plugins.bash.lang.psi.api.BashPsiElement;

public interface BashWord extends BashPsiElement, BashCharSequence {
  boolean isWrappable();
}
