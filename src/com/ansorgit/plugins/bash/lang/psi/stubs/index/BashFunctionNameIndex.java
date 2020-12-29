package com.ansorgit.plugins.bash.lang.psi.stubs.index;

import com.ansorgit.plugins.bash.lang.psi.api.function.BashFunctionDef;
import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndexKey;
import org.jetbrains.annotations.NotNull;














public class BashFunctionNameIndex
  extends StringStubIndexExtension<BashFunctionDef>
{
  public static final StubIndexKey<String, BashFunctionDef> KEY = StubIndexKey.createIndexKey("bash.function.name");

  
  @NotNull
  public StubIndexKey<String, BashFunctionDef> getKey() {
    if (KEY == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/stubs/index/BashFunctionNameIndex", "getKey" }));  return KEY;
  }

  
  public int getVersion() {
    return 94;
  }
}
