package com.ansorgit.plugins.bash.call;

import com.intellij.codeInspection.InspectionProfileEntry;
import com.intellij.codeInspection.LocalInspectionEP;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.newvfs.impl.VfsRootAccess;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.DebugUtil;
import com.intellij.testFramework.fixtures.CodeInsightTestFixture;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;

public final class BashTestUtils
{
  private static volatile String basePath;
  
  public static InspectionProfileEntry findInspectionProfileEntry(Class<? extends LocalInspectionTool> clazz) {
    LocalInspectionEP[] extensions = (LocalInspectionEP[])Extensions.getExtensions(LocalInspectionEP.LOCAL_INSPECTION);
    for (LocalInspectionEP extension : extensions) {
      if (extension.implementationClass.equals(clazz.getCanonicalName())) {
        extension.enabledByDefault = true;
        
        return extension.instantiateTool();
      } 
    } 
    
    throw new IllegalStateException("Unable to find inspection profile entry for " + clazz);
  }
  
  public static String getBasePath() {
    if (basePath == null) {
      basePath = "";
    }
    
    if (basePath == null) {
      basePath = "";
    }
    
    VfsRootAccess.allowRootAccess(new String[] { basePath });
    
    return basePath;
  }
  
  public static PsiElement configureFixturePsiAtCaret(String fileNameInTestPath, CodeInsightTestFixture fixture) {
    fixture.configureByFile(fileNameInTestPath);
    
    PsiElement element = fixture.getFile().findElementAt(fixture.getCaretOffset());
    if (element instanceof com.intellij.psi.impl.source.tree.LeafPsiElement) {
      return element.getParent();
    }
    
    return element;
  }
  
  @NotNull
  public static String loadTestCaseFile(BashTestCase testCase, String path) throws IOException {
    if (FileUtil.loadFile(new File(testCase.getTestDataPath(), path.replace('/', File.separatorChar)), "UTF-8") == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/call/BashTestUtils", "loadTestCaseFile" }));  return FileUtil.loadFile(new File(testCase.getTestDataPath(), path.replace('/', File.separatorChar)), "UTF-8");
  }
  
  public static void assertPsiTreeByFile(BashTestCase testCase, PsiFile psiFile, String filePath) throws IOException {
    String actualPsi = DebugUtil.psiToString((PsiElement)psiFile, false).trim();
    String expectedPsi = loadTestCaseFile(testCase, filePath).trim();
    
    Assert.assertEquals(expectedPsi, actualPsi);
  }
  
  private static String computeBasePath() {
    String configuredDir = StringUtils.stripToNull(System.getenv("BASHSUPPORT_TESTDATA"));
    if (configuredDir != null) {
      File dir = new File(configuredDir);
      if (dir.isDirectory() && dir.exists()) {
        return dir.getAbsolutePath();
      }
    } 

    
    URL url = BashTestUtils.class.getClassLoader().getResource("log4j.xml");
    if (url != null) {
      try {
        File basePath = new File(url.toURI());
        while (basePath.exists() && !(new File(basePath, "testData")).isDirectory()) {
          basePath = basePath.getParentFile();
        }


        
        if (basePath.isDirectory()) {
          return (new File(basePath, "testData")).getAbsolutePath();
        }
      } catch (Exception exception) {}
    }
    else {
      
      throw new IllegalStateException("Could not find log4jx.ml");
    } 
    
    return null;
  }
}
