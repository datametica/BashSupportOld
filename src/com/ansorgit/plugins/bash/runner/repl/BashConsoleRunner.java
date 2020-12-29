package com.ansorgit.plugins.bash.runner.repl;

import com.ansorgit.plugins.bash.file.BashFileType;
import com.ansorgit.plugins.bash.lang.psi.api.BashFile;
import com.ansorgit.plugins.bash.util.BashInterpreterDetection;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.console.LanguageConsoleImpl;
import com.intellij.execution.console.LanguageConsoleView;
import com.intellij.execution.console.ProcessBackedConsoleExecuteActionHandler;
import com.intellij.execution.process.ColoredProcessHandler;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.runners.AbstractConsoleRunnerWithHistory;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

















public class BashConsoleRunner
  extends AbstractConsoleRunnerWithHistory<LanguageConsoleView>
{
  public BashConsoleRunner(Project myProject, String workingDir) {
    super(myProject, "Bash", workingDir);
  }

  
  protected LanguageConsoleView createConsoleView() {
    LanguageConsoleImpl consoleView = new LanguageConsoleImpl(getProject(), "Bash", BashFileType.BASH_LANGUAGE);
    consoleView.getFile().putUserData(BashFile.LANGUAGE_CONSOLE_MARKER, Boolean.valueOf(true));
    
    return (LanguageConsoleView)consoleView;
  }

  
  protected Process createProcess() throws ExecutionException {
    String bashLocation = BashInterpreterDetection.instance().findBestLocation();
    if (bashLocation == null) {
      throw new ExecutionException("Could not locate the bash executable");
    }
    
    GeneralCommandLine commandLine = (new GeneralCommandLine()).withWorkDirectory(getWorkingDir());
    commandLine.setExePath(bashLocation);
    return commandLine.createProcess();
  }

  
  protected OSProcessHandler createProcessHandler(Process process) {
    return (OSProcessHandler)new ColoredProcessHandler(process, null);
  }

  
  @NotNull
  protected ProcessBackedConsoleExecuteActionHandler createExecuteActionHandler() {
    if (new ProcessBackedConsoleExecuteActionHandler(getProcessHandler(), true) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/runner/repl/BashConsoleRunner", "createExecuteActionHandler" }));  return new ProcessBackedConsoleExecuteActionHandler(getProcessHandler(), true);
  }
}
