package com.ansorgit.plugins.bash.lang.psi.api.expression;

import com.ansorgit.plugins.bash.lang.psi.api.BashBlock;
import com.ansorgit.plugins.bash.lang.psi.api.BashPsiElement;

public interface BashSubshellCommand extends BashPsiElement, BashBlock {
  String getCommandText();
}
