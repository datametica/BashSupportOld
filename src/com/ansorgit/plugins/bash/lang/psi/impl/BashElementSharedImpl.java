package com.ansorgit.plugins.bash.lang.psi.impl;

import com.ansorgit.plugins.bash.jetbrains.PsiScopesUtil;
import com.ansorgit.plugins.bash.lang.psi.FileInclusionManager;
import com.ansorgit.plugins.bash.lang.psi.api.BashFile;
import com.ansorgit.plugins.bash.lang.psi.api.BashPsiElement;
import com.ansorgit.plugins.bash.lang.psi.api.command.BashCommand;
import com.ansorgit.plugins.bash.lang.psi.stubs.index.BashCommandNameIndex;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.stubs.StubIndex;
import java.util.Collection;
import java.util.Set;
import org.jetbrains.annotations.NotNull;



















public final class BashElementSharedImpl
{
  public static GlobalSearchScope getElementGlobalSearchScope(BashPsiElement element, Project project) {
    PsiFile psiFile = BashPsiUtils.findFileContext((PsiElement)element);
    GlobalSearchScope currentFileScope = GlobalSearchScope.fileScope(psiFile);
    
    Set<PsiFile> includedFiles = FileInclusionManager.findIncludedFiles(psiFile, true, true);
    Collection<VirtualFile> files = Collections2.transform(includedFiles, psiToVirtualFile());
    
    return currentFileScope.uniteWith(GlobalSearchScope.filesScope(project, files));
  }



  
  public static SearchScope getElementUseScope(BashPsiElement element, Project project) {
    PsiFile currentFile = BashPsiUtils.findFileContext((PsiElement)element);
    if (currentFile == null)
    {
      return (SearchScope)GlobalSearchScope.projectScope(project);
    }
    
    Set<BashFile> includers = FileInclusionManager.findIncluders(project, currentFile);
    Set<PsiFile> included = FileInclusionManager.findIncludedFiles(currentFile, true, true);

    
    Set<PsiFile> referencingScriptFiles = Sets.newLinkedHashSet();
    if (element instanceof BashFile) {
      String searchedName = ((BashFile)element).getName();
      if (searchedName != null) {
        Collection<BashCommand> commands = StubIndex.getElements(BashCommandNameIndex.KEY, searchedName, project, 


            
            GlobalSearchScope.projectScope(project), BashCommand.class);
        
        if (commands != null) {
          for (BashCommand command : commands) {
            referencingScriptFiles.add(BashPsiUtils.findFileContext((PsiElement)command));
          }
        }
      } 
    } 
    
    if (includers.isEmpty() && included.isEmpty() && referencingScriptFiles.isEmpty())
    {
      
      return (SearchScope)GlobalSearchScope.fileScope(currentFile);
    }

    
    Set<PsiFile> union = Sets.newLinkedHashSet();
    union.addAll(included);
    union.addAll(includers);
    union.addAll(referencingScriptFiles);
    
    Collection<VirtualFile> virtualFiles = Collections2.transform(union, psiToVirtualFile());
    return (SearchScope)GlobalSearchScope.fileScope(currentFile).union((SearchScope)GlobalSearchScope.filesScope(project, virtualFiles));
  }
  
  public static boolean walkDefinitionScope(PsiElement thisElement, @NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement place) {
    if (processor == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "processor", "com/ansorgit/plugins/bash/lang/psi/impl/BashElementSharedImpl", "walkDefinitionScope" }));  if (state == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "state", "com/ansorgit/plugins/bash/lang/psi/impl/BashElementSharedImpl", "walkDefinitionScope" }));  if (place == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "place", "com/ansorgit/plugins/bash/lang/psi/impl/BashElementSharedImpl", "walkDefinitionScope" }));  return PsiScopesUtil.walkChildrenScopes(thisElement, processor, state, lastParent, place);
  }
  
  private static Function<? super PsiFile, VirtualFile> psiToVirtualFile() {
    return new Function<PsiFile, VirtualFile>() {
        public VirtualFile apply(PsiFile psiFile) {
          return psiFile.getVirtualFile();
        }
      };
  }
}
