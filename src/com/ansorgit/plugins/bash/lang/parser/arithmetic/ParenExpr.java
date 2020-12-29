package com.ansorgit.plugins.bash.lang.parser.arithmetic;

import com.ansorgit.plugins.bash.lang.parser.BashPsiBuilder;
import com.ansorgit.plugins.bash.lang.parser.util.ParserUtil;
import com.intellij.lang.PsiBuilder;



















class ParenExpr
  implements ArithmeticParsingFunction
{
  public boolean isValid(BashPsiBuilder builder) {
    return (builder.getTokenType() == LEFT_PAREN);
  }
  
  public boolean parse(BashPsiBuilder builder) {
    if (!isValid(builder)) {
      return false;
    }

    
    PsiBuilder.Marker marker = builder.mark();
    
    builder.advanceLexer();
    boolean ok = (ArithmeticFactory.entryPoint().parse(builder) && ParserUtil.conditionalRead((PsiBuilder)builder, RIGHT_PAREN));
    
    if (ok) {
      marker.done(ARITH_PARENS_ELEMENT);
      return true;
    } 
    
    marker.drop();
    return false;
  }
}
