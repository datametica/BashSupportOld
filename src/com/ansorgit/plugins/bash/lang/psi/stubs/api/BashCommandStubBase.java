package com.ansorgit.plugins.bash.lang.psi.stubs.api;

import com.intellij.psi.stubs.StubElement;

public interface BashCommandStubBase<T extends com.intellij.psi.PsiElement> extends StubElement<T> {
  boolean isInternalCommand(boolean paramBoolean);
  
  boolean isGenericCommand();
}
