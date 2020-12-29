package com.ansorgit.plugins.bash.lang.psi.api.shell;

import com.ansorgit.plugins.bash.lang.psi.api.BashPsiElement;

public interface BashConditionalCommand extends BashPsiElement {
  String getCommandText();
}
