package com.ansorgit.plugins.bash.lang.psi.api.expression;

import com.ansorgit.plugins.bash.lang.psi.api.BashPsiElement;
import org.jetbrains.annotations.Nullable;

public interface BashFiledescriptor extends BashPsiElement {
  @Nullable
  Integer descriptorAsInt();
}
