package com.ansorgit.plugins.bash.lang.psi.impl.word;

import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.word.BashExpansion;
import com.ansorgit.plugins.bash.lang.psi.impl.BashBaseElement;
import com.ansorgit.plugins.bash.lang.valueExpansion.ValueExpansionUtil;
import com.ansorgit.plugins.bash.settings.BashProjectSettings;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;















public class BashExpansionImpl
  extends BashBaseElement
  implements BashExpansion
{
  public BashExpansionImpl(ASTNode astNode) {
    super(astNode, "Bash expansion");
  }

  
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "visitor", "com/ansorgit/plugins/bash/lang/psi/impl/word/BashExpansionImpl", "accept" }));  if (visitor instanceof BashVisitor) {
      BashVisitor v = (BashVisitor)visitor;
      v.visitExpansion(this);
    } else {
      visitor.visitElement((PsiElement)this);
    } 
  }

  
  public boolean isWrapped() {
    return false;
  }

  
  public String createEquallyWrappedString(String newContent) {
    return getText();
  }
  
  public String getUnwrappedCharSequence() {
    return getText();
  }
  
  public boolean isStatic() {
    return true;
  }
  
  @NotNull
  public TextRange getTextContentRange() {
    if (TextRange.from(0, getTextLength()) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/word/BashExpansionImpl", "getTextContentRange" }));  return TextRange.from(0, getTextLength());
  }
  
  public boolean isWrappable() {
    return false;
  }
  
  public boolean isValidExpansion() {
    return ValueExpansionUtil.isValid(getText(), BashProjectSettings.storedSettings(getProject()).isSupportBash4());
  }
}
