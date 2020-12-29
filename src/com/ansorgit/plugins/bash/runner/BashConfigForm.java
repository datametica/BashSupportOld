package com.ansorgit.plugins.bash.runner;

import com.intellij.execution.ui.CommonProgramParametersPanel;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.ui.MacroAwareTextBrowseFolderListener;
import com.intellij.ui.PanelWithAnchor;
import com.intellij.ui.RawCommandLineEditor;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.util.ui.UIUtil;
import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;















public class BashConfigForm
  extends CommonProgramParametersPanel
{
  private LabeledComponent<RawCommandLineEditor> interpreterParametersComponent;
  private LabeledComponent<JComponent> interpreterPathComponent;
  private TextFieldWithBrowseButton interpreterPathField;
  private JBCheckBox projectInterpreterCheckbox;
  private LabeledComponent<JBCheckBox> projectInterpreterLabeled;
  private LabeledComponent<JComponent> scriptNameComponent;
  private TextFieldWithBrowseButton scriptNameField;
  
  public void setAnchor(JComponent anchor) {
    super.setAnchor(anchor);
    this.projectInterpreterCheckbox.setAnchor(anchor);
    this.interpreterParametersComponent.setAnchor(anchor);
    this.interpreterPathComponent.setAnchor(anchor);
    this.scriptNameComponent.setAnchor(anchor);
  }

  
  protected void setupAnchor() {
    super.setupAnchor();
    this.myAnchor = UIUtil.mergeComponentsWithAnchor(new PanelWithAnchor[] { (PanelWithAnchor)this, (PanelWithAnchor)this.projectInterpreterLabeled, (PanelWithAnchor)this.interpreterParametersComponent, (PanelWithAnchor)this.interpreterPathComponent, (PanelWithAnchor)this.scriptNameComponent });
  }
  
  protected void initOwnComponents() {
    Project project = getProject();
    
    FileChooserDescriptor chooseInterpreterDescriptor = FileChooserDescriptorFactory.createSingleLocalFileDescriptor();
    chooseInterpreterDescriptor.setTitle("Choose Interpreter...");
    
    this.interpreterPathField = new TextFieldWithBrowseButton();
    this.interpreterPathField.addBrowseFolderListener((TextBrowseFolderListener)new MacroAwareTextBrowseFolderListener(chooseInterpreterDescriptor, project));
    
    this.interpreterPathComponent = LabeledComponent.create(createComponentWithMacroBrowse(this.interpreterPathField), "Interpreter path:");
    this.interpreterPathComponent.setLabelLocation("West");
    
    this.projectInterpreterCheckbox = new JBCheckBox();
    this.projectInterpreterCheckbox.setToolTipText("If enabled then the interpreter path configured in the project settings will be used instead of a custom location.");
    this.projectInterpreterCheckbox.addChangeListener(e -> {
          boolean selected = this.projectInterpreterCheckbox.isSelected();
          UIUtil.setEnabled((Component)this.interpreterPathComponent, !selected, true);
        });
    this.projectInterpreterLabeled = LabeledComponent.create((JComponent)this.projectInterpreterCheckbox, "Use project interpreter");
    this.projectInterpreterLabeled.setLabelLocation("West");
    
    this.interpreterParametersComponent = LabeledComponent.create((JComponent)new RawCommandLineEditor(), "Interpreter options");
    this.interpreterParametersComponent.setLabelLocation("West");
    
    this.scriptNameField = new TextFieldWithBrowseButton();
    this.scriptNameField.addBrowseFolderListener((TextBrowseFolderListener)new MacroAwareTextBrowseFolderListener(FileChooserDescriptorFactory.createSingleLocalFileDescriptor(), project));
    
    this.scriptNameComponent = LabeledComponent.create(createComponentWithMacroBrowse(this.scriptNameField), "Script:");
    this.scriptNameComponent.setLabelLocation("West");
  }

  
  protected void addComponents() {
    initOwnComponents();
    
    add((Component)this.scriptNameComponent);
    add((Component)this.projectInterpreterLabeled);
    add((Component)this.interpreterPathComponent);
    add((Component)this.interpreterParametersComponent);
    
    super.addComponents();
  }
  
  public void resetFormTo(BashRunConfiguration configuration) {
    this.projectInterpreterCheckbox.setSelected(configuration.isUseProjectInterpreter());
    this.interpreterPathField.setText(configuration.getInterpreterPath());
    ((RawCommandLineEditor)this.interpreterParametersComponent.getComponent()).setText(configuration.getInterpreterOptions());
    this.scriptNameField.setText(configuration.getScriptName());
  }
  
  public void applySettingsTo(BashRunConfiguration configuration) {
    configuration.setUseProjectInterpreter(this.projectInterpreterCheckbox.isSelected());
    configuration.setInterpreterPath(this.interpreterPathField.getText());
    configuration.setInterpreterOptions(((RawCommandLineEditor)this.interpreterParametersComponent.getComponent()).getText());
    configuration.setScriptName(this.scriptNameField.getText());
  }
}
