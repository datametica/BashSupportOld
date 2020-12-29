package com.ansorgit.plugins.bash.lang;

import com.ansorgit.plugins.bash.editor.highlighting.BashSyntaxHighlighter;
import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.SingleLazyInstanceSyntaxHighlighterFactory;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import org.jetbrains.annotations.NotNull;

















public class BashLanguage
  extends Language
{
  public BashLanguage() {
    super("Bash", new String[] { "application/x-bsh", "application/x-sh", "text/x-script.sh" });
    
    SyntaxHighlighterFactory.LANGUAGE_FACTORY.addExplicitExtension(this, new BashHighlighterFactory());
  }
  
  private static class BashHighlighterFactory extends SingleLazyInstanceSyntaxHighlighterFactory {
    @NotNull
    protected SyntaxHighlighter createHighlighter() {
      if (new BashSyntaxHighlighter() == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/BashLanguage$BashHighlighterFactory", "createHighlighter" }));  return (SyntaxHighlighter)new BashSyntaxHighlighter();
    }
    
    private BashHighlighterFactory() {}
  }
}
