package com.ansorgit.plugins.bash.lang.psi.impl;

import com.ansorgit.plugins.bash.file.BashFileType;
import com.ansorgit.plugins.bash.lang.psi.api.BashPsiElement;
import com.intellij.lang.Language;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.impl.source.tree.CompositePsiElement;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;













public abstract class BashCompositeElement
  extends CompositePsiElement
  implements BashPsiElement
{
  private String name = null;
  
  protected BashCompositeElement(IElementType type) {
    super(type);
  }

  
  @NotNull
  public Language getLanguage() {
    if (BashFileType.BASH_LANGUAGE == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/BashCompositeElement", "getLanguage" }));  return BashFileType.BASH_LANGUAGE;
  }

  
  public String toString() {
    return "[PSI] " + ((this.name == null) ? super.toString() : this.name);
  }

  
  @NotNull
  public SearchScope getUseScope() {
    if (BashElementSharedImpl.getElementUseScope(this, getProject()) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/BashCompositeElement", "getUseScope" }));  return BashElementSharedImpl.getElementUseScope(this, getProject());
  }

  
  @NotNull
  public GlobalSearchScope getResolveScope() {
    if (BashElementSharedImpl.getElementGlobalSearchScope(this, getProject()) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/BashCompositeElement", "getResolveScope" }));  return BashElementSharedImpl.getElementGlobalSearchScope(this, getProject());
  }

  
  public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement place) {
    if (processor == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "processor", "com/ansorgit/plugins/bash/lang/psi/impl/BashCompositeElement", "processDeclarations" }));  if (state == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "state", "com/ansorgit/plugins/bash/lang/psi/impl/BashCompositeElement", "processDeclarations" }));  if (place == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "place", "com/ansorgit/plugins/bash/lang/psi/impl/BashCompositeElement", "processDeclarations" }));  if (!processor.execute((PsiElement)this, state)) {
      return false;
    }
    
    return BashElementSharedImpl.walkDefinitionScope((PsiElement)this, processor, state, lastParent, place);
  }
}
