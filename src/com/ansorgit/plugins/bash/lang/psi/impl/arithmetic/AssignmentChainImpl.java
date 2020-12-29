package com.ansorgit.plugins.bash.lang.psi.impl.arithmetic;

import com.ansorgit.plugins.bash.lang.psi.api.arithmetic.ArithmeticExpression;
import com.ansorgit.plugins.bash.lang.psi.api.arithmetic.AssignmentChain;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.Nullable;















public class AssignmentChainImpl
  extends AbstractExpression
  implements AssignmentChain
{
  public AssignmentChainImpl(ASTNode astNode) {
    super(astNode, "ArithAssignmentExpr", ArithmeticExpression.Type.Unsupported);
  }

  
  @Nullable
  protected Long compute(long currentValue, IElementType operator, Long nextExpressionValue) {
    throw new UnsupportedOperationException("compute is not supported");
  }



  
  public boolean isStatic() {
    return false;
  }

  
  public long computeNumericValue() {
    throw new UnsupportedOperationException("computeNumvericValue is not supported for assignment chains");
  }
}
