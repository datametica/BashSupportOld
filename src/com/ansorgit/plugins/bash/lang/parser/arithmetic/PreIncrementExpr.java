package com.ansorgit.plugins.bash.lang.parser.arithmetic;

import com.ansorgit.plugins.bash.lang.parser.BashPsiBuilder;
import com.ansorgit.plugins.bash.lang.parser.util.ParserUtil;
import com.intellij.lang.PsiBuilder;



















class PreIncrementExpr
  implements ArithmeticParsingFunction
{
  private final ArithmeticParsingFunction next;
  
  PreIncrementExpr(ArithmeticParsingFunction next) {
    this.next = next;
  }
  
  public boolean isValid(BashPsiBuilder builder) {
    return (arithmeticPreOps.contains(builder.getTokenType()) || this.next.isValid(builder));
  }
  
  public boolean parse(BashPsiBuilder builder) {
    PsiBuilder.Marker marker = builder.mark();
    boolean mark = ParserUtil.conditionalRead((PsiBuilder)builder, arithmeticPreOps);
    
    boolean ok = this.next.parse(builder);
    
    if (mark) {
      marker.done(ARITH_PRE_INC_ELEMENT);
    } else {
      marker.drop();
    } 
    
    return ok;
  }
}
