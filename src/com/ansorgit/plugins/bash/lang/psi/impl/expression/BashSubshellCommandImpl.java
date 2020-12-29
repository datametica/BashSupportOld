package com.ansorgit.plugins.bash.lang.psi.impl.expression;

import com.ansorgit.plugins.bash.lang.parser.BashElementTypes;
import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.expression.BashSubshellCommand;
import com.ansorgit.plugins.bash.lang.psi.impl.BashCompositeElement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;















public class BashSubshellCommandImpl
  extends BashCompositeElement
  implements BashSubshellCommand
{
  public BashSubshellCommandImpl() {
    super(BashElementTypes.SUBSHELL_COMMAND);
  }

  
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "visitor", "com/ansorgit/plugins/bash/lang/psi/impl/expression/BashSubshellCommandImpl", "accept" }));  if (visitor instanceof BashVisitor) {
      ((BashVisitor)visitor).visitSubshell(this);
    } else {
      visitor.visitElement((PsiElement)this);
    } 
  }
  
  public String getCommandText() {
    String text = getText();
    return text.substring(1, text.length() - 1);
  }

  
  public boolean isCommandGroup() {
    return false;
  }
}
