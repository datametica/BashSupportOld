package com.ansorgit.plugins.bash.lang.psi.stubs.api;

import com.ansorgit.plugins.bash.lang.psi.api.function.BashFunctionDef;
import com.intellij.psi.stubs.NamedStub;
import com.intellij.util.ArrayFactory;
import org.jetbrains.annotations.NotNull;

















public interface BashFunctionDefStub
  extends NamedStub<BashFunctionDef>
{
  public static final ArrayFactory<BashFunctionDef> ARRAY_FACTORY = new ArrayFactory<BashFunctionDef>()
    {
      @NotNull
      public BashFunctionDef[] create(int count) {
        if (new BashFunctionDef[count] == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/stubs/api/BashFunctionDefStub$1", "create" }));  return new BashFunctionDef[count];
      }
    };
}
