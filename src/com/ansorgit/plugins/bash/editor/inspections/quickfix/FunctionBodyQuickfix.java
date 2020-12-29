package com.ansorgit.plugins.bash.editor.inspections.quickfix;

import com.ansorgit.plugins.bash.lang.psi.api.BashBlock;
import com.ansorgit.plugins.bash.lang.psi.api.function.BashFunctionDef;
import com.intellij.codeInsight.CodeInsightUtilBase;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;



















public class FunctionBodyQuickfix
  extends AbstractBashPsiElementQuickfix
{
  public FunctionBodyQuickfix(BashFunctionDef functionDef) {
    super((PsiElement)functionDef);
  }
  
  @NotNull
  public String getText() {
    if ("Wrap function body in curly brackets" == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/inspections/quickfix/FunctionBodyQuickfix", "getText" }));  return "Wrap function body in curly brackets";
  }

  
  public void invoke(@NotNull Project project, @NotNull PsiFile file, Editor editor, @NotNull PsiElement startElement, @NotNull PsiElement endElement) {
    if (project == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "project", "com/ansorgit/plugins/bash/editor/inspections/quickfix/FunctionBodyQuickfix", "invoke" }));  if (file == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "file", "com/ansorgit/plugins/bash/editor/inspections/quickfix/FunctionBodyQuickfix", "invoke" }));  if (startElement == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "startElement", "com/ansorgit/plugins/bash/editor/inspections/quickfix/FunctionBodyQuickfix", "invoke" }));  if (endElement == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "endElement", "com/ansorgit/plugins/bash/editor/inspections/quickfix/FunctionBodyQuickfix", "invoke" }));  if (!CodeInsightUtilBase.prepareEditorForWrite(editor)) {
      return;
    }
    
    BashFunctionDef functionDef = (BashFunctionDef)startElement;
    
    BashBlock block = functionDef.functionBody();
    if (block != null) {
      StringBuilder builder = new StringBuilder();
      BashBlock body = functionDef.functionBody();
      builder.append("{ ").append(body.getText()).append(" }");
      
      int startOffset = body.getTextOffset();
      int endOffset = startOffset + body.getTextLength();
      
      Document document = PsiDocumentManager.getInstance(project).getDocument(file);
      document.replaceString(startOffset, endOffset, builder);
      
      PsiDocumentManager.getInstance(project).commitDocument(document);
    } 
  }
}
