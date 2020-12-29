package com.ansorgit.plugins.bash.lang.psi.impl.arithmetic;

import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.ansorgit.plugins.bash.lang.psi.api.arithmetic.ArithmeticExpression;
import com.ansorgit.plugins.bash.lang.psi.api.arithmetic.PreIncrementExpression;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.Nullable;















public class PreIncrementExpressionsImpl
  extends AbstractExpression
  implements PreIncrementExpression
{
  public PreIncrementExpressionsImpl(ASTNode astNode) {
    super(astNode, "ArithPreIncrement", ArithmeticExpression.Type.PrefixOperand);
  }

  
  @Nullable
  protected Long compute(long currentValue, IElementType operator, Long nextExpressionValue) {
    if (operator == BashTokenTypes.ARITH_MINUS_MINUS)
      return Long.valueOf(currentValue - 1L); 
    if (operator == BashTokenTypes.ARITH_PLUS_PLUS) {
      return Long.valueOf(currentValue + 1L);
    }
    
    return null;
  }
}
