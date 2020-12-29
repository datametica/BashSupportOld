package com.ansorgit.plugins.bash.editor.inspections;

import com.ansorgit.plugins.bash.lang.psi.util.BashPsiElementFactory;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.Nullable;




















public class SupressionUtil
{
  private static final String SUPPRESSION_PREFIX = "@IgnoreInspection";
  
  @Nullable
  public static PsiComment findSuppressionComment(PsiElement anchor) {
    return (PsiComment)PsiTreeUtil.getChildOfType(anchor, PsiComment.class);
  }
  
  public static boolean isSuppressionComment(PsiComment suppressionComment, String inspectionId) {
    return (suppressionComment != null && suppressionComment.getText().trim().endsWith("@IgnoreInspection " + inspectionId));
  }
  
  public static PsiComment createSuppressionComment(Project project, String id) {
    return BashPsiElementFactory.createComment(project, "@IgnoreInspection " + id);
  }
}
