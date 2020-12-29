package com.ansorgit.plugins.bash.lang.parser;















public enum OptionalParseResult
{
  Invalid,
  ParseError,
  Ok;
  
  public boolean isValid() {
    return (this != Invalid);
  }
  
  public boolean isParsedSuccessfully() {
    return (this == Ok);
  }
}
