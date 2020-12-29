package com.ansorgit.plugins.bash.call;

import com.ansorgit.plugins.bash.file.BashFileType;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.PsiFile;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;



















public abstract class AbstractBashPsiTree
  extends LightBashCodeInsightFixtureTestCase
{
  protected PsiFile getPsiFile(@NotNull String content) throws IOException {
    if (content == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "content", "com/ansorgit/plugins/bash/call/AbstractBashPsiTree", "getPsiFile" }));  PsiFile psiFile = this.myFixture.configureByText((FileType)BashFileType.BASH_FILE_TYPE, content);
    return psiFile;
  }

  
  protected String getBasePath() {
    return "psiTree";
  }
}
