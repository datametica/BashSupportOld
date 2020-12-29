package com.ansorgit.plugins.bash.lang.psi.impl.command;

import com.ansorgit.plugins.bash.lang.psi.api.BashFile;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiFileUtils;
import com.ansorgit.plugins.bash.lang.psi.util.BashSearchScopes;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileSystemItem;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.PathUtil;
import org.jetbrains.annotations.Nullable;




















class SmartBashFileReference
  extends AbstractBashFileReference
{
  public SmartBashFileReference(AbstractBashCommand<?> cmd) {
    super(cmd);
  }

  
  @Nullable
  public PsiElement resolveInner() {
    String referencedName = this.cmd.getReferencedCommandName();
    if (referencedName == null) {
      return null;
    }
    
    String fileName = PathUtil.getFileName(referencedName);
    GlobalSearchScope scope = BashSearchScopes.moduleScope((PsiFile)this.cmd.getContainingFile());
    
    PsiFileSystemItem[] files = FilenameIndex.getFilesByName(this.cmd.getProject(), fileName, scope, false);
    if (files.length == 0) {
      return null;
    }
    
    BashFile bashFile = this.cmd.getContainingFile();
    return (PsiElement)BashPsiFileUtils.findRelativeFile((PsiFile)bashFile, referencedName);
  }
}
