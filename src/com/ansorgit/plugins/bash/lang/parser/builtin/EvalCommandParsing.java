package com.ansorgit.plugins.bash.lang.parser.builtin;

import com.ansorgit.plugins.bash.lang.parser.BashPsiBuilder;
import com.ansorgit.plugins.bash.lang.parser.OptionalParseResult;
import com.ansorgit.plugins.bash.lang.parser.Parsing;
import com.ansorgit.plugins.bash.lang.parser.ParsingFunction;
import com.ansorgit.plugins.bash.lang.parser.ParsingTool;
import com.ansorgit.plugins.bash.lang.parser.misc.RedirectionParsing;
import com.ansorgit.plugins.bash.lang.parser.misc.ShellCommandParsing;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
















class EvalCommandParsing
  implements ParsingFunction, ParsingTool
{
  private static final TokenSet accepted = TokenSet.create(new IElementType[] { STRING2, ASSIGNMENT_WORD, EQ, WORD, VARIABLE, DOLLAR, LEFT_CURLY, RIGHT_CURLY });


  
  public boolean isValid(BashPsiBuilder builder) {
    return (builder.getTokenType() == WORD && "eval".equals(builder.getTokenText()));
  }


  
  public boolean parse(BashPsiBuilder builder) {
    builder.advanceLexer();
    while (true) {
      boolean ok;
      int start = builder.rawTokenIndex();

      
      builder.getTokenType();
      
      do {
      
      } while (readEvaluatedBeforeCode(builder));




      
      builder.getTokenType();
      
      RedirectionParsing.RedirectParseResult result = Parsing.redirection.parseSingleRedirectIfValid(builder, false);
      if (result != RedirectionParsing.RedirectParseResult.NO_REDIRECT) {
        
        if (result != RedirectionParsing.RedirectParseResult.OK) {
          break;
        }
        
        continue;
      } 
      
      PsiBuilder.Marker evalMarker = builder.mark();

      
      if (Parsing.word.isComposedString(builder.getTokenType(true))) {
        ok = Parsing.word.parseComposedString(builder);
      } else if (accepted.contains(builder.getTokenType(true))) {
        while (accepted.contains(builder.getTokenType(true))) {
          builder.advanceLexer();
        }
        
        ok = true;
      } else {
        ok = false;
      } 
      
      if (ok && builder.rawTokenIndex() > start) {
        evalMarker.collapse(EVAL_BLOCK); continue;
      } 
      evalMarker.drop();
      
      break;
    } 
    
    return true;
  }
  
  private boolean readEvaluatedBeforeCode(BashPsiBuilder builder) {
    OptionalParseResult result = Parsing.shellCommand.subshellParser.parseIfValid(builder);
    if (result.isValid()) {
      return result.isParsedSuccessfully();
    }
    
    result = Parsing.shellCommand.backtickParser.parseIfValid(builder);
    if (result.isValid()) {
      return result.isParsedSuccessfully();
    }
    
    result = ShellCommandParsing.arithmeticParser.parseIfValid(builder);
    if (result.isValid()) {
      return result.isParsedSuccessfully();
    }
    
    result = Parsing.shellCommand.conditionalCommandParser.parseIfValid(builder);
    if (result.isValid()) {
      return result.isParsedSuccessfully();
    }
    
    return false;
  }
}
