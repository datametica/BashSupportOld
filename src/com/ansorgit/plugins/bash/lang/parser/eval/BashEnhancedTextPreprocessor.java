package com.ansorgit.plugins.bash.lang.parser.eval;

import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.TextRange;
import org.jetbrains.annotations.NotNull;































class BashEnhancedTextPreprocessor
  implements TextPreprocessor
{
  private int[] outSourceOffsets;
  private final TextRange contentRange;
  
  BashEnhancedTextPreprocessor(TextRange contentRange) {
    this.contentRange = contentRange;
  }

  
  public boolean decode(String content, @NotNull StringBuilder outChars) {
    if (outChars == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "outChars", "com/ansorgit/plugins/bash/lang/parser/eval/BashEnhancedTextPreprocessor", "decode" }));  Ref<int[]> sourceOffsetsRef = new Ref();
    boolean result = TextProcessorUtil.enhancedParseStringCharacters(content, outChars, sourceOffsetsRef);
    this.outSourceOffsets = (int[])sourceOffsetsRef.get();
    
    return result;
  }
  
  public int getOffsetInHost(int offsetInDecoded) {
    int result = (offsetInDecoded >= 0 && offsetInDecoded < this.outSourceOffsets.length) ? this.outSourceOffsets[offsetInDecoded] : -1;
    if (result == -1) {
      return -1;
    }
    
    return this.contentRange.getStartOffset() + ((result <= this.contentRange.getLength()) ? result : this.contentRange.getLength());
  }

  
  public TextRange getContentRange() {
    return this.contentRange;
  }

  
  public boolean containsRange(int tokenStart, int tokenEnd) {
    return getContentRange().containsRange(tokenStart, tokenEnd);
  }

  
  public String patchOriginal(String originalText) {
    return patchOriginal(originalText, null);
  }

  
  public String patchOriginal(String originalText, String replacement) {
    return TextProcessorUtil.patchOriginal(originalText, this.outSourceOffsets, replacement);
  }
}
