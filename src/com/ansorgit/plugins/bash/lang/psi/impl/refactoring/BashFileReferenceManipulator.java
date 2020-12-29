package com.ansorgit.plugins.bash.lang.psi.impl.refactoring;

import com.ansorgit.plugins.bash.lang.psi.api.BashCharSequence;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiElementFactory;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.ElementManipulator;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;



















public class BashFileReferenceManipulator
  implements ElementManipulator
{
  public PsiElement handleContentChange(@NotNull PsiElement element, @NotNull TextRange range, String newContent) throws IncorrectOperationException {
    String name;
    if (element == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "element", "com/ansorgit/plugins/bash/lang/psi/impl/refactoring/BashFileReferenceManipulator", "handleContentChange" }));  if (range == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "range", "com/ansorgit/plugins/bash/lang/psi/impl/refactoring/BashFileReferenceManipulator", "handleContentChange" }));  PsiElement firstChild = element.getFirstChild();

    
    if (firstChild instanceof BashCharSequence) {
      name = ((BashCharSequence)firstChild).createEquallyWrappedString(newContent);
    } else {
      name = newContent;
    } 
    
    return BashPsiUtils.replaceElement(element, BashPsiElementFactory.createFileReference(element.getProject(), name));
  }

  
  public PsiElement handleContentChange(@NotNull PsiElement element, String newContent) throws IncorrectOperationException {
    if (element == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "element", "com/ansorgit/plugins/bash/lang/psi/impl/refactoring/BashFileReferenceManipulator", "handleContentChange" }));  return handleContentChange(element, TextRange.create(0, element.getTextLength()), newContent);
  }

  
  @NotNull
  public TextRange getRangeInElement(@NotNull PsiElement element) {
    if (element == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "element", "com/ansorgit/plugins/bash/lang/psi/impl/refactoring/BashFileReferenceManipulator", "getRangeInElement" }));  PsiElement firstChild = element.getFirstChild();
    if (firstChild instanceof BashCharSequence) {
      if (((BashCharSequence)firstChild).getTextContentRange().shiftRight(firstChild.getStartOffsetInParent()) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/refactoring/BashFileReferenceManipulator", "getRangeInElement" }));  return ((BashCharSequence)firstChild).getTextContentRange().shiftRight(firstChild.getStartOffsetInParent());
    } 
    
    if (TextRange.from(0, element.getTextLength()) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/refactoring/BashFileReferenceManipulator", "getRangeInElement" }));  return TextRange.from(0, element.getTextLength());
  }
}
