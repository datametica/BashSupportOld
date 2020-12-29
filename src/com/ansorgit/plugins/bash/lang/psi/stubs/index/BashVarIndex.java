package com.ansorgit.plugins.bash.lang.psi.stubs.index;

import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVar;
import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndexKey;
import org.jetbrains.annotations.NotNull;














public class BashVarIndex
  extends StringStubIndexExtension<BashVar>
{
  public static final StubIndexKey<String, BashVar> KEY = StubIndexKey.createIndexKey("bash.var");

  
  @NotNull
  public StubIndexKey<String, BashVar> getKey() {
    if (KEY == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/stubs/index/BashVarIndex", "getKey" }));  return KEY;
  }

  
  public int getVersion() {
    return 94;
  }
}
