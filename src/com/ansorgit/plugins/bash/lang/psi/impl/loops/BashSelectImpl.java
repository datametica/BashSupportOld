package com.ansorgit.plugins.bash.lang.psi.impl.loops;

import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.ansorgit.plugins.bash.lang.parser.BashElementTypes;
import com.ansorgit.plugins.bash.lang.psi.api.loops.BashSelect;
import com.ansorgit.plugins.bash.lang.psi.impl.BashKeywordDefaultImpl;
import com.intellij.psi.PsiElement;
















public class BashSelectImpl
  extends BashKeywordDefaultImpl
  implements BashSelect
{
  public BashSelectImpl() {
    super(BashElementTypes.SELECT_COMMAND);
  }
  
  public PsiElement keywordElement() {
    return findPsiChildByType(BashTokenTypes.SELECT_KEYWORD);
  }

  
  public boolean isCommandGroup() {
    return false;
  }
}
