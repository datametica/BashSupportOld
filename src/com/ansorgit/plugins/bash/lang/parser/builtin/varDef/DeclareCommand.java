package com.ansorgit.plugins.bash.lang.parser.builtin.varDef;

import com.ansorgit.plugins.bash.lang.parser.ParsingTool;




















class DeclareCommand
  extends AbstractVariableDefParsing
  implements ParsingTool
{
  DeclareCommand() {
    super(true, GENERIC_COMMAND_ELEMENT, "declare", true, false);
  }
}
