package com.ansorgit.plugins.bash.lang.parser.builtin;

import com.ansorgit.plugins.bash.lang.parser.BashPsiBuilder;
import com.ansorgit.plugins.bash.lang.parser.OptionalParseResult;
import com.ansorgit.plugins.bash.lang.parser.Parsing;
import com.ansorgit.plugins.bash.lang.parser.ParsingFunction;
import com.ansorgit.plugins.bash.lang.parser.ParsingTool;
import com.ansorgit.plugins.bash.lang.parser.command.CommandParsingUtil;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;



















class PrintfCommand
  implements ParsingFunction, ParsingTool
{
  public boolean isValid(BashPsiBuilder builder) {
    return (builder.getTokenType() == WORD && "printf".equals(builder.getTokenText()));
  }

  
  public boolean parse(BashPsiBuilder builder) {
    PsiBuilder.Marker cmdMarker = builder.mark();

    
    OptionalParseResult result = CommandParsingUtil.readAssignmentsAndRedirectsIfValid(builder, false, CommandParsingUtil.Mode.StrictAssignmentMode, false);
    if (result.isValid() && !result.isParsedSuccessfully()) {
      cmdMarker.drop();
      return false;
    } 

    
    PsiBuilder.Marker cmdWord = builder.mark();
    builder.advanceLexer();
    cmdWord.done(GENERIC_COMMAND_ELEMENT);

    
    if ("-v".equals(builder.getTokenText())) {
      builder.advanceLexer();

      
      if (Parsing.word.isWordToken(builder)) {
        if (!parseVariableName(builder)) {
          cmdMarker.drop();
          return false;
        } 
      } else {
        cmdMarker.drop();
        builder.error("Expected variable name");
        return false;
      } 
    } 



    
    CommandParsingUtil.readCommandParams(builder);
    
    cmdMarker.done((IElementType)SIMPLE_COMMAND_ELEMENT);
    return true;
  }
  
  private boolean parseVariableName(BashPsiBuilder builder) {
    IElementType token = builder.getTokenType();
    if (token == WORD || token == STRING2 || Parsing.word.isSimpleComposedString(builder, false)) {
      return parseSimpleWord(builder);
    }
    
    OptionalParseResult result = Parsing.word.parseWordIfValid(builder);
    if (result.isValid()) {
      return result.isParsedSuccessfully();
    }
    
    return false;
  }
  
  private boolean parseSimpleWord(BashPsiBuilder builder) {
    PsiBuilder.Marker marker = builder.mark();
    
    if (Parsing.word.parseWordIfValid(builder).isParsedSuccessfully()) {
      marker.done((IElementType)VAR_DEF_ELEMENT);
      return true;
    } 
    
    marker.drop();
    return false;
  }
}
