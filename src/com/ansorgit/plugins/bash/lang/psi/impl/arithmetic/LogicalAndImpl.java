package com.ansorgit.plugins.bash.lang.psi.impl.arithmetic;

import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.ansorgit.plugins.bash.lang.psi.api.arithmetic.ArithmeticExpression;
import com.ansorgit.plugins.bash.lang.psi.api.arithmetic.LogicalAnd;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.Nullable;















public class LogicalAndImpl
  extends AbstractExpression
  implements LogicalAnd
{
  public LogicalAndImpl(ASTNode astNode) {
    super(astNode, "LogicalAndExpr", ArithmeticExpression.Type.TwoOperands);
  }

  
  @Nullable
  protected Long compute(long currentValue, IElementType operator, Long nextExpressionValue) {
    if (operator == BashTokenTypes.AND_AND && nextExpressionValue != null) {
      return Long.valueOf((currentValue != 0L && nextExpressionValue.longValue() != 0L) ? 1L : 0L);
    }
    
    return null;
  }
}
