package com.ansorgit.plugins.bash.lang.psi.impl.arithmetic;

import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.ansorgit.plugins.bash.lang.psi.api.arithmetic.ArithmeticExpression;
import com.ansorgit.plugins.bash.lang.psi.api.arithmetic.ExponentExpr;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.Nullable;















public class ExponentExprImpl
  extends AbstractExpression
  implements ExponentExpr
{
  public ExponentExprImpl(ASTNode astNode) {
    super(astNode, "ExponentExpr", ArithmeticExpression.Type.TwoOperands);
  }

  
  @Nullable
  protected Long compute(long currentValue, IElementType operator, Long nextExpressionValue) {
    if (operator == BashTokenTypes.ARITH_EXPONENT) {
      return Long.valueOf((long)Math.pow(currentValue, nextExpressionValue.doubleValue()));
    }
    
    return null;
  }
}
