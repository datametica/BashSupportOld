package com.ansorgit.plugins.bash.editor.inspections.quickfix;

import com.intellij.codeInspection.LocalQuickFixAndIntentionActionOnPsiElement;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;


















abstract class AbstractBashPsiElementQuickfix
  extends LocalQuickFixAndIntentionActionOnPsiElement
{
  protected AbstractBashPsiElementQuickfix(PsiElement element) {
    super(element);
  }
  
  @NotNull
  public String getFamilyName() {
    if ("Bash" == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/inspections/quickfix/AbstractBashPsiElementQuickfix", "getFamilyName" }));  return "Bash";
  }
}
