package com.ansorgit.plugins.bash.lang.parser;

import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.intellij.lang.ITokenTypeRemapper;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;





















final class BashTokenRemapper
  implements ITokenTypeRemapper, BashTokenTypes
{
  private static final TokenSet mappedToWord = TokenSet.create(new IElementType[] { ASSIGNMENT_WORD, LEFT_SQUARE, RIGHT_SQUARE, IF_KEYWORD, THEN_KEYWORD, ELIF_KEYWORD, ELSE_KEYWORD, FI_KEYWORD, WHILE_KEYWORD, DO_KEYWORD, DONE_KEYWORD, FOR_KEYWORD, FUNCTION_KEYWORD, CASE_KEYWORD, ESAC_KEYWORD, SELECT_KEYWORD, UNTIL_KEYWORD, TIME_KEYWORD, BRACKET_KEYWORD, _BRACKET_KEYWORD, EQ, AT });




  
  private final BashPsiBuilder builder;




  
  private boolean remapShebangToComment = false;




  
  public BashTokenRemapper(BashPsiBuilder builder) {
    this.builder = builder;
  }
  
  public IElementType filter(IElementType elementType, int from, int to, CharSequence charSequence) {
    if (this.remapShebangToComment && elementType == SHEBANG) {
      return COMMENT;
    }

    
    if (this.builder.getParsingState().isInSimpleCommand() && mappedToWord.contains(elementType)) {
      return WORD;
    }
    
    return elementType;
  }
  
  public void enableShebangToCommentMapping() {
    this.remapShebangToComment = true;
  }
}
