package com.ansorgit.plugins.bash.lang.parser.builtin;

import com.ansorgit.plugins.bash.lang.parser.ParsingChain;



















public class BuiltinCommandParser
  extends ParsingChain
{
  public BuiltinCommandParser() {
    addParsingFunction(new IncludeCommand());
    addParsingFunction(new GetOptsCommand());
    addParsingFunction(new EvalCommandParsing());
    
    addParsingFunction(new PrintfCommand());
    
    addParsingFunction(new LetCommand());
  }
}
