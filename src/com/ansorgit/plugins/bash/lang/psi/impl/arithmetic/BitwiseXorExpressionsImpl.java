package com.ansorgit.plugins.bash.lang.psi.impl.arithmetic;

import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.ansorgit.plugins.bash.lang.psi.api.arithmetic.ArithmeticExpression;
import com.ansorgit.plugins.bash.lang.psi.api.arithmetic.BitwiseXor;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.Nullable;















public class BitwiseXorExpressionsImpl
  extends AbstractExpression
  implements BitwiseXor
{
  public BitwiseXorExpressionsImpl(ASTNode astNode) {
    super(astNode, "BitwiseXorExpr", ArithmeticExpression.Type.TwoOperands);
  }

  
  @Nullable
  protected Long compute(long currentValue, IElementType operator, Long nextExpressionValue) {
    if (operator == BashTokenTypes.ARITH_BITWISE_XOR) {
      return Long.valueOf(currentValue ^ nextExpressionValue.longValue());
    }
    
    return null;
  }
}
