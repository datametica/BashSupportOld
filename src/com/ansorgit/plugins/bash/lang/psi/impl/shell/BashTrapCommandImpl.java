package com.ansorgit.plugins.bash.lang.psi.impl.shell;

import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.ansorgit.plugins.bash.lang.psi.api.shell.BashTrapCommand;
import com.ansorgit.plugins.bash.lang.psi.impl.BashKeywordDefaultImpl;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import com.intellij.psi.PsiElement;
import java.util.List;


















public class BashTrapCommandImpl
  extends BashKeywordDefaultImpl
  implements BashTrapCommand
{
  public BashTrapCommandImpl() {
    super(BashTokenTypes.TRAP_KEYWORD);
  }
  
  public PsiElement keywordElement() {
    return findPsiChildByType(BashTokenTypes.TRAP_KEYWORD);
  }

  
  public List<PsiElement> getSignalSpec() {
    return null;
  }


  
  public PsiElement getSignalHandlerElement() {
    PsiElement firstParam = BashPsiUtils.findNextSibling(getFirstChild(), BashTokenTypes.WHITESPACE);
    if (firstParam == null) {
      return null;
    }
    
    String text = firstParam.getText();
    if (text.startsWith("-p") || text.startsWith("-l")) {
      return null;
    }


    
    PsiElement child = firstParam;
    while (child.getTextRange().equals(firstParam.getTextRange())) {
      PsiElement firstChild = child.getFirstChild();
      if (firstChild == null || firstChild instanceof com.intellij.psi.impl.source.tree.LeafPsiElement) {
        break;
      }
      
      child = child.getFirstChild();
    } 
    
    return child;
  }
}
