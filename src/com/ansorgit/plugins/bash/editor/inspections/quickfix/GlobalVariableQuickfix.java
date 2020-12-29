package com.ansorgit.plugins.bash.editor.inspections.quickfix;

import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVar;
import com.ansorgit.plugins.bash.settings.BashProjectSettings;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.command.UndoConfirmationPolicy;
import com.intellij.openapi.command.undo.GlobalUndoableAction;
import com.intellij.openapi.command.undo.UndoManager;
import com.intellij.openapi.command.undo.UndoableAction;
import com.intellij.openapi.command.undo.UnexpectedUndoException;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.FileContentUtil;
import java.util.Collections;
import org.jetbrains.annotations.NotNull;























public class GlobalVariableQuickfix
  extends AbstractBashPsiElementQuickfix
{
  private final boolean register;
  
  public GlobalVariableQuickfix(BashVar bashVar, boolean register) {
    super((PsiElement)bashVar);
    this.register = register;
  }
  
  @NotNull
  public String getText() {
    if ((this.register ? "Register as global variable" : "Unregister as global variable") == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/inspections/quickfix/GlobalVariableQuickfix", "getText" }));  return this.register ? "Register as global variable" : "Unregister as global variable";
  }

  
  public boolean startInWriteAction() {
    return false;
  }

  
  public void invoke(@NotNull Project project, @NotNull PsiFile file, Editor editor, @NotNull PsiElement startElement, @NotNull PsiElement endElement) {
    if (project == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "project", "com/ansorgit/plugins/bash/editor/inspections/quickfix/GlobalVariableQuickfix", "invoke" }));  if (file == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "file", "com/ansorgit/plugins/bash/editor/inspections/quickfix/GlobalVariableQuickfix", "invoke" }));  if (startElement == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "startElement", "com/ansorgit/plugins/bash/editor/inspections/quickfix/GlobalVariableQuickfix", "invoke" }));  if (endElement == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "endElement", "com/ansorgit/plugins/bash/editor/inspections/quickfix/GlobalVariableQuickfix", "invoke" }));  BashVar variable = (BashVar)startElement;
    String variableName = variable.getReference().getReferencedName();
    
    UndoConfirmationPolicy mode = ApplicationManager.getApplication().isUnitTestMode() ? UndoConfirmationPolicy.DO_NOT_REQUEST_CONFIRMATION : UndoConfirmationPolicy.DEFAULT;


    
    CommandProcessor.getInstance().executeCommand(project, new GlobalVarRegistryAction(project, variableName, this.register), getText(), null, mode);
  }

  
  private static class GlobalVarRegistryAction
    implements Runnable
  {
    private final Project project;
    private final String variableName;
    private final boolean register;
    
    public GlobalVarRegistryAction(Project project, String variableName, boolean register) {
      this.project = project;
      this.variableName = variableName;
      this.register = register;
    }
    
    private void doRegistryAction(boolean register) {
      if (register) {
        BashProjectSettings.storedSettings(this.project).addGlobalVariable(this.variableName);
      } else {
        BashProjectSettings.storedSettings(this.project).removeGlobalVariable(this.variableName);
      } 
      
      FileContentUtil.reparseFiles(this.project, Collections.emptyList(), true);
    }
    
    public void run() {
      VirtualFile[] openFiles = FileEditorManager.getInstance(this.project).getOpenFiles();
      
      UndoManager.getInstance(this.project).undoableActionPerformed((UndoableAction)new GlobalUndoableAction(openFiles)
          {
            public void undo() throws UnexpectedUndoException {
              GlobalVariableQuickfix.GlobalVarRegistryAction.this.doRegistryAction(!GlobalVariableQuickfix.GlobalVarRegistryAction.this.register);
            }

            
            public void redo() throws UnexpectedUndoException {
              GlobalVariableQuickfix.GlobalVarRegistryAction.this.doRegistryAction(GlobalVariableQuickfix.GlobalVarRegistryAction.this.register);
            }
          });
      
      doRegistryAction(this.register);
    }
  }
}
