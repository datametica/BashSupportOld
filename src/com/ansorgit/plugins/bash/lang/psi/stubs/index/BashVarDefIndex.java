package com.ansorgit.plugins.bash.lang.psi.stubs.index;

import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVarDef;
import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndexKey;
import org.jetbrains.annotations.NotNull;

















public class BashVarDefIndex
  extends StringStubIndexExtension<BashVarDef>
{
  public static final StubIndexKey<String, BashVarDef> KEY = StubIndexKey.createIndexKey("bash.vardef");

  
  @NotNull
  public StubIndexKey<String, BashVarDef> getKey() {
    if (KEY == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/stubs/index/BashVarDefIndex", "getKey" }));  return KEY;
  }

  
  public int getVersion() {
    return 94;
  }
}
