package com.ansorgit.plugins.bash.documentation;

import com.ansorgit.plugins.bash.lang.psi.api.command.BashCommand;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import com.intellij.psi.PsiElement;




















class InternalCommandDocumentation
  extends ClasspathDocSource
{
  InternalCommandDocumentation() {
    super("/documentation/internal");
  }
  
  boolean isValid(PsiElement element, PsiElement originalElement) {
    if (element instanceof com.ansorgit.plugins.bash.lang.psi.api.command.BashGenericCommand) {
      element = BashPsiUtils.findParent(element, BashCommand.class);
    }
    
    return (element instanceof BashCommand && ((BashCommand)element).isInternalCommand());
  }

  
  String resourceNameForElement(PsiElement element) {
    if (element instanceof com.ansorgit.plugins.bash.lang.psi.api.command.BashGenericCommand) {
      element = BashPsiUtils.findParent(element, BashCommand.class);
    }
    
    return (element instanceof BashCommand) ? ((BashCommand)element).getReferencedCommandName() : null;
  }
  
  public String documentationUrl(PsiElement element, PsiElement originalElement) {
    if (!isValid(element, originalElement)) {
      return null;
    }
    
    return urlForCommand(resourceNameForElement(element));
  }
  
  String urlForCommand(String commandName) {
    return String.format("https://ss64.com/bash/%s.html", new Object[] { commandName });
  }
}
