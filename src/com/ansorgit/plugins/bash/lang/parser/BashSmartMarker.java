package com.ansorgit.plugins.bash.lang.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.WhitespacesAndCommentsBinder;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;



















public final class BashSmartMarker
  implements PsiBuilder.Marker
{
  private PsiBuilder.Marker delegate;
  private boolean open = true;
  
  public BashSmartMarker(PsiBuilder.Marker delegate) {
    this.delegate = delegate;
  }
  
  public boolean isOpen() {
    return this.open;
  }
  
  public PsiBuilder.Marker precede() {
    return this.delegate.precede();
  }
  
  public void drop() {
    this.delegate.drop();
    this.open = false;
  }
  
  public void rollbackTo() {
    this.delegate.rollbackTo();
    this.open = false;
  }
  
  public void done(@NotNull IElementType type) {
    if (type == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "type", "com/ansorgit/plugins/bash/lang/parser/BashSmartMarker", "done" }));  this.delegate.done(type);
    this.open = false;
  }
  
  public void doneBefore(@NotNull IElementType type, @NotNull PsiBuilder.Marker before) {
    if (type == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "type", "com/ansorgit/plugins/bash/lang/parser/BashSmartMarker", "doneBefore" }));  if (before == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "before", "com/ansorgit/plugins/bash/lang/parser/BashSmartMarker", "doneBefore" }));  this.delegate.doneBefore(type, before);
    this.open = false;
  }
  
  public void doneBefore(@NotNull IElementType type, @NotNull PsiBuilder.Marker before, String errorMessage) {
    if (type == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "type", "com/ansorgit/plugins/bash/lang/parser/BashSmartMarker", "doneBefore" }));  if (before == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "before", "com/ansorgit/plugins/bash/lang/parser/BashSmartMarker", "doneBefore" }));  this.delegate.doneBefore(type, before, errorMessage);
    this.open = false;
  }
  
  public void error(String message) {
    this.delegate.error(message);
    this.open = false;
  }
  
  public void collapse(@NotNull IElementType iElementType) {
    if (iElementType == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "iElementType", "com/ansorgit/plugins/bash/lang/parser/BashSmartMarker", "collapse" }));  this.delegate.collapse(iElementType);
  }
  
  public void setCustomEdgeTokenBinders(@Nullable WhitespacesAndCommentsBinder whitespacesAndCommentsBinder, @Nullable WhitespacesAndCommentsBinder whitespacesAndCommentsBinder1) {
    this.delegate.setCustomEdgeTokenBinders(whitespacesAndCommentsBinder, whitespacesAndCommentsBinder1);
  }
  
  public void errorBefore(String message, @NotNull PsiBuilder.Marker marker) {
    if (marker == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "marker", "com/ansorgit/plugins/bash/lang/parser/BashSmartMarker", "errorBefore" }));  this.delegate.errorBefore(message, marker);
    this.open = false;
  }
}
