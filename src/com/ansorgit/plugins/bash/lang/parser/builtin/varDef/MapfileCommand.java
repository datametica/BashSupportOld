package com.ansorgit.plugins.bash.lang.parser.builtin.varDef;

import com.ansorgit.plugins.bash.lang.parser.ParsingTool;
import java.util.Arrays;
























class MapfileCommand
  extends AbstractVariableDefParsing
  implements ParsingTool
{
  private static final int[] VALUE_ARGS = new int[] { 100, 110, 79, 115, 117, 67, 99 };
  
  public MapfileCommand() {
    this("mapfile");
  }
  
  MapfileCommand(String commandText) {
    super(false, GENERIC_COMMAND_ELEMENT, commandText, false, false);
  }

  
  boolean argumentValueExpected(String name) {
    return name.chars().anyMatch(value -> Arrays.stream(VALUE_ARGS).anyMatch(()));
  }
}
