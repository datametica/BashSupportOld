package com.ansorgit.plugins.bash.lang.parser.arithmetic;

import com.ansorgit.plugins.bash.lang.parser.BashPsiBuilder;
import com.ansorgit.plugins.bash.lang.parser.util.ParserUtil;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
















class AbstractAssignment
  implements ArithmeticParsingFunction
{
  private static final TokenSet acceptedWords = TokenSet.create(new IElementType[] { WORD, ASSIGNMENT_WORD });
  
  private final ArithmeticParsingFunction next;
  private final TokenSet acceptedEqualTokens;
  
  public AbstractAssignment(ArithmeticParsingFunction next, TokenSet acceptedEqualTokens) {
    this.next = next;
    this.acceptedEqualTokens = acceptedEqualTokens;
  }
  
  public boolean isValid(BashPsiBuilder builder) {
    return (acceptedWords.contains(builder.getTokenType()) || this.next.isValid(builder));
  }
  
  public boolean parse(BashPsiBuilder builder) {
    PsiBuilder.Marker marker = builder.mark();
    
    boolean ok = ParserUtil.conditionalRead((PsiBuilder)builder, acceptedWords);
    
    if (ok && this.acceptedEqualTokens.contains(builder.getTokenType())) {
      marker.done((IElementType)VAR_DEF_ELEMENT);
      builder.advanceLexer();
    } else {
      marker.rollbackTo();
    } 
    
    return this.next.parse(builder);
  }
}
