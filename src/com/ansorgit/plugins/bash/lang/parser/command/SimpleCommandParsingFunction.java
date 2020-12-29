package com.ansorgit.plugins.bash.lang.parser.command;

import com.ansorgit.plugins.bash.lang.parser.BashPsiBuilder;
import com.ansorgit.plugins.bash.lang.parser.OptionalParseResult;
import com.ansorgit.plugins.bash.lang.parser.Parsing;
import com.ansorgit.plugins.bash.lang.parser.ParsingFunction;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;


















class SimpleCommandParsingFunction
  implements ParsingFunction
{
  public boolean isValid(BashPsiBuilder builder) {
    throw new UnsupportedOperationException("call parseIfValid() instead");
  }

  
  public boolean parse(BashPsiBuilder builder) {
    return parseIfValid(builder).isParsedSuccessfully();
  }

  
  public OptionalParseResult parseIfValid(BashPsiBuilder builder) {
    PsiBuilder.Marker cmdMarker = builder.mark();

    
    boolean hasAssignmentOrRedirect = CommandParsingUtil.readAssignmentsAndRedirectsIfValid(builder, true, CommandParsingUtil.Mode.StrictAssignmentMode, true).isParsedSuccessfully();
    
    boolean hasCommand = parseCommandWord(builder);
    if (hasCommand) {
      
      boolean parsedParams = CommandParsingUtil.readCommandParams(builder);
      if (!parsedParams) {
        cmdMarker.drop();
        return OptionalParseResult.ParseError;
      } 
    } else if (!hasAssignmentOrRedirect) {
      cmdMarker.drop();
      return OptionalParseResult.Invalid;
    } 
    
    cmdMarker.done((IElementType)SIMPLE_COMMAND_ELEMENT);
    return OptionalParseResult.Ok;
  }







  
  private boolean parseCommandWord(BashPsiBuilder builder) {
    PsiBuilder.Marker cmdMarker = builder.mark();
    
    if (!Parsing.word.parseWordIfValid(builder, false).isParsedSuccessfully()) {
      cmdMarker.drop();
      return false;
    } 
    
    cmdMarker.done(GENERIC_COMMAND_ELEMENT);
    return true;
  }
}
