package com.ansorgit.plugins.bash.lang.parser.command;

import com.ansorgit.plugins.bash.lang.parser.ParsingChain;
import com.ansorgit.plugins.bash.lang.parser.ParsingFunction;
import com.ansorgit.plugins.bash.lang.parser.ParsingTool;
import com.ansorgit.plugins.bash.lang.parser.builtin.BuiltinCommandParser;
import com.ansorgit.plugins.bash.lang.parser.builtin.varDef.BuiltinVarCommandParser;


























public class CommandParsing
  extends ParsingChain
  implements ParsingTool
{
  public CommandParsing() {
    addParsingFunction(new ShellCommandDelegator());
    addParsingFunction(new FunctionDefParsingFunction());
    addParsingFunction((ParsingFunction)new BuiltinVarCommandParser());
    addParsingFunction((ParsingFunction)new BuiltinCommandParser());
    addParsingFunction(new SimpleCommandParsingFunction());
  }
}
