package com.ansorgit.plugins.bash.lang.psi.impl.command;

import com.ansorgit.plugins.bash.lang.psi.api.BashFile;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiFileUtils;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.Nullable;


















class DumbBashFileReference
  extends AbstractBashFileReference
{
  public DumbBashFileReference(AbstractBashCommand<?> cmd) {
    super(cmd);
  }

  
  @Nullable
  public PsiElement resolveInner() {
    String referencedName = this.cmd.getReferencedCommandName();
    if (referencedName == null) {
      return null;
    }
    
    BashFile bashFile = this.cmd.getContainingFile();
    return (PsiElement)BashPsiFileUtils.findRelativeFile((PsiFile)bashFile, referencedName);
  }
}
