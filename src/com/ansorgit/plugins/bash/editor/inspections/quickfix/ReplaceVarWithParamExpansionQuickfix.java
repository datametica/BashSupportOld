package com.ansorgit.plugins.bash.editor.inspections.quickfix;

import com.ansorgit.plugins.bash.lang.psi.api.BashString;
import com.ansorgit.plugins.bash.lang.psi.api.arithmetic.ArithmeticExpression;
import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVar;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiElementFactory;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;




















public class ReplaceVarWithParamExpansionQuickfix
  extends AbstractBashPsiElementQuickfix
{
  private final String variableName;
  
  public ReplaceVarWithParamExpansionQuickfix(BashVar var) {
    super((PsiElement)var);
    this.variableName = var.getReference().getReferencedName();
  }
  
  public static boolean isAvailableAt(@NotNull BashVar var) {
    if (var == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "var", "com/ansorgit/plugins/bash/editor/inspections/quickfix/ReplaceVarWithParamExpansionQuickfix", "isAvailableAt" }));  if (BashPsiUtils.hasParentOfType((PsiElement)var, BashString.class, 4) || BashPsiUtils.hasParentOfType((PsiElement)var, ArithmeticExpression.class, 4)) {
      return false;
    }
    
    return (!var.isParameterExpansion() && !var.isBuiltinVar());
  }

  
  public boolean isAvailable(@NotNull Project project, @NotNull PsiFile file, @NotNull PsiElement startElement, @NotNull PsiElement endElement) {
    if (project == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "project", "com/ansorgit/plugins/bash/editor/inspections/quickfix/ReplaceVarWithParamExpansionQuickfix", "isAvailable" }));  if (file == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "file", "com/ansorgit/plugins/bash/editor/inspections/quickfix/ReplaceVarWithParamExpansionQuickfix", "isAvailable" }));  if (startElement == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "startElement", "com/ansorgit/plugins/bash/editor/inspections/quickfix/ReplaceVarWithParamExpansionQuickfix", "isAvailable" }));  if (endElement == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "endElement", "com/ansorgit/plugins/bash/editor/inspections/quickfix/ReplaceVarWithParamExpansionQuickfix", "isAvailable" }));  return (startElement instanceof BashVar && isAvailableAt((BashVar)startElement));
  }
  
  @NotNull
  public String getText() {
    if (this.variableName.length() > 10) {
      if ("Replace with '${...}'" == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/inspections/quickfix/ReplaceVarWithParamExpansionQuickfix", "getText" }));  return "Replace with '${...}'";
    } 
    
    if (String.format("Replace '%s' with '${%s}'", new Object[] { this.variableName, this.variableName }) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/inspections/quickfix/ReplaceVarWithParamExpansionQuickfix", "getText" }));  return String.format("Replace '%s' with '${%s}'", new Object[] { this.variableName, this.variableName });
  }


  
  public void invoke(@NotNull Project project, @NotNull PsiFile file, Editor editor, @NotNull PsiElement startElement, @NotNull PsiElement endElement) {
    if (project == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "project", "com/ansorgit/plugins/bash/editor/inspections/quickfix/ReplaceVarWithParamExpansionQuickfix", "invoke" }));  if (file == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "file", "com/ansorgit/plugins/bash/editor/inspections/quickfix/ReplaceVarWithParamExpansionQuickfix", "invoke" }));  if (startElement == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "startElement", "com/ansorgit/plugins/bash/editor/inspections/quickfix/ReplaceVarWithParamExpansionQuickfix", "invoke" }));  if (endElement == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "endElement", "com/ansorgit/plugins/bash/editor/inspections/quickfix/ReplaceVarWithParamExpansionQuickfix", "invoke" }));  Document document = file.getViewProvider().getDocument();
    if (document != null && document.isWritable()) {
      PsiElement replacement = BashPsiElementFactory.createComposedVar(project, this.variableName);
      startElement.replace(replacement);
    } 
  }
}
