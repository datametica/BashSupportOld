package com.ansorgit.plugins.bash.lang.psi.impl.command;

import com.ansorgit.plugins.bash.lang.psi.api.command.BashPipeline;
import com.ansorgit.plugins.bash.lang.psi.impl.BashBaseElement;
import com.intellij.lang.ASTNode;
















public class BashPipelineImpl
  extends BashBaseElement
  implements BashPipeline
{
  public BashPipelineImpl(ASTNode astNode) {
    super(astNode, "pipeline command");
  }
}
