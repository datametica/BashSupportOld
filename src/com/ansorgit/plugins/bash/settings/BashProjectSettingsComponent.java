package com.ansorgit.plugins.bash.settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;

















@State(name = "BashSupportProjectSettings", storages = {@Storage("bashsupport_project.xml")})
public class BashProjectSettingsComponent
  implements PersistentStateComponent<BashProjectSettings>, ProjectComponent
{
  private BashProjectSettings settings = new BashProjectSettings();
  
  public BashProjectSettings getState() {
    return this.settings;
  }
  
  public void loadState(@NotNull BashProjectSettings state) {
    if (state == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "state", "com/ansorgit/plugins/bash/settings/BashProjectSettingsComponent", "loadState" }));  this.settings = state;
  }

  
  public void projectOpened() {}

  
  public void projectClosed() {}
  
  @NotNull
  public String getComponentName() {
    if ("BashSupportProject" == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/settings/BashProjectSettingsComponent", "getComponentName" }));  return "BashSupportProject";
  }

  
  public void initComponent() {}
  
  public void disposeComponent() {
    this.settings = null;
  }
}
