package com.ansorgit.plugins.bash.lang.psi.impl.arithmetic;

import com.ansorgit.plugins.bash.lang.psi.api.arithmetic.ArithmeticExpression;
import com.ansorgit.plugins.bash.lang.psi.api.arithmetic.TernaryExpression;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
















public class TernaryExpressionsImpl
  extends AbstractExpression
  implements TernaryExpression
{
  public TernaryExpressionsImpl(ASTNode astNode) {
    super(astNode, "ArithTernaryExpr", ArithmeticExpression.Type.Unsupported);
  }
  
  @NotNull
  public ArithmeticExpression findCondition() {
    ArithmeticExpression[] firstChild = (ArithmeticExpression[])findChildrenByClass(ArithmeticExpression.class);
    
    if (firstChild[0] == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/arithmetic/TernaryExpressionsImpl", "findCondition" }));  return firstChild[0];
  }
  
  @NotNull
  public ArithmeticExpression findMainBranch() {
    ArithmeticExpression[] firstChild = (ArithmeticExpression[])findChildrenByClass(ArithmeticExpression.class);
    
    if (firstChild[1] == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/arithmetic/TernaryExpressionsImpl", "findMainBranch" }));  return firstChild[1];
  }
  
  @NotNull
  public ArithmeticExpression findElseBranch() {
    ArithmeticExpression[] firstChild = (ArithmeticExpression[])findChildrenByClass(ArithmeticExpression.class);
    
    if (firstChild[2] == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/arithmetic/TernaryExpressionsImpl", "findElseBranch" }));  return firstChild[2];
  }

  
  public boolean isStatic() {
    ArithmeticExpression condition = findCondition();
    ArithmeticExpression mainBranch = findMainBranch();
    ArithmeticExpression elseBranch = findElseBranch();
    
    if (condition.isStatic()) {
      return (condition.computeNumericValue() != 0L) ? mainBranch.isStatic() : elseBranch.isStatic();
    }


    
    return (mainBranch.isStatic() && elseBranch
      .isStatic() && mainBranch
      .computeNumericValue() == elseBranch.computeNumericValue());
  }


  
  public long computeNumericValue() {
    ArithmeticExpression condition = findCondition();
    
    if (condition.isStatic()) {
      if (condition.computeNumericValue() != 0L) {
        return findMainBranch().computeNumericValue();
      }
      return findElseBranch().computeNumericValue();
    } 


    
    return findMainBranch().computeNumericValue();
  }

  
  @Nullable
  protected Long compute(long currentValue, IElementType operator, Long nextExpressionValue) {
    throw new UnsupportedOperationException("compute is unsupported in a ternary expression");
  }
}
