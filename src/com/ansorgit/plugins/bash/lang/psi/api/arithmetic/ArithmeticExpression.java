package com.ansorgit.plugins.bash.lang.psi.api.arithmetic;

import com.ansorgit.plugins.bash.lang.psi.api.BashPsiElement;
import com.ansorgit.plugins.bash.lang.psi.impl.arithmetic.InvalidExpressionValue;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import java.util.List;
import org.jetbrains.annotations.Nullable;


public interface ArithmeticExpression
  extends BashPsiElement
{
  boolean isStatic();
  
  List<ArithmeticExpression> subexpressions();
  
  long computeNumericValue() throws InvalidExpressionValue;
  
  @Nullable
  ArithmeticExpression findParentExpression();
  
  @Nullable
  PsiElement findOperatorElement();
  
  @Nullable
  IElementType findOperator();
  
  public enum Type
  {
    NoOperands,
    TwoOperands,
    PrefixOperand,
    PostfixOperand,
    Unsupported;
  }
}
