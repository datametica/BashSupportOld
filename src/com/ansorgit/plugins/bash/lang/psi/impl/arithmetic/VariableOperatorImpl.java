package com.ansorgit.plugins.bash.lang.psi.impl.arithmetic;

import com.ansorgit.plugins.bash.lang.psi.api.arithmetic.ArithmeticExpression;
import com.ansorgit.plugins.bash.lang.psi.api.arithmetic.VariableOperator;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.Nullable;












public class VariableOperatorImpl
  extends AbstractExpression
  implements VariableOperator
{
  public VariableOperatorImpl(ASTNode astNode) {
    super(astNode, "VarOperator", ArithmeticExpression.Type.TwoOperands);
  }

  
  public boolean isStatic() {
    return false;
  }

  
  @Nullable
  protected Long compute(long currentValue, IElementType operator, Long nextExpressionValue) {
    throw new UnsupportedOperationException("compute is not supported");
  }
}
