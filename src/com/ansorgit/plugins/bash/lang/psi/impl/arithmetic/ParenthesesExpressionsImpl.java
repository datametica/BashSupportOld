package com.ansorgit.plugins.bash.lang.psi.impl.arithmetic;

import com.ansorgit.plugins.bash.lang.psi.api.arithmetic.ArithmeticExpression;
import com.ansorgit.plugins.bash.lang.psi.api.arithmetic.ParenthesesExpression;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.IElementType;
import java.util.List;
import org.jetbrains.annotations.Nullable;

















public class ParenthesesExpressionsImpl
  extends AbstractExpression
  implements ParenthesesExpression
{
  public ParenthesesExpressionsImpl(ASTNode astNode) {
    super(astNode, "ArithParenExpr", ArithmeticExpression.Type.NoOperands);
  }

  
  @Nullable
  protected Long compute(long currentValue, IElementType operator, Long nextExpressionValue) {
    throw new UnsupportedOperationException("unsupported");
  }

  
  public long computeNumericValue() {
    List<ArithmeticExpression> childs = subexpressions();
    if (childs.size() != 1) {
      throw new IllegalStateException("impossible state");
    }
    
    return ((ArithmeticExpression)childs.get(0)).computeNumericValue();
  }
}
