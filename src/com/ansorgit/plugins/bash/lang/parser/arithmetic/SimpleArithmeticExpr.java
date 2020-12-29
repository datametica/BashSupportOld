package com.ansorgit.plugins.bash.lang.parser.arithmetic;

import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.ansorgit.plugins.bash.lang.parser.BashPsiBuilder;
import com.ansorgit.plugins.bash.lang.parser.OptionalParseResult;
import com.ansorgit.plugins.bash.lang.parser.Parsing;
import com.ansorgit.plugins.bash.lang.parser.misc.ShellCommandParsing;
import com.ansorgit.plugins.bash.lang.parser.util.ParserUtil;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;




















class SimpleArithmeticExpr
  implements ArithmeticParsingFunction
{
  public boolean isValid(BashPsiBuilder builder) {
    IElementType tokenType = builder.getTokenType();
    return (tokenType == WORD || tokenType == ASSIGNMENT_WORD || arithLiterals
      .contains(tokenType) || arithmeticAdditionOps
      .contains(builder.getTokenType()) || Parsing.word
      .isWordToken(builder) || Parsing.word
      .isComposedString(tokenType) || ShellCommandParsing.arithmeticParser
      .isValid(builder));
  }
  public boolean parse(BashPsiBuilder builder) {
    boolean ok;
    PsiBuilder.Marker marker = builder.mark();

    
    if (arithmeticAdditionOps.contains(builder.getTokenType())) {
      builder.advanceLexer();
      ok = parse(builder);
    } else {
      OptionalParseResult varResult = Parsing.var.parseIfValid(builder);
      if (varResult.isValid()) {
        ok = varResult.isParsedSuccessfully();
      } else if (builder.getTokenType() == BashTokenTypes.ARITH_NUMBER && builder.rawLookup(1) == BashTokenTypes.ARITH_BASE_CHAR) {
        
        ParserUtil.getTokenAndAdvance(builder, false);
        ParserUtil.getTokenAndAdvance(builder, true);
        
        int startOffset = builder.getCurrentOffset();
        ok = true;
        while (true) {
          IElementType nextToken = builder.getTokenType(true);
          if (nextToken == BashTokenTypes.ARITH_NUMBER) {
            builder.advanceLexer(); continue;
          } 
          OptionalParseResult result = Parsing.word.parseWordIfValid(builder);
          if (result.isValid()) {
            ok = result.isParsedSuccessfully();
            if (!ok)
              break; 
            continue;
          } 
          varResult = Parsing.var.parseIfValid(builder);
          if (varResult.isValid()) {
            ok = varResult.isParsedSuccessfully();
            if (!ok) {
              break;
            }
            
            continue;
          } 
          
          break;
        } 
        
        ok = (ok && builder.getCurrentOffset() > startOffset);
      } else {
        
        IElementType tokenType = builder.getTokenType();
        do {
          if (tokenType == WORD) {
            
            ParserUtil.markTokenAndAdvance((PsiBuilder)builder, (IElementType)VAR_ELEMENT);
            ok = true;
          } else if (tokenType == ASSIGNMENT_WORD) {
            
            ParserUtil.markTokenAndAdvance((PsiBuilder)builder, (IElementType)VAR_ELEMENT);
            ok = ShellCommandParsing.arithmeticParser.parse(builder, LEFT_SQUARE, RIGHT_SQUARE);
          } else if (arithLiterals.contains(tokenType)) {
            builder.advanceLexer();
            ok = true;
          } else {
            varResult = Parsing.var.parseIfValid(builder);
            if (varResult.isValid()) {
              
              ok = varResult.isParsedSuccessfully();
            } else if (Parsing.word.isComposedString(tokenType)) {
              ok = Parsing.word.parseComposedString(builder);
            } else {
              OptionalParseResult result = Parsing.word.parseWordIfValid(builder);
              if (result.isValid()) {
                ok = result.isParsedSuccessfully();
              } else {
                result = ShellCommandParsing.arithmeticParser.parseIfValid(builder);
                if (result.isValid()) {
                  ok = result.isParsedSuccessfully();
                } else {
                  ok = false;
                  
                  break;
                } 
              } 
            } 
          } 
          
          tokenType = builder.getTokenType(true);
        } while (ok && isValidPart(builder, tokenType));
      } 
    } 


    
    if (ok) {
      marker.done(ARITH_SIMPLE_ELEMENT);
    } else {
      marker.drop();
    } 
    
    return ok;
  }

  
  private boolean isValidPart(BashPsiBuilder builder, IElementType tokenType) {
    return (tokenType == WORD || arithLiterals
      .contains(tokenType) || Parsing.word
      .isWordToken(builder) || Parsing.word
      .isComposedString(tokenType) || ShellCommandParsing.arithmeticParser
      .isValid(builder));
  }
}
