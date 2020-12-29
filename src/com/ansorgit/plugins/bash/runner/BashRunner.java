package com.ansorgit.plugins.bash.runner;

import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.runners.DefaultProgramRunner;
import org.jetbrains.annotations.NotNull;



















public class BashRunner
  extends DefaultProgramRunner
{
  @NotNull
  public String getRunnerId() {
    if ("BashRunner" == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/runner/BashRunner", "getRunnerId" }));  return "BashRunner";
  }
  
  public boolean canRun(@NotNull String executorId, @NotNull RunProfile profile) {
    if (executorId == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "executorId", "com/ansorgit/plugins/bash/runner/BashRunner", "canRun" }));  if (profile == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "profile", "com/ansorgit/plugins/bash/runner/BashRunner", "canRun" }));  return (DefaultRunExecutor.EXECUTOR_ID.equals(executorId) && profile instanceof BashRunConfiguration);
  }
}
