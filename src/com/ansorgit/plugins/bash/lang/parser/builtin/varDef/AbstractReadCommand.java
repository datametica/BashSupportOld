package com.ansorgit.plugins.bash.lang.parser.builtin.varDef;

import com.ansorgit.plugins.bash.lang.parser.BashElementTypes;
import com.ansorgit.plugins.bash.lang.parser.BashPsiBuilder;


















abstract class AbstractReadCommand
  extends AbstractVariableDefParsing
{
  AbstractReadCommand(String command) {
    super(true, BashElementTypes.GENERIC_COMMAND_ELEMENT, command, false, true);
  }






  
  boolean isAssignment(BashPsiBuilder builder) {
    String text = builder.getTokenText();
    return (builder.getTokenType() == WORD && text != null && !text.startsWith("-"));
  }
}
