package com.ansorgit.plugins.bash.runner;

import com.intellij.execution.CommonProgramRunConfigurationParameters;

public interface BashRunConfigurationParams extends CommonProgramRunConfigurationParameters {
  boolean isUseProjectInterpreter();
  
  void setUseProjectInterpreter(boolean paramBoolean);
  
  String getInterpreterPath();
  
  void setInterpreterPath(String paramString);
  
  String getInterpreterOptions();
  
  void setInterpreterOptions(String paramString);
  
  String getScriptName();
  
  void setScriptName(String paramString);
}
