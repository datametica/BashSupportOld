package com.ansorgit.plugins.bash.editor.inspections.quickfix;

import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.codeInspection.CommonProblemDescriptor;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;




















abstract class AbstractBashQuickfix
  implements LocalQuickFix, IntentionAction
{
  @NotNull
  public final String getText() {
    if (getName() == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/inspections/quickfix/AbstractBashQuickfix", "getText" }));  return getName();
  }
  
  @NotNull
  public String getFamilyName() {
    if ("Bash" == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/inspections/quickfix/AbstractBashQuickfix", "getFamilyName" }));  return "Bash";
  }
  
  public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
    if (project == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "project", "com/ansorgit/plugins/bash/editor/inspections/quickfix/AbstractBashQuickfix", "isAvailable" }));  return (file instanceof com.ansorgit.plugins.bash.lang.psi.api.BashFile && file.isValid() && file.isWritable());
  }
  
  public boolean startInWriteAction() {
    return true;
  }
  
  public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
    if (project == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "project", "com/ansorgit/plugins/bash/editor/inspections/quickfix/AbstractBashQuickfix", "applyFix" }));  if (descriptor == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "descriptor", "com/ansorgit/plugins/bash/editor/inspections/quickfix/AbstractBashQuickfix", "applyFix" }));  invoke(project, null, BashPsiUtils.findFileContext(descriptor.getPsiElement()));
  }
}
