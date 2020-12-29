package com.ansorgit.plugins.bash.lang.psi.api;

import com.intellij.openapi.util.TextRange;
import org.jetbrains.annotations.NotNull;

public interface BashCharSequence extends BashPsiElement {
  boolean isWrapped();
  
  String createEquallyWrappedString(String paramString);
  
  String getUnwrappedCharSequence();
  
  boolean isStatic();
  
  @NotNull
  TextRange getTextContentRange();
}
