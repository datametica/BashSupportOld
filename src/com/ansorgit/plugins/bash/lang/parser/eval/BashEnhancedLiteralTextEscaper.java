package com.ansorgit.plugins.bash.lang.parser.eval;

import com.intellij.openapi.util.ProperTextRange;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.Segment;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.LiteralTextEscaper;
import com.intellij.psi.PsiLanguageInjectionHost;
import org.jetbrains.annotations.NotNull;















public class BashEnhancedLiteralTextEscaper<T extends PsiLanguageInjectionHost>
  extends LiteralTextEscaper<T>
{
  private int[] outSourceOffsets;
  
  public BashEnhancedLiteralTextEscaper(T host) {
    super((PsiLanguageInjectionHost)host);
  }


  
  public boolean decode(@NotNull TextRange rangeInsideHost, @NotNull StringBuilder outChars) {
    if (rangeInsideHost == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "rangeInsideHost", "com/ansorgit/plugins/bash/lang/parser/eval/BashEnhancedLiteralTextEscaper", "decode" }));  if (outChars == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "outChars", "com/ansorgit/plugins/bash/lang/parser/eval/BashEnhancedLiteralTextEscaper", "decode" }));  return decodeText(rangeInsideHost.substring(this.myHost.getText()), rangeInsideHost, outChars);
  }
  
  protected boolean decodeText(String content, @NotNull TextRange rangeInsideHost, @NotNull StringBuilder outChars) {
    if (rangeInsideHost == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "rangeInsideHost", "com/ansorgit/plugins/bash/lang/parser/eval/BashEnhancedLiteralTextEscaper", "decodeText" }));  if (outChars == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "outChars", "com/ansorgit/plugins/bash/lang/parser/eval/BashEnhancedLiteralTextEscaper", "decodeText" }));  ProperTextRange.assertProperRange((Segment)rangeInsideHost);
    
    Ref<int[]> sourceOffsetsRef = new Ref();
    boolean result = TextProcessorUtil.enhancedParseStringCharacters(content, outChars, sourceOffsetsRef);
    this.outSourceOffsets = (int[])sourceOffsetsRef.get();
    
    return result;
  }
  
  public int getOffsetInHost(int offsetInDecoded, @NotNull TextRange rangeInsideHost) {
    if (rangeInsideHost == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "rangeInsideHost", "com/ansorgit/plugins/bash/lang/parser/eval/BashEnhancedLiteralTextEscaper", "getOffsetInHost" }));  int result = (offsetInDecoded < this.outSourceOffsets.length) ? this.outSourceOffsets[offsetInDecoded] : -1;
    if (result == -1) {
      return -1;
    }
    
    return ((result <= rangeInsideHost.getLength()) ? result : rangeInsideHost.getLength()) + rangeInsideHost.getStartOffset();
  }

  
  public boolean isOneLine() {
    return true;
  }
}
