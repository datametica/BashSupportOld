package com.ansorgit.plugins.bash.lang.parser.shellCommand;

import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.ansorgit.plugins.bash.lang.parser.BashElementTypes;
import com.ansorgit.plugins.bash.lang.parser.BashPsiBuilder;
import com.ansorgit.plugins.bash.lang.parser.Parsing;
import com.ansorgit.plugins.bash.lang.parser.ParsingFunction;
import com.ansorgit.plugins.bash.lang.parser.util.ParserUtil;
import com.intellij.lang.PsiBuilder;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.tree.IElementType;



















public class SubshellParsingFunction
  implements ParsingFunction
{
  private static final Logger log = Logger.getInstance("#bash.SubshellParsingFunction");
  
  public boolean isValid(BashPsiBuilder builder) {
    return (builder.getTokenType() == BashTokenTypes.LEFT_PAREN);
  }




  
  public boolean parse(BashPsiBuilder builder) {
    PsiBuilder.Marker subshell = builder.mark();
    
    builder.advanceLexer();

    
    if (builder.getTokenType() != RIGHT_PAREN)
    {
      if (!Parsing.list.parseCompoundList(builder, true, false)) {
        ParserUtil.error((PsiBuilder)builder, "parser.shell.expectedCommands");
        subshell.drop();
        return false;
      } 
    }

    
    IElementType lastToken = ParserUtil.getTokenAndAdvance((PsiBuilder)builder);
    if (lastToken != BashTokenTypes.RIGHT_PAREN) {
      ParserUtil.error((PsiBuilder)builder, "parser.unexpected.token");
      subshell.drop();
      return false;
    } 
    
    subshell.done(BashElementTypes.SUBSHELL_COMMAND);
    return true;
  }
}
