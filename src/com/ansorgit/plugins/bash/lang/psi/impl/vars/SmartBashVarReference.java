package com.ansorgit.plugins.bash.lang.psi.impl.vars;

import com.ansorgit.plugins.bash.lang.psi.util.BashResolveUtil;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;




















class SmartBashVarReference
  extends AbstractBashVarReference
{
  private final boolean preferNeighborhood;
  
  public SmartBashVarReference(BashVarImpl bashVar) {
    this(bashVar, false);
  }
  
  public SmartBashVarReference(BashVarImpl bashVar, boolean preferNeighborhood) {
    super(bashVar);
    this.preferNeighborhood = preferNeighborhood;
  }

  
  @Nullable
  public PsiElement resolveInner() {
    return BashResolveUtil.resolve(this.bashVar, false, this.preferNeighborhood);
  }
}
