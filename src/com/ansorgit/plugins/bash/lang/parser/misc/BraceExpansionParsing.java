package com.ansorgit.plugins.bash.lang.parser.misc;

import com.ansorgit.plugins.bash.lang.parser.BashPsiBuilder;
import com.ansorgit.plugins.bash.lang.parser.Parsing;
import com.ansorgit.plugins.bash.lang.parser.ParsingFunction;
import com.ansorgit.plugins.bash.lang.parser.util.ParserUtil;
import com.ansorgit.plugins.bash.util.NullMarker;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;



















public class BraceExpansionParsing
  implements ParsingFunction
{
  private static final TokenSet validExpansionTokens = TokenSet.create(new IElementType[] { WORD, INTEGER_LITERAL });





  
  public boolean isValid(BashPsiBuilder builder) {
    if (!ParserUtil.containsTokenInLookahead((PsiBuilder)builder, LEFT_CURLY, 10, false)) {
      return false;
    }
    
    PsiBuilder.Marker start = builder.mark();
    
    boolean result = doParse(builder, true);
    
    start.rollbackTo();
    
    return result;
  }
  
  public boolean parse(BashPsiBuilder builder) {
    return doParse(builder, false);
  }
  
  private boolean doParse(BashPsiBuilder builder, boolean checkMode) {
    PsiBuilder.Marker marker = checkMode ? NullMarker.get() : builder.mark();

    
    while (true) {
      IElementType tokenType = builder.getTokenType(true);
      if (Parsing.word.isComposedString(tokenType)) {
        Parsing.word.parseComposedString(builder); continue;
      } 
      if (validExpansionTokens.contains(tokenType)) {
        builder.advanceLexer();
        
        continue;
      } 
      
      break;
    } 
    
    if (builder.getTokenType(true) != LEFT_CURLY) {
      marker.drop();
      return false;
    } 
    
    while (builder.getTokenType(true) == LEFT_CURLY) {
      builder.advanceLexer();
      
      boolean isExpansion = (builder.getTokenType(true) != WHITESPACE);
      if (!isExpansion) {
        marker.drop();
        return false;
      } 


      
      while (validExpansionTokens.contains(builder.getTokenType(true))) {
        builder.advanceLexer();
        if (builder.getTokenType(true) == COMMA) {
          builder.advanceLexer();
        }
      } 
      
      if (builder.getTokenType(true) != RIGHT_CURLY) {
        marker.drop();
        return false;
      } 

      
      builder.advanceLexer();

      
      while (builder.getTokenType(true) == WORD) {
        builder.advanceLexer();
      }
    } 
    
    marker.done(EXPANSION_ELEMENT);
    return true;
  }
}
