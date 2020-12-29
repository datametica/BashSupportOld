package com.ansorgit.plugins.bash.editor.codecompletion;

import com.ansorgit.plugins.bash.lang.psi.api.BashShebang;
import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.patterns.ElementPattern;
import com.intellij.psi.PsiElement;
import java.io.File;
import java.util.function.Predicate;






















class ShebangPathCompletionProvider
  extends AbsolutePathCompletionProvider
{
  void addTo(CompletionContributor contributor) {
    contributor.extend(CompletionType.BASIC, (ElementPattern)(new BashPsiPattern()).inside(BashShebang.class), this);
  }

  
  protected Predicate<File> createFileFilter() {
    return file -> (file.canExecute() && file.canRead());
  }

  
  protected String findOriginalText(PsiElement element) {
    String original = element.getText();
    
    if (element instanceof BashShebang) {
      int offset = ((BashShebang)element).getShellCommandOffset();
      return original.substring(offset);
    } 
    
    return original;
  }

  
  protected String findCurrentText(CompletionParameters parameters, PsiElement element) {
    PsiElement command = element;
    while (command != null && !(command instanceof BashShebang)) {
      command = command.getParent();
    }
    
    if (command != null) {
      BashShebang shebang = (BashShebang)command;
      String shellcommand = shebang.shellCommand(false);
      
      int elementOffset = parameters.getOffset() - shebang.commandRange().getStartOffset();
      return (elementOffset > 0 && elementOffset <= shellcommand.length()) ? shellcommand
        .substring(0, elementOffset) : null;
    } 

    
    return super.findCurrentText(parameters, element);
  }
}
