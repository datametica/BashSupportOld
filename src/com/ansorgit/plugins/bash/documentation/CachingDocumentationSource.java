package com.ansorgit.plugins.bash.documentation;

import com.google.common.collect.MapMaker;
import com.intellij.psi.PsiElement;
import java.util.Map;
import org.jetbrains.annotations.Nullable;





























class CachingDocumentationSource
  implements DocumentationSource
{
  private final CachableDocumentationSource delegate;
  private final Map<String, String> documentationCache = (new MapMaker()).weakValues().makeMap();
  
  CachingDocumentationSource(CachableDocumentationSource source) {
    this.delegate = source;
  }
  
  @Nullable
  public String documentation(PsiElement element, PsiElement originalElement) {
    String key = this.delegate.findCacheKey(element, originalElement);
    if (key == null) {
      return this.delegate.documentation(element, originalElement);
    }
    
    if (!this.documentationCache.containsKey(key) || this.documentationCache.get(key) == null) {
      String data = this.delegate.documentation(element, originalElement);
      if (data == null) {
        return null;
      }
      
      this.documentationCache.put(key, data);
    } 
    
    return this.documentationCache.get(key);
  }
  
  @Nullable
  public String documentationUrl(PsiElement element, PsiElement originalElement) {
    return this.delegate.documentationUrl(element, originalElement);
  }
}
