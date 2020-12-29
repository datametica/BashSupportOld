package com.ansorgit.plugins.bash.editor.inspections.quickfix;

import com.ansorgit.plugins.bash.lang.psi.api.word.BashWord;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;















public class WordToDoublequotedStringQuickfix
  extends AbstractWordWrapQuickfix
{
  public WordToDoublequotedStringQuickfix(BashWord word) {
    super(word);
  }
  
  protected String wrapText(String text) {
    return "\"" + text + "\"";
  }
  
  @NotNull
  public String getText() {
    if ("Convert to quoted string \"...\"" == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/inspections/quickfix/WordToDoublequotedStringQuickfix", "getText" }));  return "Convert to quoted string \"...\"";
  }
}
