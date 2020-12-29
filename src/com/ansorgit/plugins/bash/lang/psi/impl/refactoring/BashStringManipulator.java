package com.ansorgit.plugins.bash.lang.psi.impl.refactoring;

import com.ansorgit.plugins.bash.lang.psi.api.BashString;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiElementFactory;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import com.ansorgit.plugins.bash.lang.psi.util.BashStringUtils;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.ElementManipulator;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;



















public class BashStringManipulator
  implements ElementManipulator<BashString>
{
  public BashString handleContentChange(@NotNull BashString element, @NotNull TextRange textRange, String contentForRange) throws IncorrectOperationException {
    if (element == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "element", "com/ansorgit/plugins/bash/lang/psi/impl/refactoring/BashStringManipulator", "handleContentChange" }));  if (textRange == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "textRange", "com/ansorgit/plugins/bash/lang/psi/impl/refactoring/BashStringManipulator", "handleContentChange" }));  TextRange elementContentRange = element.getTextContentRange();
    
    if (contentForRange.length() > 2 && textRange.getStartOffset() == 0 && textRange.getLength() == element.getTextLength()) {
      contentForRange = contentForRange.substring(1, contentForRange.length() - 1);
    }
    
    String escapedContent = BashStringUtils.escape(contentForRange, '"');
    String newContent = elementContentRange.replace(element.getText(), escapedContent);
    
    BashString replacement = BashPsiElementFactory.createString(element.getProject(), newContent);
    assert replacement != null;
    
    return (BashString)BashPsiUtils.replaceElement((PsiElement)element, (PsiElement)replacement);
  }

  
  public BashString handleContentChange(@NotNull BashString element, String newContent) throws IncorrectOperationException {
    if (element == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "element", "com/ansorgit/plugins/bash/lang/psi/impl/refactoring/BashStringManipulator", "handleContentChange" }));  return handleContentChange(element, TextRange.create(0, element.getTextLength()), newContent);
  }

  
  @NotNull
  public TextRange getRangeInElement(@NotNull BashString element) {
    if (element == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "element", "com/ansorgit/plugins/bash/lang/psi/impl/refactoring/BashStringManipulator", "getRangeInElement" }));  if (element.getTextContentRange() == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/refactoring/BashStringManipulator", "getRangeInElement" }));  return element.getTextContentRange();
  }
}
