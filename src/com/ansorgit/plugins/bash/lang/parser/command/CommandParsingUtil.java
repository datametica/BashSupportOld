package com.ansorgit.plugins.bash.lang.parser.command;

import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.ansorgit.plugins.bash.lang.parser.BashElementTypes;
import com.ansorgit.plugins.bash.lang.parser.BashPsiBuilder;
import com.ansorgit.plugins.bash.lang.parser.OptionalParseResult;
import com.ansorgit.plugins.bash.lang.parser.Parsing;
import com.ansorgit.plugins.bash.lang.parser.misc.RedirectionParsing;
import com.ansorgit.plugins.bash.lang.parser.misc.ShellCommandParsing;
import com.ansorgit.plugins.bash.lang.parser.util.ParserUtil;
import com.ansorgit.plugins.bash.util.NullMarker;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;




















public class CommandParsingUtil
  implements BashTokenTypes, BashElementTypes
{
  private static final TokenSet assignmentSeparators = TokenSet.create(new IElementType[] { LINE_FEED, SEMI, WHITESPACE });
  private static final TokenSet validWordTokens = TokenSet.create(new IElementType[] { ARITH_NUMBER });



  
  public static boolean readCommandParams(BashPsiBuilder builder) {
    return readCommandParams(builder, TokenSet.EMPTY);
  }







  
  public static boolean readCommandParams(BashPsiBuilder builder, TokenSet validExtraTokens) {
    boolean ok = true;
    
    while (!builder.eof() && ok) {
      RedirectionParsing.RedirectParseResult result = Parsing.redirection.parseListIfValid(builder, true);
      if (result != RedirectionParsing.RedirectParseResult.NO_REDIRECT) {
        ok = (result != RedirectionParsing.RedirectParseResult.PARSING_FAILED); continue;
      } 
      OptionalParseResult parseResult = Parsing.word.parseWordIfValid(builder, true);
      if (parseResult.isValid()) {
        ok = parseResult.isParsedSuccessfully(); continue;
      }  if (validExtraTokens.contains(builder.getTokenType())) {
        builder.advanceLexer();
        ok = true;
      } 
    } 



    
    return ok;
  }
  
  public static boolean readOptionalAssignmentOrRedirects(BashPsiBuilder builder, Mode asssignmentMode, boolean markAsVarDef, boolean acceptArrayVars) {
    boolean ok = true;
    while (ok) {
      OptionalParseResult result = readAssignmentsAndRedirectsIfValid(builder, markAsVarDef, asssignmentMode, acceptArrayVars);
      if (!result.isValid()) {
        break;
      }
      ok = result.isParsedSuccessfully();
    } 
    return ok;
  }









  
  public static OptionalParseResult readAssignmentsAndRedirectsIfValid(BashPsiBuilder builder, boolean markAsVarDef, Mode mode, boolean acceptArrayVars) {
    boolean ok = false;
    int count = 0;
    
    do {
      OptionalParseResult parseResult = readAssignmentIfValid(builder, mode, markAsVarDef, acceptArrayVars);
      if (parseResult.isValid()) {
        ok = parseResult.isParsedSuccessfully();
      } else {
        RedirectionParsing.RedirectParseResult result = Parsing.redirection.parseSingleRedirectIfValid(builder, true);
        if (result != RedirectionParsing.RedirectParseResult.NO_REDIRECT) {
          if (result == RedirectionParsing.RedirectParseResult.INVALID_REDIRECT) {
            builder.error("Invalid redirect");
          }
          ok = (result == RedirectionParsing.RedirectParseResult.OK || result == RedirectionParsing.RedirectParseResult.INVALID_REDIRECT);
        } else if (mode == Mode.LaxAssignmentMode) {
          parseResult = Parsing.word.parseWordIfValid(builder);
          if (parseResult.isValid()) {
            ok = parseResult.isParsedSuccessfully();
          } else {
            break;
          } 
        } else {
          break;
        } 
      } 
      count++;
    } while (ok && !builder.eof());
    
    if (!ok && count == 0) {
      return OptionalParseResult.Invalid;
    }
    return ok ? OptionalParseResult.Ok : OptionalParseResult.ParseError;
  }







  
  public static OptionalParseResult readAssignmentIfValid(BashPsiBuilder builder, Mode mode, boolean markAsVarDef, boolean acceptArrayVars) {
    boolean isValid;
    OptionalParseResult result, varResult;
    IElementType nextToken;
    switch (mode) {

      
      case SimpleMode:
        isValid = ((acceptArrayVars && ParserUtil.hasNextTokens((PsiBuilder)builder, false, new IElementType[] { ASSIGNMENT_WORD, LEFT_SQUARE })) || ParserUtil.isWordToken(builder.getTokenType()) || Parsing.word.isWordToken(builder));
        break;

      
      case LaxAssignmentMode:
        isValid = (builder.getTokenType() == ASSIGNMENT_WORD || ParserUtil.isWordToken(builder.getTokenType()) || Parsing.word.isWordToken(builder));
        break;
      default:
        isValid = (builder.getTokenType() == ASSIGNMENT_WORD || (builder.isEvalMode() && ParserUtil.hasNextTokens((PsiBuilder)builder, false, new IElementType[] { VARIABLE, EQ })));
        break;
    } 
    if (!isValid) {
      return OptionalParseResult.Invalid;
    }

    
    PsiBuilder.Marker assignment = builder.mark();
    
    switch (mode) {
      case SimpleMode:
        if (acceptArrayVars && ParserUtil.hasNextTokens((PsiBuilder)builder, false, new IElementType[] { ASSIGNMENT_WORD, LEFT_SQUARE })) {
          break;
        }
        
        result = Parsing.word.parseWordIfValid(builder);
        if (!result.isParsedSuccessfully()) {
          assignment.drop();
          return result;
        } 
        break;
      
      case LaxAssignmentMode:
        if (builder.getTokenType() == ASSIGNMENT_WORD) {
          builder.advanceLexer(); break;
        } 
        varResult = Parsing.var.parseIfValid(builder);
        if (varResult.isValid()) {
          assignment.drop();
          
          if (!varResult.isParsedSuccessfully()) {
            return varResult;
          }

          
          assignment = NullMarker.get(); break;
        } 
        result = Parsing.word.parseWordIfValid(builder, false, BashTokenTypes.EQ_SET, TokenSet.EMPTY, null);
        if (!result.isParsedSuccessfully()) {
          assignment.drop();
          return result;
        } 
        break;


      
      case StrictAssignmentMode:
        if (builder.isEvalMode() && ParserUtil.hasNextTokens((PsiBuilder)builder, false, new IElementType[] { VARIABLE, EQ })) {
          
          markAsVarDef = false;
          result = Parsing.var.parseIfValid(builder);
          if (!result.isParsedSuccessfully()) {
            assignment.drop();
            return result;
          } 
          
          break;
        } 
        
        nextToken = ParserUtil.getTokenAndAdvance((PsiBuilder)builder);
        if (nextToken != ASSIGNMENT_WORD) {
          ParserUtil.error(assignment, "parser.unexpected.token");
          return OptionalParseResult.ParseError;
        } 
        break;

      
      default:
        assignment.drop();
        throw new IllegalStateException("Invalid parsing mode found");
    } 
    
    if (mode == Mode.SimpleMode && acceptArrayVars && builder.getTokenType() == ASSIGNMENT_WORD) {

      
      builder.advanceLexer();

      
      boolean hasArrayIndex = readArrayIndex(builder, assignment);
      if (!hasArrayIndex)
      {
        return OptionalParseResult.ParseError;
      }
    } 
    
    if (mode != Mode.SimpleMode) {
      if (!readArrayIndex(builder, assignment))
      {
        return OptionalParseResult.ParseError;
      }

      
      IElementType iElementType = builder.getTokenType(true);
      boolean hasAssignment = (iElementType == EQ || iElementType == ADD_EQ);
      if (!hasAssignment && mode == Mode.StrictAssignmentMode) {
        ParserUtil.error(assignment, "parser.unexpected.token");
        return OptionalParseResult.ParseError;
      } 
      
      if (hasAssignment) {
        builder.advanceLexer();
      }


      
      if (hasAssignment) {
        if (builder.getTokenType(true) == LEFT_PAREN) {
          
          boolean ok = parseAssignmentList(builder);
          if (!ok) {
            ParserUtil.error((PsiBuilder)builder, "parser.unexpected.token");
            assignment.drop();
            return OptionalParseResult.ParseError;
          } 
        } 
        
        IElementType token = builder.getTokenType(true);
        boolean isEndToken = assignmentSeparators.contains(token);
        if (token != null && !isEndToken && 
          !Parsing.word.parseWordIfValid(builder, true, TokenSet.EMPTY, validWordTokens, null).isParsedSuccessfully()) {
          ParserUtil.error((PsiBuilder)builder, "parser.unexpected.token");
          assignment.drop();
          return OptionalParseResult.ParseError;
        } 
      } 
    } 

    
    if (markAsVarDef) {
      assignment.done((IElementType)VAR_DEF_ELEMENT);
    } else {
      assignment.drop();
    } 
    
    return OptionalParseResult.Ok;
  }














  
  public static boolean parseAssignmentList(BashPsiBuilder builder) {
    IElementType first = builder.getTokenType();
    if (first != LEFT_PAREN) {
      builder.advanceLexer();
      return false;
    } 
    
    PsiBuilder.Marker marker = builder.mark();
    builder.advanceLexer();
    
    while (!builder.eof() && builder.getTokenType(true) != RIGHT_PAREN) {
      
      builder.readOptionalNewlines();
      
      if (builder.getTokenType() == LEFT_SQUARE) {
        
        boolean ok = ShellCommandParsing.arithmeticParser.parse(builder, LEFT_SQUARE, RIGHT_SQUARE);
        if (!ok) {
          marker.drop();
          return false;
        } 

        
        IElementType eqToken = ParserUtil.getTokenAndAdvance((PsiBuilder)builder);
        if (eqToken != EQ) {
          marker.drop();
          return false;
        } 
      } 


      
      OptionalParseResult result = Parsing.word.parseWordIfValid(builder, true);
      if (result.isValid() && 
        !result.isParsedSuccessfully()) {
        marker.drop();
        return false;
      } 


      
      boolean hadNewlines = builder.readOptionalNewlines(-1, true);


      
      if (!hadNewlines && builder.getTokenType(true) != WHITESPACE) {
        break;
      }
    } 




    
    if (ParserUtil.getTokenAndAdvance((PsiBuilder)builder) != RIGHT_PAREN) {
      marker.drop();
      return false;
    } 
    
    marker.done(VAR_ASSIGNMENT_LIST);
    return true;
  }
  
  private static boolean readArrayIndex(BashPsiBuilder builder, PsiBuilder.Marker assignment) {
    if (builder.getTokenType() == LEFT_SQUARE) {

      
      boolean valid = ShellCommandParsing.arithmeticParser.parse(builder, LEFT_SQUARE, RIGHT_SQUARE);
      if (!valid) {
        ParserUtil.error((PsiBuilder)builder, "parser.unexpected.token");
        assignment.drop();
        return false;
      } 
    } 
    
    return true;
  }



  
  public enum Mode
  {
    StrictAssignmentMode,


    
    LaxAssignmentMode,


    
    SimpleMode;
  }
}
