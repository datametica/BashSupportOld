package com.ansorgit.plugins.bash.lang.psi.impl.shell;

import com.ansorgit.plugins.bash.lang.parser.BashElementTypes;
import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.shell.BashExtendedConditionalCommand;
import com.ansorgit.plugins.bash.lang.psi.impl.BashCompositeElement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;













public class BashExtendedConditionalCommandImpl
  extends BashCompositeElement
  implements BashExtendedConditionalCommand
{
  public BashExtendedConditionalCommandImpl() {
    super(BashElementTypes.EXTENDED_CONDITIONAL_COMMAND);
  }

  
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "visitor", "com/ansorgit/plugins/bash/lang/psi/impl/shell/BashExtendedConditionalCommandImpl", "accept" }));  if (visitor instanceof BashVisitor) {
      ((BashVisitor)visitor).visitExtendedConditional(this);
    } else {
      visitor.visitElement((PsiElement)this);
    } 
  }
}
