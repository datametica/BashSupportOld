package com.ansorgit.plugins.bash.lang.psi.impl.heredoc;

import com.ansorgit.plugins.bash.lang.psi.api.heredoc.BashHereDocMarker;
import com.ansorgit.plugins.bash.lang.psi.util.BashIdentifierUtil;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import com.ansorgit.plugins.bash.lang.util.HeredocSharedImpl;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.resolve.reference.impl.CachingReference;
import com.intellij.refactoring.rename.BindablePsiReference;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
















abstract class HeredocMarkerReference
  extends CachingReference
  implements BindablePsiReference
{
  protected final BashHereDocMarker marker;
  
  HeredocMarkerReference(BashHereDocMarker marker) {
    this.marker = marker;
  }





  
  public BashHereDocMarker getElement() {
    return this.marker;
  }

  
  public TextRange getRangeInElement() {
    String markerText = this.marker.getText();
    
    return TextRange.create(HeredocSharedImpl.startMarkerTextOffset(markerText, this.marker.isIgnoringTabs()), HeredocSharedImpl.endMarkerTextOffset(markerText));
  }

  
  @NotNull
  public String getCanonicalText() {
    if (this.marker.getText() == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/heredoc/HeredocMarkerReference", "getCanonicalText" }));  return this.marker.getText();
  }

  
  public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
    if (!BashIdentifierUtil.isValidHeredocIdentifier(newElementName)) {
      throw new IncorrectOperationException("Invalid name " + newElementName);
    }
    
    return BashPsiUtils.replaceElement((PsiElement)this.marker, createMarkerElement(newElementName));
  }

  
  public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException {
    if (element == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "element", "com/ansorgit/plugins/bash/lang/psi/impl/heredoc/HeredocMarkerReference", "bindToElement" }));  if (element instanceof BashHereDocMarker) {
      return handleElementRename(((BashHereDocMarker)element).getMarkerText());
    }
    
    throw new IncorrectOperationException("Unsupported element type");
  }

  
  @NotNull
  public Object[] getVariants() {
    if (EMPTY_ARRAY == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/heredoc/HeredocMarkerReference", "getVariants" }));  return (Object[])EMPTY_ARRAY;
  }
  
  @Nullable
  public abstract PsiElement resolveInner();
  
  protected abstract PsiElement createMarkerElement(String paramString);
}
