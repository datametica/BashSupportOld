package com.ansorgit.plugins.bash.actions;

import com.ansorgit.plugins.bash.util.BashIcons;
import com.ansorgit.plugins.bash.util.BashStrings;
import com.intellij.CommonBundle;
import com.intellij.ide.actions.CreateElementActionBase;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.io.FileUtilRt;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import java.io.File;
import org.jetbrains.annotations.NotNull;























public class NewBashFileAction
  extends CreateElementActionBase
{
  public NewBashFileAction() {
    super(BashStrings.message("newfile.menu.action.text", new Object[0]), BashStrings.message("newfile.menu.action.description", new Object[0]), BashIcons.BASH_FILE_ICON);
  }
  
  static String computeFilename(String inputFilename) {
    String usedExtension = FileUtilRt.getExtension(inputFilename);
    boolean withExtension = !usedExtension.isEmpty();
    
    return withExtension ? inputFilename : (inputFilename + "." + "sh");
  }
  
  private String getDialogPrompt() {
    return BashStrings.message("newfile.dialog.prompt", new Object[0]);
  }
  
  private String getDialogTitle() {
    return BashStrings.message("newfile.dialog.title", new Object[0]);
  }
  
  protected String getCommandName() {
    return BashStrings.message("newfile.command.name", new Object[0]);
  }
  
  protected String getActionName(PsiDirectory directory, String newName) {
    return BashStrings.message("newfile.menu.action.text", new Object[0]);
  }
  
  @NotNull
  protected final PsiElement[] invokeDialog(Project project, PsiDirectory directory) {
    CreateElementActionBase.MyInputValidator validator = new CreateElementActionBase.MyInputValidator(this, project, directory);
    Messages.showInputDialog(project, getDialogPrompt(), getDialogTitle(), Messages.getQuestionIcon(), "", (InputValidator)validator);
    
    if (validator.getCreatedElements() == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/actions/NewBashFileAction", "invokeDialog" }));  return validator.getCreatedElements();
  }
  
  @NotNull
  protected PsiElement[] create(String newName, PsiDirectory directory) throws Exception {
    PsiFile file = BashTemplatesFactory.createFromTemplate(directory, computeFilename(newName), "Bash Script.sh");
    
    File ioFile = VfsUtil.virtualToIoFile(file.getVirtualFile());
    if (ioFile.exists()) {
      ioFile.setExecutable(true, true);
    }
    
    PsiElement child = file.getLastChild();
    (new PsiElement[2])[0] = (PsiElement)file; (new PsiElement[2])[1] = child; (new PsiElement[1])[0] = (PsiElement)file; if (((child != null) ? new PsiElement[2] : new PsiElement[1]) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/actions/NewBashFileAction", "create" }));  return (child != null) ? new PsiElement[2] : new PsiElement[1];
  }
  
  protected String getErrorTitle() {
    return CommonBundle.getErrorTitle();
  }
}
