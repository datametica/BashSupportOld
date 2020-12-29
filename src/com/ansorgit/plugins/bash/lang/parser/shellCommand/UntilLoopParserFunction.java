package com.ansorgit.plugins.bash.lang.parser.shellCommand;

import com.ansorgit.plugins.bash.lang.parser.BashElementTypes;



















public class UntilLoopParserFunction
  extends AbstractLoopParser
{
  public UntilLoopParserFunction() {
    super(UNTIL_KEYWORD, BashElementTypes.UNTIL_COMMAND);
  }
}
