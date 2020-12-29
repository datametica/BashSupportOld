package com.ansorgit.plugins.bash.lang.parser.shellCommand;

import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.ansorgit.plugins.bash.lang.parser.BashElementTypes;
import com.ansorgit.plugins.bash.lang.parser.BashPsiBuilder;
import com.ansorgit.plugins.bash.lang.parser.ParseResult;
import com.ansorgit.plugins.bash.lang.parser.Parsing;
import com.ansorgit.plugins.bash.lang.parser.ParsingFunction;
import com.ansorgit.plugins.bash.lang.parser.util.ParserUtil;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;















public class IfParsingFunction
  implements ParsingFunction
{
  private static final TokenSet ELSE_ELIF_FI = TokenSet.create(new IElementType[] { ELIF_KEYWORD, ELSE_KEYWORD, FI_KEYWORD });
  
  public boolean isValid(BashPsiBuilder builder) {
    return (builder.getTokenType() == BashTokenTypes.IF_KEYWORD);
  }








  
  public boolean parse(BashPsiBuilder builder) {
    PsiBuilder.Marker ifCommand = builder.mark();
    ParserUtil.getTokenAndAdvance((PsiBuilder)builder);
    
    if (ParserUtil.isEmptyListFollowedBy(builder, THEN_KEYWORD)) {
      ParserUtil.error((PsiBuilder)builder, "parser.shell.if.expectedCommands");
      ParserUtil.readEmptyListFollowedBy(builder, THEN_KEYWORD);
    } else if (!Parsing.list.parseCompoundList(builder, false)) {
      ParserUtil.error((PsiBuilder)builder, "parser.shell.if.expectedCommands");
      ifCommand.drop();
      return false;
    } 
    
    IElementType thenKeyword = ParserUtil.getTokenAndAdvance((PsiBuilder)builder);
    if (thenKeyword != BashTokenTypes.THEN_KEYWORD) {
      ParserUtil.error((PsiBuilder)builder, "parser.shell.if.expectedThen");
      ifCommand.drop();
      return false;
    } 
    
    if (ParserUtil.isEmptyListFollowedBy(builder, ELSE_ELIF_FI)) {
      ParserUtil.error((PsiBuilder)builder, "parser.shell.if.expectedCommands");
      ParserUtil.readEmptyListFollowedBy(builder, ELSE_ELIF_FI);
    } else if (!Parsing.list.parseCompoundList(builder, true)) {
      ifCommand.drop();
      return false;
    } 
    
    if (builder.getTokenType() == BashTokenTypes.ELIF_KEYWORD) {
      if (ParserUtil.isEmptyListFollowedBy(builder, ELSE_KEYWORD)) {
        ParserUtil.error((PsiBuilder)builder, "parser.shell.if.expectedCommands");
        ParserUtil.readEmptyListFollowedBy(builder, ELSE_KEYWORD);
      } else if (parseElifClause(builder) == ParseResult.ERRORS) {
        ifCommand.drop();
        return false;
      } 
    }
    
    if (builder.getTokenType() == BashTokenTypes.ELSE_KEYWORD) {
      builder.advanceLexer();
      
      if (ParserUtil.isEmptyListFollowedBy(builder, FI_KEYWORD)) {
        ParserUtil.error((PsiBuilder)builder, "parser.shell.if.expectedCommands");
        ParserUtil.readEmptyListFollowedBy(builder, FI_KEYWORD);
      } else if (!Parsing.list.parseCompoundList(builder, true)) {
        ifCommand.drop();
        return false;
      } 
    } 
    
    IElementType fiKeyword = ParserUtil.getTokenAndAdvance((PsiBuilder)builder);
    if (fiKeyword != BashTokenTypes.FI_KEYWORD) {
      ParserUtil.error((PsiBuilder)builder, "parser.shell.if.expectedFi");
      ifCommand.drop();
      return false;
    } 
    
    ifCommand.done(BashElementTypes.IF_COMMAND);
    return true;
  }







  
  private ParseResult parseElifClause(BashPsiBuilder builder) {
    boolean withErrors = false;
    
    IElementType token = ParserUtil.getTokenAndAdvance((PsiBuilder)builder);
    if (token != BashTokenTypes.ELIF_KEYWORD) {
      ParserUtil.error((PsiBuilder)builder, "parser.shell.if.expectedElif");
      return ParseResult.ERRORS;
    } 
    
    if (ParserUtil.isEmptyListFollowedBy(builder, THEN_KEYWORD)) {
      ParserUtil.error((PsiBuilder)builder, "parser.shell.if.expectedCommands");
      ParserUtil.readEmptyListFollowedBy(builder, THEN_KEYWORD);
      withErrors = true;
    } else if (!Parsing.list.parseCompoundList(builder, false)) {
      ParserUtil.error((PsiBuilder)builder, "parser.command.expected.command");
      return ParseResult.ERRORS;
    } 
    
    IElementType thenToken = ParserUtil.getTokenAndAdvance((PsiBuilder)builder);
    if (thenToken != BashTokenTypes.THEN_KEYWORD) {
      ParserUtil.error((PsiBuilder)builder, "parser.shell.if.expectedThen");
      return ParseResult.ERRORS;
    } 

    
    if (ParserUtil.isEmptyListFollowedBy(builder, ELSE_ELIF_FI)) {
      ParserUtil.error((PsiBuilder)builder, "parser.shell.if.expectedCommands");
      ParserUtil.readEmptyListFollowedBy(builder, ELSE_ELIF_FI);
      withErrors = true;
    } else if (!Parsing.list.parseCompoundList(builder, true)) {
      ParserUtil.error((PsiBuilder)builder, "parser.command.expected.command");
      return ParseResult.ERRORS;
    } 
    
    if (builder.getTokenType() == BashTokenTypes.ELIF_KEYWORD) {
      return parseElifClause(builder);
    }

    
    return withErrors ? ParseResult.ERRORS_OK : ParseResult.OK;
  }
}
