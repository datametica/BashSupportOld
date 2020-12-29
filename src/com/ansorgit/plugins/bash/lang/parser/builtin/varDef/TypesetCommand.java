package com.ansorgit.plugins.bash.lang.parser.builtin.varDef;

import com.ansorgit.plugins.bash.lang.parser.ParsingTool;





















class TypesetCommand
  extends AbstractVariableDefParsing
  implements ParsingTool
{
  TypesetCommand() {
    super(true, GENERIC_COMMAND_ELEMENT, "typeset", true, false);
  }
}
