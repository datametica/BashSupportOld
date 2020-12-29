package com.ansorgit.plugins.bash.lang.parser.shellCommand;

import com.ansorgit.plugins.bash.lang.parser.BashPsiBuilder;
import com.ansorgit.plugins.bash.lang.parser.Parsing;
import com.ansorgit.plugins.bash.lang.parser.ParsingFunction;
import com.ansorgit.plugins.bash.lang.parser.arithmetic.ArithmeticFactory;
import com.ansorgit.plugins.bash.lang.parser.misc.ShellCommandParsing;
import com.ansorgit.plugins.bash.lang.parser.util.ParserUtil;
import com.intellij.lang.PsiBuilder;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.tree.IElementType;




















public class ForLoopParsingFunction
  implements ParsingFunction
{
  private static final Logger log = Logger.getInstance("#bash.ForLoopParsingFunction");
  private static final IElementType[] ARITH_FOR_LOOP_START = new IElementType[] { FOR_KEYWORD, EXPR_ARITH };
  
  public boolean isValid(BashPsiBuilder builder) {
    return (builder.getTokenType() == FOR_KEYWORD);
  }
  
  private boolean isArithmeticForLoop(PsiBuilder builder) {
    return ParserUtil.hasNextTokens(builder, false, ARITH_FOR_LOOP_START);
  }













  
  public boolean parse(BashPsiBuilder builder) {
    return isArithmeticForLoop((PsiBuilder)builder) ? 
      parseArithmeticForLoop(builder) : 
      parseForLoop(builder);
  }
  
  private boolean parseForLoop(BashPsiBuilder builder) {
    PsiBuilder.Marker forLoop = builder.mark();
    builder.advanceLexer();

    
    if (ParserUtil.isIdentifier(builder.getTokenType())) {
      
      ParserUtil.remapMarkAdvance((PsiBuilder)builder, WORD, (IElementType)VAR_DEF_ELEMENT);
    } else {
      forLoop.drop();
      ParserUtil.error((PsiBuilder)builder, "parser.shell.for.expectedWord");
      return false;
    } 
    
    builder.readOptionalNewlines();

    
    IElementType afterLoopValue = builder.getTokenType();
    if (afterLoopValue == SEMI) {
      builder.advanceLexer();
      builder.readOptionalNewlines();
    } else if ((afterLoopValue == WORD || afterLoopValue == IN_KEYWORD_REMAPPED) && "in".equals(builder.getTokenText())) {
      builder.remapCurrentToken(IN_KEYWORD_REMAPPED);
      builder.advanceLexer();

      
      if (builder.getTokenType() == SEMI) {
        builder.advanceLexer();
      } else if (!Parsing.word.parseWordListIfValid(builder, true, false).isParsedSuccessfully()) {
        forLoop.drop();
        return false;
      } 
      
      builder.readOptionalNewlines();
    } 
    
    if (!LoopParserUtil.parseLoopBody(builder, true, false)) {
      forLoop.drop();
      return false;
    } 
    
    forLoop.done(ShellCommandParsing.FOR_COMMAND);
    return true;
  }















  
  boolean parseArithmeticForLoop(BashPsiBuilder builder) {
    PsiBuilder.Marker marker = builder.mark();
    builder.advanceLexer();


    
    if (builder.getTokenType() != EXPR_ARITH) {
      ParserUtil.error(marker, "parser.unexpected.token");
      return false;
    } 
    
    builder.advanceLexer();
    
    if (!parseArithmeticExpression(builder, SEMI)) {
      ParserUtil.error(marker, "parser.unexpected.token");
      return false;
    } 
    
    if (!parseArithmeticExpression(builder, SEMI)) {
      ParserUtil.error(marker, "parser.unexpected.token");
      return false;
    } 
    
    if (!parseArithmeticExpression(builder, _EXPR_ARITH)) {
      ParserUtil.error(marker, "parser.unexpected.token");
      return false;
    } 
    
    if (Parsing.list.isListTerminator(builder.getTokenType())) {
      builder.advanceLexer();
      builder.readOptionalNewlines();
    } 
    
    if (!LoopParserUtil.parseLoopBody(builder, true, false)) {
      marker.drop();
      return false;
    } 
    
    marker.done(ShellCommandParsing.FOR_COMMAND);
    return true;
  }








  
  private boolean parseArithmeticExpression(BashPsiBuilder builder, IElementType endToken) {
    if (builder.getTokenType() == endToken) {
      builder.advanceLexer();
      return true;
    } 
    
    while (builder.getTokenType() != endToken && !builder.eof()) {
      if (!ArithmeticFactory.entryPoint().parse(builder)) {
        return false;
      }
    } 
    
    IElementType foundEndToken = ParserUtil.getTokenAndAdvance((PsiBuilder)builder);
    return (!builder.eof() && foundEndToken == endToken);
  }
}
