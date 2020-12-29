package com.ansorgit.plugins.bash.lang.psi.stubs.index;

import com.ansorgit.plugins.bash.lang.psi.api.command.BashCommand;
import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndexKey;
import org.jetbrains.annotations.NotNull;














public class BashCommandNameIndex
  extends StringStubIndexExtension<BashCommand>
{
  public static final StubIndexKey<String, BashCommand> KEY = StubIndexKey.createIndexKey("bash.scriptCommandReference");

  
  @NotNull
  public StubIndexKey<String, BashCommand> getKey() {
    if (KEY == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/stubs/index/BashCommandNameIndex", "getKey" }));  return KEY;
  }

  
  public int getVersion() {
    return 94;
  }
}
