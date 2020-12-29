package com.ansorgit.plugins.bash.lang.psi.impl.vars;

import com.ansorgit.plugins.bash.lang.psi.util.BashResolveUtil;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;


















final class SmartVarDefReference
  extends AbstractVarDefReference
{
  public SmartVarDefReference(BashVarDefImpl bashVarDef) {
    super(bashVarDef);
  }

  
  @Nullable
  public PsiElement resolveInner() {
    if (this.bashVarDef.isCommandLocal()) {
      return null;
    }
    
    return BashResolveUtil.resolve(this.bashVarDef, false, false);
  }
}
