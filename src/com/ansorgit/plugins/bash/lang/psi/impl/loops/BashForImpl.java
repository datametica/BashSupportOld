package com.ansorgit.plugins.bash.lang.psi.impl.loops;

import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.ansorgit.plugins.bash.lang.parser.BashElementTypes;
import com.ansorgit.plugins.bash.lang.psi.api.loops.BashFor;
import com.ansorgit.plugins.bash.lang.psi.impl.BashKeywordDefaultImpl;
import com.intellij.psi.PsiElement;
















public class BashForImpl
  extends BashKeywordDefaultImpl
  implements BashFor
{
  public BashForImpl() {
    super(BashElementTypes.FOR_COMMAND);
  }
  
  public PsiElement keywordElement() {
    return findPsiChildByType(BashTokenTypes.FOR_KEYWORD);
  }
  
  public boolean isArithmetic() {
    return (findPsiChildByType(BashElementTypes.ARITHMETIC_COMMAND) != null);
  }

  
  public boolean isCommandGroup() {
    return false;
  }
}
