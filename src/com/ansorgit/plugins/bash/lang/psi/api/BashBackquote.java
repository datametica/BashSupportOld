package com.ansorgit.plugins.bash.lang.psi.api;

import com.intellij.openapi.util.TextRange;
import org.jetbrains.annotations.NotNull;

public interface BashBackquote extends BashPsiElement {
  String getCommandText();
  
  @NotNull
  TextRange getCommandTextRange();
}
