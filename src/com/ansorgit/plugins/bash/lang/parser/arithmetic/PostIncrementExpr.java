package com.ansorgit.plugins.bash.lang.parser.arithmetic;

import com.ansorgit.plugins.bash.lang.parser.BashPsiBuilder;
import com.ansorgit.plugins.bash.lang.parser.util.ParserUtil;
import com.intellij.lang.PsiBuilder;



















class PostIncrementExpr
  implements ArithmeticParsingFunction
{
  private final ArithmeticParsingFunction next;
  
  PostIncrementExpr(ArithmeticParsingFunction next) {
    this.next = next;
  }
  
  public boolean isValid(BashPsiBuilder builder) {
    return this.next.isValid(builder);
  }
  
  public boolean parse(BashPsiBuilder builder) {
    PsiBuilder.Marker marker = builder.mark();
    
    boolean ok = this.next.parse(builder);
    if (ok && ParserUtil.conditionalRead((PsiBuilder)builder, arithmeticPostOps)) {
      marker.done(ARITH_POST_INCR_ELEMENT);
    } else {
      marker.drop();
    } 
    
    return ok;
  }
}
