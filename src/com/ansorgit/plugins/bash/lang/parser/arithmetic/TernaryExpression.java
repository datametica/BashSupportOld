package com.ansorgit.plugins.bash.lang.parser.arithmetic;

import com.ansorgit.plugins.bash.lang.parser.BashPsiBuilder;
import com.ansorgit.plugins.bash.lang.parser.util.ParserUtil;
import com.intellij.lang.PsiBuilder;



















class TernaryExpression
  implements ArithmeticParsingFunction
{
  private final ArithmeticParsingFunction next;
  
  TernaryExpression(ArithmeticParsingFunction next) {
    this.next = next;
  }
  
  public boolean isValid(BashPsiBuilder builder) {
    return this.next.isValid(builder);
  }
  
  public boolean parse(BashPsiBuilder builder) {
    PsiBuilder.Marker marker = builder.mark();
    boolean ok = this.next.parse(builder);
    
    if (ok && ParserUtil.conditionalRead((PsiBuilder)builder, ARITH_QMARK)) {
      ok = this.next.parse(builder);
      ok = (ok && ParserUtil.conditionalRead((PsiBuilder)builder, ARITH_COLON) && this.next.parse(builder));
      
      if (ok) {
        marker.done(ARITH_TERNERAY_ELEMENT);
      } else {
        marker.drop();
      } 
    } else {
      marker.drop();
    } 
    
    return ok;
  }
}
