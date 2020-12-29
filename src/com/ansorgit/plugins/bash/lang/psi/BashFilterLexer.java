package com.ansorgit.plugins.bash.lang.psi;

import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.intellij.lexer.Lexer;
import com.intellij.psi.impl.cache.impl.BaseFilterLexer;
import com.intellij.psi.impl.cache.impl.OccurrenceConsumer;
import com.intellij.psi.tree.IElementType;















class BashFilterLexer
  extends BaseFilterLexer
{
  BashFilterLexer(Lexer lexer, OccurrenceConsumer consumer) {
    super(lexer, consumer);
  }

  
  public void advance() {
    IElementType tokenType = this.myDelegate.getTokenType();
    
    if (tokenType == BashTokenTypes.COMMENT) {
      scanWordsInToken(2, false, false);
      advanceTodoItemCountsInToken();
    } else if (tokenType == BashTokenTypes.STRING2) {
      addOccurrenceInToken(13);
      scanWordsInToken(5, true, true);
    } else {
      addOccurrenceInToken(5);
      scanWordsInToken(5, true, false);
    } 
    
    this.myDelegate.advance();
  }
}
