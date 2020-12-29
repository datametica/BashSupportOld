package com.ansorgit.plugins.bash.lang.psi.impl.command;

import com.ansorgit.plugins.bash.lang.psi.api.command.BashGenericCommand;
import com.ansorgit.plugins.bash.lang.psi.impl.BashBaseElement;
import com.intellij.lang.ASTNode;













public class BashGenericCommandImpl
  extends BashBaseElement
  implements BashGenericCommand
{
  public BashGenericCommandImpl(ASTNode astNode) {
    super(astNode, "BashGenericCommand");
  }

  
  public boolean canNavigate() {
    return false;
  }

  
  public boolean canNavigateToSource() {
    return false;
  }
}
