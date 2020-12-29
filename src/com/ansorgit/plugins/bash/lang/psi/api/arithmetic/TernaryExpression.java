package com.ansorgit.plugins.bash.lang.psi.api.arithmetic;

import org.jetbrains.annotations.NotNull;

public interface TernaryExpression extends ArithmeticExpression {
  @NotNull
  ArithmeticExpression findCondition();
  
  @NotNull
  ArithmeticExpression findMainBranch();
  
  @NotNull
  ArithmeticExpression findElseBranch();
}
