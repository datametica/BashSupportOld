package com.ansorgit.plugins.bash.lang.psi.impl.arithmetic;

import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.ansorgit.plugins.bash.lang.psi.api.arithmetic.ArithmeticExpression;
import com.ansorgit.plugins.bash.lang.psi.api.arithmetic.ExponentExpr;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.Nullable;















public class EqualityExprImpl
  extends AbstractExpression
  implements ExponentExpr
{
  public EqualityExprImpl(ASTNode astNode) {
    super(astNode, "EqualityExpr", ArithmeticExpression.Type.TwoOperands);
  }

  
  @Nullable
  protected Long compute(long currentValue, IElementType operator, Long nextExpressionValue) {
    if (operator == BashTokenTypes.ARITH_EQ)
      return Long.valueOf((currentValue == nextExpressionValue.longValue()) ? 1L : 0L); 
    if (operator == BashTokenTypes.ARITH_NE) {
      return Long.valueOf((currentValue != nextExpressionValue.longValue()) ? 1L : 0L);
    }
    
    return null;
  }
}
