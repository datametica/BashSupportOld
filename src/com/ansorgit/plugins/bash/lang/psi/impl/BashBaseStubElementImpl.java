package com.ansorgit.plugins.bash.lang.psi.impl;

import com.ansorgit.plugins.bash.file.BashFileType;
import com.ansorgit.plugins.bash.lang.psi.api.BashPsiElement;
import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.StubBasedPsiElement;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
















public abstract class BashBaseStubElementImpl<T extends StubElement>
  extends StubBasedPsiElementBase<T>
  implements BashPsiElement, StubBasedPsiElement<T>
{
  private final String name;
  
  public BashBaseStubElementImpl(ASTNode astNode) {
    this(astNode, null);
  }
  
  public BashBaseStubElementImpl(ASTNode astNode, @Nullable String name) {
    super(astNode);
    this.name = name;
  }
  
  public BashBaseStubElementImpl(@NotNull T stub, @NotNull IStubElementType nodeType, @Nullable String name) {
    super((StubElement)stub, nodeType);
    this.name = name;
  }

  
  @NotNull
  public Language getLanguage() {
    if (BashFileType.BASH_LANGUAGE == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/BashBaseStubElementImpl", "getLanguage" }));  return BashFileType.BASH_LANGUAGE;
  }

  
  public String toString() {
    return "[PSI] " + ((this.name == null) ? "<undefined>" : this.name);
  }

  
  @NotNull
  public SearchScope getUseScope() {
    if (BashElementSharedImpl.getElementUseScope(this, getProject()) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/BashBaseStubElementImpl", "getUseScope" }));  return BashElementSharedImpl.getElementUseScope(this, getProject());
  }

  
  @NotNull
  public GlobalSearchScope getResolveScope() {
    if (BashElementSharedImpl.getElementGlobalSearchScope(this, getProject()) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/BashBaseStubElementImpl", "getResolveScope" }));  return BashElementSharedImpl.getElementGlobalSearchScope(this, getProject());
  }

  
  public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement place) {
    if (processor == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "processor", "com/ansorgit/plugins/bash/lang/psi/impl/BashBaseStubElementImpl", "processDeclarations" }));  if (state == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "state", "com/ansorgit/plugins/bash/lang/psi/impl/BashBaseStubElementImpl", "processDeclarations" }));  if (place == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "place", "com/ansorgit/plugins/bash/lang/psi/impl/BashBaseStubElementImpl", "processDeclarations" }));  if (!processor.execute((PsiElement)this, state)) {
      return false;
    }
    
    return BashElementSharedImpl.walkDefinitionScope((PsiElement)this, processor, state, lastParent, place);
  }
}
