package com.ansorgit.plugins.bash.runner;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.CommandLineState;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.KillableColoredProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.process.ProcessTerminatedListener;
import com.intellij.execution.runners.ExecutionEnvironment;
import org.jetbrains.annotations.NotNull;















class BashCommandLineState
  extends CommandLineState
{
  private final BashRunConfiguration runConfig;
  
  BashCommandLineState(BashRunConfiguration runConfig, ExecutionEnvironment environment) {
    super(environment);
    this.runConfig = runConfig;
  }

  
  @NotNull
  protected ProcessHandler startProcess() throws ExecutionException {
    String workingDir = BashRunConfigUtil.findWorkingDir(this.runConfig);
    GeneralCommandLine cmd = BashRunConfigUtil.createCommandLine(workingDir, this.runConfig);
    if (!cmd.getEnvironment().containsKey("TERM")) {
      cmd.getEnvironment().put("TERM", "xterm-256color");
    }
    
    KillableColoredProcessHandler killableColoredProcessHandler = new KillableColoredProcessHandler(cmd);
    ProcessTerminatedListener.attach((ProcessHandler)killableColoredProcessHandler, getEnvironment().getProject());

    
    if (killableColoredProcessHandler == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/runner/BashCommandLineState", "startProcess" }));  return (ProcessHandler)killableColoredProcessHandler;
  }
}
