package com.ansorgit.plugins.bash.editor.inspections.inspections;

import com.ansorgit.plugins.bash.editor.inspections.quickfix.RegisterShebangCommandQuickfix;
import com.ansorgit.plugins.bash.editor.inspections.quickfix.ReplaceShebangQuickfix;
import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.BashShebang;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.apache.commons.lang.StringUtils;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;


















public class FixShebangInspection
  extends LocalInspectionTool
{
  private static final List<String> DEFAULT_COMMANDS = Lists.newArrayList((Object[])new String[] { "/bin/bash", "/bin/sh" });
  private static final List<String> VALID_ENV_SHELLS = Lists.newArrayList((Object[])new String[] { "bash", "sh" });
  
  private static final String ELEMENT_NAME_SHEBANG = "shebang";
  private List<String> validShebangCommands = DEFAULT_COMMANDS;

  
  public JComponent createOptionsPanel() {
    FixShebangSettings settings = new FixShebangSettings();
    JTextArea textArea = settings.getValidCommandsTextArea();
    textArea.setText(Joiner.on('\n').join(this.validShebangCommands));
    
    textArea.getDocument().addDocumentListener(new DocumentListener()
        {
          public void insertUpdate(DocumentEvent documentEvent) {
            FixShebangInspection.this.updateShebangLines(documentEvent);
          }

          
          public void removeUpdate(DocumentEvent documentEvent) {
            FixShebangInspection.this.updateShebangLines(documentEvent);
          }

          
          public void changedUpdate(DocumentEvent documentEvent) {
            FixShebangInspection.this.updateShebangLines(documentEvent);
          }
        });
    
    return settings.getSettingsPanel();
  }
  
  private void updateShebangLines(DocumentEvent documentEvent) {
    this.validShebangCommands.clear();
    try {
      Document doc = documentEvent.getDocument();
      for (String item : doc.getText(0, doc.getLength()).split("\n")) {
        if (item.trim().length() != 0) {
          this.validShebangCommands.add(item);
        }
      } 
    } catch (BadLocationException e) {
      throw new RuntimeException("Could not save shebang inspection settings input", e);
    } 
  }

  
  public void readSettings(@NotNull Element node) throws InvalidDataException {
    if (node == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "node", "com/ansorgit/plugins/bash/editor/inspections/inspections/FixShebangInspection", "readSettings" }));  this.validShebangCommands = Lists.newLinkedList();
    
    List<Element> shebangs = node.getChildren("shebang");
    for (Element shebang : shebangs) {
      this.validShebangCommands.add(shebang.getText());
    }
    
    if (this.validShebangCommands.isEmpty()) {
      this.validShebangCommands.addAll(DEFAULT_COMMANDS);
    }
  }

  
  public void writeSettings(@NotNull Element node) throws WriteExternalException {
    if (node == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "node", "com/ansorgit/plugins/bash/editor/inspections/inspections/FixShebangInspection", "writeSettings" }));  for (String shebangCommand : this.validShebangCommands) {
      Element shebandElement = new Element("shebang");
      shebandElement.setText(shebangCommand.trim());
      node.addContent(shebandElement);
    } 
  }

  
  @NotNull
  public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, final boolean isOnTheFly) {
    if (holder == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "holder", "com/ansorgit/plugins/bash/editor/inspections/inspections/FixShebangInspection", "buildVisitor" }));  if (new BashVisitor()
      {
        public void visitShebang(BashShebang shebang) {
          String shellCommand = shebang.shellCommand(false);
          String shellCommandWithParams = shebang.shellCommand(true);
          
          if (FixShebangInspection.this.validShebangCommands.contains(shellCommand) || FixShebangInspection.this.validShebangCommands.contains(shellCommandWithParams)) {
            return;
          }
          
          if ("/usr/bin/env".equals(shellCommand))
          
          { 
            String paramString = shebang.shellCommandParams();
            String[] params = StringUtils.split(paramString, ' ');
            boolean noShellParam = (params == null || params.length == 0);
            boolean invalidShell = (params != null && params.length > 0 && !FixShebangInspection.VALID_ENV_SHELLS.contains(params[0]));
            
            if (noShellParam || invalidShell) {
              List<LocalQuickFix> quickFixes = Lists.newLinkedList();
              
              if (invalidShell) {
                quickFixes.add(new RegisterShebangCommandQuickfix(FixShebangInspection.this, shebang));
              }
              
              for (String validCommand : FixShebangInspection.VALID_ENV_SHELLS) {
                quickFixes.add(new ReplaceShebangQuickfix(shebang, "/usr/bin/env " + validCommand, shebang.commandAndParamsRange()));
              }
              
              String message = noShellParam ? "'/usr/bin/env' needs a shell parameter" : "Unknown shell for /usr/bin/env";
              holder.registerProblem((PsiElement)shebang, message, ProblemHighlightType.GENERIC_ERROR_OR_WARNING, shebang
                  
                  .commandAndParamsRange(), quickFixes
                  .<LocalQuickFix>toArray(new LocalQuickFix[quickFixes.size()]));
            }  }
          else
          { List<LocalQuickFix> quickFixes = Lists.newLinkedList();
            
            if (isOnTheFly) {
              quickFixes.add(new RegisterShebangCommandQuickfix(FixShebangInspection.this, shebang));
            }
            
            for (String validCommand : FixShebangInspection.this.validShebangCommands) {
              if (!validCommand.equals(shellCommand) && !validCommand.equals(shellCommandWithParams)) {
                quickFixes.add(new ReplaceShebangQuickfix(shebang, validCommand));
              }
            } 
            
            holder.registerProblem((PsiElement)shebang, "Unknown shebang command", ProblemHighlightType.GENERIC_ERROR_OR_WARNING, shebang.commandRange(), quickFixes.<LocalQuickFix>toArray(new LocalQuickFix[quickFixes.size()])); }  } } == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/inspections/inspections/FixShebangInspection", "buildVisitor" }));  return (PsiElementVisitor)new BashVisitor() { public void visitShebang(BashShebang shebang) { String shellCommand = shebang.shellCommand(false); String shellCommandWithParams = shebang.shellCommand(true); if (FixShebangInspection.this.validShebangCommands.contains(shellCommand) || FixShebangInspection.this.validShebangCommands.contains(shellCommandWithParams)) return;  if ("/usr/bin/env".equals(shellCommand)) { String paramString = shebang.shellCommandParams(); String[] params = StringUtils.split(paramString, ' '); boolean noShellParam = (params == null || params.length == 0); boolean invalidShell = (params != null && params.length > 0 && !FixShebangInspection.VALID_ENV_SHELLS.contains(params[0])); if (noShellParam || invalidShell) { List<LocalQuickFix> quickFixes = Lists.newLinkedList(); if (invalidShell) quickFixes.add(new RegisterShebangCommandQuickfix(FixShebangInspection.this, shebang));  for (String validCommand : FixShebangInspection.VALID_ENV_SHELLS) quickFixes.add(new ReplaceShebangQuickfix(shebang, "/usr/bin/env " + validCommand, shebang.commandAndParamsRange()));  String message = noShellParam ? "'/usr/bin/env' needs a shell parameter" : "Unknown shell for /usr/bin/env"; holder.registerProblem((PsiElement)shebang, message, ProblemHighlightType.GENERIC_ERROR_OR_WARNING, shebang.commandAndParamsRange(), quickFixes.<LocalQuickFix>toArray(new LocalQuickFix[quickFixes.size()])); }  } else { List<LocalQuickFix> quickFixes = Lists.newLinkedList(); if (isOnTheFly) quickFixes.add(new RegisterShebangCommandQuickfix(FixShebangInspection.this, shebang));  for (String validCommand : FixShebangInspection.this.validShebangCommands) { if (!validCommand.equals(shellCommand) && !validCommand.equals(shellCommandWithParams)) quickFixes.add(new ReplaceShebangQuickfix(shebang, validCommand));  }  holder.registerProblem((PsiElement)shebang, "Unknown shebang command", ProblemHighlightType.GENERIC_ERROR_OR_WARNING, shebang.commandRange(), quickFixes.<LocalQuickFix>toArray(new LocalQuickFix[quickFixes.size()])); }
           }
         }
      ;
  }
  
  public void registerShebangCommand(String command) {
    this.validShebangCommands.add(command);
  }
}
