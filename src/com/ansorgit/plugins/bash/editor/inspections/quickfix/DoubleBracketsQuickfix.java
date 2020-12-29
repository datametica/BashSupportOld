package com.ansorgit.plugins.bash.editor.inspections.quickfix;

import com.ansorgit.plugins.bash.lang.psi.api.shell.BashConditionalCommand;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiElementFactory;
import com.intellij.codeInspection.LocalQuickFixAndIntentionActionOnPsiElement;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;



















public class DoubleBracketsQuickfix
  extends LocalQuickFixAndIntentionActionOnPsiElement
{
  public DoubleBracketsQuickfix(BashConditionalCommand conditionalCommand) {
    super((PsiElement)conditionalCommand);
  }
  
  @NotNull
  public String getText() {
    if ("Replace with double brackets" == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/inspections/quickfix/DoubleBracketsQuickfix", "getText" }));  return "Replace with double brackets";
  }

  
  @NotNull
  public String getFamilyName() {
    if ("Bash" == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/inspections/quickfix/DoubleBracketsQuickfix", "getFamilyName" }));  return "Bash";
  }

  
  public boolean isAvailable(@NotNull Project project, @NotNull PsiFile file, @NotNull PsiElement startElement, @NotNull PsiElement endElement) {
    if (project == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "project", "com/ansorgit/plugins/bash/editor/inspections/quickfix/DoubleBracketsQuickfix", "isAvailable" }));  if (file == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "file", "com/ansorgit/plugins/bash/editor/inspections/quickfix/DoubleBracketsQuickfix", "isAvailable" }));  if (startElement == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "startElement", "com/ansorgit/plugins/bash/editor/inspections/quickfix/DoubleBracketsQuickfix", "isAvailable" }));  if (endElement == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "endElement", "com/ansorgit/plugins/bash/editor/inspections/quickfix/DoubleBracketsQuickfix", "isAvailable" }));  if (ApplicationManager.getApplication().isUnitTestMode())
    {
      return true;
    }
    
    Document document = file.getViewProvider().getDocument();
    return (document != null && document.isWritable());
  }

  
  public void invoke(@NotNull Project project, @NotNull PsiFile file, Editor editor, @NotNull PsiElement startElement, @NotNull PsiElement endElement) {
    if (project == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "project", "com/ansorgit/plugins/bash/editor/inspections/quickfix/DoubleBracketsQuickfix", "invoke" }));  if (file == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "file", "com/ansorgit/plugins/bash/editor/inspections/quickfix/DoubleBracketsQuickfix", "invoke" }));  if (startElement == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "startElement", "com/ansorgit/plugins/bash/editor/inspections/quickfix/DoubleBracketsQuickfix", "invoke" }));  if (endElement == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "endElement", "com/ansorgit/plugins/bash/editor/inspections/quickfix/DoubleBracketsQuickfix", "invoke" }));  BashConditionalCommand conditionalCommand = (BashConditionalCommand)startElement;
    String command = conditionalCommand.getCommandText();
    PsiElement replacement = useDoubleBrackets(project, command);
    startElement.replace(replacement);
  }
  
  private static PsiElement useDoubleBrackets(Project project, String command) {
    String newCommand = "[[" + command + "]]";
    for (Replacement replacement : Replacement.values()) {
      newCommand = replacement.apply(newCommand);
    }
    
    PsiFile dummyBashFile = BashPsiElementFactory.createDummyBashFile(project, newCommand);
    return dummyBashFile.getFirstChild();
  }
  
  private enum Replacement {
    AND("(?<!\\S)-a(?!\\S)", "&&"),
    OR("(?<!\\S)-o(?!\\S)", "||"),
    LESS_THAN("\\\\<", "<"),
    MORE_THAN("\\\\>", ">"),
    LEFT_PARANTHESIS("\\\\\\(", "("),
    RIGHT_PARANTHESIS("\\\\\\)", ")");
    
    private final Pattern regex;
    private final String replacement;
    
    Replacement(String regex, String replacement) {
      this.regex = Pattern.compile(regex);
      this.replacement = replacement;
    }
    
    public String apply(CharSequence input) {
      return this.regex.matcher(input).replaceAll(this.replacement);
    }
  }
}
