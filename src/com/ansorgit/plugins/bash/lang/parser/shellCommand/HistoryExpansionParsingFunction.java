package com.ansorgit.plugins.bash.lang.parser.shellCommand;

import com.ansorgit.plugins.bash.lang.parser.BashPsiBuilder;
import com.ansorgit.plugins.bash.lang.parser.Parsing;
import com.ansorgit.plugins.bash.lang.parser.ParsingFunction;
import com.ansorgit.plugins.bash.lang.parser.util.ParserUtil;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;


















public class HistoryExpansionParsingFunction
  implements ParsingFunction
{
  public boolean isValid(BashPsiBuilder builder) {
    IElementType token = builder.rawLookup(1);
    return (token != null && 
      ParserUtil.isWord(builder, "!") && 
      !ParserUtil.isWhitespaceOrLineFeed(token));
  }
  
  private final TokenSet accepted = TokenSet.create(new IElementType[] { WORD, ARITH_NUMBER, DOLLAR });


  
  public boolean parse(BashPsiBuilder builder) {
    builder.advanceLexer();




    
    boolean ok = false;
    
    if (this.accepted.contains(builder.getTokenType())) {
      builder.advanceLexer();
      ok = true;
    } else if (Parsing.word.isComposedString(builder.getTokenType())) {
      ok = Parsing.word.parseComposedString(builder);
    } else {
      int count = 0;
      
      while (!builder.eof() && !ParserUtil.isWhitespaceOrLineFeed(builder.getTokenType(true))) {
        builder.advanceLexer();
        count++;
      } 
      
      ok = (count > 0);
    } 
    
    return ok;
  }
}
