package com.ansorgit.plugins.bash.lang.psi.impl.arithmetic;

import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.ansorgit.plugins.bash.lang.psi.api.arithmetic.ArithmeticExpression;
import com.ansorgit.plugins.bash.lang.psi.api.arithmetic.SumExpression;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.Nullable;















public class SumExpressionsImpl
  extends AbstractExpression
  implements SumExpression
{
  public SumExpressionsImpl(ASTNode astNode) {
    super(astNode, "ArithSumExpr", ArithmeticExpression.Type.TwoOperands);
  }

  
  @Nullable
  protected Long compute(long currentValue, IElementType operator, Long nextExpressionValue) {
    if (operator == BashTokenTypes.ARITH_PLUS)
      return Long.valueOf(currentValue + nextExpressionValue.longValue()); 
    if (operator == BashTokenTypes.ARITH_MINUS) {
      return Long.valueOf(currentValue - nextExpressionValue.longValue());
    }
    
    return null;
  }
}
