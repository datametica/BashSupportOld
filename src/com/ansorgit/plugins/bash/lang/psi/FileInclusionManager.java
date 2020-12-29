package com.ansorgit.plugins.bash.lang.psi;

import com.ansorgit.plugins.bash.lang.psi.api.BashFile;
import com.ansorgit.plugins.bash.lang.psi.api.BashFileReference;
import com.ansorgit.plugins.bash.lang.psi.api.command.BashIncludeCommand;
import com.ansorgit.plugins.bash.lang.psi.stubs.index.BashIncludeCommandIndex;
import com.ansorgit.plugins.bash.lang.psi.stubs.index.BashIncludedFilenamesIndex;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import com.ansorgit.plugins.bash.lang.psi.util.BashSearchScopes;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StubIndex;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

















public class FileInclusionManager
{
  @NotNull
  public static Set<PsiFile> findIncludedFiles(@NotNull PsiFile sourceFile, boolean diveDeep, boolean bashOnly) {
    if (sourceFile == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "sourceFile", "com/ansorgit/plugins/bash/lang/psi/FileInclusionManager", "findIncludedFiles" }));  if (!(sourceFile instanceof BashFile)) {
      if (Collections.emptySet() == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/FileInclusionManager", "findIncludedFiles" }));  return (Set)Collections.emptySet();
    } 
    
    if (!sourceFile.isPhysical()) {
      if (Collections.emptySet() == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/FileInclusionManager", "findIncludedFiles" }));  return (Set)Collections.emptySet();
    } 
    
    if (DumbService.isDumb(sourceFile.getProject())) {
      if (Collections.emptySet() == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/FileInclusionManager", "findIncludedFiles" }));  return (Set)Collections.emptySet();
    } 
    
    Project project = sourceFile.getProject();
    
    Set<PsiFile> includersTodo = Sets.newLinkedHashSet(Collections.singletonList(sourceFile));
    Set<PsiFile> includersDone = Sets.newLinkedHashSet();
    
    Set<PsiFile> allIncludedFiles = Sets.newHashSet();
    
    while (!includersTodo.isEmpty()) {
      Iterator<PsiFile> iterator = includersTodo.iterator();
      
      PsiFile file = iterator.next();
      iterator.remove();
      
      includersDone.add(file);
      
      VirtualFile virtualFile = file.getVirtualFile();
      if (virtualFile == null) {
        continue;
      }
      
      String filePath = virtualFile.getPath();
      
      Collection<BashIncludeCommand> commands = StubIndex.getElements(BashIncludeCommandIndex.KEY, filePath, project, BashSearchScopes.bashOnly(BashSearchScopes.moduleScope(file)), BashIncludeCommand.class);
      if (commands.isEmpty()) {
        continue;
      }
      
      for (BashIncludeCommand command : commands) {
        BashFileReference fileReference = command.getFileReference();
        if (fileReference != null && fileReference.isStatic()) {
          PsiFile referencedFile = fileReference.findReferencedFile();
          if (bashOnly && !(referencedFile instanceof BashFile)) {
            continue;
          }
          
          if (referencedFile != null) {
            allIncludedFiles.add(referencedFile);
            
            if (!includersDone.contains(referencedFile))
            {
              includersTodo.add(referencedFile);
            }
          } 
        } 
      } 
      
      if (!diveDeep) {
        break;
      }
    } 

    
    if (allIncludedFiles == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/FileInclusionManager", "findIncludedFiles" }));  return allIncludedFiles;
  }








  
  @NotNull
  public static Set<BashFile> findIncluders(@NotNull Project project, @NotNull PsiFile file) {
    if (project == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "project", "com/ansorgit/plugins/bash/lang/psi/FileInclusionManager", "findIncluders" }));  if (file == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "file", "com/ansorgit/plugins/bash/lang/psi/FileInclusionManager", "findIncluders" }));  if (DumbService.isDumb(project)) {
      if (Collections.emptySet() == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/FileInclusionManager", "findIncluders" }));  return (Set)Collections.emptySet();
    } 
    
    GlobalSearchScope searchScope = BashSearchScopes.moduleScope(file);
    
    String filename = file.getName();
    if (StringUtils.isEmpty(filename)) {
      if (Collections.emptySet() == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/FileInclusionManager", "findIncluders" }));  return (Set)Collections.emptySet();
    } 
    
    Collection<BashIncludeCommand> includeCommands = StubIndex.getElements(BashIncludedFilenamesIndex.KEY, filename, project, BashSearchScopes.bashOnly(searchScope), BashIncludeCommand.class);
    if (includeCommands == null || includeCommands.isEmpty()) {
      if (Collections.emptySet() == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/FileInclusionManager", "findIncluders" }));  return (Set)Collections.emptySet();
    } 
    
    Set<BashFile> includers = Sets.newLinkedHashSet();
    for (BashIncludeCommand command : includeCommands) {
      BashFile includer = (BashFile)BashPsiUtils.findFileContext((PsiElement)command);
      
      if (!file.equals(includer)) {
        includers.add(includer);
      }
    } 
    
    if (includers == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/FileInclusionManager", "findIncluders" }));  return includers;
  }
  
  public static GlobalSearchScope includedFilesUnionScope(PsiFile source) {
    List<VirtualFile> scopes = Lists.newLinkedList();
    scopes.add(source.getVirtualFile());
    
    Set<PsiFile> includedFiles = findIncludedFiles(source, true, true);
    for (PsiFile file : includedFiles) {
      scopes.add(file.getVirtualFile());
    }
    
    return GlobalSearchScope.filesScope(source.getProject(), scopes);
  }
}
