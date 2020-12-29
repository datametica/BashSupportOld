package com.ansorgit.plugins.bash.lang.psi.impl.shell;

import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.ansorgit.plugins.bash.lang.parser.BashElementTypes;
import com.ansorgit.plugins.bash.lang.psi.api.shell.BashTimeCommand;
import com.ansorgit.plugins.bash.lang.psi.impl.BashKeywordDefaultImpl;
import com.intellij.psi.PsiElement;
















public class BashTimeCommandImpl
  extends BashKeywordDefaultImpl
  implements BashTimeCommand
{
  public BashTimeCommandImpl() {
    super(BashElementTypes.TIME_COMMAND);
  }
  
  public PsiElement keywordElement() {
    return findPsiChildByType(BashTokenTypes.TIME_KEYWORD);
  }
}
