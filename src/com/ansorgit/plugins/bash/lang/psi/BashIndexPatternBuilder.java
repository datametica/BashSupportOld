package com.ansorgit.plugins.bash.lang.psi;

import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.ansorgit.plugins.bash.lang.parser.BashParserDefinition;
import com.intellij.lexer.Lexer;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.search.IndexPatternBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;



















public class BashIndexPatternBuilder
  implements IndexPatternBuilder
{
  @Nullable
  public Lexer getIndexingLexer(@NotNull PsiFile file) {
    if (file == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "file", "com/ansorgit/plugins/bash/lang/psi/BashIndexPatternBuilder", "getIndexingLexer" }));  if (file instanceof com.ansorgit.plugins.bash.lang.psi.api.BashFile) {
      return BashParserDefinition.createBashLexer(file.getProject());
    }
    return null;
  }

  
  @Nullable
  public TokenSet getCommentTokenSet(@NotNull PsiFile file) {
    if (file == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "file", "com/ansorgit/plugins/bash/lang/psi/BashIndexPatternBuilder", "getCommentTokenSet" }));  if (file instanceof com.ansorgit.plugins.bash.lang.psi.api.BashFile) {
      return BashTokenTypes.commentTokens;
    }
    return null;
  }

  
  public int getCommentStartDelta(IElementType tokenType) {
    return (tokenType == BashTokenTypes.COMMENT) ? 1 : 0;
  }

  
  public int getCommentEndDelta(IElementType tokenType) {
    return 0;
  }
}
