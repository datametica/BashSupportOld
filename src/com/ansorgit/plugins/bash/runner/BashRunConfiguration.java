package com.ansorgit.plugins.bash.runner;

import com.ansorgit.plugins.bash.runner.terminal.BashTerminalRunConfigurationService;
import com.ansorgit.plugins.bash.settings.BashProjectSettings;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configuration.AbstractRunConfiguration;
import com.intellij.execution.configuration.EnvironmentVariablesComponent;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RunConfigurationModule;
import com.intellij.execution.configurations.RunConfigurationWithSuppressedDefaultDebugAction;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.configurations.RuntimeConfigurationError;
import com.intellij.execution.configurations.RuntimeConfigurationException;
import com.intellij.execution.configurations.RuntimeConfigurationWarning;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.util.ProgramParametersUtil;
import com.intellij.openapi.components.ComponentManager;
import com.intellij.openapi.components.PathMacroManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.DefaultJDOMExternalizer;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.JDOMExternalizerUtil;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.openapi.util.text.StringUtil;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import org.apache.commons.lang.StringUtils;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;











public class BashRunConfiguration
  extends AbstractRunConfiguration
  implements BashRunConfigurationParams, RunConfigurationWithSuppressedDefaultDebugAction
{
  private String interpreterOptions = "";
  private String workingDirectory = "";
  private String interpreterPath = "";
  private boolean useProjectInterpreter = false;
  private String scriptName;
  private String programsParameters;
  
  BashRunConfiguration(String name, RunConfigurationModule module, ConfigurationFactory configurationFactory) {
    super(name, module, configurationFactory);
  }

  
  public Collection<Module> getValidModules() {
    return getAllModules();
  }

  
  public boolean isCompileBeforeLaunchAddedByDefault() {
    return false;
  }

  
  public boolean excludeCompileBeforeLaunchOption() {
    return false;
  }
  
  @NotNull
  public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
    if (new BashRunConfigurationEditor(getConfigurationModule().getModule()) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/runner/BashRunConfiguration", "getConfigurationEditor" }));  return new BashRunConfigurationEditor(getConfigurationModule().getModule());
  }
  
  @Nullable
  public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment env) throws ExecutionException {
    if (executor == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "executor", "com/ansorgit/plugins/bash/runner/BashRunConfiguration", "getState" }));  if (env == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "env", "com/ansorgit/plugins/bash/runner/BashRunConfiguration", "getState" }));  BashTerminalRunConfigurationService service = (BashTerminalRunConfigurationService)ServiceManager.getService(BashTerminalRunConfigurationService.class);
    if (service != null && BashProjectSettings.storedSettings(getProject()).isUseTerminalPlugin())
    {
      return service.getState(this, executor, env);
    }
    
    BashCommandLineState state = new BashCommandLineState(this, env);
    state.getConsoleBuilder().addFilter(new BashLineErrorFilter(getProject()));
    return (RunProfileState)state;
  }
  
  public String getInterpreterPath() {
    return this.interpreterPath;
  }
  
  public void setInterpreterPath(String path) {
    this.interpreterPath = path;
  }

  
  public boolean isUseProjectInterpreter() {
    return this.useProjectInterpreter;
  }

  
  public void setUseProjectInterpreter(boolean useProjectInterpreter) {
    this.useProjectInterpreter = useProjectInterpreter;
  }

  
  public void checkConfiguration() throws RuntimeConfigurationException {
    super.checkConfiguration();
    
    Project project = getProject();
    
    Module module = getConfigurationModule().getModule();
    if (module != null)
    {
      ProgramParametersUtil.checkWorkingDirectoryExist(this, project, module);
    }
    
    if (this.useProjectInterpreter) {
      Path path; BashProjectSettings settings = BashProjectSettings.storedSettings(project);
      String interpreter = settings.getProjectInterpreter();
      if (interpreter.isEmpty()) {
        throw new RuntimeConfigurationError("No project interpreter configured");
      }

      
      try {
        path = Paths.get(interpreter, new String[0]);
      } catch (InvalidPathException e) {
        path = null;
      } 
      if (path == null || !Files.isRegularFile(path, new java.nio.file.LinkOption[0]) || !Files.isReadable(path))
        throw new RuntimeConfigurationWarning("Project interpreter path is invalid or not readable."); 
    } else {
      Path interpreterFile;
      if (StringUtil.isEmptyOrSpaces(this.interpreterPath)) {
        throw new RuntimeConfigurationException("No interpreter path given.");
      }

      
      try {
        interpreterFile = Paths.get(this.interpreterPath, new String[0]);
      } catch (InvalidPathException e) {
        interpreterFile = null;
      } 

      
      if (interpreterFile == null || !Files.isRegularFile(interpreterFile, new java.nio.file.LinkOption[0]) || !Files.isReadable(interpreterFile)) {
        throw new RuntimeConfigurationWarning("Interpreter path is invalid or not readable.");
      }
    } 
    
    if (StringUtil.isEmptyOrSpaces(this.scriptName)) {
      throw new RuntimeConfigurationError("Script name not given.");
    }
  }

  
  public String suggestedName() {
    if (this.scriptName == null || this.scriptName.isEmpty()) {
      return null;
    }
    
    try {
      Path fileName = Paths.get(this.scriptName, new String[0]).getFileName();
      if (fileName == null) {
        return null;
      }
      
      String name = fileName.toString();
      
      int ind = name.lastIndexOf('.');
      if (ind != -1) {
        return name.substring(0, ind);
      }
      return name;
    } catch (InvalidPathException e) {
      return null;
    } 
  }

  
  public void readExternal(Element element) throws InvalidDataException {
    PathMacroManager.getInstance((ComponentManager)getProject()).expandPaths(element);
    super.readExternal(element);
    
    DefaultJDOMExternalizer.readExternal(this, element);
    readModule(element);
    EnvironmentVariablesComponent.readExternal(element, getEnvs());

    
    this.interpreterOptions = JDOMExternalizerUtil.readField(element, "INTERPRETER_OPTIONS");
    this.interpreterPath = JDOMExternalizerUtil.readField(element, "INTERPRETER_PATH");
    this.workingDirectory = JDOMExternalizerUtil.readField(element, "WORKING_DIRECTORY");


    
    String useProjectInterpreterValue = JDOMExternalizerUtil.readField(element, "PROJECT_INTERPRETER");
    String oldUseProjectInterpreterValue = JDOMExternalizerUtil.readField(element, "USE_PROJECT_INTERPRETER");
    if (useProjectInterpreterValue != null) {
      this.useProjectInterpreter = Boolean.parseBoolean(useProjectInterpreterValue);
    } else if (StringUtils.isEmpty(this.interpreterPath) && oldUseProjectInterpreterValue != null) {
      
      Project project = getProject();
      if (!BashProjectSettings.storedSettings(project).getProjectInterpreter().isEmpty()) {
        this.useProjectInterpreter = Boolean.parseBoolean(oldUseProjectInterpreterValue);
      }
    } 
    
    String parentEnvValue = JDOMExternalizerUtil.readField(element, "PARENT_ENVS");
    if (parentEnvValue != null) {
      setPassParentEnvs(Boolean.parseBoolean(parentEnvValue));
    }

    
    this.scriptName = JDOMExternalizerUtil.readField(element, "SCRIPT_NAME");
    setProgramParameters(JDOMExternalizerUtil.readField(element, "PARAMETERS"));
  }

  
  public void writeExternal(Element element) throws WriteExternalException {
    super.writeExternal(element);

    
    JDOMExternalizerUtil.writeField(element, "INTERPRETER_OPTIONS", this.interpreterOptions);
    JDOMExternalizerUtil.writeField(element, "INTERPRETER_PATH", this.interpreterPath);
    JDOMExternalizerUtil.writeField(element, "PROJECT_INTERPRETER", Boolean.toString(this.useProjectInterpreter));
    JDOMExternalizerUtil.writeField(element, "WORKING_DIRECTORY", this.workingDirectory);
    JDOMExternalizerUtil.writeField(element, "PARENT_ENVS", Boolean.toString(isPassParentEnvs()));

    
    JDOMExternalizerUtil.writeField(element, "SCRIPT_NAME", this.scriptName);
    JDOMExternalizerUtil.writeField(element, "PARAMETERS", getProgramParameters());

    
    DefaultJDOMExternalizer.writeExternal(this, element);
    writeModule(element);
    EnvironmentVariablesComponent.writeExternal(element, getEnvs());
    
    PathMacroManager.getInstance((ComponentManager)getProject()).collapsePathsRecursively(element);
  }
  
  public String getInterpreterOptions() {
    return this.interpreterOptions;
  }
  
  public void setInterpreterOptions(String interpreterOptions) {
    this.interpreterOptions = interpreterOptions;
  }
  
  public String getWorkingDirectory() {
    return this.workingDirectory;
  }
  
  public void setWorkingDirectory(String workingDirectory) {
    this.workingDirectory = workingDirectory;
  }
  
  @Nullable
  public String getProgramParameters() {
    return this.programsParameters;
  }
  
  public void setProgramParameters(@Nullable String programParameters) {
    this.programsParameters = programParameters;
  }
  
  public String getScriptName() {
    return this.scriptName;
  }
  
  public void setScriptName(String scriptName) {
    this.scriptName = scriptName;
  }
}
