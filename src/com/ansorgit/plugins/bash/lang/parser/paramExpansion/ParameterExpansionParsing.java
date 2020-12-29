package com.ansorgit.plugins.bash.lang.parser.paramExpansion;

import com.ansorgit.plugins.bash.lang.parser.BashPsiBuilder;
import com.ansorgit.plugins.bash.lang.parser.BashSmartMarker;
import com.ansorgit.plugins.bash.lang.parser.OptionalParseResult;
import com.ansorgit.plugins.bash.lang.parser.Parsing;
import com.ansorgit.plugins.bash.lang.parser.ParsingFunction;
import com.ansorgit.plugins.bash.lang.parser.misc.ShellCommandParsing;
import com.ansorgit.plugins.bash.lang.parser.util.ParserUtil;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;


















public class ParameterExpansionParsing
  implements ParsingFunction
{
  private static final TokenSet validTokens = TokenSet.orSet(new TokenSet[] { paramExpansionOperators, TokenSet.create(new IElementType[] { PARAM_EXPANSION_OP_UNKNOWN, LEFT_SQUARE, RIGHT_SQUARE, LEFT_PAREN, RIGHT_PAREN, LINE_FEED, LESS_THAN, GREATER_THAN }) });



  
  private static final TokenSet prefixlessExpansionsOperators = TokenSet.create(new IElementType[] { PARAM_EXPANSION_OP_HASH });
  
  private static final TokenSet singleExpansionOperators = TokenSet.create(new IElementType[] { PARAM_EXPANSION_OP_AT, PARAM_EXPANSION_OP_QMARK, DOLLAR, PARAM_EXPANSION_OP_EXCL, PARAM_EXPANSION_OP_MINUS, PARAM_EXPANSION_OP_STAR, ARITH_NUMBER, PARAM_EXPANSION_OP_HASH, PARAM_EXPANSION_OP_HASH_HASH });


  
  private static final TokenSet variableMarkingExpansionOperators = TokenSet.create(new IElementType[] { PARAM_EXPANSION_OP_AT, PARAM_EXPANSION_OP_STAR });

  
  private static final TokenSet substitutionOperators = TokenSet.create(new IElementType[] { PARAM_EXPANSION_OP_COLON_MINUS, PARAM_EXPANSION_OP_COLON_QMARK, PARAM_EXPANSION_OP_COLON_PLUS });

  
  private static final TokenSet validFirstTokens = TokenSet.create(new IElementType[] { DOLLAR, PARAM_EXPANSION_OP_AT, PARAM_EXPANSION_OP_STAR });
  private static final TokenSet TOKEN_SET_CURLY_RIGHT = TokenSet.create(new IElementType[] { RIGHT_CURLY });
  
  public boolean isValid(BashPsiBuilder builder) {
    return (builder.getTokenType() == LEFT_CURLY);
  }
  
  public boolean parse(BashPsiBuilder builder) {
    PsiBuilder.Marker marker = builder.mark();
    
    ParserUtil.getTokenAndAdvance((PsiBuilder)builder);

    
    if (builder.rawLookup(0) == RIGHT_CURLY) {
      builder.advanceLexer();
      ParserUtil.error(marker, "parser.paramExpansion.empty");
      return true;
    } 
    
    if (singleExpansionOperators.contains(builder.rawLookup(0)) && builder.rawLookup(1) == RIGHT_CURLY) {
      
      if (variableMarkingExpansionOperators.contains(builder.rawLookup(0))) {
        ParserUtil.markTokenAndAdvance((PsiBuilder)builder, (IElementType)VAR_ELEMENT);
      } else {
        ParserUtil.getTokenAndAdvance((PsiBuilder)builder);
      } 
      ParserUtil.getTokenAndAdvance((PsiBuilder)builder);
      marker.done(PARAM_EXPANSION_ELEMENT);
      return true;
    } 
    
    if (builder.getTokenType(true) == PARAM_EXPANSION_OP_EXCL)
    {
      builder.advanceLexer();
    }

    
    IElementType firstToken = builder.getTokenType(true);

    
    if (prefixlessExpansionsOperators.contains(firstToken)) {
      builder.advanceLexer();
      firstToken = builder.getTokenType(true);
      
      if (firstToken == WHITESPACE) {
        builder.error("Expected a variable.");
        marker.drop();
        return false;
      } 
    } 

    
    BashSmartMarker firstElementMarker = new BashSmartMarker(builder.mark());
    
    if (!validFirstTokens.contains(firstToken) && !ParserUtil.isWordToken(firstToken) && (
      !builder.isEvalMode() || Parsing.var.isInvalid(builder))) {
      builder.error("Expected a valid parameter expansion token.");
      firstElementMarker.drop();

      
      return readRemainingExpansionTokens(builder, marker);
    } 




    
    if (builder.isEvalMode()) {
      OptionalParseResult varResult = Parsing.var.parseIfValid(builder);
      if (varResult.isValid()) {
        if (!varResult.isParsedSuccessfully()) {
          firstElementMarker.drop();
          return false;
        } 
      } else {
        builder.advanceLexer();
      } 
    } else {
      builder.advanceLexer();
    } 
    
    boolean markedAsVar = false;
    boolean isValid = true;
    boolean readFurther = true;
    
    if (builder.getTokenType(true) != RIGHT_CURLY) {
      IElementType operator = builder.getTokenType(true);

      
      if (operator == LEFT_SQUARE) {

        
        boolean isSpecialReference = (ParserUtil.hasNextTokens((PsiBuilder)builder, false, new IElementType[] { LEFT_SQUARE, PARAM_EXPANSION_OP_AT, RIGHT_SQUARE }) || ParserUtil.hasNextTokens((PsiBuilder)builder, false, new IElementType[] { LEFT_SQUARE, PARAM_EXPANSION_OP_STAR, RIGHT_SQUARE }));

        
        boolean isValidReference = ParserUtil.checkAndRollback(builder, psiBuilder -> ShellCommandParsing.arithmeticParser.parse(psiBuilder, LEFT_SQUARE, RIGHT_SQUARE));
        
        if (isSpecialReference || isValidReference) {
          firstElementMarker.done((IElementType)VAR_ELEMENT);
        }

        
        if (isSpecialReference) {
          ParserUtil.getTokenAndAdvance((PsiBuilder)builder);
          ParserUtil.getTokenAndAdvance((PsiBuilder)builder);
          ParserUtil.getTokenAndAdvance((PsiBuilder)builder);
        } else {
          boolean validArrayReference = ShellCommandParsing.arithmeticParser.parse(builder, LEFT_SQUARE, RIGHT_SQUARE);
          if (!validArrayReference) {
            firstElementMarker.drop();
            marker.drop();
            return false;
          } 
        } 
      } else if (substitutionOperators.contains(operator)) {



        
        firstElementMarker.done((IElementType)VAR_ELEMENT);
        markedAsVar = true;
        
        builder.advanceLexer();

        
        readFurther = false;
        
        boolean wordIsOk = true;


        
        PsiBuilder.Marker replacementValueMarker = builder.mark();
        while (builder.getTokenType() != RIGHT_CURLY && wordIsOk && !builder.eof()) {
          OptionalParseResult result = Parsing.word.parseWordIfValid(builder, false, TOKEN_SET_CURLY_RIGHT, TokenSet.EMPTY, null);
          if (result.isValid()) {
            
            wordIsOk = result.isParsedSuccessfully(); continue;
          } 
          builder.advanceLexer();
        } 

        
        if (builder.getTokenType() == RIGHT_CURLY) {
          replacementValueMarker.collapse(WORD);
        } else {
          replacementValueMarker.drop();
        } 
      } else {
        if (!paramExpansionOperators.contains(operator)) {
          builder.error("Unknown parameter expansion operator " + operator);
          firstElementMarker.drop();

          
          return readRemainingExpansionTokens(builder, marker);
        } 
        
        if (paramExpansionAssignmentOps.contains(operator)) {
          firstElementMarker.done((IElementType)VAR_DEF_ELEMENT);
          builder.advanceLexer();
          markedAsVar = true;
        } else if (variableMarkingExpansionOperators.contains(operator)) {
          builder.advanceLexer();
        } else if (paramExpansionOperators.contains(operator)) {
          
          firstElementMarker.done((IElementType)VAR_ELEMENT);
          builder.advanceLexer();
          markedAsVar = true;
        } else {
          
          firstElementMarker.drop();
        } 
      } 
      
      while (readFurther && isValid && builder.getTokenType() != RIGHT_CURLY) {
        OptionalParseResult varResult = Parsing.var.parseIfValid(builder);
        if (varResult.isValid()) {
          isValid = varResult.isParsedSuccessfully(); continue;
        }  if (Parsing.word.isComposedString(builder.getTokenType())) {
          isValid = Parsing.word.parseComposedString(builder); continue;
        }  if (Parsing.shellCommand.backtickParser.isValid(builder)) {
          isValid = Parsing.shellCommand.backtickParser.parse(builder); continue;
        } 
        isValid = readComposedValue(builder);
      }
    
    } else if (builder.isEvalMode() && firstToken == VARIABLE) {
      firstElementMarker.drop();
    } else {
      firstElementMarker.done((IElementType)VAR_ELEMENT);
    } 

    
    if (firstElementMarker.isOpen()) {
      firstElementMarker.drop();
    }
    
    IElementType endToken = ParserUtil.getTokenAndAdvance((PsiBuilder)builder);
    boolean validEnd = (RIGHT_CURLY == endToken);
    
    if (validEnd && !markedAsVar) {
      marker.done(PARAM_EXPANSION_ELEMENT);
    } else {
      marker.drop();
    } 
    
    return (validEnd && isValid);
  }
  
  private boolean readComposedValue(BashPsiBuilder builder) {
    PsiBuilder.Marker marker = builder.mark();
    
    int count = 0;
    
    IElementType next = builder.getTokenType(true);
    while (validTokens.contains(next) || ParserUtil.isWordToken(next)) {
      builder.advanceLexer();
      count++;
      
      next = builder.getTokenType(true);
    } 
    
    if (count > 0) {
      marker.collapse(WORD);
    } else {
      marker.drop();
    } 
    
    return (count > 0);
  }
  
  private boolean readRemainingExpansionTokens(BashPsiBuilder builder, PsiBuilder.Marker marker) {
    PsiBuilder.Marker start = builder.mark();
    
    int max = 10;
    while (!builder.eof() && builder.getTokenType() != RIGHT_CURLY && max > 0) {
      builder.advanceLexer();
      max--;
    } 
    
    if (max <= 0) {
      start.rollbackTo();
      marker.drop();
      return false;
    } 
    
    builder.advanceLexer();
    start.drop();
    marker.done(PARAM_EXPANSION_ELEMENT);
    return true;
  }
}
