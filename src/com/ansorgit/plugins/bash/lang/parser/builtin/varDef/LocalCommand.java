package com.ansorgit.plugins.bash.lang.parser.builtin.varDef;

import com.ansorgit.plugins.bash.lang.parser.ParsingTool;




















class LocalCommand
  extends AbstractVariableDefParsing
  implements ParsingTool
{
  public LocalCommand() {
    super(true, GENERIC_COMMAND_ELEMENT, "local", true, false);
  }
}
