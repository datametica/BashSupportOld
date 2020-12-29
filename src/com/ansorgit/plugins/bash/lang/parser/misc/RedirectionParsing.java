package com.ansorgit.plugins.bash.lang.parser.misc;

import com.ansorgit.plugins.bash.lang.parser.BashPsiBuilder;
import com.ansorgit.plugins.bash.lang.parser.Parsing;
import com.ansorgit.plugins.bash.lang.parser.ParsingTool;
import com.ansorgit.plugins.bash.lang.parser.util.ParserUtil;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;


















public class RedirectionParsing
  implements ParsingTool
{
  private static final TokenSet validBeforeFiledescriptor = TokenSet.create(new IElementType[] { GREATER_THAN, LESS_THAN });




  
  public boolean isRedirect(BashPsiBuilder builder, boolean allowHeredocs) {
    if (builder.eof()) {
      return false;
    }
    
    if (isProcessSubstitution(builder)) {
      return false;
    }
    
    PsiBuilder.Marker marker = builder.mark();
    builder.enterNewErrorLevel(false);
    
    boolean result = (parseSingleRedirectIfValid(builder, allowHeredocs) == RedirectParseResult.OK);
    
    builder.leaveLastErrorLevel();
    
    marker.rollbackTo();
    return result;
  }
  
  public RedirectParseResult parseRequiredListIfValid(BashPsiBuilder builder, boolean allowHeredocs) {
    RedirectParseResult result = parseListIfValid(builder, allowHeredocs);
    if (result == RedirectParseResult.NO_REDIRECT) {
      builder.error("Missing redirect");
    }
    return result;
  }
  public RedirectParseResult parseListIfValid(BashPsiBuilder builder, boolean allowHeredocs) {
    RedirectParseResult result;
    int count = 0;
    PsiBuilder.Marker redirectList = builder.mark();

    
    do {
      result = parseSingleRedirectIfValid(builder, allowHeredocs);
      if (result == RedirectParseResult.NO_REDIRECT) {
        break;
      }
      if (result == RedirectParseResult.INVALID_REDIRECT) {
        builder.error("Invalid redirect");
        if (builder.getTokenType() != LINE_FEED) {
          builder.advanceLexer();
        }
      } else if (result == RedirectParseResult.PARSING_FAILED) {
        builder.error("Invalid redirect");
        if (builder.getTokenType() != LINE_FEED) {
          builder.advanceLexer();
        }
      } 
      count++;
    } while (result == RedirectParseResult.OK);
    
    if (count == 0) {
      redirectList.drop();
      return result;
    } 
    
    redirectList.done(REDIRECT_LIST_ELEMENT);

    
    return (result == RedirectParseResult.NO_REDIRECT) ? RedirectParseResult.OK : result;
  }
  public RedirectParseResult parseSingleRedirectIfValid(BashPsiBuilder builder, boolean allowHeredoc) {
    IElementType secondToken;
    if (isProcessSubstitution(builder)) {
      return RedirectParseResult.NO_REDIRECT;
    }
    
    PsiBuilder.Marker marker = builder.mark();
    
    IElementType firstToken = builder.getTokenType();
    boolean firstIsInt = (firstToken == INTEGER_LITERAL);


    
    if (firstIsInt) {
      builder.advanceLexer();
      secondToken = builder.rawLookup(0);
    } else {
      
      secondToken = firstToken;
    } 
    
    if (!redirectionSet.contains(secondToken)) {
      marker.rollbackTo();
      return RedirectParseResult.NO_REDIRECT;
    } 
    
    if (validBeforeFiledescriptor.contains(secondToken) && ParserUtil.hasNextTokens((PsiBuilder)builder, true, new IElementType[] { secondToken, FILEDESCRIPTOR })) {
      
      ParserUtil.getTokenAndAdvance(builder, true);
      
      PsiBuilder.Marker descriptorMarker = builder.mark();
      ParserUtil.getTokenAndAdvance(builder, true);
      descriptorMarker.done(FILEDESCRIPTOR);
      
      marker.done(REDIRECT_ELEMENT);
      return RedirectParseResult.OK;
    } 

    
    builder.advanceLexer();
    
    if (allowHeredoc && secondToken == HEREDOC_MARKER_TAG) {
      marker.drop();
      
      if (builder.getTokenType() == LINE_FEED) {
        
        builder.error("missing heredoc start tag");
        builder.advanceLexer();
        return RedirectParseResult.OK;
      } 
      
      if (builder.getTokenType() != HEREDOC_MARKER_START) {
        return RedirectParseResult.PARSING_FAILED;
      }
      
      ParserUtil.markTokenAndAdvance((PsiBuilder)builder, HEREDOC_START_ELEMENT);
      builder.getParsingState().pushHeredocMarker(builder.rawTokenIndex());
      return RedirectParseResult.OK;
    } 

    
    boolean ok = Parsing.word.parseWordIfValid(builder).isParsedSuccessfully();
    if (ok) {
      marker.done(REDIRECT_ELEMENT);
      return RedirectParseResult.OK;
    } 
    
    marker.drop();
    return RedirectParseResult.INVALID_REDIRECT;
  }


  
  private boolean isProcessSubstitution(BashPsiBuilder builder) {
    int i = 0;
    while (builder.rawLookup(i) == WHITESPACE) {
      i++;
    }
    
    IElementType lookAhead = builder.rawLookup(i);
    return ((lookAhead == LESS_THAN || lookAhead == GREATER_THAN) && builder.rawLookup(i + 1) == LEFT_PAREN);
  }
  
  public enum RedirectParseResult
  {
    NO_REDIRECT,
    INVALID_REDIRECT,
    PARSING_FAILED,
    OK;
  }
}
