package com.ansorgit.plugins.bash.lang.parser.shellCommand;

import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.ansorgit.plugins.bash.lang.parser.BashPsiBuilder;
import com.ansorgit.plugins.bash.lang.parser.Parsing;
import com.ansorgit.plugins.bash.lang.parser.util.ParserUtil;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;






























class LoopParserUtil
  implements BashTokenTypes
{
  static boolean parseLoopBody(BashPsiBuilder builder, boolean parseCompoundList, boolean enforcedDoKeyword) {
    IElementType firstToken = ParserUtil.getTokenAndAdvance((PsiBuilder)builder);
    
    if (firstToken == DO_KEYWORD && ParserUtil.isEmptyListFollowedBy(builder, DONE_KEYWORD)) {
      ParserUtil.error((PsiBuilder)builder, "parser.shell.expectedCommands");
      ParserUtil.readEmptyListFollowedBy(builder, DONE_KEYWORD);
      builder.advanceLexer();
      return true;
    } 
    
    if (enforcedDoKeyword && firstToken != DO_KEYWORD) {
      ParserUtil.error((PsiBuilder)builder, "parser.unexpected.token");
      return false;
    } 
    
    if (firstToken == DO_KEYWORD || firstToken == LEFT_CURLY) {

      
      boolean parsed = parseCompoundList ? Parsing.list.parseCompoundList(builder, true, true) : Parsing.list.parseList(builder);
      
      if (parsed) {
        IElementType lastToken = ParserUtil.getTokenAndAdvance((PsiBuilder)builder);
        boolean ok = ((firstToken == DO_KEYWORD && lastToken == DONE_KEYWORD) || (firstToken == LEFT_CURLY && lastToken == RIGHT_CURLY));

        
        if (!ok) {
          ParserUtil.error((PsiBuilder)builder, "parser.unexpected.token");
        }
        
        return ok;
      } 
      ParserUtil.error((PsiBuilder)builder, "parser.unexpected.token");
      
      return false;
    } 

    
    return false;
  }
}
