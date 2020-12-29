package com.ansorgit.plugins.bash.documentation;

import com.ansorgit.plugins.bash.lang.parser.BashElementTypes;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import com.intellij.lang.documentation.AbstractDocumentationProvider;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.SystemInfoRt;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;














@Metadata(mv = {1, 1, 1}, bv = {1, 0, 0}, k = 1, d1 = {"\000H\n\002\030\002\n\002\030\002\n\002\b\002\n\002\020!\n\002\030\002\n\000\n\002\020\016\n\000\n\002\030\002\n\002\b\003\n\002\030\002\n\000\n\002\030\002\n\002\b\003\n\002\030\002\n\002\b\004\n\002\020\000\n\002\b\002\n\002\020 \n\000\030\0002\0020\001B\005¢\006\002\020\002J\036\020\006\032\004\030\0010\0072\b\020\b\032\004\030\0010\t2\b\020\n\032\004\030\0010\tH\026J$\020\013\032\004\030\0010\t2\006\020\f\032\0020\r2\006\020\016\032\0020\0172\b\020\020\032\004\030\0010\tH\026J(\020\021\032\004\030\0010\t2\b\020\022\032\004\030\0010\0232\b\020\024\032\004\030\0010\0072\b\020\025\032\004\030\0010\tH\026J(\020\026\032\004\030\0010\t2\b\020\022\032\004\030\0010\0232\b\020\027\032\004\030\0010\0302\b\020\b\032\004\030\0010\tH\026J\036\020\031\032\004\030\0010\0072\b\020\b\032\004\030\0010\t2\b\020\n\032\004\030\0010\tH\026J$\020\032\032\n\022\004\022\0020\007\030\0010\0332\b\020\b\032\004\030\0010\t2\b\020\n\032\004\030\0010\tH\026R\024\020\003\032\b\022\004\022\0020\0050\004X\004¢\006\002\n\000¨\006\034"}, d2 = {"Lcom/ansorgit/plugins/bash/documentation/BashDocumentationProvider;", "Lcom/intellij/lang/documentation/AbstractDocumentationProvider;", "()V", "sourceList", "", "Lcom/ansorgit/plugins/bash/documentation/DocumentationSource;", "generateDoc", "", "element", "Lcom/intellij/psi/PsiElement;", "originalElement", "getCustomDocumentationElement", "editor", "Lcom/intellij/openapi/editor/Editor;", "file", "Lcom/intellij/psi/PsiFile;", "contextElement", "getDocumentationElementForLink", "psiManager", "Lcom/intellij/psi/PsiManager;", "link", "context", "getDocumentationElementForLookupItem", "object", "", "getQuickNavigateInfo", "getUrlFor", "", "BashSupport1_main"})
public final class BashDocumentationProvider
  extends AbstractDocumentationProvider
{
  private final List<DocumentationSource> sourceList = CollectionsKt.mutableListOf((Object[])new DocumentationSource[] { new PsiElementCommentSource(), new BashKeywordDocSource(), new InternalCommandDocumentation(), new ManpageDocSource() });
  
  public BashDocumentationProvider() {
    if (!SystemInfoRt.isWindows)
    {
      this.sourceList.add(new CachingDocumentationSource(new SystemInfopageDocSource())); } 
  }
  
  @Nullable
  public PsiElement getCustomDocumentationElement(@NotNull Editor editor, @NotNull PsiFile file, @Nullable PsiElement contextElement) {
    Intrinsics.checkParameterIsNotNull(editor, "editor"); Intrinsics.checkParameterIsNotNull(file, "file"); if (contextElement instanceof com.ansorgit.plugins.bash.lang.psi.api.vars.BashVar) {
      return (PsiElement)null;
    }

    
    int offset = editor.getCaretModel().getOffset();
    for (Iterator<Number> iterator = CollectionsKt.listOf((Object[])new Integer[] { Integer.valueOf(offset), Integer.valueOf(offset - 1) }).iterator(); iterator.hasNext(); ) { int o = ((Number)iterator.next()).intValue();
      PsiElement psi = file.findElementAt(o);
      if (psi != null) {
        PsiElement parent = BashPsiUtils.findEquivalentParent(psi, BashElementTypes.GENERIC_COMMAND_ELEMENT);
        if (parent != null) {
          return parent;
        }
      }  }

    
    return (PsiElement)null;
  } @Nullable
  public String getQuickNavigateInfo(@Nullable PsiElement element, @Nullable PsiElement originalElement) {
    return (String)null;
  } @Nullable
  public List<String> getUrlFor(@Nullable PsiElement element, @Nullable PsiElement originalElement) {
    for (DocumentationSource source : this.sourceList) {
      String url = source.documentationUrl(element, originalElement);
      if (url != null && StringUtils.stripToNull(url) != null) {
        return CollectionsKt.listOf(url);
      }
    } 
    
    return (List<String>)null;
  }








  
  @Nullable
  public String generateDoc(@Nullable PsiElement element, @Nullable PsiElement originalElement) {
    for (DocumentationSource source : this.sourceList) {
      String doc = source.documentation(element, originalElement);
      if (StringUtils.stripToNull(doc) != null) {
        return doc;
      }
    } 
    
    return (String)null;
  }
  @Nullable
  public PsiElement getDocumentationElementForLookupItem(@Nullable PsiManager psiManager, @Nullable Object object, @Nullable PsiElement element) { return element; } @Nullable
  public PsiElement getDocumentationElementForLink(@Nullable PsiManager psiManager, @Nullable String link, @Nullable PsiElement context) {
    return context;
  }
}
