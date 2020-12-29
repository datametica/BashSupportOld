package com.ansorgit.plugins.bash.lang.psi.util;

import com.ansorgit.plugins.bash.lang.psi.api.BashFileReference;
import com.ansorgit.plugins.bash.lang.psi.api.BashPsiElement;
import com.ansorgit.plugins.bash.lang.psi.api.BashReference;
import com.ansorgit.plugins.bash.lang.psi.api.ResolveProcessor;
import com.ansorgit.plugins.bash.lang.psi.api.command.BashIncludeCommand;
import com.ansorgit.plugins.bash.lang.psi.api.function.BashFunctionDef;
import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVar;
import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVarDef;
import com.ansorgit.plugins.bash.lang.psi.impl.Keys;
import com.ansorgit.plugins.bash.lang.psi.impl.vars.BashVarProcessor;
import com.ansorgit.plugins.bash.lang.psi.stubs.index.BashIncludeCommandIndex;
import com.ansorgit.plugins.bash.lang.psi.stubs.index.BashVarDefIndex;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.intellij.ide.scratch.ScratchFileService;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.FileIndexFacade;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.psi.util.PsiTreeUtil;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


















public final class BashResolveUtil
{
  public static GlobalSearchScope varDefSearchScope(BashVar reference, boolean withIncludedFiles) {
    PsiFile referenceFile = BashPsiUtils.findFileContext((PsiElement)reference);
    if (!withIncludedFiles) {
      return GlobalSearchScope.fileScope(referenceFile.getProject(), referenceFile.getVirtualFile());
    }
    
    Set<VirtualFile> result = Sets.newLinkedHashSet();
    result.add(referenceFile.getVirtualFile());
    
    int referenceFileOffset = BashPsiUtils.getFileTextOffset((PsiElement)reference);
    BashFunctionDef referenceFunctionContainer = BashPsiUtils.findNextVarDefFunctionDefScope((PsiElement)reference);
    
    for (BashIncludeCommand command : BashPsiUtils.findIncludeCommands(referenceFile, null)) {
      boolean includeIsInFunction = (BashPsiUtils.findNextVarDefFunctionDefScope((PsiElement)command) != null);

      
      if (referenceFunctionContainer != null || includeIsInFunction || referenceFileOffset > BashPsiUtils.getFileTextEndOffset((PsiElement)command)) {
        BashFileReference fileReference = command.getFileReference();
        PsiFile includedFile = (fileReference != null) ? fileReference.findReferencedFile() : null;
        if (includedFile != null) {
          result.add(includedFile.getVirtualFile());

          
          for (PsiFile file : BashPsiUtils.findIncludedFiles(includedFile, true)) {
            result.add(file.getVirtualFile());
          }
        } 
      } 
    } 
    
    return GlobalSearchScope.filesScope(referenceFile.getProject(), result);
  }








  
  public static void walkVariableDefinitions(@NotNull BashVar reference, @NotNull Function<BashVarDef, Boolean> resultProcessor) {
    if (reference == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "reference", "com/ansorgit/plugins/bash/lang/psi/util/BashResolveUtil", "walkVariableDefinitions" }));  if (resultProcessor == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "resultProcessor", "com/ansorgit/plugins/bash/lang/psi/util/BashResolveUtil", "walkVariableDefinitions" }));  String varName = reference.getName();
    if (StringUtils.isBlank(varName)) {
      return;
    }
    
    BashVarProcessor processor = new BashVarProcessor(reference, varName, true, false, true);
    resolve(reference, false, (ResolveProcessor)processor);
    
    Collection<PsiElement> results = processor.getResults();
    if (results != null) {
      for (PsiElement result : results) {
        if (result instanceof BashVarDef) {
          resultProcessor.apply((BashVarDef)result);
        }
      } 
    }
  }
  
  public static PsiElement resolve(BashVar bashVar, boolean dumbMode, boolean preferNeighborhood) {
    if (bashVar == null || !bashVar.isPhysical()) {
      return null;
    }
    
    String varName = bashVar.getReferenceName();
    if (varName == null) {
      return null;
    }
    
    return resolve(bashVar, dumbMode, (ResolveProcessor)new BashVarProcessor(bashVar, varName, true, preferNeighborhood, false));
  }
  public static PsiElement resolve(BashVar bashVar, boolean dumbMode, ResolveProcessor processor) {
    Collection<BashVarDef> varDefs;
    if (bashVar == null || !bashVar.isPhysical()) {
      return null;
    }
    
    String varName = bashVar.getReferenceName();
    if (varName == null) {
      return null;
    }
    
    PsiFile psiFile = BashPsiUtils.findFileContext((PsiElement)bashVar);
    VirtualFile virtualFile = psiFile.getVirtualFile();
    
    String filePath = (virtualFile != null) ? virtualFile.getPath() : null;
    Project project = bashVar.getProject();
    
    ResolveState resolveState = ResolveState.initial();
    
    GlobalSearchScope fileScope = GlobalSearchScope.fileScope(psiFile);

    
    if (dumbMode || isScratchFile(virtualFile) || isNotIndexedFile(project, virtualFile)) {
      varDefs = PsiTreeUtil.collectElementsOfType((PsiElement)psiFile, new Class[] { BashVarDef.class });
    } else {
      varDefs = StubIndex.getElements(BashVarDefIndex.KEY, varName, project, fileScope, BashVarDef.class);
    } 
    
    for (BashVarDef varDef : varDefs) {
      ProgressManager.checkCanceled();
      
      processor.execute((PsiElement)varDef, resolveState);
    } 
    
    if (!dumbMode && filePath != null) {
      Collection<BashIncludeCommand> includeCommands = StubIndex.getElements(BashIncludeCommandIndex.KEY, filePath, project, fileScope, BashIncludeCommand.class);
      if (!includeCommands.isEmpty()) {
        boolean varIsInFunction = (BashPsiUtils.findNextVarDefFunctionDefScope((PsiElement)bashVar) != null);
        
        for (BashIncludeCommand command : includeCommands) {
          ProgressManager.checkCanceled();
          
          boolean includeIsInFunction = (BashPsiUtils.findNextVarDefFunctionDefScope((PsiElement)command) != null);

          
          if (varIsInFunction || includeIsInFunction || BashPsiUtils.getFileTextOffset((PsiElement)bashVar) > BashPsiUtils.getFileTextEndOffset((PsiElement)command)) {
            try {
              resolveState = resolveState.put(Keys.resolvingIncludeCommand, command);
              
              command.processDeclarations((PsiScopeProcessor)processor, resolveState, (PsiElement)command, (PsiElement)bashVar);
            } finally {
              resolveState = resolveState.put(Keys.resolvingIncludeCommand, null);
            } 
          }
        } 
      } 
    } 
    
    processor.prepareResults();
    
    return processor.getBestResult(false, (PsiElement)bashVar);
  }
  
  public static boolean isNotIndexedFile(@NonNls Project project, @Nullable VirtualFile virtualFile) {
    return (virtualFile == null || virtualFile instanceof com.intellij.injected.editor.VirtualFileWindow || 
      
      !FileIndexFacade.getInstance(project).isInContent(virtualFile));
  }
  
  public static boolean processContainerDeclarations(BashPsiElement thisElement, @NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement place) {
    if (processor == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "processor", "com/ansorgit/plugins/bash/lang/psi/util/BashResolveUtil", "processContainerDeclarations" }));  if (state == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "state", "com/ansorgit/plugins/bash/lang/psi/util/BashResolveUtil", "processContainerDeclarations" }));  if (place == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "place", "com/ansorgit/plugins/bash/lang/psi/util/BashResolveUtil", "processContainerDeclarations" }));  if (thisElement == lastParent) {
      return true;
    }
    
    if (!processor.execute((PsiElement)thisElement, state)) {
      return false;
    }

    
    List<PsiElement> functions = Lists.newLinkedList();

    
    for (PsiElement child = thisElement.getFirstChild(); child != null && child != lastParent; child = child.getNextSibling()) {
      if (child instanceof BashFunctionDef) {
        functions.add(child);
      } else if (!child.processDeclarations(processor, state, lastParent, place)) {
        return false;
      } 
    } 
    
    for (PsiElement function : functions) {
      if (!function.processDeclarations(processor, state, lastParent, place)) {
        return false;
      }
    } 

    
    if (lastParent != null && lastParent.getParent().isEquivalentTo((PsiElement)thisElement) && BashPsiUtils.findNextVarDefFunctionDefScope(place) != null) {
      for (PsiElement sibling = lastParent.getNextSibling(); sibling != null; sibling = sibling.getNextSibling()) {
        if (!sibling.processDeclarations(processor, state, null, place)) {
          return false;
        }
      } 
    }
    
    return true;
  }
  
  public static boolean isScratchFile(@Nullable PsiFile file) {
    return (file != null && isScratchFile(file.getVirtualFile()));
  }
  
  public static boolean isScratchFile(@Nullable VirtualFile file) {
    return (file != null && ScratchFileService.getInstance().getRootType(file) != null);
  }



  
  public static boolean hasStaticVarDefPath(BashVar bashVar) {
    BashReference reference = bashVar.getNeighborhoodReference();
    if (reference == null) {
      return false;
    }
    
    PsiElement closestDef = reference.resolve();
    if (closestDef == null) {
      return false;
    }


    
    BashFunctionDef varScope = BashPsiUtils.findNextVarDefFunctionDefScope((PsiElement)bashVar);
    BashFunctionDef defScope = BashPsiUtils.findNextVarDefFunctionDefScope(closestDef);
    if (varScope == null && defScope != null) {
      return false;
    }

    
    if (varScope != null && !varScope.isEquivalentTo((PsiElement)defScope)) {
      return false;
    }

    
    PsiFile psiFile = bashVar.getContainingFile();
    if (varScope == null && !psiFile.isEquivalentTo((PsiElement)closestDef.getContainingFile())) {
      return false;
    }
    
    Collection<BashVarDef> allDefs = StubIndex.getElements(BashVarDefIndex.KEY, bashVar.getReferenceName(), bashVar.getProject(), GlobalSearchScope.fileScope(psiFile), BashVarDef.class);
    for (BashVarDef candidateDef : allDefs) {
      ProgressManager.checkCanceled();

      
      BashFunctionDef scope = BashPsiUtils.findNextVarDefFunctionDefScope((PsiElement)candidateDef);
      if (varScope != null && !varScope.isEquivalentTo((PsiElement)scope)) {
        continue;
      }

      
      PsiElement parent = PsiTreeUtil.findFirstParent((PsiElement)candidateDef, psi -> (psi instanceof com.ansorgit.plugins.bash.lang.psi.api.BashConditionalBlock || psi instanceof com.ansorgit.plugins.bash.lang.psi.api.loops.BashLoop));
      if (parent != null && !PsiTreeUtil.isAncestor(parent, (PsiElement)bashVar, true)) {
        return false;
      }
    } 
    
    return true;
  }
}
