package com.ansorgit.plugins.bash.lang.psi.impl.arithmetic;

import com.ansorgit.plugins.bash.lang.psi.api.arithmetic.ArithmeticExpression;
import com.ansorgit.plugins.bash.lang.psi.api.arithmetic.PostIncrementExpression;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.Nullable;















public class PostIncrementExpressionsImpl
  extends AbstractExpression
  implements PostIncrementExpression
{
  public PostIncrementExpressionsImpl(ASTNode astNode) {
    super(astNode, "ArithPostIncrement", ArithmeticExpression.Type.PostfixOperand);
  }

  
  @Nullable
  protected Long compute(long currentValue, IElementType operator, Long nextExpressionValue) {
    throw new UnsupportedOperationException("unsupported");
  }
}
