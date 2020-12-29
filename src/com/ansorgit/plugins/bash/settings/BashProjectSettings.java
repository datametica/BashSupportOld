package com.ansorgit.plugins.bash.settings;

import com.google.common.collect.Sets;
import com.intellij.openapi.project.Project;
import java.io.Serializable;
import java.util.Set;
import org.jetbrains.annotations.NotNull;



















public class BashProjectSettings
  implements Serializable
{
  private final Set<String> globalVariables = Sets.newConcurrentHashSet();
  
  private boolean autcompleteGlobalVars = true;
  
  private boolean supportBash4 = true;
  
  private boolean autocompleteBuiltinVars = false;
  
  private boolean autocompleteBuiltinCommands = true;
  
  private boolean autocompletePathCommands = true;
  
  private boolean globalFunctionVarDefs = false;
  
  private boolean formatterEnabled = false;
  private boolean evalEscapesEnabled = false;
  private boolean validateWithCurrentEnv = true;
  private boolean useTerminalPlugin = false;
  private boolean variableFolding = false;
  @NotNull
  private String projectInterpreter = "";

  
  public static BashProjectSettings storedSettings(@NotNull Project project) {
    if (project == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "project", "com/ansorgit/plugins/bash/settings/BashProjectSettings", "storedSettings" }));  BashProjectSettingsComponent component = (BashProjectSettingsComponent)project.getComponent(BashProjectSettingsComponent.class);
    if (component == null)
    {
      return new BashProjectSettings();
    }
    
    return component.getState();
  }
  
  public boolean isAutocompleteBuiltinVars() {
    return this.autocompleteBuiltinVars;
  }
  
  public void setAutocompleteBuiltinVars(boolean autocompleteBuiltinVars) {
    this.autocompleteBuiltinVars = autocompleteBuiltinVars;
  }
  
  public boolean isAutocompleteBuiltinCommands() {
    return this.autocompleteBuiltinCommands;
  }
  
  public void setAutocompleteBuiltinCommands(boolean autocompleteBuiltinCommands) {
    this.autocompleteBuiltinCommands = autocompleteBuiltinCommands;
  }
  
  public void addGlobalVariable(String variableName) {
    this.globalVariables.add(variableName);
  }
  
  public void removeGlobalVariable(String varName) {
    this.globalVariables.remove(varName);
  }
  
  public Set<String> getGlobalVariables() {
    return this.globalVariables;
  }
  
  public void setGlobalVariables(Set<String> globalVariables) {
    if (this.globalVariables != globalVariables) {
      this.globalVariables.clear();
      this.globalVariables.addAll(globalVariables);
    } 
  }
  
  public boolean isAutcompleteGlobalVars() {
    return this.autcompleteGlobalVars;
  }
  
  public void setAutcompleteGlobalVars(boolean autcompleteGlobalVars) {
    this.autcompleteGlobalVars = autcompleteGlobalVars;
  }
  
  public boolean isSupportBash4() {
    return this.supportBash4;
  }
  
  public void setSupportBash4(boolean supportBash4) {
    this.supportBash4 = supportBash4;
  }
  
  public boolean isFormatterEnabled() {
    return this.formatterEnabled;
  }
  
  public void setFormatterEnabled(boolean formatterEnabled) {
    this.formatterEnabled = formatterEnabled;
  }
  
  public boolean isEvalEscapesEnabled() {
    return this.evalEscapesEnabled;
  }
  
  public void setEvalEscapesEnabled(boolean evalEscapedEnabled) {
    this.evalEscapesEnabled = evalEscapedEnabled;
  }
  
  public boolean isAutocompletePathCommands() {
    return this.autocompletePathCommands;
  }
  
  public void setAutocompletePathCommands(boolean autocompletePathCommands) {
    this.autocompletePathCommands = autocompletePathCommands;
  }
  
  public boolean isGlobalFunctionVarDefs() {
    return this.globalFunctionVarDefs;
  }
  
  public void setGlobalFunctionVarDefs(boolean globalFunctionVarDefs) {
    this.globalFunctionVarDefs = globalFunctionVarDefs;
  }
  
  public boolean isUseTerminalPlugin() {
    return this.useTerminalPlugin;
  }
  
  public void setUseTerminalPlugin(boolean useTerminalPlugin) {
    this.useTerminalPlugin = useTerminalPlugin;
  }
  
  public boolean isValidateWithCurrentEnv() {
    return this.validateWithCurrentEnv;
  }
  
  public void setValidateWithCurrentEnv(boolean validateWithCurrentEnv) {
    this.validateWithCurrentEnv = validateWithCurrentEnv;
  }
  
  @NotNull
  public String getProjectInterpreter() {
    if (this.projectInterpreter == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/settings/BashProjectSettings", "getProjectInterpreter" }));  return this.projectInterpreter;
  }
  
  public void setProjectInterpreter(@NotNull String projectInterpreter) {
    if (projectInterpreter == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "projectInterpreter", "com/ansorgit/plugins/bash/settings/BashProjectSettings", "setProjectInterpreter" }));  this.projectInterpreter = projectInterpreter;
  }
  
  public boolean isVariableFolding() {
    return this.variableFolding;
  }
  
  public void setVariableFolding(boolean variableFolding) {
    this.variableFolding = variableFolding;
  }
}
