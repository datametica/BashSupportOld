package com.ansorgit.plugins.bash.editor.inspections.quickfix;

import com.ansorgit.plugins.bash.lang.psi.api.BashShebang;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;



















public class ReplaceShebangQuickfix
  extends AbstractBashPsiElementQuickfix
{
  private final String command;
  private final TextRange replacementRange;
  
  public ReplaceShebangQuickfix(BashShebang shebang, String command) {
    this(shebang, command, null);
  }
  
  public ReplaceShebangQuickfix(BashShebang shebang, String command, @Nullable TextRange replacementRange) {
    super((PsiElement)shebang);
    this.command = command;
    this.replacementRange = replacementRange;
  }
  
  @NotNull
  public String getText() {
    if ("Replace with '" + this.command + "'" == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/inspections/quickfix/ReplaceShebangQuickfix", "getText" }));  return "Replace with '" + this.command + "'";
  }

  
  public void invoke(@NotNull Project project, @NotNull PsiFile file, Editor editor, @NotNull PsiElement startElement, @NotNull PsiElement endElement) {
    if (project == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "project", "com/ansorgit/plugins/bash/editor/inspections/quickfix/ReplaceShebangQuickfix", "invoke" }));  if (file == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "file", "com/ansorgit/plugins/bash/editor/inspections/quickfix/ReplaceShebangQuickfix", "invoke" }));  if (startElement == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "startElement", "com/ansorgit/plugins/bash/editor/inspections/quickfix/ReplaceShebangQuickfix", "invoke" }));  if (endElement == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "endElement", "com/ansorgit/plugins/bash/editor/inspections/quickfix/ReplaceShebangQuickfix", "invoke" }));  BashShebang shebang = (BashShebang)startElement;
    
    shebang.updateCommand(this.command, this.replacementRange);
  }
}
