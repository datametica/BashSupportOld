package com.ansorgit.plugins.bash.lang.psi.api.vars;

import com.ansorgit.plugins.bash.lang.psi.api.BashPsiElement;

public interface BashParameterExpansion extends BashPsiElement {
  boolean isParameterReference();
}
