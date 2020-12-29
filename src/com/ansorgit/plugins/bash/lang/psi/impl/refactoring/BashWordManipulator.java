package com.ansorgit.plugins.bash.lang.psi.impl.refactoring;

import com.ansorgit.plugins.bash.lang.psi.api.word.BashWord;
import com.ansorgit.plugins.bash.lang.psi.util.BashStringUtils;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.ElementManipulator;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;



















public class BashWordManipulator
  implements ElementManipulator<BashWord>
{
  private static final char[] DOLLAR_IGNORED = new char[] { '$' };

  
  public BashWord handleContentChange(@NotNull BashWord element, @NotNull TextRange textRange, String contentForRange) throws IncorrectOperationException {
    if (element == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "element", "com/ansorgit/plugins/bash/lang/psi/impl/refactoring/BashWordManipulator", "handleContentChange" }));  if (textRange == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "textRange", "com/ansorgit/plugins/bash/lang/psi/impl/refactoring/BashWordManipulator", "handleContentChange" }));  String oldContent = element.getText();
    String newContent = oldContent.substring(0, textRange.getStartOffset()) + contentForRange + oldContent.substring(textRange.getEndOffset());

    
    int contentStart = newContent.indexOf('\'') + 1;
    int contentEnd = newContent.lastIndexOf('\'');
    if (contentStart > contentEnd) {
      throw new IncorrectOperationException("Invalid content change");
    }
    
    if (newContent.startsWith("$'") && newContent.indexOf('\\', contentStart) < contentEnd) {
      
      String toEscape = newContent.substring(contentStart, contentEnd);
      newContent = "$'" + BashStringUtils.escape(toEscape, '\\', DOLLAR_IGNORED) + "'";
      contentEnd = newContent.length() - 1;
    } 
    
    if (newContent.indexOf('\'', contentStart) < contentEnd) {
      
      String toEscape = newContent.substring(contentStart, contentEnd);
      newContent = "$'" + BashStringUtils.escape(toEscape, '\'') + "'";
    } 



    
    ASTNode valueNode = element.getNode().getFirstChildNode();
    assert valueNode instanceof LeafElement;
    ((LeafElement)valueNode).replaceWithText(newContent);
    
    return element;
  }



  
  public BashWord handleContentChange(@NotNull BashWord element, String newContent) throws IncorrectOperationException {
    if (element == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "element", "com/ansorgit/plugins/bash/lang/psi/impl/refactoring/BashWordManipulator", "handleContentChange" }));  return handleContentChange(element, TextRange.create(0, element.getTextLength()), newContent);
  }

  
  @NotNull
  public TextRange getRangeInElement(@NotNull BashWord element) {
    if (element == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "element", "com/ansorgit/plugins/bash/lang/psi/impl/refactoring/BashWordManipulator", "getRangeInElement" }));  if (element.getTextContentRange() == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/refactoring/BashWordManipulator", "getRangeInElement" }));  return element.getTextContentRange();
  }
}
