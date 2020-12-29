package com.ansorgit.plugins.bash.lang.parser.command;

import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.ansorgit.plugins.bash.lang.parser.BashElementTypes;
import com.ansorgit.plugins.bash.lang.parser.BashPsiBuilder;
import com.ansorgit.plugins.bash.lang.parser.Parsing;
import com.ansorgit.plugins.bash.lang.parser.ParsingFunction;
import com.ansorgit.plugins.bash.lang.parser.util.ParserUtil;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;




















public class FunctionDefParsingFunction
  implements ParsingFunction
{
  private static final IElementType[] FUNCTION_DEF_TOKENLIST = new IElementType[] { BashTokenTypes.WORD, BashTokenTypes.LEFT_PAREN, BashTokenTypes.RIGHT_PAREN };
  
  public boolean isValid(BashPsiBuilder builder) {
    IElementType current = builder.getTokenType();
    return (current == BashTokenTypes.FUNCTION_KEYWORD || (current == WORD && 
      ParserUtil.hasNextTokens((PsiBuilder)builder, false, FUNCTION_DEF_TOKENLIST)));
  }













  
  public boolean parse(BashPsiBuilder builder) {
    PsiBuilder.Marker function = builder.mark();
    
    IElementType firstToken = builder.getTokenType();
    if (firstToken == BashTokenTypes.FUNCTION_KEYWORD) {
      builder.advanceLexer();

      
      PsiBuilder.Marker nameMarker = builder.mark();

      
      IElementType nameToken = ParserUtil.getTokenAndAdvance((PsiBuilder)builder);
      if (nameToken != BashTokenTypes.WORD) {
        nameMarker.drop();
        ParserUtil.error(function, "parser.unexpected.token");
        return false;
      } 
      
      nameMarker.done(BashElementTypes.FUNCTION_DEF_NAME_ELEMENT);

      
      if (builder.getTokenType() == BashTokenTypes.LEFT_PAREN) {
        builder.advanceLexer();
        if (builder.getTokenType() != BashTokenTypes.RIGHT_PAREN) {
          ParserUtil.error(function, "parser.unexpected.token");
          return false;
        } 
        
        builder.advanceLexer();
      } 
    } else if (firstToken == BashTokenTypes.WORD) {
      
      ParserUtil.markTokenAndAdvance((PsiBuilder)builder, BashElementTypes.FUNCTION_DEF_NAME_ELEMENT);
      
      IElementType leftBracket = ParserUtil.getTokenAndAdvance((PsiBuilder)builder);
      if (leftBracket != BashTokenTypes.LEFT_PAREN) {
        ParserUtil.error(function, "parser.unexpected.token");
        return false;
      } 
      
      IElementType rightBracket = ParserUtil.getTokenAndAdvance((PsiBuilder)builder);
      if (rightBracket != BashTokenTypes.RIGHT_PAREN) {
        ParserUtil.error(function, "parser.unexpected.token");
        return false;
      } 
    } else {
      ParserUtil.error(function, "parser.unexpected.token");
      return false;
    } 

    
    builder.readOptionalNewlines();

    
    PsiBuilder.Marker bodyMarker = builder.mark();
    
    boolean parsed = Parsing.shellCommand.parse(builder);
    if (!parsed) {
      
      function.doneBefore((IElementType)BashElementTypes.FUNCTION_DEF_COMMAND, bodyMarker);
      bodyMarker.drop();
      
      ParserUtil.errorToken((PsiBuilder)builder, "parser.unexpected.token");
      
      return true;
    } 

    
    bodyMarker.drop();

    
    if (builder.getTokenType() == BashTokenTypes.SEMI) {
      builder.advanceLexer();
    }
    
    function.done((IElementType)BashElementTypes.FUNCTION_DEF_COMMAND);
    
    return true;
  }
}
