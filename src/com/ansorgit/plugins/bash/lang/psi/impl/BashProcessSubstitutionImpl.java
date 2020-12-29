package com.ansorgit.plugins.bash.lang.psi.impl;

import com.ansorgit.plugins.bash.lang.psi.api.BashProcessSubstitution;
import com.intellij.lang.ASTNode;
















public class BashProcessSubstitutionImpl
  extends BashBaseElement
  implements BashProcessSubstitution
{
  public BashProcessSubstitutionImpl(ASTNode astNode) {
    super(astNode, "process substitution element");
  }
}
