package com.ansorgit.plugins.bash.call;

import com.intellij.openapi.util.SystemInfo;
import com.intellij.psi.PsiElement;
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase;
import java.io.File;
import java.io.IOException;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NonNls;
















public abstract class LightBashCodeInsightFixtureTestCase
  extends LightPlatformCodeInsightFixtureTestCase
  implements BashTestCase
{
  protected PsiElement configurePsiAtCaret() {
    return configurePsiAtCaret(getTestName(true) + ".bash");
  }
  
  protected PsiElement configurePsiAtCaret(String fileNameInTestPath) {
    return BashTestUtils.configureFixturePsiAtCaret(fileNameInTestPath, this.myFixture);
  }






  
  @NonNls
  protected String getBasePath() {
    return "";
  }





  
  @NonNls
  public final String getTestDataPath() {
    String basePath = getBasePath();
    if (SystemInfo.isWindows) {
      basePath = StringUtils.replace(basePath, "/", File.separator);
    }
    
    return BashTestUtils.getBasePath() + (basePath.startsWith(File.separator) ? "" : File.separator) + basePath;
  }
  
  public String loadTestDataFile(String path) throws IOException {
    return BashTestUtils.loadTestCaseFile(this, path);
  }
}
