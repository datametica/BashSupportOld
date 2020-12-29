package com.ansorgit.plugins.bash.lang.parser.variable;

import com.ansorgit.plugins.bash.lang.parser.ParsingChain;
import com.ansorgit.plugins.bash.lang.parser.ParsingTool;




















public class VarParsing
  extends ParsingChain
  implements ParsingTool
{
  public VarParsing() {
    addParsingFunction(new SimpleVarParsing());
    addParsingFunction(new ComposedVariableParsing());
  }
}
