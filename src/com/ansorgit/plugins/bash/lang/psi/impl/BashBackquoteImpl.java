package com.ansorgit.plugins.bash.lang.psi.impl;

import com.ansorgit.plugins.bash.lang.parser.BashElementTypes;
import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.BashBackquote;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;















public class BashBackquoteImpl
  extends BashCompositeElement
  implements BashBackquote
{
  public BashBackquoteImpl() {
    super(BashElementTypes.BACKQUOTE_COMMAND);
  }
  
  public String getCommandText() {
    return getCommandTextRange().substring(getText());
  }

  
  @NotNull
  public TextRange getCommandTextRange() {
    if (TextRange.from(1, getTextLength() - 2) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/BashBackquoteImpl", "getCommandTextRange" }));  return TextRange.from(1, getTextLength() - 2);
  }

  
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "visitor", "com/ansorgit/plugins/bash/lang/psi/impl/BashBackquoteImpl", "accept" }));  if (visitor instanceof BashVisitor) {
      ((BashVisitor)visitor).visitBackquoteCommand(this);
    } else {
      visitor.visitElement((PsiElement)this);
    } 
  }
}
