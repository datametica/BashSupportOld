package com.ansorgit.plugins.bash.lang.psi.impl.loops;

import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.ansorgit.plugins.bash.lang.parser.BashElementTypes;
import com.ansorgit.plugins.bash.lang.psi.api.loops.BashWhile;
import com.ansorgit.plugins.bash.lang.psi.impl.BashKeywordDefaultImpl;
import com.intellij.psi.PsiElement;
















public class BashWhileImpl
  extends BashKeywordDefaultImpl
  implements BashWhile
{
  public BashWhileImpl() {
    super(BashElementTypes.WHILE_COMMAND);
  }
  
  public PsiElement keywordElement() {
    return findPsiChildByType(BashTokenTypes.WHILE_KEYWORD);
  }

  
  public boolean isCommandGroup() {
    return false;
  }
}
