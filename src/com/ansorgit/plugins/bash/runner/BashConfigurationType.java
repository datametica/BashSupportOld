package com.ansorgit.plugins.bash.runner;

import com.ansorgit.plugins.bash.util.BashIcons;
import com.intellij.execution.RunManagerEx;
import com.intellij.execution.configuration.ConfigurationFactoryEx;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.ConfigurationTypeBase;
import com.intellij.execution.configurations.ConfigurationTypeUtil;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RunConfigurationModule;
import com.intellij.openapi.project.PossiblyDumbAware;
import com.intellij.openapi.project.Project;
import java.util.Collections;
import org.jetbrains.annotations.NotNull;



















public class BashConfigurationType
  extends ConfigurationTypeBase
  implements PossiblyDumbAware
{
  public BashConfigurationType() {
    super("BashConfigurationType", "Bash", "Bash run configuration", BashIcons.BASH_FILE_ICON);
    
    addFactory((ConfigurationFactory)new BashConfigurationFactory(this));
  }
  
  @NotNull
  public static BashConfigurationType getInstance() {
    if ((BashConfigurationType)ConfigurationTypeUtil.findConfigurationType(BashConfigurationType.class) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/runner/BashConfigurationType", "getInstance" }));  return (BashConfigurationType)ConfigurationTypeUtil.findConfigurationType(BashConfigurationType.class);
  }

  
  public boolean isDumbAware() {
    return true;
  }
  
  public static class BashConfigurationFactory extends ConfigurationFactoryEx {
    BashConfigurationFactory(BashConfigurationType configurationType) {
      super((ConfigurationType)configurationType);
    }


    
    public void onNewConfigurationCreated(@NotNull RunConfiguration configuration) {
      if (configuration == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "configuration", "com/ansorgit/plugins/bash/runner/BashConfigurationType$BashConfigurationFactory", "onNewConfigurationCreated" }));  RunManagerEx.getInstanceEx(configuration.getProject()).setBeforeRunTasks(configuration, Collections.emptyList(), false);
    }

    
    @NotNull
    public RunConfiguration createTemplateConfiguration(@NotNull Project project) {
      if (project == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "project", "com/ansorgit/plugins/bash/runner/BashConfigurationType$BashConfigurationFactory", "createTemplateConfiguration" }));  if (new BashRunConfiguration("", new RunConfigurationModule(project), (ConfigurationFactory)this) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/runner/BashConfigurationType$BashConfigurationFactory", "createTemplateConfiguration" }));  return (RunConfiguration)new BashRunConfiguration("", new RunConfigurationModule(project), (ConfigurationFactory)this);
    }
  }
}
