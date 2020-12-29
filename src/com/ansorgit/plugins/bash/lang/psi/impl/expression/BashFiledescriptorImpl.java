package com.ansorgit.plugins.bash.lang.psi.impl.expression;

import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.expression.BashFiledescriptor;
import com.ansorgit.plugins.bash.lang.psi.impl.BashBaseElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;















public class BashFiledescriptorImpl
  extends BashBaseElement
  implements BashFiledescriptor
{
  public BashFiledescriptorImpl(ASTNode astNode) {
    super(astNode, "Bash filedescriptor");
  }

  
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "visitor", "com/ansorgit/plugins/bash/lang/psi/impl/expression/BashFiledescriptorImpl", "accept" }));  if (visitor instanceof BashVisitor) {
      ((BashVisitor)visitor).visitFiledescriptor(this);
    } else {
      visitor.visitElement((PsiElement)this);
    } 
  }
  
  @Nullable
  public Integer descriptorAsInt() {
    String text = getText();
    if (text.length() <= 0 || text.charAt(0) != '&' || text.equals("&-")) {
      return null;
    }
    
    try {
      return Integer.valueOf(text.substring(1));
    } catch (NumberFormatException e) {
      return null;
    } 
  }
}
