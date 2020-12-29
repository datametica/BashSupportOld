package com.ansorgit.plugins.bash.runner;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import javax.swing.JComponent;
import org.jetbrains.annotations.NotNull;















class BashRunConfigurationEditor
  extends SettingsEditor<BashRunConfiguration>
{
  private BashConfigForm form;
  
  BashRunConfigurationEditor(Module module) {
    this.form = new BashConfigForm();
    this.form.setModuleContext(module);
  }

  
  protected void resetEditorFrom(BashRunConfiguration runConfiguration) {
    this.form.reset(runConfiguration);
    this.form.resetFormTo(runConfiguration);
  }

  
  protected void applyEditorTo(BashRunConfiguration runConfiguration) throws ConfigurationException {
    this.form.applyTo(runConfiguration);
    this.form.applySettingsTo(runConfiguration);
  }

  
  @NotNull
  protected JComponent createEditor() {
    if (this.form == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/runner/BashRunConfigurationEditor", "createEditor" }));  return (JComponent)this.form;
  }

  
  protected void disposeEditor() {
    this.form = null;
  }
}
