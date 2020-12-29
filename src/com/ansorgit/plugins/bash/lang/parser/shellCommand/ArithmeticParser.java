package com.ansorgit.plugins.bash.lang.parser.shellCommand;

import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.ansorgit.plugins.bash.lang.parser.BashElementTypes;
import com.ansorgit.plugins.bash.lang.parser.BashPsiBuilder;
import com.ansorgit.plugins.bash.lang.parser.ParsingFunction;
import com.ansorgit.plugins.bash.lang.parser.arithmetic.ArithmeticFactory;
import com.ansorgit.plugins.bash.lang.parser.util.ParserUtil;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;





















public final class ArithmeticParser
  implements ParsingFunction
{
  private static final ParsingFunction arithmeticExprParser = (ParsingFunction)ArithmeticFactory.entryPoint();
  
  public boolean isValid(BashPsiBuilder builder) {
    IElementType tokenType = builder.getTokenType();
    return (tokenType == BashTokenTypes.EXPR_ARITH || tokenType == BashTokenTypes.EXPR_ARITH_SQUARE);
  }







  
  public boolean parse(BashPsiBuilder builder) {
    if (ParserUtil.hasNextTokens((PsiBuilder)builder, true, new IElementType[] { EXPR_ARITH, _EXPR_ARITH }) || ParserUtil.hasNextTokens((PsiBuilder)builder, true, new IElementType[] { EXPR_ARITH_SQUARE, _EXPR_ARITH_SQUARE })) {
      builder.advanceLexer();
      builder.advanceLexer();
      return true;
    } 
    
    if (builder.getTokenType() == BashTokenTypes.EXPR_ARITH_SQUARE) {
      return parse(builder, BashTokenTypes.EXPR_ARITH_SQUARE, BashTokenTypes._EXPR_ARITH_SQUARE);
    }
    
    return parse(builder, BashTokenTypes.EXPR_ARITH, BashTokenTypes._EXPR_ARITH);
  }













  
  public boolean parse(BashPsiBuilder builder, IElementType startToken, IElementType endToken) {
    if (builder.getTokenType() != startToken) {
      return false;
    }
    
    PsiBuilder.Marker arithmetic = builder.mark();
    builder.advanceLexer();
    
    if (!arithmeticExprParser.parse(builder)) {
      builder.getTokenType();
      arithmetic.drop();
      ParserUtil.error((PsiBuilder)builder, "parser.unexpected.token");
      return false;
    } 
    
    IElementType lastToken = ParserUtil.getTokenAndAdvance((PsiBuilder)builder);
    if (lastToken != endToken) {
      arithmetic.drop();
      ParserUtil.error((PsiBuilder)builder, "parser.unexpected.token");
      return false;
    } 
    
    arithmetic.done(BashElementTypes.ARITHMETIC_COMMAND);
    return true;
  }
}
