package com.ansorgit.plugins.bash.lang.psi.util;

import com.ansorgit.plugins.bash.file.BashFileType;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.GlobalSearchScope;




















public final class BashSearchScopes
{
  public static GlobalSearchScope moduleScope(PsiFile file) {
    VirtualFile virtualFile = file.getVirtualFile();
    if (virtualFile == null) {
      return GlobalSearchScope.EMPTY_SCOPE;
    }
    
    Module module = ProjectRootManager.getInstance(file.getProject()).getFileIndex().getModuleForFile(virtualFile);
    if (module == null) {
      return GlobalSearchScope.fileScope(file);
    }


    
    return module.getModuleContentScope();
  }
  
  public static GlobalSearchScope bashOnly(GlobalSearchScope scope) {
    return GlobalSearchScope.getScopeRestrictedByFileTypes(scope, new FileType[] { (FileType)BashFileType.BASH_FILE_TYPE });
  }
}
