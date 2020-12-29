package com.ansorgit.plugins.bash.lang.parser.shellCommand;

import com.ansorgit.plugins.bash.lang.parser.BashPsiBuilder;
import com.ansorgit.plugins.bash.lang.parser.OptionalParseResult;
import com.ansorgit.plugins.bash.lang.parser.Parsing;
import com.ansorgit.plugins.bash.lang.parser.ParsingFunction;
import com.ansorgit.plugins.bash.lang.parser.util.ParserUtil;
import com.intellij.lang.PsiBuilder;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;



















public class ConditionalExpressionParsingFunction
  implements ParsingFunction
{
  private static final Logger log = Logger.getInstance("#bash.ConditionalParsingFunction");
  
  private static final TokenSet conditionalRejects = TokenSet.create(new IElementType[] { _EXPR_CONDITIONAL });
  
  public boolean isValid(BashPsiBuilder builder) {
    return (builder.getTokenType() == EXPR_CONDITIONAL);
  }






  
  public boolean parse(BashPsiBuilder builder) {
    PsiBuilder.Marker command = builder.mark();
    
    boolean result = parseConditionalExpression(builder);
    
    command.done(CONDITIONAL_COMMAND);
    return result;
  }






  
  private boolean parseConditionalExpression(BashPsiBuilder builder) {
    ParserUtil.getTokenAndAdvance((PsiBuilder)builder);
    
    boolean success = true;
    
    IElementType tokenType = builder.getTokenType();
    while (!isEndToken(tokenType) && success) {
      if (ParserUtil.isWordToken(tokenType)) {
        builder.advanceLexer();
      } else {
        OptionalParseResult result = Parsing.word.parseWordIfValid(builder, true, conditionalRejects, TokenSet.EMPTY, null);
        if (result.isValid()) {
          success = result.isParsedSuccessfully();
        } else {
          success = ConditionalParsingUtil.readTestExpression(builder, conditionalRejects);
        } 
      } 
      
      tokenType = builder.getTokenType();
    } 


    
    if (builder.getTokenType() == _EXPR_CONDITIONAL) {
      builder.advanceLexer();
      return true;
    } 
    
    ParserUtil.error((PsiBuilder)builder, "parser.shell.conditional.expectedEnd");
    return false;
  }
  
  private boolean isEndToken(IElementType tokenType) {
    return (tokenType == _EXPR_CONDITIONAL);
  }
}
