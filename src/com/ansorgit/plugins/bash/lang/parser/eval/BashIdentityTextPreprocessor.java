package com.ansorgit.plugins.bash.lang.parser.eval;

import com.intellij.openapi.util.ProperTextRange;
import com.intellij.openapi.util.Segment;
import com.intellij.openapi.util.TextRange;
import org.jetbrains.annotations.NotNull;












public class BashIdentityTextPreprocessor
  implements TextPreprocessor
{
  private final TextRange contentRange;
  
  public BashIdentityTextPreprocessor(TextRange contentRange) {
    ProperTextRange.assertProperRange((Segment)contentRange);
    this.contentRange = contentRange;
  }

  
  public boolean decode(String content, @NotNull StringBuilder outChars) {
    if (outChars == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "outChars", "com/ansorgit/plugins/bash/lang/parser/eval/BashIdentityTextPreprocessor", "decode" }));  outChars.append(content);
    return true;
  }
  
  public int getOffsetInHost(int offsetInDecoded) {
    return offsetInDecoded + this.contentRange.getStartOffset();
  }

  
  public TextRange getContentRange() {
    return this.contentRange;
  }

  
  public boolean containsRange(int tokenStart, int tokenEnd) {
    return getContentRange().containsRange(tokenStart, tokenEnd);
  }

  
  public String patchOriginal(String originalText) {
    return originalText;
  }

  
  public String patchOriginal(String originalText, String replacement) {
    return originalText;
  }
}
