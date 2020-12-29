package com.ansorgit.plugins.bash.lang.psi.api.command;

import com.ansorgit.plugins.bash.lang.psi.api.BashFileReference;
import org.jetbrains.annotations.Nullable;

public interface BashIncludeCommand extends BashCommand {
  @Nullable
  BashFileReference getFileReference();
}
