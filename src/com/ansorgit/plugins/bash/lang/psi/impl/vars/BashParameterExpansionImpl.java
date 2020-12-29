package com.ansorgit.plugins.bash.lang.psi.impl.vars;

import com.ansorgit.plugins.bash.lang.psi.api.vars.BashParameterExpansion;
import com.ansorgit.plugins.bash.lang.psi.impl.BashBaseElement;
import com.intellij.lang.ASTNode;


















public class BashParameterExpansionImpl
  extends BashBaseElement
  implements BashParameterExpansion
{
  public BashParameterExpansionImpl(ASTNode astNode) {
    super(astNode, "Parameter expansion");
  }

  
  public boolean isParameterReference() {
    return ("*".equals(getText()) || "@".equals(getText()));
  }
}
