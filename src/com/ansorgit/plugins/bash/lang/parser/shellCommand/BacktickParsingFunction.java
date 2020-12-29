package com.ansorgit.plugins.bash.lang.parser.shellCommand;

import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.ansorgit.plugins.bash.lang.parser.BashElementTypes;
import com.ansorgit.plugins.bash.lang.parser.BashPsiBuilder;
import com.ansorgit.plugins.bash.lang.parser.Parsing;
import com.ansorgit.plugins.bash.lang.parser.ParsingFunction;
import com.ansorgit.plugins.bash.lang.parser.util.ParserUtil;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;




















public class BacktickParsingFunction
  implements ParsingFunction
{
  public boolean isValid(BashPsiBuilder builder) {
    return (!builder.getBackquoteData().isInBackquote() && builder.getTokenType() == BashTokenTypes.BACKQUOTE);
  }




  
  public boolean parse(BashPsiBuilder builder) {
    PsiBuilder.Marker backquote = builder.mark();
    builder.advanceLexer();
    
    builder.getBackquoteData().enterBackquote();
    
    try {
      boolean empty = (builder.getTokenType() == BashTokenTypes.BACKQUOTE);
      if (!empty && !Parsing.list.parseCompoundList(builder, true, false)) {
        ParserUtil.error(backquote, "parser.shell.expectedCommands");
        return false;
      } 

      
      IElementType lastToken = ParserUtil.getTokenAndAdvance((PsiBuilder)builder);
      if (lastToken != BashTokenTypes.BACKQUOTE) {
        ParserUtil.error(backquote, "parser.unexpected.token");
        return false;
      } 
      
      backquote.done(BashElementTypes.BACKQUOTE_COMMAND);
      return true;
    } finally {
      builder.getBackquoteData().leaveBackquote();
    } 
  }
}
