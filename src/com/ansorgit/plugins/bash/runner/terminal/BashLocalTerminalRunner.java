package com.ansorgit.plugins.bash.runner.terminal;

import com.google.common.collect.Maps;
import com.intellij.execution.configurations.EncodingEnvironmentUtil;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.openapi.project.Project;
import com.pty4j.PtyProcess;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.terminal.LocalTerminalDirectRunner;



public class BashLocalTerminalRunner
  extends LocalTerminalDirectRunner
{
  private final String scriptName;
  private final GeneralCommandLine cmd;
  private volatile ProcessHandler processHandler;
  
  public BashLocalTerminalRunner(Project project, String scriptName, GeneralCommandLine cmd) {
    super(project);
    this.scriptName = scriptName;
    this.cmd = cmd;
  }

  
  public String runningTargetName() {
    return "BashSupport " + this.scriptName;
  }

  
  protected String getTerminalConnectionName(PtyProcess process) {
    return "BashSupport " + this.scriptName;
  }

  
  public String[] getCommand() {
    String exePath = this.cmd.getExePath();
    return (String[])this.cmd.getCommandLineList(exePath).toArray((Object[])new String[0]);
  }


  
  protected PtyProcess createProcess(@Nullable String directory) throws ExecutionException {
    Map<String, String> env = Maps.newHashMap();
    if (this.cmd.isPassParentEnvironment()) {
      env.putAll(this.cmd.getParentEnvironment());
    }
    env.putAll(this.cmd.getEnvironment());
    
    env.put("TERM", "xterm-256color");
    EncodingEnvironmentUtil.setLocaleEnvironmentIfMac(env, this.cmd.getCharset());
    
    try {
      return PtyProcess.exec(getCommand(), env, (directory != null) ? directory : this.cmd.getWorkDirectory().getAbsolutePath(), true);
    } catch (IOException e) {
      throw new ExecutionException(e);
    } 
  }
  
  public ProcessHandler getProcessHandler() {
    return this.processHandler;
  }

  
  protected ProcessHandler createProcessHandler(PtyProcess process) {
    if (this.processHandler == null) {
      this.processHandler = super.createProcessHandler(process);
    }
    return this.processHandler;
  }
}
