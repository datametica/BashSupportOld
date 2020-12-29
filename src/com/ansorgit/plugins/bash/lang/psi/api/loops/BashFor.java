package com.ansorgit.plugins.bash.lang.psi.api.loops;

import com.ansorgit.plugins.bash.lang.psi.api.BashBlock;
import com.ansorgit.plugins.bash.lang.psi.api.BashKeyword;
import com.ansorgit.plugins.bash.lang.psi.api.BashPsiElement;

public interface BashFor extends BashLoop, BashPsiElement, BashKeyword, BashBlock {
  boolean isArithmetic();
}
