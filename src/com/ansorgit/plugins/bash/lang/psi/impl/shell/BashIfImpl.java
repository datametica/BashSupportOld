package com.ansorgit.plugins.bash.lang.psi.impl.shell;

import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.ansorgit.plugins.bash.lang.parser.BashElementTypes;
import com.ansorgit.plugins.bash.lang.psi.api.shell.BashIf;
import com.ansorgit.plugins.bash.lang.psi.impl.BashKeywordDefaultImpl;
import com.intellij.psi.PsiElement;


















public class BashIfImpl
  extends BashKeywordDefaultImpl
  implements BashIf
{
  public BashIfImpl() {
    super(BashElementTypes.IF_COMMAND);
  }
  
  public PsiElement keywordElement() {
    return findPsiChildByType(BashTokenTypes.IF_KEYWORD);
  }

  
  public boolean isCommandGroup() {
    return false;
  }
}
