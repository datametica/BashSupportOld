package com.ansorgit.plugins.bash.lang.psi.impl.shell;

import com.ansorgit.plugins.bash.lang.parser.BashElementTypes;
import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.shell.BashConditionalCommand;
import com.ansorgit.plugins.bash.lang.psi.impl.BashCompositeElement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;













public class BashConditionalCommandImpl
  extends BashCompositeElement
  implements BashConditionalCommand
{
  private static final int NUMBER_OF_CHARACTERS = 1;
  
  public BashConditionalCommandImpl() {
    super(BashElementTypes.CONDITIONAL_COMMAND);
  }

  
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "visitor", "com/ansorgit/plugins/bash/lang/psi/impl/shell/BashConditionalCommandImpl", "accept" }));  if (visitor instanceof BashVisitor) {
      ((BashVisitor)visitor).visitConditional(this);
    } else {
      visitor.visitElement((PsiElement)this);
    } 
  }
  
  public String getCommandText() {
    String text = getText();
    return text.substring(1, text.length() - 1);
  }
}
