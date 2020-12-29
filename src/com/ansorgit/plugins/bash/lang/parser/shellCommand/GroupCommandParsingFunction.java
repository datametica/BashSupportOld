package com.ansorgit.plugins.bash.lang.parser.shellCommand;

import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.ansorgit.plugins.bash.lang.parser.BashElementTypes;
import com.ansorgit.plugins.bash.lang.parser.BashPsiBuilder;
import com.ansorgit.plugins.bash.lang.parser.Parsing;
import com.ansorgit.plugins.bash.lang.parser.ParsingFunction;
import com.ansorgit.plugins.bash.lang.parser.util.ParserUtil;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;



















public class GroupCommandParsingFunction
  implements ParsingFunction
{
  public boolean isValid(BashPsiBuilder builder) {
    return (builder.rawLookup(0) == LEFT_CURLY && ParserUtil.isWhitespaceOrLineFeed(builder.rawLookup(1)));
  }
  
  public boolean parse(BashPsiBuilder builder) {
    PsiBuilder.Marker group = builder.mark();
    builder.advanceLexer();
    if (builder.rawLookup(0) == LINE_FEED) {
      builder.advanceLexer();
    }
    
    Parsing.list.parseCompoundList(builder, true, false);

    
    IElementType lastToken = ParserUtil.getTokenAndAdvance((PsiBuilder)builder);
    if (lastToken != BashTokenTypes.RIGHT_CURLY) {
      group.drop();
      return false;
    } 
    
    group.done(BashElementTypes.GROUP_COMMAND);
    return true;
  }
}
