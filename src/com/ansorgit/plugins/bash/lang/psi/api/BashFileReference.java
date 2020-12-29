package com.ansorgit.plugins.bash.lang.psi.api;

import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;






















public interface BashFileReference
  extends BashPsiElement
{
  @Nullable
  PsiFile findReferencedFile();
  
  @NotNull
  String getFilename();
  
  boolean isStatic();
  
  default boolean isDynamic() {
    return !isStatic();
  }
}
