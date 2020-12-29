package com.ansorgit.plugins.bash.editor.inspections.quickfix;

import com.ansorgit.plugins.bash.editor.inspections.SupressionUtil;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiElementFactory;
import com.intellij.codeInsight.FileModificationService;
import com.intellij.codeInspection.CommonProblemDescriptor;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.SuppressQuickFix;
import com.intellij.openapi.command.undo.UndoUtil;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;














public class SupressAddShebangInspectionQuickfix
  implements SuppressQuickFix
{
  private final String inspectionId;
  
  public SupressAddShebangInspectionQuickfix(String inspectionId) {
    this.inspectionId = inspectionId;
  }

  
  public boolean isSuppressAll() {
    return false;
  }

  
  @NotNull
  public String getName() {
    if ("Suppress for file ..." == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/inspections/quickfix/SupressAddShebangInspectionQuickfix", "getName" }));  return "Suppress for file ...";
  }

  
  @NotNull
  public String getFamilyName() {
    if ("Bash" == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/inspections/quickfix/SupressAddShebangInspectionQuickfix", "getFamilyName" }));  return "Bash";
  }

  
  public boolean isAvailable(@NotNull Project project, @NotNull PsiElement context) {
    if (project == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "project", "com/ansorgit/plugins/bash/editor/inspections/quickfix/SupressAddShebangInspectionQuickfix", "isAvailable" }));  if (context == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "context", "com/ansorgit/plugins/bash/editor/inspections/quickfix/SupressAddShebangInspectionQuickfix", "isAvailable" }));  return true;
  }
  
  public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
    PsiElement inserted;
    if (project == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "project", "com/ansorgit/plugins/bash/editor/inspections/quickfix/SupressAddShebangInspectionQuickfix", "applyFix" }));  if (descriptor == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "descriptor", "com/ansorgit/plugins/bash/editor/inspections/quickfix/SupressAddShebangInspectionQuickfix", "applyFix" }));  PsiFile file = descriptor.getPsiElement().getContainingFile();
    if (file == null) {
      return;
    }
    
    if (!FileModificationService.getInstance().preparePsiElementForWrite((PsiElement)file)) {
      return;
    }
    
    PsiComment suppressionComment = SupressionUtil.createSuppressionComment(project, this.inspectionId);
    
    PsiElement firstChild = file.getFirstChild();
    
    if (firstChild != null) {
      inserted = file.addBefore((PsiElement)suppressionComment, firstChild);
    } else {
      inserted = file.add((PsiElement)suppressionComment);
    } 
    
    if (inserted != null) {
      file.addAfter(BashPsiElementFactory.createNewline(project), inserted);
    }
    
    UndoUtil.markPsiFileForUndo(file);
  }
}
