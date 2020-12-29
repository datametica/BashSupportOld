package com.ansorgit.plugins.bash.documentation;

import com.ansorgit.plugins.bash.lang.psi.api.command.BashCommand;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import com.intellij.psi.PsiElement;






















class ManpageDocSource
  extends ClasspathDocSource
{
  ManpageDocSource() {
    super("/documentation/external");
  }

  
  String resourceNameForElement(PsiElement element) {
    return commandElement(element).getReferencedCommandName();
  }
  
  boolean isValid(PsiElement element, PsiElement originalElement) {
    BashCommand command = commandElement(element);
    
    return (command != null && command.isExternalCommand());
  }
  
  public String documentationUrl(PsiElement element, PsiElement originalElement) {
    if (!isValid(element, originalElement)) {
      return null;
    }
    
    return String.format("http://man.he.net/man1/%s", new Object[] { commandElement(element).getReferencedCommandName() });
  }
  
  private BashCommand commandElement(PsiElement element) {
    if (element instanceof com.ansorgit.plugins.bash.lang.psi.api.command.BashGenericCommand) {
      return (BashCommand)BashPsiUtils.findParent(element, BashCommand.class);
    }
    
    if (element instanceof BashCommand) {
      return (BashCommand)element;
    }
    
    return null;
  }
}
