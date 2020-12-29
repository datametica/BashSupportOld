package com.ansorgit.plugins.bash.runner;

import com.ansorgit.plugins.bash.settings.BashProjectSettings;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.util.ProgramParametersUtil;
import org.jetbrains.annotations.NotNull;





















public final class BashRunConfigUtil
{
  @NotNull
  public static GeneralCommandLine createCommandLine(String workingDir, BashRunConfiguration runConfig) {
    String interpreterPath;
    if (runConfig.isUseProjectInterpreter()) {
      interpreterPath = BashProjectSettings.storedSettings(runConfig.getProject()).getProjectInterpreter();
    } else {
      interpreterPath = runConfig.getInterpreterPath();
    } 
    
    GeneralCommandLine cmd = new GeneralCommandLine();
    cmd.setExePath(interpreterPath);
    cmd.getParametersList().addParametersString(runConfig.getInterpreterOptions());
    
    cmd.addParameter(runConfig.getScriptName());
    cmd.getParametersList().addParametersString(runConfig.getProgramParameters());
    
    cmd.withWorkDirectory(workingDir);
    cmd.withParentEnvironmentType(runConfig.isPassParentEnvs() ? GeneralCommandLine.ParentEnvironmentType.CONSOLE : GeneralCommandLine.ParentEnvironmentType.NONE);
    cmd.withEnvironment(runConfig.getEnvs());
    if (cmd == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/runner/BashRunConfigUtil", "createCommandLine" }));  return cmd;
  }
  
  public static String findWorkingDir(BashRunConfiguration runConfig) {
    return ProgramParametersUtil.getWorkingDir(runConfig, runConfig.getProject(), runConfig.getConfigurationModule().getModule());
  }
}
