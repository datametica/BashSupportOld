package com.ansorgit.plugins.bash.editor.inspections.quickfix;

import com.ansorgit.plugins.bash.lang.psi.api.arithmetic.ArithmeticExpression;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;



















public class EvaluateArithExprQuickfix
  extends AbstractBashPsiElementQuickfix
{
  private final String expressionText;
  private final long numericValue;
  
  public EvaluateArithExprQuickfix(ArithmeticExpression expression) {
    super((PsiElement)expression);
    this.expressionText = expression.getText();
    this.numericValue = expression.computeNumericValue();
  }
  
  @NotNull
  public String getText() {
    if ("Replace '" + this.expressionText + "' with the result '" + this.numericValue + "'" == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/inspections/quickfix/EvaluateArithExprQuickfix", "getText" }));  return "Replace '" + this.expressionText + "' with the result '" + this.numericValue + "'";
  }

  
  public void invoke(@NotNull Project project, @NotNull PsiFile file, Editor editor, @NotNull PsiElement startElement, @NotNull PsiElement endElement) {
    if (project == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "project", "com/ansorgit/plugins/bash/editor/inspections/quickfix/EvaluateArithExprQuickfix", "invoke" }));  if (file == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "file", "com/ansorgit/plugins/bash/editor/inspections/quickfix/EvaluateArithExprQuickfix", "invoke" }));  if (startElement == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "startElement", "com/ansorgit/plugins/bash/editor/inspections/quickfix/EvaluateArithExprQuickfix", "invoke" }));  if (endElement == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "endElement", "com/ansorgit/plugins/bash/editor/inspections/quickfix/EvaluateArithExprQuickfix", "invoke" }));  TextRange r = startElement.getTextRange();
    String replacement = String.valueOf(this.numericValue);
    
    Document document = PsiDocumentManager.getInstance(project).getDocument(file);
    if (document != null) {
      document.replaceString(r.getStartOffset(), r.getEndOffset(), replacement);
      PsiDocumentManager.getInstance(project).commitDocument(document);
    } 
  }
}
