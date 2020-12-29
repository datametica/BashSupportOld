package com.ansorgit.plugins.bash.lang.parser.builtin.varDef;

import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.ansorgit.plugins.bash.lang.parser.BashPsiBuilder;
import com.ansorgit.plugins.bash.lang.parser.OptionalParseResult;
import com.ansorgit.plugins.bash.lang.parser.Parsing;
import com.ansorgit.plugins.bash.lang.parser.command.CommandParsingUtil;
import com.ansorgit.plugins.bash.lang.parser.util.ParserUtil;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;




















abstract class AbstractVariableDefParsing
  implements BashTokenTypes
{
  private static final TokenSet EQ_SET = TokenSet.create(new IElementType[] { BashTokenTypes.EQ });

  
  private final boolean acceptFrontVarDef;

  
  private final IElementType commandElementType;

  
  private final String commandName;

  
  private final boolean acceptArrayVars;
  
  private final CommandParsingUtil.Mode parsingMode;

  
  protected AbstractVariableDefParsing(boolean acceptFrontVarDef, IElementType commandElementType, String commandText, boolean acceptVarAssignments, boolean acceptArrayVars) {
    this.acceptFrontVarDef = acceptFrontVarDef;
    this.commandElementType = commandElementType;
    this.commandName = commandText;
    this.acceptArrayVars = acceptArrayVars;
    
    if (acceptVarAssignments) {
      this.parsingMode = CommandParsingUtil.Mode.LaxAssignmentMode;
    } else {
      this.parsingMode = CommandParsingUtil.Mode.SimpleMode;
    } 
  }
  
  String getCommandName() {
    return this.commandName;
  }
  
  OptionalParseResult parseIfValid(BashPsiBuilder builder) {
    OptionalParseResult result = CommandParsingUtil.readAssignmentsAndRedirectsIfValid(builder, false, CommandParsingUtil.Mode.StrictAssignmentMode, this.acceptArrayVars);
    if (this.acceptFrontVarDef && result.isValid() && !result.isParsedSuccessfully()) {
      throw new IllegalStateException("Unexpected state");
    }
    
    ParserUtil.markTokenAndAdvance((PsiBuilder)builder, this.commandElementType);

    
    if (!readOptions(builder)) {
      return OptionalParseResult.ParseError;
    }
    
    result = CommandParsingUtil.readAssignmentsAndRedirectsIfValid(builder, true, this.parsingMode, this.acceptArrayVars);
    if (!result.isValid() || result.isParsedSuccessfully()) {
      return OptionalParseResult.Ok;
    }
    return OptionalParseResult.ParseError;
  }

  
  private boolean readOptions(BashPsiBuilder builder) {
    while (Parsing.word.isWordToken(builder) && !isAssignment(builder)) {
      String argName = builder.getTokenText();
      
      boolean ok = Parsing.word.parseWordIfValid(builder, false, EQ_SET, TokenSet.EMPTY, null).isParsedSuccessfully();
      if (!ok) {
        return false;
      }
      
      if (argumentValueExpected(argName)) {
        ok = parseArgumentValue(argName, builder);
        if (!ok) {
          return false;
        }
      } 
    } 
    
    return true;
  }
  
  protected boolean parseArgumentValue(String argName, BashPsiBuilder builder) {
    return Parsing.word.parseWordIfValid(builder, false, EQ_SET, TokenSet.EMPTY, null).isParsedSuccessfully();
  }
  
  boolean argumentValueExpected(String name) {
    return false;
  }
  
  boolean isAssignment(BashPsiBuilder builder) {
    String text = builder.getTokenText();
    if (text != null && !text.isEmpty() && text.charAt(0) == '-') {
      return false;
    }
    
    PsiBuilder.Marker start = builder.mark();
    
    if (builder.getTokenType() == BashTokenTypes.ASSIGNMENT_WORD) {
      start.drop();
      return true;
    } 
    
    OptionalParseResult result = Parsing.word.parseWordIfValid(builder, false, EQ_SET, TokenSet.EMPTY, null);
    if (result.isValid() && !result.isParsedSuccessfully()) {
      start.rollbackTo();
      return false;
    } 





    
    start.rollbackTo();
    return true;
  }
}
