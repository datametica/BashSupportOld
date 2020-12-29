package com.ansorgit.plugins.bash.editor.highlighting;

import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;



















public class BashBraceMatcher
  implements PairedBraceMatcher
{
  private static final BracePair[] PAIRS = new BracePair[] { new BracePair(BashTokenTypes.LEFT_PAREN, BashTokenTypes.RIGHT_PAREN, true), new BracePair(BashTokenTypes.LEFT_SQUARE, BashTokenTypes.RIGHT_SQUARE, false), new BracePair(BashTokenTypes.EXPR_ARITH, BashTokenTypes._EXPR_ARITH, true), new BracePair(BashTokenTypes.EXPR_CONDITIONAL, BashTokenTypes._EXPR_CONDITIONAL, false), new BracePair(BashTokenTypes.STRING_BEGIN, BashTokenTypes.STRING_END, false), new BracePair(BashTokenTypes.LEFT_CURLY, BashTokenTypes.RIGHT_CURLY, true) };







  
  public BracePair[] getPairs() {
    return PAIRS;
  }
  
  public boolean isPairedBracesAllowedBeforeType(@NotNull IElementType lbraceType, @Nullable IElementType tokenType) {
    if (lbraceType == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "lbraceType", "com/ansorgit/plugins/bash/editor/highlighting/BashBraceMatcher", "isPairedBracesAllowedBeforeType" }));  return (BashTokenTypes.WHITESPACE == tokenType || BashTokenTypes.commentTokens
      .contains(tokenType) || tokenType == BashTokenTypes.SEMI || tokenType == BashTokenTypes.COMMA || tokenType == BashTokenTypes.RIGHT_PAREN || tokenType == BashTokenTypes.RIGHT_SQUARE || tokenType == BashTokenTypes.RIGHT_CURLY || null == tokenType);
  }






  
  public int getCodeConstructStart(PsiFile file, int openingBraceOffset) {
    return openingBraceOffset;
  }
}
