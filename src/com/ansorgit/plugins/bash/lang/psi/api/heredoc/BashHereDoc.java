package com.ansorgit.plugins.bash.lang.psi.api.heredoc;

import com.ansorgit.plugins.bash.lang.psi.api.BashPsiElement;

public interface BashHereDoc extends BashPsiElement {
  boolean isEvaluatingVariables();
  
  boolean isStrippingLeadingWhitespace();
}
