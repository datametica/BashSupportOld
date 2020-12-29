package com.ansorgit.plugins.bash.lang.psi.stubs.index;

import com.ansorgit.plugins.bash.lang.psi.api.command.BashIncludeCommand;
import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndexKey;
import org.jetbrains.annotations.NotNull;














public class BashIncludeCommandIndex
  extends StringStubIndexExtension<BashIncludeCommand>
{
  public static final StubIndexKey<String, BashIncludeCommand> KEY = StubIndexKey.createIndexKey("bash.includers");

  
  @NotNull
  public StubIndexKey<String, BashIncludeCommand> getKey() {
    if (KEY == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/stubs/index/BashIncludeCommandIndex", "getKey" }));  return KEY;
  }

  
  public int getVersion() {
    return 94;
  }
}
