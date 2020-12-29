package com.ansorgit.plugins.bash.lang.parser.eval;

import com.intellij.openapi.util.ProperTextRange;
import com.intellij.openapi.util.Segment;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.LiteralTextEscaper;
import com.intellij.psi.PsiLanguageInjectionHost;
import org.jetbrains.annotations.NotNull;















public class BashIdentityStringLiteralEscaper<T extends PsiLanguageInjectionHost>
  extends LiteralTextEscaper<T>
{
  public BashIdentityStringLiteralEscaper(T host) {
    super((PsiLanguageInjectionHost)host);
  }

  
  public boolean decode(@NotNull TextRange rangeInsideHost, @NotNull StringBuilder outChars) {
    if (rangeInsideHost == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "rangeInsideHost", "com/ansorgit/plugins/bash/lang/parser/eval/BashIdentityStringLiteralEscaper", "decode" }));  if (outChars == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "outChars", "com/ansorgit/plugins/bash/lang/parser/eval/BashIdentityStringLiteralEscaper", "decode" }));  ProperTextRange.assertProperRange((Segment)rangeInsideHost);
    
    outChars.append(rangeInsideHost.substring(this.myHost.getText()));
    
    return true;
  }

  
  public int getOffsetInHost(int offsetInDecoded, @NotNull TextRange rangeInsideHost) {
    if (rangeInsideHost == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "rangeInsideHost", "com/ansorgit/plugins/bash/lang/parser/eval/BashIdentityStringLiteralEscaper", "getOffsetInHost" }));  return offsetInDecoded + rangeInsideHost.getStartOffset();
  }

  
  public boolean isOneLine() {
    return true;
  }
}
