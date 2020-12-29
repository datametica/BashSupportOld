package com.ansorgit.plugins.bash.runner.terminal;

import com.ansorgit.plugins.bash.runner.BashRunConfigUtil;
import com.ansorgit.plugins.bash.runner.BashRunConfiguration;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.CommandLineState;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ExecutionConsole;
import com.intellij.openapi.actionSystem.AnAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;














class BashTerminalCommandLineState
  extends CommandLineState
{
  private final BashRunConfiguration runConfig;
  
  BashTerminalCommandLineState(BashRunConfiguration runConfig, ExecutionEnvironment environment) {
    super(environment);
    this.runConfig = runConfig;
  }

  
  @NotNull
  public ExecutionResult execute(@NotNull Executor executor, @NotNull ProgramRunner runner) throws ExecutionException {
    if (executor == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "executor", "com/ansorgit/plugins/bash/runner/terminal/BashTerminalCommandLineState", "execute" }));  if (runner == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "runner", "com/ansorgit/plugins/bash/runner/terminal/BashTerminalCommandLineState", "execute" }));  String workingDir = BashRunConfigUtil.findWorkingDir(this.runConfig);
    GeneralCommandLine cmd = BashRunConfigUtil.createCommandLine(workingDir, this.runConfig);
    
    BashLocalTerminalRunner myRunner = new BashLocalTerminalRunner(this.runConfig.getProject(), this.runConfig.getScriptName(), cmd);
    myRunner.run();
    if (new BashTerminalExecutionResult(myRunner.getProcessHandler()) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/runner/terminal/BashTerminalCommandLineState", "execute" }));  return new BashTerminalExecutionResult(myRunner.getProcessHandler());
  }

  
  @Nullable
  protected ConsoleView createConsole(@NotNull Executor executor) throws ExecutionException {
    if (executor == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "executor", "com/ansorgit/plugins/bash/runner/terminal/BashTerminalCommandLineState", "createConsole" }));  return null;
  }

  
  @NotNull
  protected ProcessHandler startProcess() throws ExecutionException {
    throw new UnsupportedOperationException("not supported by terminal implementation");
  }
  
  private static class BashTerminalExecutionResult implements ExecutionResult {
    private final ProcessHandler processHandler;
    
    public BashTerminalExecutionResult(ProcessHandler processHandler) {
      this.processHandler = processHandler;
    }

    
    public ExecutionConsole getExecutionConsole() {
      return null;
    }

    
    public AnAction[] getActions() {
      return new AnAction[0];
    }

    
    public ProcessHandler getProcessHandler() {
      return this.processHandler;
    }
  }
}
