package com.ansorgit.plugins.bash.lang.psi.api;

import org.jetbrains.annotations.NotNull;

public interface BashFunctionDefName extends BashPsiElement {
  @NotNull
  String getNameString();
}
