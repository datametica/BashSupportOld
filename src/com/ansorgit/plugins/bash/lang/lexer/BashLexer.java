package com.ansorgit.plugins.bash.lang.lexer;

import com.ansorgit.plugins.bash.lang.BashVersion;
import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.Lexer;
import com.intellij.lexer.MergeFunction;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;



















public class BashLexer
  extends MergingLexer
  implements BashTokenTypes
{
  public BashLexer() {
    this(BashVersion.Bash_v4);
  }
  
  public BashLexer(BashVersion bashVersion) {
    super((Lexer)new FlexAdapter(new _BashLexer(bashVersion, null)), new MergeTuple[] {
          
          MergeTuple.create(TokenSet.create(new IElementType[] { STRING_DATA }, ), STRING_CONTENT), 
          MergeTuple.create(TokenSet.create(new IElementType[] { HEREDOC_LINE }, ), HEREDOC_CONTENT)
        });
  }
}
