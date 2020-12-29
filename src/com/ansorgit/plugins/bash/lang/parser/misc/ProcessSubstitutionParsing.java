package com.ansorgit.plugins.bash.lang.parser.misc;

import com.ansorgit.plugins.bash.lang.parser.BashPsiBuilder;
import com.ansorgit.plugins.bash.lang.parser.Parsing;
import com.ansorgit.plugins.bash.lang.parser.ParsingFunction;
import com.ansorgit.plugins.bash.lang.parser.util.ParserUtil;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
























public class ProcessSubstitutionParsing
  implements ParsingFunction
{
  public boolean isValid(BashPsiBuilder builder) {
    IElementType first = builder.rawLookup(0);
    IElementType second = builder.rawLookup(1);
    
    return ((first == LESS_THAN || first == GREATER_THAN) && second == LEFT_PAREN);
  }
  
  public boolean parse(BashPsiBuilder builder) {
    PsiBuilder.Marker marker = builder.mark();
    
    builder.getTokenType();
    builder.advanceLexer();
    
    IElementType second = builder.getTokenType(true);
    builder.advanceLexer();
    
    if (second != LEFT_PAREN) {
      marker.drop();
      return false;
    } 
    
    boolean ok = Parsing.list.parseCompoundList(builder, true, false);
    
    if (!ok) {
      marker.drop();
      
      return false;
    } 

    
    if (!ParserUtil.conditionalRead((PsiBuilder)builder, RIGHT_PAREN)) {
      marker.drop();
      return false;
    } 
    
    marker.done(PROCESS_SUBSTITUTION_ELEMENT);
    return true;
  }
}
