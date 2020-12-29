package com.ansorgit.plugins.bash.editor.inspections.quickfix;

import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
















public class AddShebangQuickfix
  extends AbstractBashQuickfix
{
  @NotNull
  public String getName() {
    if ("Add shebang line" == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/inspections/quickfix/AddShebangQuickfix", "getName" }));  return "Add shebang line";
  }


  
  public void invoke(@NotNull final Project project, Editor editor, final PsiFile file) throws IncorrectOperationException {
    if (project == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "project", "com/ansorgit/plugins/bash/editor/inspections/quickfix/AddShebangQuickfix", "invoke" }));  ApplicationManager.getApplication().runWriteAction(new Runnable() {
          public void run() {
            Document document = PsiDocumentManager.getInstance(project).getDocument(BashPsiUtils.findFileContext((PsiElement)file));
            if (document != null) {
              document.insertString(0, "#!/usr/bin/env bash\n");
              PsiDocumentManager.getInstance(project).commitDocument(document);
            } 
          }
        });
  }
}
