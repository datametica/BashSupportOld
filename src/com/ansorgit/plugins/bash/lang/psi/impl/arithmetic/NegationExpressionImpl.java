package com.ansorgit.plugins.bash.lang.psi.impl.arithmetic;

import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.ansorgit.plugins.bash.lang.psi.api.arithmetic.ArithmeticExpression;
import com.ansorgit.plugins.bash.lang.psi.api.arithmetic.NegationExpression;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.Nullable;















public class NegationExpressionImpl
  extends AbstractExpression
  implements NegationExpression
{
  public NegationExpressionImpl(ASTNode astNode) {
    super(astNode, "NegationExpr", ArithmeticExpression.Type.PrefixOperand);
  }

  
  @Nullable
  protected Long compute(long currentValue, IElementType operator, Long nextExpressionValue) {
    if (operator == BashTokenTypes.ARITH_NEGATE)
      return Long.valueOf((currentValue != 0L) ? 0L : 1L); 
    if (operator == BashTokenTypes.ARITH_BITWISE_NEGATE) {
      return Long.valueOf(currentValue ^ 0xFFFFFFFFFFFFFFFFL);
    }
    
    return null;
  }
}
