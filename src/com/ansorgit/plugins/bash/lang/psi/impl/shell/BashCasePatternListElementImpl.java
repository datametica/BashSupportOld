package com.ansorgit.plugins.bash.lang.psi.impl.shell;

import com.ansorgit.plugins.bash.lang.psi.api.shell.BashCasePatternListElement;
import com.ansorgit.plugins.bash.lang.psi.impl.BashBaseElement;
import com.intellij.lang.ASTNode;
















public class BashCasePatternListElementImpl
  extends BashBaseElement
  implements BashCasePatternListElement
{
  public BashCasePatternListElementImpl(ASTNode astNode) {
    super(astNode, "case pattern list element");
  }
}
