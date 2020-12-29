package com.ansorgit.plugins.bash.settings;

import com.ansorgit.plugins.bash.util.BashIcons;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import javax.swing.Icon;
import javax.swing.JComponent;
import org.jetbrains.annotations.Nls;

















public class BashProjectSettingsConfigurable
  implements Configurable
{
  private final Project project;
  private BashProjectSettingsPane settingsPanel;
  
  public BashProjectSettingsConfigurable(Project project) {
    this.project = project;
  }
  
  @Nls
  public String getDisplayName() {
    return "BashSupport";
  }
  
  public Icon getIcon() {
    return BashIcons.BASH_FILE_ICON;
  }
  
  public String getHelpTopic() {
    return null;
  }
  
  public JComponent createComponent() {
    if (this.settingsPanel == null) {
      this.settingsPanel = new BashProjectSettingsPane(this.project);
    }
    
    return this.settingsPanel.getPanel();
  }
  
  public boolean isModified() {
    if (this.project == null) {
      return false;
    }
    
    return (this.settingsPanel != null && this.settingsPanel.isModified(BashProjectSettings.storedSettings(this.project)));
  }
  
  public void apply() throws ConfigurationException {
    this.settingsPanel.storeSettings(BashProjectSettings.storedSettings(this.project));
  }
  
  public void reset() {
    this.settingsPanel.setData(BashProjectSettings.storedSettings(this.project));
  }
  
  public void disposeUIResources() {
    if (this.settingsPanel != null) {
      Disposer.dispose(this.settingsPanel);
      this.settingsPanel = null;
    } 
  }
}
