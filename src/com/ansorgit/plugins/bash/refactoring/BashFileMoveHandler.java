package com.ansorgit.plugins.bash.refactoring;

import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.BashFileReference;
import com.ansorgit.plugins.bash.lang.psi.api.command.BashCommand;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import com.google.common.collect.Maps;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.UserDataHolder;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileSystemItem;
import com.intellij.psi.PsiReference;
import com.intellij.refactoring.move.moveFilesOrDirectories.MoveFileHandler;
import com.intellij.usageView.UsageInfo;
import com.intellij.util.IncorrectOperationException;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.Nullable;

















public class BashFileMoveHandler
  extends MoveFileHandler
{
  private static final Key<Map<PsiReference, PsiFileSystemItem>> REPLACEMENT_MAP = new Key("move file replacements");

  
  public boolean canProcessElement(PsiFile element) {
    return element instanceof com.ansorgit.plugins.bash.lang.psi.api.BashFile;
  }

  
  public void prepareMovedFile(PsiFile file, PsiDirectory moveDestination, Map<PsiElement, PsiElement> oldToNewMap) {
    FileReferenceCollectionVisitor visitor = new FileReferenceCollectionVisitor();
    
    BashPsiUtils.visitRecursively((PsiElement)file, visitor);
    
    REPLACEMENT_MAP.set((UserDataHolder)file, visitor.getReferenceMap());
  }

  
  @Nullable
  public List<UsageInfo> findUsages(PsiFile psiFile, PsiDirectory newParent, boolean searchInComments, boolean searchInNonJavaFiles) {
    return null;
  }





  
  public void retargetUsages(List<UsageInfo> usageInfos, Map<PsiElement, PsiElement> oldToNewMap) {}




  
  public void updateMovedFile(PsiFile file) throws IncorrectOperationException {
    try {
      Map<PsiReference, PsiFileSystemItem> psiReferences = (Map<PsiReference, PsiFileSystemItem>)REPLACEMENT_MAP.get((UserDataHolder)file);
      
      if (psiReferences != null) {
        for (Map.Entry<PsiReference, PsiFileSystemItem> entry : psiReferences.entrySet()) {
          PsiReference key = entry.getKey();
          if (key instanceof com.intellij.refactoring.rename.BindablePsiReference) {
            key.bindToElement((PsiElement)entry.getValue());
          }
        } 
        
        psiReferences.clear();
      } 
    } finally {
      REPLACEMENT_MAP.set((UserDataHolder)file, null);
    } 
  }

  
  private static class FileReferenceCollectionVisitor
    extends BashVisitor
  {
    private final Map<PsiReference, PsiFileSystemItem> replacementMap = Maps.newLinkedHashMap();

    
    public Map<PsiReference, PsiFileSystemItem> getReferenceMap() {
      return this.replacementMap;
    }

    
    public void visitFileReference(BashFileReference fileReference) {
      handleReference(fileReference.getReference());
    }

    
    public void visitGenericCommand(BashCommand bashCommand) {
      if (bashCommand.isBashScriptCall()) {
        handleReference(bashCommand.getReference());
      }
    }
    
    private void handleReference(@Nullable PsiReference psiReference) {
      if (psiReference != null) {
        PsiElement oldTarget = psiReference.resolve();
        
        if (oldTarget instanceof PsiFileSystemItem)
          this.replacementMap.put(psiReference, (PsiFileSystemItem)oldTarget); 
      } 
    }
  }
}
