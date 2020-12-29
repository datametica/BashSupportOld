package com.ansorgit.plugins.bash.lang.psi.impl.shell;

import com.ansorgit.plugins.bash.lang.psi.api.shell.BashCasePattern;
import com.ansorgit.plugins.bash.lang.psi.impl.BashBaseElement;
import com.intellij.lang.ASTNode;
















public class BashCasePatternImpl
  extends BashBaseElement
  implements BashCasePattern
{
  public BashCasePatternImpl(ASTNode astNode) {
    super(astNode, "BashCasePattern");
  }
}
