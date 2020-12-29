package com.ansorgit.plugins.bash.lang.psi;

import com.ansorgit.plugins.bash.lang.lexer.BashLexer;
import com.intellij.lexer.Lexer;
import com.intellij.psi.impl.cache.impl.OccurrenceConsumer;
import com.intellij.psi.impl.cache.impl.id.LexerBasedIdIndexer;















public class BashIdIndexer
  extends LexerBasedIdIndexer
{
  public static BashFilterLexer createIndexingLexer(OccurrenceConsumer occurendeConsumer) {
    return new BashFilterLexer((Lexer)new BashLexer(), occurendeConsumer);
  }

  
  public Lexer createLexer(OccurrenceConsumer consumer) {
    return (Lexer)createIndexingLexer(consumer);
  }

  
  public int getVersion() {
    return 82;
  }
}
