package com.ansorgit.plugins.bash.editor.inspections.quickfix;

import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVarDef;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

















public class RemoveLocalQualifierQuickfix
  extends AbstractBashPsiElementQuickfix
{
  public RemoveLocalQualifierQuickfix(BashVarDef varDef) {
    super((PsiElement)varDef);
  }
  
  @NotNull
  public String getText() {
    if ("Remove 'local' qualifier" == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/inspections/quickfix/RemoveLocalQualifierQuickfix", "getText" }));  return "Remove 'local' qualifier";
  }

  
  public void invoke(@NotNull Project project, @NotNull PsiFile file, Editor editor, @NotNull PsiElement startElement, @NotNull PsiElement endElement) {
    if (project == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "project", "com/ansorgit/plugins/bash/editor/inspections/quickfix/RemoveLocalQualifierQuickfix", "invoke" }));  if (file == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "file", "com/ansorgit/plugins/bash/editor/inspections/quickfix/RemoveLocalQualifierQuickfix", "invoke" }));  if (startElement == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "startElement", "com/ansorgit/plugins/bash/editor/inspections/quickfix/RemoveLocalQualifierQuickfix", "invoke" }));  if (endElement == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "endElement", "com/ansorgit/plugins/bash/editor/inspections/quickfix/RemoveLocalQualifierQuickfix", "invoke" }));  BashVarDef varDef = (BashVarDef)startElement;
    
    PsiElement context = varDef.getContext();
    if (context != null)
    {
      
      if (!varDef.hasAssignmentValue()) {
        Document document = PsiDocumentManager.getInstance(project).getDocument(varDef.getContainingFile());
        if (document != null) {
          int endOffset = context.getTextOffset() + context.getTextLength();
          document.replaceString(context.getTextOffset(), endOffset, varDef.getName() + "=");
          
          PsiDocumentManager.getInstance(project).commitDocument(document);
        } 
      } else {
        BashPsiUtils.replaceElement(context, (PsiElement)varDef);
      } 
    }
  }
}
