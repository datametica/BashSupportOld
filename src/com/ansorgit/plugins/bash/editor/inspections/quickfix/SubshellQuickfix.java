package com.ansorgit.plugins.bash.editor.inspections.quickfix;

import com.ansorgit.plugins.bash.lang.psi.api.expression.BashSubshellCommand;
import com.intellij.codeInspection.LocalQuickFixAndIntentionActionOnPsiElement;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;


















public class SubshellQuickfix
  extends LocalQuickFixAndIntentionActionOnPsiElement
{
  public SubshellQuickfix(BashSubshellCommand subshellCommand) {
    super((PsiElement)subshellCommand);
  }
  
  @NotNull
  public String getText() {
    if ("Replace with backquote command" == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/inspections/quickfix/SubshellQuickfix", "getText" }));  return "Replace with backquote command";
  }

  
  @NotNull
  public String getFamilyName() {
    if ("Bash" == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/inspections/quickfix/SubshellQuickfix", "getFamilyName" }));  return "Bash";
  }

  
  public void invoke(@NotNull Project project, @NotNull PsiFile file, Editor editor, @NotNull PsiElement startElement, @NotNull PsiElement endElement) {
    if (project == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "project", "com/ansorgit/plugins/bash/editor/inspections/quickfix/SubshellQuickfix", "invoke" }));  if (file == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "file", "com/ansorgit/plugins/bash/editor/inspections/quickfix/SubshellQuickfix", "invoke" }));  if (startElement == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "startElement", "com/ansorgit/plugins/bash/editor/inspections/quickfix/SubshellQuickfix", "invoke" }));  if (endElement == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "endElement", "com/ansorgit/plugins/bash/editor/inspections/quickfix/SubshellQuickfix", "invoke" }));  Document document = PsiDocumentManager.getInstance(project).getDocument(file);
    if (document != null) {
      BashSubshellCommand subshellCommand = (BashSubshellCommand)startElement;


      
      int startOffset = subshellCommand.getTextOffset();
      int endOffset = subshellCommand.getTextRange().getEndOffset();
      String command = subshellCommand.getCommandText();
      
      document.replaceString(startOffset, endOffset, "`" + command + "`");
      
      PsiDocumentManager.getInstance(project).commitDocument(document);
    } 
  }
}
