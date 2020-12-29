package com.ansorgit.plugins.bash.settings;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import java.awt.Component;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;





public class BashProjectSettingsPane
  extends WithProject
  implements Disposable
{
  JCheckBox useTerminalPlugin;
  JCheckBox validateWithCurrentEnv;
  private JPanel settingsPane;
  private JTextArea globalVarList;
  private JCheckBox globalVarAutocompletion;
  private JCheckBox bash4Support;
  private JCheckBox autocompleteInternalVars;
  private JCheckBox autocompleteInternalCommands;
  private JCheckBox enableFormatterCheckbox;
  private JCheckBox autocompletePathCommands;
  private JCheckBox globalFunctionVarDefs;
  private JCheckBox enableEvalEscapesCheckbox;
  private TextFieldWithBrowseButton interpreterPath;
  private JCheckBox enableVariableFolding;
  
  public BashProjectSettingsPane(Project project) {
    super(project);


































































































    
    $$$setupUI$$$();
  }
  
  public void dispose() {
    this.settingsPane = null;
    this.globalVarList = null;
    this.bash4Support = null;
    this.autocompleteInternalVars = null;
    this.autocompleteInternalCommands = null;
    this.enableFormatterCheckbox = null;
    this.autocompletePathCommands = null;
    this.globalFunctionVarDefs = null;
    this.enableEvalEscapesCheckbox = null;
    this.validateWithCurrentEnv = null;
    this.enableVariableFolding = null;
  }
  
  public void setData(BashProjectSettings settings) {
    this.globalVarAutocompletion.setSelected(settings.isAutcompleteGlobalVars());
    this.globalVarList.setText(joinGlobalVarList(settings.getGlobalVariables()));
    this.bash4Support.setSelected(settings.isSupportBash4());
    this.autocompleteInternalCommands.setSelected(settings.isAutocompleteBuiltinCommands());
    this.autocompleteInternalVars.setSelected(settings.isAutocompleteBuiltinVars());
    this.autocompletePathCommands.setSelected(settings.isAutocompletePathCommands());
    this.enableFormatterCheckbox.setSelected(settings.isFormatterEnabled());
    this.enableEvalEscapesCheckbox.setSelected(settings.isEvalEscapesEnabled());
    this.globalFunctionVarDefs.setSelected(settings.isGlobalFunctionVarDefs());
    this.validateWithCurrentEnv.setSelected(settings.isValidateWithCurrentEnv());
    this.interpreterPath.setText(settings.getProjectInterpreter());
    this.useTerminalPlugin.setSelected(settings.isUseTerminalPlugin());
    this.enableVariableFolding.setSelected(settings.isVariableFolding());
  }
  
  public void storeSettings(BashProjectSettings settings) {
    settings.setAutcompleteGlobalVars(this.globalVarAutocompletion.isSelected());
    settings.setGlobalVariables(splitGlobalVarList(this.globalVarList.getText()));
    settings.setSupportBash4(this.bash4Support.isSelected());
    settings.setAutocompleteBuiltinCommands(this.autocompleteInternalCommands.isSelected());
    settings.setAutocompleteBuiltinVars(this.autocompleteInternalVars.isSelected());
    settings.setFormatterEnabled(this.enableFormatterCheckbox.isSelected());
    settings.setEvalEscapesEnabled(this.enableEvalEscapesCheckbox.isSelected());
    settings.setAutocompletePathCommands(this.autocompletePathCommands.isSelected());
    settings.setGlobalFunctionVarDefs(this.globalFunctionVarDefs.isSelected());
    settings.setValidateWithCurrentEnv(this.validateWithCurrentEnv.isSelected());
    settings.setProjectInterpreter(this.interpreterPath.getText());
    settings.setUseTerminalPlugin(this.useTerminalPlugin.isSelected());
    settings.setVariableFolding(this.enableVariableFolding.isSelected());
  }
  
  public boolean isModified(BashProjectSettings settings) {
    return (settings.isAutcompleteGlobalVars() != this.globalVarAutocompletion.isSelected() || !joinGlobalVarList(settings.getGlobalVariables()).equals(this.globalVarList.getText()) || this.bash4Support.isSelected() != settings.isSupportBash4() || this.autocompleteInternalVars.isSelected() != settings.isAutocompleteBuiltinVars() || this.autocompleteInternalCommands.isSelected() != settings.isAutocompleteBuiltinCommands() || this.enableFormatterCheckbox.isSelected() != settings.isFormatterEnabled() || this.enableEvalEscapesCheckbox.isSelected() != settings.isEvalEscapesEnabled() || this.autocompletePathCommands.isSelected() != settings.isAutocompletePathCommands() || this.globalFunctionVarDefs.isSelected() != settings.isGlobalFunctionVarDefs() || this.useTerminalPlugin.isSelected() != settings.isUseTerminalPlugin() || this.validateWithCurrentEnv.isSelected() != settings.isValidateWithCurrentEnv() || !this.interpreterPath.getText().equals(settings.getProjectInterpreter()) || this.enableVariableFolding.isSelected() != settings.isVariableFolding());
  }
  
  public JPanel getPanel() {
    return this.settingsPane;
  }
  
  private String joinGlobalVarList(Collection<String> data) {
    return StringUtil.join(data, "\n");
  }
  
  private Set<String> splitGlobalVarList(String data) {
    if (data.isEmpty())
      return Collections.emptySet(); 
    return new LinkedHashSet<>(Arrays.asList((Object[])data.split("\\n").clone()));
  }
  
  private void createUIComponents() {
    FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleLocalFileDescriptor();
    descriptor.setTitle("Chooser Bash Interpreter...");
    this.interpreterPath = new TextFieldWithBrowseButton(null, this);
    this.interpreterPath.addBrowseFolderListener("Choose Bash Interpreter...", null, this.project, descriptor);
  }
}
