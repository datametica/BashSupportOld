package com.ansorgit.plugins.bash.lang.psi.impl.heredoc;

import com.ansorgit.plugins.bash.lang.psi.api.heredoc.BashHereDocMarker;
import com.ansorgit.plugins.bash.lang.psi.impl.BashBaseElement;
import com.ansorgit.plugins.bash.lang.util.HeredocSharedImpl;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;


















abstract class AbstractHeredocMarker
  extends BashBaseElement
  implements BashHereDocMarker
{
  private HeredocMarkerReference reference;
  
  AbstractHeredocMarker(ASTNode astNode, String name) {
    super(astNode, name);
  }

  
  public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement place) {
    if (processor == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "processor", "com/ansorgit/plugins/bash/lang/psi/impl/heredoc/AbstractHeredocMarker", "processDeclarations" }));  if (state == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "state", "com/ansorgit/plugins/bash/lang/psi/impl/heredoc/AbstractHeredocMarker", "processDeclarations" }));  if (place == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "place", "com/ansorgit/plugins/bash/lang/psi/impl/heredoc/AbstractHeredocMarker", "processDeclarations" }));  return processor.execute((PsiElement)this, state);
  }

  
  public String getName() {
    return getMarkerText();
  }

  
  public String getMarkerText() {
    return HeredocSharedImpl.cleanMarker(getText(), isIgnoringTabs());
  }

  
  public PsiElement setName(@NotNull String newElementName) throws IncorrectOperationException {
    if (newElementName == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "newElementName", "com/ansorgit/plugins/bash/lang/psi/impl/heredoc/AbstractHeredocMarker", "setName" }));  PsiReference reference = getReference();
    return (reference != null) ? reference.handleElementRename(newElementName) : null;
  }

  
  public PsiReference getReference() {
    if (this.reference == null) {
      this.reference = createReference();
    }
    
    return (PsiReference)this.reference;
  }
  
  public abstract HeredocMarkerReference createReference();
  
  @NotNull
  public String getCanonicalText() {
    if (getText() == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/heredoc/AbstractHeredocMarker", "getCanonicalText" }));  return getText();
  }
}
