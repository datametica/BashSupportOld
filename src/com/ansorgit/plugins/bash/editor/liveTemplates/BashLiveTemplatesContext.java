package com.ansorgit.plugins.bash.editor.liveTemplates;

import com.ansorgit.plugins.bash.file.BashFileType;
import com.ansorgit.plugins.bash.lang.psi.api.BashShebang;
import com.ansorgit.plugins.bash.lang.psi.api.heredoc.BashHereDoc;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import com.intellij.codeInsight.template.TemplateContextType;
import com.intellij.lang.Language;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilCore;
import org.jetbrains.annotations.NotNull;

















public class BashLiveTemplatesContext
  extends TemplateContextType
{
  protected BashLiveTemplatesContext() {
    super("Bash", "Bash");
  }

  
  public boolean isInContext(@NotNull PsiFile file, int offset) {
    if (file == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "file", "com/ansorgit/plugins/bash/editor/liveTemplates/BashLiveTemplatesContext", "isInContext" }));  Language language = PsiUtilCore.getLanguageAtOffset(file, offset);
    if (language.isKindOf(BashFileType.BASH_LANGUAGE)) {
      PsiElement element = file.findElementAt(offset);
      if (element == null)
      {
        
        element = file.findElementAt(offset - 1);
      }
      
      return (!BashPsiUtils.hasParentOfType(element, PsiComment.class, 3) && 
        !BashPsiUtils.hasParentOfType(element, BashShebang.class, 3) && 
        !BashPsiUtils.hasParentOfType(element, BashHereDoc.class, 1) && 
        !BashPsiUtils.isCommandParameterWord(element));
    } 
    
    return false;
  }
}
