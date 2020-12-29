package com.ansorgit.plugins.bash.editor.formatting;

import com.ansorgit.plugins.bash.editor.formatting.noOpModel.NoOpBlock;
import com.ansorgit.plugins.bash.settings.BashProjectSettings;
import com.intellij.formatting.Block;
import com.intellij.formatting.FormattingModel;
import com.intellij.formatting.FormattingModelBuilder;
import com.intellij.formatting.FormattingModelProvider;
import com.intellij.formatting.Indent;
import com.intellij.lang.ASTNode;
import com.intellij.lang.FileASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

















public class BashFormattingModelBuilder
  implements FormattingModelBuilder
{
  @NotNull
  public FormattingModel createModel(PsiElement element, CodeStyleSettings settings) {
    ASTNode node = element.getNode();
    assert node != null;
    
    PsiFile containingFile = element.getContainingFile();
    FileASTNode fileASTNode = containingFile.getNode();
    assert fileASTNode != null;
    
    BashProjectSettings projectSettings = BashProjectSettings.storedSettings(containingFile.getProject());
    if (!projectSettings.isFormatterEnabled()) {
      if (FormattingModelProvider.createFormattingModelForPsiFile(containingFile, (Block)new NoOpBlock((ASTNode)fileASTNode), settings) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/formatting/BashFormattingModelBuilder", "createModel" }));  return FormattingModelProvider.createFormattingModelForPsiFile(containingFile, (Block)new NoOpBlock((ASTNode)fileASTNode), settings);
    } 

    
    if (FormattingModelProvider.createFormattingModelForPsiFile(containingFile, new BashBlock((ASTNode)fileASTNode, null, 
          Indent.getAbsoluteNoneIndent(), null, settings), settings) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/formatting/BashFormattingModelBuilder", "createModel" }));  return FormattingModelProvider.createFormattingModelForPsiFile(containingFile, new BashBlock((ASTNode)fileASTNode, null, Indent.getAbsoluteNoneIndent(), null, settings), settings);
  }
  
  @Nullable
  public TextRange getRangeAffectingIndent(PsiFile file, int offset, ASTNode elementAtOffset) {
    return null;
  }
}
