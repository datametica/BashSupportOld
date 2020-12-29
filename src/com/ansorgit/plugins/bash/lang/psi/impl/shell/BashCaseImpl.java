package com.ansorgit.plugins.bash.lang.psi.impl.shell;

import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.ansorgit.plugins.bash.lang.parser.BashElementTypes;
import com.ansorgit.plugins.bash.lang.psi.api.shell.BashCase;
import com.ansorgit.plugins.bash.lang.psi.impl.BashKeywordDefaultImpl;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import java.util.Collection;



















public class BashCaseImpl
  extends BashKeywordDefaultImpl
  implements BashCase
{
  public BashCaseImpl() {
    super(BashElementTypes.CASE_COMMAND);
  }
  
  public PsiElement keywordElement() {
    return findPsiChildByType(BashTokenTypes.CASE_KEYWORD);
  }
  
  public Collection<? extends PsiElement> patternList() {
    return PsiTreeUtil.findChildrenOfType((PsiElement)this, BashCasePatternListElementImpl.class);
  }

  
  public boolean isCommandGroup() {
    return false;
  }
}
