package com.ansorgit.plugins.bash.lang.psi.impl.arithmetic;

import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.ansorgit.plugins.bash.lang.psi.api.arithmetic.ArithmeticExpression;
import com.ansorgit.plugins.bash.lang.psi.api.arithmetic.ProductExpression;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.IElementType;
import java.util.List;
import org.jetbrains.annotations.Nullable;

















public class ProductExpressionsImpl
  extends AbstractExpression
  implements ProductExpression
{
  public ProductExpressionsImpl(ASTNode astNode) {
    super(astNode, "ArithProductExpr", ArithmeticExpression.Type.TwoOperands);
  }

  
  @Nullable
  protected Long compute(long currentValue, IElementType operator, Long nextExpressionValue) {
    if (operator == BashTokenTypes.ARITH_MULT)
      return Long.valueOf(currentValue * nextExpressionValue.longValue()); 
    if (operator == BashTokenTypes.ARITH_DIV) {
      if (nextExpressionValue.longValue() == 0L) {
        throw new InvalidExpressionValue("Division by zero");
      }
      
      return Long.valueOf(currentValue / nextExpressionValue.longValue());
    }  if (operator == BashTokenTypes.ARITH_MOD) {
      return Long.valueOf(currentValue % nextExpressionValue.longValue());
    }
    
    return null;
  }
  
  public boolean hasDivisionRemainder() {
    List<ArithmeticExpression> subs = subexpressions();
    
    if (subs.size() == 2 && findOperator() == BashTokenTypes.ARITH_DIV) {
      if (!((ArithmeticExpression)subs.get(0)).isStatic() || !((ArithmeticExpression)subs.get(1)).isStatic()) {
        return false;
      }
      
      long leftValue = ((ArithmeticExpression)subs.get(0)).computeNumericValue();
      long rightValue = ((ArithmeticExpression)subs.get(1)).computeNumericValue();
      
      return (rightValue != 0L && leftValue != leftValue / rightValue * rightValue);
    } 
    
    return false;
  }
}
