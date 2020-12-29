package com.ansorgit.plugins.bash.editor.inspections.quickfix;

import com.ansorgit.plugins.bash.editor.inspections.inspections.FixShebangInspection;
import com.ansorgit.plugins.bash.lang.psi.api.BashShebang;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.SmartPointerManager;
import com.intellij.psi.SmartPsiElementPointer;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;












public class RegisterShebangCommandQuickfix
  extends AbstractBashQuickfix
{
  private final FixShebangInspection inspection;
  private final SmartPsiElementPointer<BashShebang> shebang;
  
  public RegisterShebangCommandQuickfix(FixShebangInspection fixShebangInspection, BashShebang shebang) {
    this.inspection = fixShebangInspection;
    
    Project project = shebang.getProject();
    this.shebang = SmartPointerManager.getInstance(project).createSmartPsiElementPointer((PsiElement)shebang);
  }

  
  public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
    if (project == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "project", "com/ansorgit/plugins/bash/editor/inspections/quickfix/RegisterShebangCommandQuickfix", "isAvailable" }));  return (this.shebang != null && super.isAvailable(project, editor, file));
  }

  
  public void invoke(@NotNull Project project, Editor editor, PsiFile file) throws IncorrectOperationException {
    if (project == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "project", "com/ansorgit/plugins/bash/editor/inspections/quickfix/RegisterShebangCommandQuickfix", "invoke" }));  BashShebang element = (BashShebang)this.shebang.getElement();
    if (element == null) {
      return;
    }
    
    this.inspection.registerShebangCommand(element.shellCommand(true));

    
    element.updateCommand(element.shellCommand(false), null);
  }

  
  @NotNull
  public String getName() {
    if ("Mark as valid command" == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/inspections/quickfix/RegisterShebangCommandQuickfix", "getName" }));  return "Mark as valid command";
  }
}
