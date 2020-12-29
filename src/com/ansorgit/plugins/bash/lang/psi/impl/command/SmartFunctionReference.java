package com.ansorgit.plugins.bash.lang.psi.impl.command;

import com.ansorgit.plugins.bash.lang.psi.FileInclusionManager;
import com.ansorgit.plugins.bash.lang.psi.api.BashFile;
import com.ansorgit.plugins.bash.lang.psi.api.function.BashFunctionDef;
import com.ansorgit.plugins.bash.lang.psi.stubs.index.BashFunctionNameIndex;
import com.google.common.collect.Lists;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.ResolveState;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StubIndex;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.jetbrains.annotations.Nullable;























class SmartFunctionReference
  extends AbstractFunctionReference
{
  public SmartFunctionReference(AbstractBashCommand<?> cmd) {
    super(cmd);
  }

  
  @Nullable
  public PsiElement resolveInner() {
    String referencedName = this.cmd.getReferencedCommandName();
    if (referencedName == null) {
      return null;
    }
    
    BashFunctionProcessor bashFunctionProcessor = new BashFunctionProcessor(referencedName);
    
    Project project = this.cmd.getProject();
    BashFile bashFile = this.cmd.getContainingFile();
    
    GlobalSearchScope allFiles = FileInclusionManager.includedFilesUnionScope((PsiFile)bashFile);
    Collection<BashFunctionDef> functionDefs = StubIndex.getElements(BashFunctionNameIndex.KEY, referencedName, project, allFiles, BashFunctionDef.class);
    
    ResolveState initial = ResolveState.initial();
    for (BashFunctionDef functionDef : functionDefs) {
      bashFunctionProcessor.execute((PsiElement)functionDef, initial);
    }

    
    if (!bashFunctionProcessor.hasResults()) {
      Set<BashFile> includingFiles = FileInclusionManager.findIncluders(project, (PsiFile)bashFile);
      
      List<GlobalSearchScope> scopes = Lists.newLinkedList();
      for (BashFile file : includingFiles) {
        scopes.add(GlobalSearchScope.fileScope((PsiFile)file));
      }
      
      if (!scopes.isEmpty()) {
        GlobalSearchScope scope = GlobalSearchScope.union(scopes.<GlobalSearchScope>toArray(new GlobalSearchScope[scopes.size()]));
        
        functionDefs = StubIndex.getElements(BashFunctionNameIndex.KEY, referencedName, project, scope, BashFunctionDef.class);
        
        for (BashFunctionDef def : functionDefs) {
          bashFunctionProcessor.execute((PsiElement)def, initial);
        }
      } 
    } 
    
    bashFunctionProcessor.prepareResults();
    
    return bashFunctionProcessor.hasResults() ? bashFunctionProcessor.getBestResult(true, (PsiElement)this.cmd) : null;
  }
}
