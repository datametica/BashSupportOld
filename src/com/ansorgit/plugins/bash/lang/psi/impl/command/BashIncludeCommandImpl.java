package com.ansorgit.plugins.bash.lang.psi.impl.command;

import com.ansorgit.plugins.bash.jetbrains.PsiScopesUtil;
import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.BashFile;
import com.ansorgit.plugins.bash.lang.psi.api.BashFileReference;
import com.ansorgit.plugins.bash.lang.psi.api.command.BashIncludeCommand;
import com.ansorgit.plugins.bash.lang.psi.stubs.api.BashIncludeCommandStub;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import com.intellij.psi.ResolveState;
import com.intellij.psi.StubBasedPsiElement;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.stubs.IStubElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;














public class BashIncludeCommandImpl
  extends AbstractBashCommand<BashIncludeCommandStub>
  implements BashIncludeCommand, StubBasedPsiElement<BashIncludeCommandStub>
{
  public BashIncludeCommandImpl(ASTNode astNode) {
    super(astNode, "Bash include command");
  }
  
  public BashIncludeCommandImpl(@NotNull BashIncludeCommandStub stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType, (String)null);
  }
  
  @Nullable
  public BashFileReference getFileReference() {
    return (BashFileReference)findChildByClass(BashFileReference.class);
  }

  
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "visitor", "com/ansorgit/plugins/bash/lang/psi/impl/command/BashIncludeCommandImpl", "accept" }));  if (visitor instanceof BashVisitor) {
      ((BashVisitor)visitor).visitIncludeCommand(this);
    } else {
      visitor.visitElement((PsiElement)this);
    } 
  }

  
  public boolean isIncludeCommand() {
    return true;
  }

  
  public boolean isFunctionCall() {
    return false;
  }

  
  public boolean isInternalCommand() {
    return true;
  }

  
  public boolean isExternalCommand() {
    return false;
  }

  
  public boolean isPureAssignment() {
    return false;
  }

  
  public boolean isVarDefCommand() {
    return false;
  }

  
  public boolean isBashScriptCall() {
    return false;
  }
  
  public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement place) {
    ListMultimap listMultimap;
    if (processor == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "processor", "com/ansorgit/plugins/bash/lang/psi/impl/command/BashIncludeCommandImpl", "processDeclarations" }));  if (state == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "state", "com/ansorgit/plugins/bash/lang/psi/impl/command/BashIncludeCommandImpl", "processDeclarations" }));  if (place == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "place", "com/ansorgit/plugins/bash/lang/psi/impl/command/BashIncludeCommandImpl", "processDeclarations" }));  boolean result = PsiScopesUtil.walkChildrenScopes((PsiElement)this, processor, state, lastParent, place);
    if (!result)
    {
      return false;
    }

    
    BashFile bashFile = getContainingFile();
    PsiFile includedFile = BashPsiUtils.findIncludedFile(this);
    
    Multimap<VirtualFile, PsiElement> visitedFiles = (Multimap<VirtualFile, PsiElement>)state.get(visitedIncludeFiles);
    if (visitedFiles == null) {
      listMultimap = Multimaps.newListMultimap(Maps.newHashMap(), Lists::newLinkedList);
    }
    
    listMultimap.put(bashFile.getVirtualFile(), null);
    
    if (includedFile != null && !listMultimap.containsKey(includedFile.getVirtualFile())) {
      
      listMultimap.put(includedFile.getVirtualFile(), this);
      
      state = state.put(visitedIncludeFiles, listMultimap);
      
      return includedFile.processDeclarations(processor, state, null, place);
    } 
    
    return true;
  }
}
