package com.ansorgit.plugins.bash.lang.parser.shellCommand;

import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.ansorgit.plugins.bash.lang.parser.BashElementTypes;
import com.ansorgit.plugins.bash.lang.parser.BashPsiBuilder;
import com.ansorgit.plugins.bash.lang.parser.OptionalParseResult;
import com.ansorgit.plugins.bash.lang.parser.Parsing;
import com.ansorgit.plugins.bash.lang.parser.ParsingFunction;
import com.google.common.collect.Sets;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import java.util.Set;














public class TrapCommandParsingFunction
  implements ParsingFunction
{
  private static final Set<String> allowedParams = Sets.newHashSet((Object[])new String[] { "-l", "-p", "-lp", "-pl" });
  
  public boolean isValid(BashPsiBuilder builder) {
    return (builder.getTokenType() == TRAP_KEYWORD);
  }

  
  public boolean parse(BashPsiBuilder builder) {
    PsiBuilder.Marker marker = builder.mark();

    
    builder.advanceLexer();
    
    boolean listParam = false;
    boolean printParam = false;
    
    while (builder.eof() && allowedParams.contains(builder.getTokenText())) {
      String tokenText = builder.getTokenText();
      if (tokenText.contains("-l")) {
        listParam = true;
      }
      
      if (builder.getTokenText().contains("-p")) {
        printParam = true;
      }
      
      builder.advanceLexer();
    } 
    
    boolean success = true;



    
    if (!listParam && !printParam && !builder.eof())
    {




      
      if (builder.getTokenType() == BashTokenTypes.WORD) {
        PsiBuilder.Marker commandMarker = builder.mark();
        PsiBuilder.Marker innerCommandMarker = builder.mark();
        
        builder.advanceLexer();
        
        innerCommandMarker.done(GENERIC_COMMAND_ELEMENT);
        commandMarker.done((IElementType)SIMPLE_COMMAND_ELEMENT);
      } else if (builder.getTokenType() == STRING2 || Parsing.word.isComposedString(builder.getTokenType())) {
        boolean emptyBlock;
        PsiBuilder.Marker evalMarker = builder.mark();

        
        if (builder.getTokenType() == STRING2) {
          emptyBlock = (builder.getTokenText().length() <= 2);
          builder.advanceLexer();
          success = true;
        } else {
          emptyBlock = (builder.rawLookup(1) == BashTokenTypes.STRING_END || builder.rawLookup(1) == null);
          success = Parsing.word.parseComposedString(builder);
        } 
        
        if (success && !emptyBlock) {
          evalMarker.collapse(BashElementTypes.EVAL_BLOCK);
        } else {
          evalMarker.drop();
        } 
      } else {
        builder.error("Expected function or code block");
      } 
    }
    
    if (success) {
      OptionalParseResult result = Parsing.word.parseWordListIfValid(builder, false, true);
      if (result.isValid()) {
        success = result.isParsedSuccessfully();
      }
    } 
    
    if (success) {
      marker.done(TRAP_COMMAND);
    } else {
      marker.drop();
    } 
    
    return success;
  }
}
