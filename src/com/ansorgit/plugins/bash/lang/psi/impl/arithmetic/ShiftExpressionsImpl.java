package com.ansorgit.plugins.bash.lang.psi.impl.arithmetic;

import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.ansorgit.plugins.bash.lang.psi.api.arithmetic.ArithmeticExpression;
import com.ansorgit.plugins.bash.lang.psi.api.arithmetic.ShiftExpression;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.Nullable;















public class ShiftExpressionsImpl
  extends AbstractExpression
  implements ShiftExpression
{
  public ShiftExpressionsImpl(ASTNode astNode) {
    super(astNode, "AritShiftExpr", ArithmeticExpression.Type.TwoOperands);
  }

  
  @Nullable
  protected Long compute(long currentValue, IElementType operator, Long nextExpressionValue) {
    if (operator == BashTokenTypes.ARITH_SHIFT_RIGHT) {
      return Long.valueOf(currentValue >> (int)nextExpressionValue.longValue());
    }
    
    if (operator == BashTokenTypes.ARITH_SHIFT_LEFT) {
      return Long.valueOf(currentValue << (int)nextExpressionValue.longValue());
    }
    
    return null;
  }
}
