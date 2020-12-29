package com.ansorgit.plugins.bash.lang.parser.shellCommand;

import com.ansorgit.plugins.bash.lang.parser.BashPsiBuilder;
import com.ansorgit.plugins.bash.lang.parser.OptionalParseResult;
import com.ansorgit.plugins.bash.lang.parser.Parsing;
import com.ansorgit.plugins.bash.lang.parser.ParsingFunction;
import com.ansorgit.plugins.bash.lang.parser.util.ParserUtil;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;



















public class SelectParsingFunction
  implements ParsingFunction
{
  public boolean isValid(BashPsiBuilder builder) {
    return (builder.getTokenType() == SELECT_KEYWORD);
  }











  
  public boolean parse(BashPsiBuilder builder) {
    PsiBuilder.Marker selectCommand = builder.mark();
    builder.advanceLexer();
    
    if (ParserUtil.isIdentifier(builder.getTokenType())) {
      ParserUtil.markTokenAndAdvance((PsiBuilder)builder, (IElementType)VAR_DEF_ELEMENT);
    } else {
      ParserUtil.error(selectCommand, "parser.unexpected.token");
      return false;
    } 
    
    builder.readOptionalNewlines();
    
    if ((builder.getTokenType() == WORD || builder.getTokenType() == IN_KEYWORD_REMAPPED) && "in".equals(builder.getTokenText())) {
      builder.remapCurrentToken(IN_KEYWORD_REMAPPED);
      builder.advanceLexer();
      
      if (ParserUtil.isEmptyListFollowedBy(builder, DO_KEYWORD)) {
        ParserUtil.error((PsiBuilder)builder, "parser.unexpected.token");
        ParserUtil.readEmptyListFollowedBy(builder, DO_KEYWORD);
      } else {
        OptionalParseResult result = Parsing.word.parseWordListIfValid(builder, true, false);
        if (result.isValid() && !result.isParsedSuccessfully()) {
          selectCommand.drop();
          return false;
        } 
      } 
    } 
    
    builder.readOptionalNewlines();

    
    if (!LoopParserUtil.parseLoopBody(builder, false, false)) {
      selectCommand.drop();
      return false;
    } 
    
    selectCommand.done(SELECT_COMMAND);
    return true;
  }
}
