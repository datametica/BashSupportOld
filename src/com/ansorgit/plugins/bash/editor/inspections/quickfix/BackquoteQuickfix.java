package com.ansorgit.plugins.bash.editor.inspections.quickfix;

import com.ansorgit.plugins.bash.lang.psi.api.BashBackquote;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;



















public class BackquoteQuickfix
  extends AbstractBashPsiElementQuickfix
{
  public BackquoteQuickfix(BashBackquote backquote) {
    super((PsiElement)backquote);
  }

  
  @NotNull
  public String getText() {
    if ("Replace with subshell command" == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/inspections/quickfix/BackquoteQuickfix", "getText" }));  return "Replace with subshell command";
  }

  
  public void invoke(@NotNull Project project, @NotNull PsiFile file, Editor editor, @NotNull PsiElement startElement, @NotNull PsiElement endElement) {
    if (project == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "project", "com/ansorgit/plugins/bash/editor/inspections/quickfix/BackquoteQuickfix", "invoke" }));  if (file == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "file", "com/ansorgit/plugins/bash/editor/inspections/quickfix/BackquoteQuickfix", "invoke" }));  if (startElement == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "startElement", "com/ansorgit/plugins/bash/editor/inspections/quickfix/BackquoteQuickfix", "invoke" }));  if (endElement == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "endElement", "com/ansorgit/plugins/bash/editor/inspections/quickfix/BackquoteQuickfix", "invoke" }));  Document document = PsiDocumentManager.getInstance(project).getDocument(file);
    if (document != null) {
      BashBackquote backquote = (BashBackquote)startElement;
      int endOffset = startElement.getTextRange().getEndOffset();
      String command = backquote.getCommandText();

      
      document.replaceString(startElement.getTextOffset(), endOffset, "$(" + command + ")");
      
      PsiDocumentManager.getInstance(project).commitDocument(document);
    } 
  }
}
