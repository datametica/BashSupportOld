package com.ansorgit.plugins.bash.lang.parser.builtin.varDef;

import com.ansorgit.plugins.bash.lang.parser.ParsingTool;




















class ReadonlyCommand
  extends AbstractVariableDefParsing
  implements ParsingTool
{
  ReadonlyCommand() {
    super(true, GENERIC_COMMAND_ELEMENT, "readonly", true, false);
  }
}
