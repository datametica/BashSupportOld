package com.ansorgit.plugins.bash.lang.parser;

import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;



























public interface ParsingFunction
  extends BashTokenTypes, BashElementTypes
{
  boolean isValid(BashPsiBuilder paramBashPsiBuilder);
  
  default boolean isInvalid(BashPsiBuilder builder) {
    return !isValid(builder);
  }








  
  boolean parse(BashPsiBuilder paramBashPsiBuilder);








  
  default OptionalParseResult parseIfValid(BashPsiBuilder builder) {
    if (isValid(builder)) {
      return parse(builder) ? OptionalParseResult.Ok : OptionalParseResult.ParseError;
    }

    
    return OptionalParseResult.Invalid;
  }
}
