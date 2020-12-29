package com.ansorgit.plugins.bash.editor.inspections.quickfix;

import com.ansorgit.plugins.bash.lang.psi.api.word.BashExpansion;
import com.ansorgit.plugins.bash.lang.valueExpansion.ValueExpansionUtil;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;



















public class EvaluateExpansionQuickfix
  extends AbstractBashPsiElementQuickfix
{
  private final boolean enableBash4;
  private final String expansionDef;
  
  public EvaluateExpansionQuickfix(BashExpansion expansion, boolean enableBash4) {
    super((PsiElement)expansion);
    this.enableBash4 = enableBash4;
    this.expansionDef = expansion.getText();
  }
  
  @NotNull
  public String getText() {
    String replacement = ValueExpansionUtil.expand(this.expansionDef, this.enableBash4);
    
    if (replacement.length() < 20) {
      if ("Replace with the result '" + replacement + "'" == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/inspections/quickfix/EvaluateExpansionQuickfix", "getText" }));  return "Replace with the result '" + replacement + "'";
    } 
    
    if ("Replace with evaluated expansion" == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/inspections/quickfix/EvaluateExpansionQuickfix", "getText" }));  return "Replace with evaluated expansion";
  }

  
  public void invoke(@NotNull Project project, @NotNull PsiFile file, Editor editor, @NotNull PsiElement startElement, @NotNull PsiElement endElement) {
    if (project == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "project", "com/ansorgit/plugins/bash/editor/inspections/quickfix/EvaluateExpansionQuickfix", "invoke" }));  if (file == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "file", "com/ansorgit/plugins/bash/editor/inspections/quickfix/EvaluateExpansionQuickfix", "invoke" }));  if (startElement == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "startElement", "com/ansorgit/plugins/bash/editor/inspections/quickfix/EvaluateExpansionQuickfix", "invoke" }));  if (endElement == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "endElement", "com/ansorgit/plugins/bash/editor/inspections/quickfix/EvaluateExpansionQuickfix", "invoke" }));  TextRange r = startElement.getTextRange();
    
    Document document = PsiDocumentManager.getInstance(project).getDocument(file);
    
    String replacement = ValueExpansionUtil.expand(startElement.getText(), this.enableBash4);
    if (replacement != null && document != null) {
      editor.getDocument().replaceString(r.getStartOffset(), r.getEndOffset(), replacement);
      PsiDocumentManager.getInstance(project).commitDocument(document);
    } 
  }
}
