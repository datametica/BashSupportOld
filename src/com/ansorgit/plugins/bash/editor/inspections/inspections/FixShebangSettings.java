package com.ansorgit.plugins.bash.editor.inspections.inspections;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import java.awt.Insets;
import java.awt.LayoutManager;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

























public class FixShebangSettings
{
  private JTextArea validCommandsEdit;
  private JPanel settingsPanel;
  
  public FixShebangSettings() {
    $$$setupUI$$$();
  }
  
  public JPanel getSettingsPanel() {
    return this.settingsPanel;
  }
  
  public JTextArea getValidCommandsTextArea() {
    return this.validCommandsEdit;
  }
}
