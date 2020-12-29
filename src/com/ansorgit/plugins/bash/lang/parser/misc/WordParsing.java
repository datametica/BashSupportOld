package com.ansorgit.plugins.bash.lang.parser.misc;

import com.ansorgit.plugins.bash.lang.parser.BashPsiBuilder;
import com.ansorgit.plugins.bash.lang.parser.OptionalParseResult;
import com.ansorgit.plugins.bash.lang.parser.Parsing;
import com.ansorgit.plugins.bash.lang.parser.ParsingTool;
import com.ansorgit.plugins.bash.lang.parser.util.ParserUtil;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import java.util.Set;
import org.jetbrains.annotations.Nullable;





















public class WordParsing
  implements ParsingTool
{
  private static final TokenSet singleDollarFollowups = TokenSet.create(new IElementType[] { STRING_END, WHITESPACE, LINE_FEED, SEMI });

  
  public boolean isWordToken(BashPsiBuilder builder) {
    if (builder.eof()) {
      return false;
    }
    
    PsiBuilder.Marker mark = builder.mark();
    boolean valid = parseWordIfValid(builder).isValid();
    mark.rollbackTo();
    return valid;
  }
  
  public boolean isComposedString(IElementType tokenType) {
    return (tokenType == STRING_BEGIN);
  }







  
  public boolean isSimpleComposedString(BashPsiBuilder builder, boolean allowWhitespace) {
    if (builder.getTokenType() != STRING_BEGIN) {
      return false;
    }
    
    TokenSet accepted = TokenSet.create(new IElementType[] { STRING_CONTENT });
    if (allowWhitespace) {
      accepted = TokenSet.orSet(new TokenSet[] { accepted, TokenSet.create(new IElementType[] { WHITESPACE }) });
    }
    
    int lookahead = 1;
    while (accepted.contains(builder.rawLookup(lookahead))) {
      lookahead++;
    }
    
    if (builder.rawLookup(lookahead) != STRING_END) {
      return false;
    }
    
    IElementType end = builder.rawLookup(lookahead + 1);
    return (end == null || end == WHITESPACE || end == LINE_FEED);
  }
  
  public OptionalParseResult parseWordIfValid(BashPsiBuilder builder) {
    return parseWordIfValid(builder, false);
  }
  
  public OptionalParseResult parseWordIfValid(BashPsiBuilder builder, boolean enableRemapping) {
    return parseWordIfValid(builder, enableRemapping, TokenSet.EMPTY, TokenSet.EMPTY, (Set<String>)null);
  }















  
  public OptionalParseResult parseWordIfValid(BashPsiBuilder builder, boolean enableRemapping, TokenSet reject, TokenSet accept, @Nullable Set<String> rejectTexts) {
    if (builder.eof()) {
      return OptionalParseResult.Invalid;
    }
    
    int processedTokens = 0;
    int parsedStringParts = 0;
    boolean firstStep = true;

    
    boolean isOk = true;
    
    PsiBuilder.Marker marker = builder.mark();
    
    while (isOk) {
      IElementType rawCurrentToken = builder.rawLookup(0);
      if (rawCurrentToken == null) {
        break;
      }
      
      if (!firstStep && (rawCurrentToken == WHITESPACE || reject.contains(rawCurrentToken))) {
        break;
      }
      
      if (rejectTexts != null && rejectTexts.contains(builder.getTokenText(true))) {
        break;
      }
      
      IElementType nextToken = enableRemapping ? builder.getRemappingTokenType() : builder.getTokenType();
      
      OptionalParseResult result = Parsing.braceExpansionParsing.parseIfValid(builder);
      if (result.isValid()) {
        isOk = result.isParsedSuccessfully();
        processedTokens++;
      } else if (nextToken == STRING_BEGIN) {
        isOk = parseComposedString(builder);
        parsedStringParts++;
      } else if (accept.contains(nextToken) || stringLiterals.contains(nextToken)) {
        builder.advanceLexer();
        processedTokens++;
      } else {
        result = Parsing.var.parseIfValid(builder);
        if (result.isValid()) {
          isOk = result.isParsedSuccessfully();
          processedTokens++;
        } else if (Parsing.shellCommand.backtickParser.isValid(builder)) {
          isOk = Parsing.shellCommand.backtickParser.parse(builder);
          processedTokens++;
        } else if (Parsing.shellCommand.conditionalExpressionParser.isValid(builder)) {
          isOk = Parsing.shellCommand.conditionalExpressionParser.parse(builder);
          processedTokens++;
        } else if (Parsing.shellCommand.historyExpansionParser.isValid(builder)) {
          isOk = Parsing.shellCommand.historyExpansionParser.parse(builder);
          processedTokens++;
        } else if (Parsing.processSubstitutionParsing.isValid(builder)) {
          isOk = Parsing.processSubstitutionParsing.parse(builder);
          processedTokens++;
        } else if (nextToken == LEFT_CURLY || (!firstStep && nextToken == RIGHT_CURLY)) {

          
          builder.advanceLexer();
          processedTokens++;
        } else if (nextToken == DOLLAR) {
          IElementType followup = builder.rawLookup(1);
          if (followup == null || singleDollarFollowups.contains(followup)) {
            builder.advanceLexer();
            processedTokens++;
          } else {
            break;
          } 
        } else if (!firstStep && nextToken == EQ) {
          builder.advanceLexer();
          processedTokens++;
        } else if (ParserUtil.isWord(builder, "!")) {
          IElementType followup = builder.rawLookup(1);
          if (followup == null || ParserUtil.isWhitespaceOrLineFeed(followup)) {
            
            builder.advanceLexer();
            processedTokens++;
          } else {
            break;
          } 
        } else {
          break;
        } 
      } 
      
      firstStep = false;
    } 

    
    if (processedTokens == 0 && parsedStringParts == 0) {
      marker.drop();
      return OptionalParseResult.Invalid;
    } 
    
    if (!isOk) {
      marker.drop();
      return OptionalParseResult.ParseError;
    } 

    
    if (parsedStringParts >= 1 && processedTokens == 0) {
      marker.drop();
    } else {
      marker.done(PARSED_WORD_ELEMENT);
    } 
    
    return OptionalParseResult.Ok;
  }
  
  public boolean parseComposedString(BashPsiBuilder builder) {
    PsiBuilder.Marker stringStart = builder.mark();

    
    builder.advanceLexer();
    
    while (builder.getTokenType() != STRING_END) {
      boolean ok = false;
      
      if (builder.getTokenType() == STRING_CONTENT) {
        builder.advanceLexer();
        ok = true;
      } else {
        OptionalParseResult varResult = Parsing.var.parseIfValid(builder);
        if (varResult.isValid()) {
          ok = varResult.isParsedSuccessfully();
        } else if (Parsing.shellCommand.backtickParser.isValid(builder)) {
          ok = Parsing.shellCommand.backtickParser.parse(builder);
        } 
      } 
      
      if (!ok) {
        stringStart.drop();
        return false;
      } 
    } 
    
    IElementType end = ParserUtil.getTokenAndAdvance((PsiBuilder)builder);
    if (end != STRING_END) {
      stringStart.error("String end marker not found");
      return false;
    } 
    
    stringStart.done(STRING_ELEMENT);
    return true;
  }
  
  public OptionalParseResult parseWordListIfValid(BashPsiBuilder builder, boolean readListTerminator, boolean enableRemapping) {
    if (builder.eof()) {
      return OptionalParseResult.Invalid;
    }
    
    while (!builder.eof()) {
      OptionalParseResult result = parseWordIfValid(builder, enableRemapping);
      if (result == OptionalParseResult.Invalid) {
        break;
      }
      if (!result.isParsedSuccessfully()) {
        return OptionalParseResult.ParseError;
      }
    } 
    
    if (readListTerminator) {
      if (!Parsing.list.isListTerminator(builder.getTokenType())) {
        return OptionalParseResult.ParseError;
      }
      builder.advanceLexer();
    } 
    
    return OptionalParseResult.Ok;
  }
}
