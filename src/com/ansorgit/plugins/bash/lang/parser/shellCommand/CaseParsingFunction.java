package com.ansorgit.plugins.bash.lang.parser.shellCommand;

import com.ansorgit.plugins.bash.lang.parser.BashPsiBuilder;
import com.ansorgit.plugins.bash.lang.parser.Parsing;
import com.ansorgit.plugins.bash.lang.parser.ParsingFunction;
import com.ansorgit.plugins.bash.lang.parser.util.ParserUtil;
import com.intellij.lang.PsiBuilder;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import java.util.Collections;





















public class CaseParsingFunction
  implements ParsingFunction
{
  private static final Logger log = Logger.getInstance("#bash.CaseCommandParsingFunction");
  
  public boolean isValid(BashPsiBuilder builder) {
    return (builder.getTokenType() == CASE_KEYWORD);
  }






































  
  public boolean parse(BashPsiBuilder builder) {
    PsiBuilder.Marker caseCommand = builder.mark();
    
    builder.advanceLexer();
    
    if (!Parsing.word.parseWordIfValid(builder, false, TokenSet.EMPTY, TokenSet.EMPTY, Collections.singleton("in")).isParsedSuccessfully()) {
      caseCommand.drop();
      ParserUtil.error((PsiBuilder)builder, "parser.unexpected.token");
      return false;
    } 

    
    builder.readOptionalNewlines();
    
    boolean ok = ParserUtil.smartRemapAndAdvance((PsiBuilder)builder, "in", WORD, IN_KEYWORD_REMAPPED);
    if (!ok) {
      caseCommand.drop();
      ParserUtil.error((PsiBuilder)builder, "parser.unexpected.token");
      return false;
    } 
    
    builder.readOptionalNewlines();
    if (builder.getTokenType() == ESAC_KEYWORD) {
      builder.advanceLexer();
      caseCommand.done(CASE_COMMAND);
      
      return true;
    } 

    
    CaseParseResult hasPattern = parsePatternList(builder);
    if (hasPattern == CaseParseResult.Faulty) {
      log.debug("Could not find first case pattern");
      caseCommand.drop();
      ParserUtil.error((PsiBuilder)builder, "parser.unexpected.token");
      return false;
    } 

    
    while (hasPattern != CaseParseResult.Faulty) {
      
      if (hasPattern == CaseParseResult.ElementWithEndMarker) {
        builder.readOptionalNewlines();
        if (builder.getTokenType() == ESAC_KEYWORD) {
          break;
        }
        
        hasPattern = parsePatternList(builder);
      } 
    } 




    
    IElementType endToken = ParserUtil.getTokenAndAdvance((PsiBuilder)builder);
    if (endToken != ESAC_KEYWORD) {
      caseCommand.drop();
      
      builder.error("Unexpected token");
      return false;
    } 
    
    caseCommand.done(CASE_COMMAND);
    return true;
  }














  
  CaseParseResult parsePatternList(BashPsiBuilder builder) {
    builder.readOptionalNewlines();
    
    PsiBuilder.Marker casePattern = builder.mark();
    
    if (builder.getTokenType() == LEFT_PAREN) {
      builder.advanceLexer();
    }

    
    if (!readCasePattern(builder)) {
      ParserUtil.error((PsiBuilder)builder, "parser.unexpected.token");
      casePattern.drop();
      return CaseParseResult.Faulty;
    } 

    
    IElementType closingBracket = ParserUtil.getTokenAndAdvance((PsiBuilder)builder);
    if (closingBracket != RIGHT_PAREN) {
      ParserUtil.error((PsiBuilder)builder, "parser.unexpected.token");
      casePattern.drop();
      return CaseParseResult.Faulty;
    } 

    
    builder.readOptionalNewlines();

    
    if (builder.getTokenType() != CASE_END && builder.getTokenType() != ESAC_KEYWORD) {
      boolean parsed = Parsing.list.parseCompoundList(builder, true, true);
      if (!parsed) {
        
        casePattern.drop();
        return CaseParseResult.Faulty;
      } 
    } 
    
    boolean hasEndMarker = (builder.getTokenType() == CASE_END);
    if (hasEndMarker) {
      builder.advanceLexer();
      
      IElementType nextToken = builder.rawLookup(0);
      if (builder.isBash4() && nextToken == AMP) {
        builder.advanceLexer();
      }
    } 
    
    casePattern.done(CASE_PATTERN_LIST_ELEMENT);
    return hasEndMarker ? CaseParseResult.ElementWithEndMarker : CaseParseResult.SingleElement;
  }







  
  boolean readCasePattern(BashPsiBuilder builder) {
    PsiBuilder.Marker pattern = builder.mark();
    
    boolean wordParsed = Parsing.word.parseWordIfValid(builder).isParsedSuccessfully();
    if (!wordParsed) {
      pattern.drop();
      return false;
    } 

    
    while (builder.getTokenType() == PIPE) {
      builder.advanceLexer();
      
      boolean myWordParsed = Parsing.word.parseWordIfValid(builder).isParsedSuccessfully();
      if (!myWordParsed) {
        pattern.drop();
        return false;
      } 
    } 
    
    pattern.done(CASE_PATTERN_ELEMENT);
    return true;
  }
  
  private enum CaseParseResult {
    Faulty, SingleElement, ElementWithEndMarker;
  }
}
