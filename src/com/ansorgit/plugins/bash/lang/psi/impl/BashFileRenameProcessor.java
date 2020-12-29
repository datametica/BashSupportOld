package com.ansorgit.plugins.bash.lang.psi.impl;

import com.ansorgit.plugins.bash.lang.psi.api.BashPsiElement;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.refactoring.rename.RenameDialog;
import com.intellij.refactoring.rename.RenamePsiFileProcessor;
import com.intellij.util.Query;
import java.util.Collection;
import org.jetbrains.annotations.NotNull;
























public class BashFileRenameProcessor
  extends RenamePsiFileProcessor
{
  public RenameDialog createRenameDialog(Project project, PsiElement element, PsiElement nameSuggestionContext, Editor editor) {
    return new BashFileRenameDialog(project, element, nameSuggestionContext, editor);
  }










  
  @NotNull
  public Collection<PsiReference> findReferences(PsiElement element) {
    SearchScope scope = (element instanceof BashPsiElement) ? BashElementSharedImpl.getElementUseScope((BashPsiElement)element, element.getProject()) : (SearchScope)GlobalSearchScope.projectScope(element.getProject());
    
    Query<PsiReference> search = ReferencesSearch.search(element, scope);
    if (search.findAll() == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/BashFileRenameProcessor", "findReferences" }));  return search.findAll();
  }

  
  public boolean canProcessElement(@NotNull PsiElement element) {
    if (element == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "element", "com/ansorgit/plugins/bash/lang/psi/impl/BashFileRenameProcessor", "canProcessElement" }));  return element instanceof com.ansorgit.plugins.bash.lang.psi.api.BashFile;
  }
  
  private static class BashFileRenameDialog extends RenameDialog {
    public BashFileRenameDialog(Project project, PsiElement element, PsiElement nameSuggestionContext, Editor editor) {
      super(project, element, nameSuggestionContext, editor);
      
      setTitle("Rename Bash file");
    }
  }
}
