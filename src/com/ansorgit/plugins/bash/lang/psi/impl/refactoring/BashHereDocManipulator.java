package com.ansorgit.plugins.bash.lang.psi.impl.refactoring;

import com.ansorgit.plugins.bash.lang.psi.api.heredoc.BashHereDoc;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiElementFactory;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.ElementManipulator;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;




















public class BashHereDocManipulator
  implements ElementManipulator<BashHereDoc>
{
  public BashHereDoc handleContentChange(@NotNull BashHereDoc bashHereDoc, @NotNull TextRange textRange, String contentForRange) throws IncorrectOperationException {
    if (bashHereDoc == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "bashHereDoc", "com/ansorgit/plugins/bash/lang/psi/impl/refactoring/BashHereDocManipulator", "handleContentChange" }));  if (textRange == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "textRange", "com/ansorgit/plugins/bash/lang/psi/impl/refactoring/BashHereDocManipulator", "handleContentChange" }));  String oldContent = bashHereDoc.getText();
    String newContent = textRange.replace(oldContent, contentForRange);
    
    PsiElement replacement = BashPsiElementFactory.createHeredocContent(bashHereDoc.getProject(), newContent);
    
    return (BashHereDoc)BashPsiUtils.replaceElement((PsiElement)bashHereDoc, replacement);
  }

  
  public BashHereDoc handleContentChange(@NotNull BashHereDoc element, String newContent) throws IncorrectOperationException {
    if (element == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "element", "com/ansorgit/plugins/bash/lang/psi/impl/refactoring/BashHereDocManipulator", "handleContentChange" }));  return handleContentChange(element, TextRange.create(0, element.getTextLength()), newContent);
  }

  
  @NotNull
  public TextRange getRangeInElement(@NotNull BashHereDoc element) {
    if (element == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "element", "com/ansorgit/plugins/bash/lang/psi/impl/refactoring/BashHereDocManipulator", "getRangeInElement" }));  if (TextRange.create(0, element.getTextLength()) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/refactoring/BashHereDocManipulator", "getRangeInElement" }));  return TextRange.create(0, element.getTextLength());
  }
}
