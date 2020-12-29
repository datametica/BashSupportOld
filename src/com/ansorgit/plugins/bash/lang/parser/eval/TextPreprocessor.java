package com.ansorgit.plugins.bash.lang.parser.eval;

import com.intellij.openapi.util.TextRange;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TextPreprocessor {
  boolean decode(String paramString, @NotNull StringBuilder paramStringBuilder);
  
  int getOffsetInHost(int paramInt);
  
  TextRange getContentRange();
  
  boolean containsRange(int paramInt1, int paramInt2);
  
  String patchOriginal(String paramString);
  
  String patchOriginal(String paramString1, @Nullable String paramString2);
}
