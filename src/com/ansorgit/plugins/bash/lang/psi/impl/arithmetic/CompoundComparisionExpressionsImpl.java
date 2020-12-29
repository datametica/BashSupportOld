package com.ansorgit.plugins.bash.lang.psi.impl.arithmetic;

import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.ansorgit.plugins.bash.lang.psi.api.arithmetic.ArithmeticExpression;
import com.ansorgit.plugins.bash.lang.psi.api.arithmetic.CompoundComparision;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.Nullable;















public class CompoundComparisionExpressionsImpl
  extends AbstractExpression
  implements CompoundComparision
{
  public CompoundComparisionExpressionsImpl(ASTNode astNode) {
    super(astNode, "Compound comparision", ArithmeticExpression.Type.TwoOperands);
  }

  
  @Nullable
  protected Long compute(long currentValue, IElementType operator, Long nextExpressionValue) {
    if (operator == BashTokenTypes.ARITH_LT) {
      return Long.valueOf((currentValue < nextExpressionValue.longValue()) ? 1L : 0L);
    }
    
    if (operator == BashTokenTypes.ARITH_LE) {
      return Long.valueOf((currentValue <= nextExpressionValue.longValue()) ? 1L : 0L);
    }
    
    if (operator == BashTokenTypes.ARITH_GT) {
      return Long.valueOf((currentValue > nextExpressionValue.longValue()) ? 1L : 0L);
    }
    
    if (operator == BashTokenTypes.ARITH_GE) {
      return Long.valueOf((currentValue >= nextExpressionValue.longValue()) ? 1L : 0L);
    }
    
    return null;
  }
}
