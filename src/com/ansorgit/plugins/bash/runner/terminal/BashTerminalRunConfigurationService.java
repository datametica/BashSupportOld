package com.ansorgit.plugins.bash.runner.terminal;

import com.ansorgit.plugins.bash.runner.BashRunConfiguration;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.runners.ExecutionEnvironment;
import org.jetbrains.annotations.NotNull;










public class BashTerminalRunConfigurationService
{
  public RunProfileState getState(BashRunConfiguration bashRunConfiguration, @NotNull Executor executor, @NotNull ExecutionEnvironment env) throws ExecutionException {
    if (executor == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "executor", "com/ansorgit/plugins/bash/runner/terminal/BashTerminalRunConfigurationService", "getState" }));  if (env == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "env", "com/ansorgit/plugins/bash/runner/terminal/BashTerminalRunConfigurationService", "getState" }));  return (RunProfileState)new BashTerminalCommandLineState(bashRunConfiguration, env);
  }
}
