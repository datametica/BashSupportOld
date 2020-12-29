package com.ansorgit.plugins.bash.lang.psi.impl;

import com.ansorgit.plugins.bash.file.BashFileType;
import com.ansorgit.plugins.bash.lang.psi.api.BashPsiElement;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import org.jetbrains.annotations.NotNull;













public class BashBaseElement
  extends ASTWrapperPsiElement
  implements BashPsiElement
{
  private final String name;
  
  public BashBaseElement(@NotNull ASTNode node, String name) {
    super(node);
    this.name = name;
  }

  
  @NotNull
  public Language getLanguage() {
    if (BashFileType.BASH_LANGUAGE == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/BashBaseElement", "getLanguage" }));  return BashFileType.BASH_LANGUAGE;
  }

  
  public String toString() {
    return "[PSI] " + ((this.name == null) ? super.toString() : this.name);
  }

  
  @NotNull
  public SearchScope getUseScope() {
    if (BashElementSharedImpl.getElementUseScope(this, getProject()) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/BashBaseElement", "getUseScope" }));  return BashElementSharedImpl.getElementUseScope(this, getProject());
  }

  
  @NotNull
  public GlobalSearchScope getResolveScope() {
    if (BashElementSharedImpl.getElementGlobalSearchScope(this, getProject()) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/BashBaseElement", "getResolveScope" }));  return BashElementSharedImpl.getElementGlobalSearchScope(this, getProject());
  }
}
