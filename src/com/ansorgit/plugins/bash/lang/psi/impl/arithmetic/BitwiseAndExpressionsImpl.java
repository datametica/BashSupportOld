package com.ansorgit.plugins.bash.lang.psi.impl.arithmetic;

import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.ansorgit.plugins.bash.lang.psi.api.arithmetic.ArithmeticExpression;
import com.ansorgit.plugins.bash.lang.psi.api.arithmetic.BitwiseAnd;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.Nullable;















public class BitwiseAndExpressionsImpl
  extends AbstractExpression
  implements BitwiseAnd
{
  public BitwiseAndExpressionsImpl(ASTNode astNode) {
    super(astNode, "BitwiseAndExpr", ArithmeticExpression.Type.TwoOperands);
  }

  
  @Nullable
  protected Long compute(long currentValue, IElementType operator, Long nextExpressionValue) {
    if (operator == BashTokenTypes.ARITH_BITWISE_AND) {
      return Long.valueOf(currentValue & nextExpressionValue.longValue());
    }
    
    return null;
  }
}
