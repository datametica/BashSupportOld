package com.ansorgit.plugins.bash.editor.inspections.quickfix;

import com.ansorgit.plugins.bash.lang.psi.api.word.BashWord;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;


















abstract class AbstractWordWrapQuickfix
  extends AbstractBashPsiElementQuickfix
{
  AbstractWordWrapQuickfix(BashWord word) {
    super((PsiElement)word);
  }

  
  public void invoke(@NotNull Project project, @NotNull PsiFile file, Editor editor, @NotNull PsiElement startElement, @NotNull PsiElement endElement) {
    if (project == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "project", "com/ansorgit/plugins/bash/editor/inspections/quickfix/AbstractWordWrapQuickfix", "invoke" }));  if (file == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "file", "com/ansorgit/plugins/bash/editor/inspections/quickfix/AbstractWordWrapQuickfix", "invoke" }));  if (startElement == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "startElement", "com/ansorgit/plugins/bash/editor/inspections/quickfix/AbstractWordWrapQuickfix", "invoke" }));  if (endElement == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "endElement", "com/ansorgit/plugins/bash/editor/inspections/quickfix/AbstractWordWrapQuickfix", "invoke" }));  Document document = PsiDocumentManager.getInstance(project).getDocument(file);
    if (document != null) {
      int endOffset = startElement.getTextOffset() + startElement.getTextLength();
      
      String replacement = wrapText(startElement.getText());
      
      document.replaceString(startElement.getTextOffset(), endOffset, replacement);
      
      PsiDocumentManager.getInstance(project).commitDocument(document);
    } 
  }
  
  protected abstract String wrapText(String paramString);
}
