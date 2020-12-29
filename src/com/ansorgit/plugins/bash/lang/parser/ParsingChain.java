package com.ansorgit.plugins.bash.lang.parser;

import java.util.ArrayList;
import java.util.List;























public abstract class ParsingChain
  implements ParsingFunction
{
  private final List<ParsingFunction> parsingFunctions = new ArrayList<>();
  
  protected final void addParsingFunction(ParsingFunction f) {
    this.parsingFunctions.add(f);
  }
  
  public boolean isValid(BashPsiBuilder builder) {
    if (builder.eof()) {
      return false;
    }
    
    for (ParsingFunction f : this.parsingFunctions) {
      if (f.isValid(builder)) {
        return true;
      }
    } 
    
    return false;
  }
  
  public boolean parse(BashPsiBuilder builder) {
    return parseIfValid(builder).isParsedSuccessfully();
  }
  
  public OptionalParseResult parseIfValid(BashPsiBuilder builder) {
    if (builder.eof()) {
      return OptionalParseResult.Invalid;
    }
    
    for (ParsingFunction f : this.parsingFunctions) {
      OptionalParseResult parseResult = f.parseIfValid(builder);
      if (parseResult.isValid()) {
        return parseResult;
      }
    } 
    
    return OptionalParseResult.Invalid;
  }
}
