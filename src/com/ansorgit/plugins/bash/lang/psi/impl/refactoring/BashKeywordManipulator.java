package com.ansorgit.plugins.bash.lang.psi.impl.refactoring;

import com.ansorgit.plugins.bash.lang.psi.api.BashKeyword;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.ElementManipulator;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;




















public class BashKeywordManipulator
  implements ElementManipulator<BashKeyword>
{
  public BashKeyword handleContentChange(@NotNull BashKeyword bashHereDoc, @NotNull TextRange textRange, String contentForRange) throws IncorrectOperationException {
    if (bashHereDoc == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "bashHereDoc", "com/ansorgit/plugins/bash/lang/psi/impl/refactoring/BashKeywordManipulator", "handleContentChange" }));  if (textRange == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "textRange", "com/ansorgit/plugins/bash/lang/psi/impl/refactoring/BashKeywordManipulator", "handleContentChange" }));  throw new IncorrectOperationException();
  }

  
  public BashKeyword handleContentChange(@NotNull BashKeyword element, String newContent) throws IncorrectOperationException {
    if (element == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "element", "com/ansorgit/plugins/bash/lang/psi/impl/refactoring/BashKeywordManipulator", "handleContentChange" }));  throw new IncorrectOperationException();
  }

  
  @NotNull
  public TextRange getRangeInElement(@NotNull BashKeyword element) {
    if (element == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "element", "com/ansorgit/plugins/bash/lang/psi/impl/refactoring/BashKeywordManipulator", "getRangeInElement" }));  PsiElement keywordElement = element.keywordElement();
    if (keywordElement == null) {
      if (TextRange.create(0, element.getTextLength()) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/refactoring/BashKeywordManipulator", "getRangeInElement" }));  return TextRange.create(0, element.getTextLength());
    } 
    
    if (TextRange.create(0, keywordElement.getTextLength()) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/refactoring/BashKeywordManipulator", "getRangeInElement" }));  return TextRange.create(0, keywordElement.getTextLength());
  }
}
