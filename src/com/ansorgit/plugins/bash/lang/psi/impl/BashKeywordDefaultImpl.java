package com.ansorgit.plugins.bash.lang.psi.impl;

import com.ansorgit.plugins.bash.lang.psi.api.BashKeyword;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import com.ansorgit.plugins.bash.lang.psi.util.BashResolveUtil;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.tree.IElementType;
import javax.swing.Icon;
import org.jetbrains.annotations.NotNull;

















public abstract class BashKeywordDefaultImpl
  extends BashCompositeElement
  implements BashKeyword
{
  protected BashKeywordDefaultImpl(IElementType type) {
    super(type);
  }

  
  public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement place) {
    if (processor == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "processor", "com/ansorgit/plugins/bash/lang/psi/impl/BashKeywordDefaultImpl", "processDeclarations" }));  if (state == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "state", "com/ansorgit/plugins/bash/lang/psi/impl/BashKeywordDefaultImpl", "processDeclarations" }));  if (place == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "place", "com/ansorgit/plugins/bash/lang/psi/impl/BashKeywordDefaultImpl", "processDeclarations" }));  return BashResolveUtil.processContainerDeclarations(this, processor, state, lastParent, place);
  }


  
  public PsiReference getReference() {
    return BashPsiUtils.selfReference((PsiElement)this);
  }

  
  public ItemPresentation getPresentation() {
    return new KeywordPresentation(keywordElement());
  }

  
  public boolean canNavigate() {
    return false;
  }
  
  private class KeywordPresentation implements ItemPresentation {
    private final PsiElement element;
    
    KeywordPresentation(PsiElement element) {
      this.element = element;
    }
    
    public String getPresentableText() {
      return (this.element != null) ? this.element.getText() : null;
    }
    
    public String getLocationString() {
      return null;
    }
    
    public Icon getIcon(boolean open) {
      return null;
    }
  }
}
