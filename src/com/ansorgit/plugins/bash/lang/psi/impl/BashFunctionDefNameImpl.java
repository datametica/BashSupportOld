package com.ansorgit.plugins.bash.lang.psi.impl;

import com.ansorgit.plugins.bash.lang.psi.api.BashFunctionDefName;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;
















public class BashFunctionDefNameImpl
  extends BashBaseElement
  implements BashFunctionDefName
{
  public BashFunctionDefNameImpl(ASTNode astNode) {
    super(astNode, "BashFunctionDefName");
  }
  
  @NotNull
  public String getNameString() {
    if (getText() == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/BashFunctionDefNameImpl", "getNameString" }));  return getText();
  }
}
