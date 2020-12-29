package com.ansorgit.plugins.bash.lang.parser.builtin;

import com.ansorgit.plugins.bash.lang.LanguageBuiltins;
import com.ansorgit.plugins.bash.lang.parser.BashPsiBuilder;
import com.ansorgit.plugins.bash.lang.parser.OptionalParseResult;
import com.ansorgit.plugins.bash.lang.parser.Parsing;
import com.ansorgit.plugins.bash.lang.parser.ParsingFunction;
import com.ansorgit.plugins.bash.lang.parser.ParsingTool;
import com.ansorgit.plugins.bash.lang.parser.command.CommandParsingUtil;
import com.ansorgit.plugins.bash.lang.parser.util.ParserUtil;
import com.google.common.collect.Sets;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import java.util.Set;













class IncludeCommand
  implements ParsingFunction, ParsingTool
{
  private static final Set<String> acceptedCommands = Sets.newHashSet((Object[])new String[] { ".", "source" });
  private static final TokenSet invalidFollowups = TokenSet.create(new IElementType[] { EQ });
  
  public boolean isValid(BashPsiBuilder builder) {
    if (invalidFollowups.contains(builder.rawLookup(1))) {
      return false;
    }
    
    String tokenText = builder.getTokenText();
    return (LanguageBuiltins.isInternalCommand(tokenText, builder.isBash4()) && acceptedCommands.contains(tokenText));
  }
  
  public boolean parse(BashPsiBuilder builder) {
    PsiBuilder.Marker commandMarker = builder.mark();
    
    boolean ok = CommandParsingUtil.readOptionalAssignmentOrRedirects(builder, CommandParsingUtil.Mode.StrictAssignmentMode, false, true);
    if (!ok) {
      commandMarker.drop();
      return false;
    } 

    
    ParserUtil.markTokenAndAdvance((PsiBuilder)builder, GENERIC_COMMAND_ELEMENT);

    
    PsiBuilder.Marker fileMarker = builder.mark();
    OptionalParseResult wordResult = Parsing.word.parseWordIfValid(builder, false);
    if (!wordResult.isParsedSuccessfully()) {
      fileMarker.drop();
      commandMarker.drop();
      builder.error("Expected file name");
      return false;
    } 
    
    fileMarker.done(FILE_REFERENCE);
    
    Parsing.word.parseWordListIfValid(builder, false, false);


    
    ok = CommandParsingUtil.readOptionalAssignmentOrRedirects(builder, CommandParsingUtil.Mode.SimpleMode, false, false);
    if (!ok) {
      commandMarker.drop();
      return false;
    } 
    
    commandMarker.done((IElementType)INCLUDE_COMMAND_ELEMENT);
    
    return true;
  }
}
