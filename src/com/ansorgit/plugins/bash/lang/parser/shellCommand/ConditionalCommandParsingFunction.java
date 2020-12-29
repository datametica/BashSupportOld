package com.ansorgit.plugins.bash.lang.parser.shellCommand;

import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.ansorgit.plugins.bash.lang.parser.BashElementTypes;
import com.ansorgit.plugins.bash.lang.parser.BashPsiBuilder;
import com.ansorgit.plugins.bash.lang.parser.ParsingFunction;
import com.ansorgit.plugins.bash.lang.parser.util.ParserUtil;
import com.intellij.lang.PsiBuilder;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;




















public class ConditionalCommandParsingFunction
  implements ParsingFunction
{
  private static final Logger log = Logger.getInstance("#bash.ConditionalCommandParsingFunction");
  
  private static final TokenSet endTokens = TokenSet.create(new IElementType[] { _BRACKET_KEYWORD, AND_AND, OR_OR });

  
  public boolean isValid(BashPsiBuilder builder) {
    return (builder.getTokenType() == BashTokenTypes.BRACKET_KEYWORD);
  }























  
  public boolean parse(BashPsiBuilder builder) {
    boolean ok;
    IElementType token = builder.getTokenType();
    log.assertTrue((token == BRACKET_KEYWORD));
    
    PsiBuilder.Marker startMarker = builder.mark();
    builder.advanceLexer();

    
    if (builder.getTokenType() == _BRACKET_KEYWORD) {
      builder.error("Empty expression is not allowed");
      ok = false;
    } else {
      ok = parseExpression(builder);
    } 
    
    int i = ok & ((builder.getTokenType() == _BRACKET_KEYWORD) ? 1 : 0);
    
    if (i != 0) {
      builder.advanceLexer();
      startMarker.done(BashElementTypes.EXTENDED_CONDITIONAL_COMMAND);
      return true;
    } 
    
    startMarker.drop();
    return false;
  }
  
  private boolean parseExpression(BashPsiBuilder builder) {
    return parseExpression(builder, TokenSet.EMPTY);
  }
  
  private boolean parseExpression(BashPsiBuilder builder, TokenSet additionalEndTokens) {
    boolean ok = true;
    
    int counter = 0;
    
    while (ok) {
      IElementType token = builder.getTokenType();

      
      if (token == LEFT_PAREN) {
        builder.advanceLexer();
        ok = parseExpression(builder, TokenSet.create(new IElementType[] { RIGHT_PAREN }));
        ok &= ParserUtil.conditionalRead((PsiBuilder)builder, RIGHT_PAREN);
      } else if (token == COND_OP_NOT) {
        builder.advanceLexer();
        ok = parseExpression(builder, additionalEndTokens);
      } else if (counter >= 1 && token == OR_OR) {
        builder.advanceLexer();
        ok = parseExpression(builder, additionalEndTokens);
      } else if (counter >= 1 && token == AND_AND) {
        builder.advanceLexer();
        ok = parseExpression(builder, additionalEndTokens);
      } else {
        ok = ConditionalParsingUtil.readTestExpression(builder, TokenSet.orSet(new TokenSet[] { endTokens, additionalEndTokens }));
      } 
      
      if (ok) {
        counter++;
      }
      
      if (RIGHT_PAREN == builder.getTokenType() || _BRACKET_KEYWORD == builder.getTokenType()) {
        break;
      }
    } 
    
    return ok;
  }
}
