package com.ansorgit.plugins.bash.lang.psi.impl.refactoring;

import com.ansorgit.plugins.bash.lang.psi.api.command.BashCommand;
import com.ansorgit.plugins.bash.lang.psi.api.command.BashGenericCommand;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiElementFactory;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.ElementManipulator;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;


















public class BashCommandManipulator
  implements ElementManipulator<BashCommand>
{
  public BashCommand handleContentChange(@NotNull BashCommand cmd, @NotNull TextRange textRange, String newElementName) throws IncorrectOperationException {
    if (cmd == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "cmd", "com/ansorgit/plugins/bash/lang/psi/impl/refactoring/BashCommandManipulator", "handleContentChange" }));  if (textRange == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "textRange", "com/ansorgit/plugins/bash/lang/psi/impl/refactoring/BashCommandManipulator", "handleContentChange" }));  if (StringUtil.isEmpty(newElementName)) {
      throw new IncorrectOperationException("Can not handle empty names");
    }
    
    PsiElement commandElement = cmd.commandElement();
    if (commandElement == null) {
      throw new IncorrectOperationException("invalid command");
    }
    
    BashGenericCommand replacement = BashPsiElementFactory.createCommand(cmd.getProject(), newElementName);
    BashPsiUtils.replaceElement(commandElement, (PsiElement)replacement);
    
    return cmd;
  }

  
  public BashCommand handleContentChange(@NotNull BashCommand element, String newContent) throws IncorrectOperationException {
    if (element == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "element", "com/ansorgit/plugins/bash/lang/psi/impl/refactoring/BashCommandManipulator", "handleContentChange" }));  return handleContentChange(element, TextRange.create(0, element.getTextLength()), newContent);
  }

  
  @NotNull
  public TextRange getRangeInElement(@NotNull BashCommand cmd) {
    if (cmd == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "cmd", "com/ansorgit/plugins/bash/lang/psi/impl/refactoring/BashCommandManipulator", "getRangeInElement" }));  PsiElement element = cmd.commandElement();
    if (element == null) {
      if (TextRange.from(0, cmd.getTextLength()) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/refactoring/BashCommandManipulator", "getRangeInElement" }));  return TextRange.from(0, cmd.getTextLength());
    } 
    
    if (TextRange.from(element.getStartOffsetInParent(), element.getTextLength()) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/refactoring/BashCommandManipulator", "getRangeInElement" }));  return TextRange.from(element.getStartOffsetInParent(), element.getTextLength());
  }
}
