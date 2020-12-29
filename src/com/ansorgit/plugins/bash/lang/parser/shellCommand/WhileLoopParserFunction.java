package com.ansorgit.plugins.bash.lang.parser.shellCommand;

import com.ansorgit.plugins.bash.lang.parser.BashElementTypes;



















public class WhileLoopParserFunction
  extends AbstractLoopParser
{
  public WhileLoopParserFunction() {
    super(WHILE_KEYWORD, BashElementTypes.WHILE_COMMAND);
  }
}
