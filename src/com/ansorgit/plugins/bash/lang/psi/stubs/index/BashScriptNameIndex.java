package com.ansorgit.plugins.bash.lang.psi.stubs.index;

import com.ansorgit.plugins.bash.lang.psi.api.BashFile;
import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndexKey;
import org.jetbrains.annotations.NotNull;

















public class BashScriptNameIndex
  extends StringStubIndexExtension<BashFile>
{
  public static final StubIndexKey<String, BashFile> KEY = StubIndexKey.createIndexKey("bash.script.name");

  
  @NotNull
  public StubIndexKey<String, BashFile> getKey() {
    if (KEY == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/stubs/index/BashScriptNameIndex", "getKey" }));  return KEY;
  }

  
  public int getVersion() {
    return 94;
  }
}
