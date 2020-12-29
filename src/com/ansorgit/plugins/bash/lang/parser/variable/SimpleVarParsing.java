package com.ansorgit.plugins.bash.lang.parser.variable;

import com.ansorgit.plugins.bash.lang.parser.BashPsiBuilder;
import com.ansorgit.plugins.bash.lang.parser.ParsingFunction;
import com.ansorgit.plugins.bash.lang.parser.util.ParserUtil;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;















public class SimpleVarParsing
  implements ParsingFunction
{
  public boolean isValid(BashPsiBuilder builder) {
    return (builder.getTokenType(true) == VARIABLE);
  }
  
  public boolean parse(BashPsiBuilder builder) {
    ParserUtil.markTokenAndAdvance((PsiBuilder)builder, (IElementType)VAR_ELEMENT);
    return true;
  }
}
