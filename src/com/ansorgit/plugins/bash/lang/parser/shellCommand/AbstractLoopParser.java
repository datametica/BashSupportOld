package com.ansorgit.plugins.bash.lang.parser.shellCommand;

import com.ansorgit.plugins.bash.lang.parser.BashPsiBuilder;
import com.ansorgit.plugins.bash.lang.parser.Parsing;
import com.ansorgit.plugins.bash.lang.parser.ParsingFunction;
import com.ansorgit.plugins.bash.lang.parser.ParsingTool;
import com.ansorgit.plugins.bash.lang.parser.misc.ShellCommandParsing;
import com.ansorgit.plugins.bash.lang.parser.util.ParserUtil;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;



















public class AbstractLoopParser
  implements ParsingTool, ParsingFunction
{
  private final IElementType startToken;
  private final IElementType commandMarker;
  
  public AbstractLoopParser(IElementType startToken, IElementType commandMarker) {
    this.startToken = startToken;
    this.commandMarker = commandMarker;
  }
  
  public boolean isValid(BashPsiBuilder builder) {
    return (builder.getTokenType() == this.startToken);
  }





  
  public boolean parse(BashPsiBuilder builder) {
    PsiBuilder.Marker loopMarker = builder.mark();
    builder.advanceLexer();
    
    if (ParserUtil.isEmptyListFollowedBy(builder, DO_KEYWORD)) {
      ParserUtil.error((PsiBuilder)builder, "parser.shell.expectedCommands");
      ParserUtil.readEmptyListFollowedBy(builder, DO_KEYWORD);
    } else if (!Parsing.list.parseCompoundList(builder, false)) {
      loopMarker.drop();
      return false;
    } 
    
    if (!ParserUtil.checkNextOrError(builder, loopMarker, ShellCommandParsing.DO_KEYWORD, "parser.shell.expectedDo")) {
      return false;
    }

    
    if (ParserUtil.isEmptyListFollowedBy(builder, DONE_KEYWORD)) {
      ParserUtil.error((PsiBuilder)builder, "parser.shell.expectedCommands");
      ParserUtil.readEmptyListFollowedBy(builder, DONE_KEYWORD);
    } else if (!Parsing.list.parseCompoundList(builder, true)) {
      loopMarker.drop();
      return false;
    } 
    
    if (!ParserUtil.checkNextOrError(builder, loopMarker, ShellCommandParsing.DONE_KEYWORD, "parser.shell.expectedDone")) {
      return false;
    }
    
    loopMarker.done(this.commandMarker);
    return true;
  }
}
