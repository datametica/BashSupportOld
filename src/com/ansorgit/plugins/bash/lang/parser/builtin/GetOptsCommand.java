package com.ansorgit.plugins.bash.lang.parser.builtin;

import com.ansorgit.plugins.bash.lang.parser.BashPsiBuilder;
import com.ansorgit.plugins.bash.lang.parser.OptionalParseResult;
import com.ansorgit.plugins.bash.lang.parser.Parsing;
import com.ansorgit.plugins.bash.lang.parser.ParsingFunction;
import com.ansorgit.plugins.bash.lang.parser.command.CommandParsingUtil;
import com.ansorgit.plugins.bash.lang.parser.util.ParserUtil;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;














public class GetOptsCommand
  implements ParsingFunction
{
  public boolean isValid(BashPsiBuilder builder) {
    return "getopts".equals(builder.getTokenText());
  }

  
  public boolean parse(BashPsiBuilder builder) {
    if (!isValid(builder)) {
      return false;
    }
    
    PsiBuilder.Marker cmdMarker = builder.mark();

    
    PsiBuilder.Marker getOpts = builder.mark();
    builder.advanceLexer();
    getOpts.done(GENERIC_COMMAND_ELEMENT);

    
    if (Parsing.word.isComposedString(builder.getTokenType())) {
      if (!Parsing.word.parseComposedString(builder)) {
        cmdMarker.drop();
        return false;
      } 
    } else if (ParserUtil.isWordToken(builder.getTokenType())) {
      builder.advanceLexer();
    } else {
      cmdMarker.drop();
      builder.error("Expected the getopts option string");
      return false;
    } 

    
    boolean varDefRead = CommandParsingUtil.readAssignmentIfValid(builder, CommandParsingUtil.Mode.SimpleMode, true, false).isParsedSuccessfully();
    if (!varDefRead) {
      cmdMarker.drop();
      builder.error("Expected getops variable name");
      return false;
    } 

    
    OptionalParseResult result = Parsing.word.parseWordListIfValid(builder, false, false);
    if (result.isValid() && !result.isParsedSuccessfully()) {
      cmdMarker.drop();
      return false;
    } 
    
    cmdMarker.done((IElementType)SIMPLE_COMMAND_ELEMENT);
    return true;
  }
}
