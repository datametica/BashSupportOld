package com.ansorgit.plugins.bash.documentation;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.io.StreamUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;





















abstract class ClasspathDocSource
  implements DocumentationSource
{
  private static final Logger log = Logger.getInstance("#bash.DocumentationReader");
  private final String prefixPath;
  
  ClasspathDocSource(String prefixPath) {
    this.prefixPath = prefixPath;
  }
  
  public String documentation(PsiElement element, PsiElement originalElement) {
    if (!isValid(element, originalElement)) {
      return null;
    }
    
    return readFromClasspath(this.prefixPath, resourceNameForElement(element));
  }



  
  abstract String resourceNameForElement(PsiElement paramPsiElement);


  
  abstract boolean isValid(PsiElement paramPsiElement1, PsiElement paramPsiElement2);


  
  private String readFromClasspath(String path, String command) {
    if (StringUtil.isEmpty(path) || StringUtil.isEmpty(command)) {
      return null;
    }
    
    String fullPath = path + "/" + command + ".html";
    try {
      URL url = getClass().getResource(fullPath);
      if (url == null) {
        return null;
      }
      
      InputStream inputStream = new BufferedInputStream(url.openStream());
      
      return StreamUtil.readText(inputStream, "UTF-8");
    } catch (IOException e) {
      log.debug("Failed to read documentation.", e);

      
      return null;
    } 
  }
}
