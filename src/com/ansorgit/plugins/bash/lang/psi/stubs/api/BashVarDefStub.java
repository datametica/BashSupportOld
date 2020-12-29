package com.ansorgit.plugins.bash.lang.psi.stubs.api;

import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVarDef;
import com.intellij.psi.stubs.NamedStub;

public interface BashVarDefStub extends NamedStub<BashVarDef> {
  boolean isReadOnly();
}
