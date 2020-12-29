package com.ansorgit.plugins.bash.lang.parser.eval;

import com.intellij.openapi.util.ProperTextRange;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.Segment;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.LiteralTextEscaper;
import com.intellij.psi.PsiLanguageInjectionHost;
import org.jetbrains.annotations.NotNull;


























public class BashSimpleTextLiteralEscaper<T extends PsiLanguageInjectionHost>
  extends LiteralTextEscaper<T>
{
  private int[] outSourceOffsets;
  
  public BashSimpleTextLiteralEscaper(T host) {
    super((PsiLanguageInjectionHost)host);
  }

  
  public boolean decode(@NotNull TextRange rangeInsideHost, @NotNull StringBuilder outChars) {
    if (rangeInsideHost == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "rangeInsideHost", "com/ansorgit/plugins/bash/lang/parser/eval/BashSimpleTextLiteralEscaper", "decode" }));  if (outChars == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "outChars", "com/ansorgit/plugins/bash/lang/parser/eval/BashSimpleTextLiteralEscaper", "decode" }));  ProperTextRange.assertProperRange((Segment)rangeInsideHost);
    String subText = rangeInsideHost.substring(this.myHost.getText());
    
    Ref<int[]> sourceOffsetsRef = new Ref();
    boolean result = TextProcessorUtil.parseStringCharacters(subText, outChars, sourceOffsetsRef);
    this.outSourceOffsets = (int[])sourceOffsetsRef.get();
    
    return result;
  }
  
  public int getOffsetInHost(int offsetInDecoded, @NotNull TextRange rangeInsideHost) {
    if (rangeInsideHost == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "rangeInsideHost", "com/ansorgit/plugins/bash/lang/parser/eval/BashSimpleTextLiteralEscaper", "getOffsetInHost" }));  int result = (offsetInDecoded < this.outSourceOffsets.length) ? this.outSourceOffsets[offsetInDecoded] : -1;
    if (result == -1) {
      return -1;
    }
    
    return rangeInsideHost.getStartOffset() + ((result <= rangeInsideHost.getLength()) ? result : rangeInsideHost.getLength());
  }

  
  public boolean isOneLine() {
    return true;
  }
}
