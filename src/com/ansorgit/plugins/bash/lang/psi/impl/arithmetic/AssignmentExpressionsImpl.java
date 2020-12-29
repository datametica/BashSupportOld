package com.ansorgit.plugins.bash.lang.psi.impl.arithmetic;

import com.ansorgit.plugins.bash.lang.psi.api.arithmetic.ArithmeticExpression;
import com.ansorgit.plugins.bash.lang.psi.api.arithmetic.AssignmentExpression;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.Nullable;
















public class AssignmentExpressionsImpl
  extends AbstractExpression
  implements AssignmentExpression
{
  public AssignmentExpressionsImpl(ASTNode astNode) {
    super(astNode, "ArithmeticAssignmentChain", ArithmeticExpression.Type.Unsupported);
  }

  
  @Nullable
  protected Long compute(long currentValue, IElementType operator, Long nextExpressionValue) {
    throw new UnsupportedOperationException("compute is not unsupported");
  }


  
  public long computeNumericValue() {
    PsiElement child = getLastChild();
    if (child instanceof ArithmeticExpression) {
      return ((ArithmeticExpression)child).computeNumericValue();
    }
    
    throw new IllegalStateException("computeNumericValue is not supported in this configuration");
  }
}
