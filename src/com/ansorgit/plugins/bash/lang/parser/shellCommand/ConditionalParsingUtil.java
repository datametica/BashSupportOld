package com.ansorgit.plugins.bash.lang.parser.shellCommand;

import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.ansorgit.plugins.bash.lang.parser.BashPsiBuilder;
import com.ansorgit.plugins.bash.lang.parser.OptionalParseResult;
import com.ansorgit.plugins.bash.lang.parser.Parsing;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;


















public final class ConditionalParsingUtil
{
  private static final TokenSet operators = TokenSet.create(new IElementType[] { BashTokenTypes.COND_OP, BashTokenTypes.COND_OP_EQ_EQ, BashTokenTypes.COND_OP_REGEX });
  private static final TokenSet regExpEndTokens = TokenSet.create(new IElementType[] { BashTokenTypes.WHITESPACE, BashTokenTypes._BRACKET_KEYWORD });





  
  public static boolean readTestExpression(BashPsiBuilder builder, TokenSet endTokens) {
    boolean ok = true;
    
    while (ok && !endTokens.contains(builder.getTokenType())) {
      OptionalParseResult result = Parsing.word.parseWordIfValid(builder);
      if (result.isValid()) {
        ok = result.isParsedSuccessfully(); continue;
      }  if (builder.getTokenType() == BashTokenTypes.COND_OP_NOT) {
        builder.advanceLexer();
        ok = readTestExpression(builder, endTokens); continue;
      }  if (builder.getTokenType() == BashTokenTypes.COND_OP_REGEX) {
        builder.advanceLexer();

        
        ok = parseRegularExpression(builder, endTokens); continue;
      }  if (operators.contains(builder.getTokenType())) {
        builder.advanceLexer(); continue;
      } 
      ok = false;
    } 


    
    return ok;
  }
  
  public static boolean parseRegularExpression(BashPsiBuilder builder, TokenSet endMarkerTokens) {
    int count = 0;

    
    while (!builder.eof()) {
      IElementType current = builder.getTokenType(true);
      if (count == 0 && current == BashTokenTypes.WHITESPACE) {
        builder.advanceLexer();
        
        continue;
      } 
      if (endMarkerTokens.contains(current)) {
        break;
      }
      
      if (Parsing.word.isComposedString(current)) {
        if (!Parsing.word.parseComposedString(builder)) {
          break;
        }
      } else if (!regExpEndTokens.contains(current)) {
        builder.advanceLexer();
      } else {
        break;
      } 
      
      count++;
    } 
    
    return (count > 0);
  }
}
