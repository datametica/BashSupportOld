package com.ansorgit.plugins.bash.lang.psi.api;

import com.intellij.openapi.util.TextRange;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface BashShebang extends BashPsiElement {
  String shellCommand(boolean paramBoolean);
  
  String shellCommandParams();
  
  @NotNull
  TextRange commandAndParamsRange();
  
  void updateCommand(String paramString, @Nullable TextRange paramTextRange);
  
  @NotNull
  TextRange commandRange();
  
  int getShellCommandOffset();
}
