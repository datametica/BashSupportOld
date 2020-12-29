package com.ansorgit.plugins.bash.call;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.testFramework.UsefulTestCase;
import com.intellij.testFramework.builders.EmptyModuleFixtureBuilder;
import com.intellij.testFramework.builders.ModuleFixtureBuilder;
import com.intellij.testFramework.fixtures.CodeInsightTestFixture;
import com.intellij.testFramework.fixtures.IdeaProjectTestFixture;
import com.intellij.testFramework.fixtures.IdeaTestFixtureFactory;
import com.intellij.testFramework.fixtures.TestFixtureBuilder;
import java.io.File;
import org.jetbrains.annotations.NonNls;


















public abstract class BashCodeInsightFixtureTestCase<T extends ModuleFixtureBuilder>
  extends UsefulTestCase
{
  protected CodeInsightTestFixture myFixture;
  protected Module myModule;
  
  protected PsiElement configurePsiAtCaret() {
    return configurePsiAtCaret(getTestName(true) + ".bash");
  }
  
  protected PsiElement configurePsiAtCaret(String fileNameInTestPath) {
    return BashTestUtils.configureFixturePsiAtCaret(fileNameInTestPath, this.myFixture);
  }

  
  protected void setUp() throws Exception {
    super.setUp();
    
    String name = getClass().getName() + "." + getName();
    TestFixtureBuilder<IdeaProjectTestFixture> projectBuilder = IdeaTestFixtureFactory.getFixtureFactory().createFixtureBuilder(name);
    this.myFixture = IdeaTestFixtureFactory.getFixtureFactory().createCodeInsightFixture((IdeaProjectTestFixture)projectBuilder.getFixture());
    
    ModuleFixtureBuilder moduleFixtureBuilder = projectBuilder.addModule(getModuleBuilderClass());
    tuneFixture((T)moduleFixtureBuilder);
    
    this.myFixture.setUp();
    this.myFixture.setTestDataPath(getTestDataPath());
    this.myModule = moduleFixtureBuilder.getFixture().getModule();
  }
  
  protected Class<T> getModuleBuilderClass() {
    return (Class)EmptyModuleFixtureBuilder.class;
  }

  
  protected void tearDown() throws Exception {
    try {
      this.myFixture.tearDown();
    } finally {
      this.myFixture = null;
      this.myModule = null;
      
      super.tearDown();
    } 
  }
  
  protected void tuneFixture(T moduleBuilder) {
    moduleBuilder.addSourceContentRoot(this.myFixture.getTempDirPath());
  }






  
  @NonNls
  protected String getBasePath() {
    return "";
  }





  
  @NonNls
  protected String getTestDataPath() {
    String basePath = getBasePath();
    return BashTestUtils.getBasePath() + (basePath.endsWith(File.separator) ? "" : File.separator) + basePath;
  }
  
  protected boolean isCommunity() {
    return false;
  }
  
  protected Project getProject() {
    return this.myFixture.getProject();
  }
  
  protected Editor getEditor() {
    return this.myFixture.getEditor();
  }
  
  protected PsiFile getFile() {
    return this.myFixture.getFile();
  }
}
