package com.ansorgit.plugins.bash.lang.psi.impl.arithmetic;

import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.ansorgit.plugins.bash.lang.psi.api.arithmetic.ArithmeticExpression;
import com.ansorgit.plugins.bash.lang.psi.api.arithmetic.BitwiseOr;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.Nullable;















public class BitwiseOrExpressionsImpl
  extends AbstractExpression
  implements BitwiseOr
{
  public BitwiseOrExpressionsImpl(ASTNode astNode) {
    super(astNode, "BitwiseOrExpr", ArithmeticExpression.Type.TwoOperands);
  }

  
  @Nullable
  protected Long compute(long currentValue, IElementType operator, Long nextExpressionValue) {
    if (operator == BashTokenTypes.PIPE) {
      return Long.valueOf(currentValue | nextExpressionValue.longValue());
    }
    
    return null;
  }
}
