package com.ansorgit.plugins.bash.lang.psi.impl.vars;

import com.ansorgit.plugins.bash.lang.psi.api.vars.BashAssignmentList;
import com.ansorgit.plugins.bash.lang.psi.impl.BashBaseElement;
import com.intellij.lang.ASTNode;
















public class BashAssignmentListImpl
  extends BashBaseElement
  implements BashAssignmentList
{
  public BashAssignmentListImpl(ASTNode node) {
    super(node, "assignment list");
  }
}
