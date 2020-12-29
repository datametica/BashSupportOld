package com.ansorgit.plugins.bash.actions;

import com.ansorgit.plugins.bash.file.BashFileType;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.util.IncorrectOperationException;
import java.io.IOException;
import java.util.Properties;
import org.jetbrains.annotations.NotNull;


















class BashTemplatesFactory
{
  static final String DEFAULT_TEMPLATE_FILENAME = "Bash Script.sh";
  
  @NotNull
  static PsiFile createFromTemplate(PsiDirectory directory, String fileName, String templateName) throws IncorrectOperationException {
    String templateText;
    Project project = directory.getProject();
    FileTemplateManager templateManager = FileTemplateManager.getInstance(project);
    FileTemplate template = templateManager.getInternalTemplate(templateName);
    
    Properties properties = new Properties(templateManager.getDefaultProperties());

    
    try {
      templateText = template.getText(properties);
    } catch (IOException e) {
      throw new RuntimeException("Unable to load template for " + templateManager.internalTemplateToSubject(templateName), e);
    } 
    
    PsiFile file = PsiFileFactory.getInstance(project).createFileFromText(fileName, (FileType)BashFileType.BASH_FILE_TYPE, templateText);
    if ((PsiFile)directory.add((PsiElement)file) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/actions/BashTemplatesFactory", "createFromTemplate" }));  return (PsiFile)directory.add((PsiElement)file);
  }
}
