package com.ansorgit.plugins.bash.util;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.WhitespacesAndCommentsBinder;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

















public final class NullMarker
  implements PsiBuilder.Marker
{
  private static final PsiBuilder.Marker instance = new NullMarker();



  
  public static PsiBuilder.Marker get() {
    return instance;
  }
  
  @NotNull
  public PsiBuilder.Marker precede() {
    if (this == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/util/NullMarker", "precede" }));  return this;
  }

  
  public void drop() {}

  
  public void rollbackTo() {}
  
  public void done(@NotNull IElementType iElementType) {
    if (iElementType == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "iElementType", "com/ansorgit/plugins/bash/util/NullMarker", "done" })); 
  }
  public void collapse(@NotNull IElementType iElementType) {
    if (iElementType == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "iElementType", "com/ansorgit/plugins/bash/util/NullMarker", "collapse" })); 
  }
  public void doneBefore(@NotNull IElementType iElementType, @NotNull PsiBuilder.Marker marker) {
    if (iElementType == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "iElementType", "com/ansorgit/plugins/bash/util/NullMarker", "doneBefore" }));  if (marker == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "marker", "com/ansorgit/plugins/bash/util/NullMarker", "doneBefore" })); 
  }
  public void doneBefore(@NotNull IElementType iElementType, @NotNull PsiBuilder.Marker marker, String s) {
    if (iElementType == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "iElementType", "com/ansorgit/plugins/bash/util/NullMarker", "doneBefore" }));  if (marker == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "marker", "com/ansorgit/plugins/bash/util/NullMarker", "doneBefore" })); 
  }
  
  public void error(String s) {}
  
  public void errorBefore(String s, @NotNull PsiBuilder.Marker marker) {
    if (marker == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "marker", "com/ansorgit/plugins/bash/util/NullMarker", "errorBefore" })); 
  }
  
  public void setCustomEdgeTokenBinders(@Nullable WhitespacesAndCommentsBinder whitespacesAndCommentsBinder, @Nullable WhitespacesAndCommentsBinder whitespacesAndCommentsBinder1) {}
}
