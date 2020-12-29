package com.ansorgit.plugins.bash.editor;

import com.intellij.codeInsight.CodeInsightSettings;
import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegate;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Ref;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;







public class BashUnmatchedBraceEnterProcessor
  implements EnterHandlerDelegate
{
  public EnterHandlerDelegate.Result preprocessEnter(@NotNull PsiFile file, @NotNull Editor editor, @NotNull Ref<Integer> caretOffset, @NotNull Ref<Integer> caretAdvance, @NotNull DataContext dataContext, @Nullable EditorActionHandler originalHandler) {
    if (file == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "file", "com/ansorgit/plugins/bash/editor/BashUnmatchedBraceEnterProcessor", "preprocessEnter" }));  if (editor == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "editor", "com/ansorgit/plugins/bash/editor/BashUnmatchedBraceEnterProcessor", "preprocessEnter" }));  if (caretOffset == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "caretOffset", "com/ansorgit/plugins/bash/editor/BashUnmatchedBraceEnterProcessor", "preprocessEnter" }));  if (caretAdvance == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "caretAdvance", "com/ansorgit/plugins/bash/editor/BashUnmatchedBraceEnterProcessor", "preprocessEnter" }));  if (dataContext == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "dataContext", "com/ansorgit/plugins/bash/editor/BashUnmatchedBraceEnterProcessor", "preprocessEnter" }));  Project project = editor.getProject();
    
    if ((CodeInsightSettings.getInstance()).INSERT_BRACE_ON_ENTER && file instanceof com.ansorgit.plugins.bash.lang.psi.api.BashFile && project != null) {
      Document document = editor.getDocument();
      CharSequence chars = document.getCharsSequence();
      
      int offset = ((Integer)caretOffset.get()).intValue();
      int length = chars.length();
      
      if (offset < length && offset >= 1 && chars.charAt(offset - 1) == '{') {
        int start = offset + 1;
        int end = offset + 1 + "function".length();
        
        if (start < length && end < length && "function".contentEquals(chars.subSequence(start, end))) {
          document.insertString(start, "\n");
          PsiDocumentManager.getInstance(project).commitDocument(document);
        } 
      } 
    } 
    
    return EnterHandlerDelegate.Result.Continue;
  }

  
  public EnterHandlerDelegate.Result postProcessEnter(@NotNull PsiFile file, @NotNull Editor editor, @NotNull DataContext dataContext) {
    if (file == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "file", "com/ansorgit/plugins/bash/editor/BashUnmatchedBraceEnterProcessor", "postProcessEnter" }));  if (editor == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "editor", "com/ansorgit/plugins/bash/editor/BashUnmatchedBraceEnterProcessor", "postProcessEnter" }));  if (dataContext == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "dataContext", "com/ansorgit/plugins/bash/editor/BashUnmatchedBraceEnterProcessor", "postProcessEnter" }));  return EnterHandlerDelegate.Result.Default;
  }
}
