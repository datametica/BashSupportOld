package com.ansorgit.plugins.bash.lang.psi.impl;

import com.ansorgit.plugins.bash.file.BashFileType;
import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.BashFile;
import com.ansorgit.plugins.bash.lang.psi.api.BashPsiElement;
import com.ansorgit.plugins.bash.lang.psi.api.BashShebang;
import com.ansorgit.plugins.bash.lang.psi.api.function.BashFunctionDef;
import com.ansorgit.plugins.bash.lang.psi.stubs.api.BashFileStub;
import com.ansorgit.plugins.bash.lang.psi.util.BashResolveUtil;
import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.stubs.StubElement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;














public class BashFileImpl
  extends PsiFileBase
  implements BashFile
{
  private final Object cacheLock = new Object();
  
  private volatile List<BashFunctionDef> cachedFunctions;
  
  public BashFileImpl(FileViewProvider viewProvider) {
    super(viewProvider, BashFileType.BASH_LANGUAGE);
  }

  
  @Nullable
  public BashFileStub getStub() {
    return (BashFileStub)super.getStub();
  }
  
  @NotNull
  public FileType getFileType() {
    if (BashFileType.BASH_FILE_TYPE == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/BashFileImpl", "getFileType" }));  return (FileType)BashFileType.BASH_FILE_TYPE;
  }
  
  public boolean hasShebangLine() {
    return (findShebang() != null);
  }

  
  @Nullable
  public BashShebang findShebang() {
    return (BashShebang)findChildByClass(BashShebang.class);
  }

  
  public List<BashFunctionDef> allFunctionDefinitions() {
    if (this.cachedFunctions == null) {
      synchronized (this.cacheLock) {
        if (this.cachedFunctions == null) {
          List<BashFunctionDef> functions = new ArrayList<>();
          collectNestedFunctionDefinitions((PsiElement)this, functions);
          functions.sort(Comparator.comparingInt(PsiElement::getTextOffset));
          
          this.cachedFunctions = functions;
        } 
      } 
    }
    
    return this.cachedFunctions;
  }

  
  public void clearCaches() {
    synchronized (this.cacheLock) {
      this.cachedFunctions = null;
    } 
    super.clearCaches();
  }

  
  public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement place) {
    if (processor == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "processor", "com/ansorgit/plugins/bash/lang/psi/impl/BashFileImpl", "processDeclarations" }));  if (state == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "state", "com/ansorgit/plugins/bash/lang/psi/impl/BashFileImpl", "processDeclarations" }));  if (place == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "place", "com/ansorgit/plugins/bash/lang/psi/impl/BashFileImpl", "processDeclarations" }));  return BashResolveUtil.processContainerDeclarations((BashPsiElement)this, processor, state, lastParent, place);
  }

  
  @NotNull
  public SearchScope getUseScope() {
    if (BashElementSharedImpl.getElementUseScope((BashPsiElement)this, getProject()) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/BashFileImpl", "getUseScope" }));  return BashElementSharedImpl.getElementUseScope((BashPsiElement)this, getProject());
  }

  
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "visitor", "com/ansorgit/plugins/bash/lang/psi/impl/BashFileImpl", "accept" }));  if (visitor instanceof BashVisitor) {
      ((BashVisitor)visitor).visitFile(this);
    } else {
      visitor.visitFile((PsiFile)this);
    } 
  }
  
  private static void collectNestedFunctionDefinitions(PsiElement parent, List<BashFunctionDef> target) {
    for (PsiElement e = parent.getFirstChild(); e != null; e = e.getNextSibling()) {
      if (e instanceof BashFunctionDef) {
        target.add((BashFunctionDef)e);
      }
      
      collectNestedFunctionDefinitions(e, target);
    } 
  }
}
