package com.ansorgit.plugins.bash.lang.psi.impl.command;

import com.ansorgit.plugins.bash.lang.psi.api.command.BashComposedCommand;
import com.ansorgit.plugins.bash.lang.psi.impl.BashBaseElement;
import com.intellij.lang.ASTNode;
















public class BashComposedCommandImpl
  extends BashBaseElement
  implements BashComposedCommand
{
  public BashComposedCommandImpl(ASTNode astNode) {
    super(astNode, "bash composed command");
  }
}
