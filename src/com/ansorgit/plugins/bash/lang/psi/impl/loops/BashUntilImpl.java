package com.ansorgit.plugins.bash.lang.psi.impl.loops;

import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.ansorgit.plugins.bash.lang.parser.BashElementTypes;
import com.ansorgit.plugins.bash.lang.psi.api.loops.BashUntil;
import com.ansorgit.plugins.bash.lang.psi.impl.BashKeywordDefaultImpl;
import com.intellij.psi.PsiElement;
















public class BashUntilImpl
  extends BashKeywordDefaultImpl
  implements BashUntil
{
  public BashUntilImpl() {
    super(BashElementTypes.UNTIL_COMMAND);
  }
  
  public PsiElement keywordElement() {
    return findPsiChildByType(BashTokenTypes.UNTIL_KEYWORD);
  }

  
  public boolean isCommandGroup() {
    return false;
  }
}
