package com.ansorgit.plugins.bash.lang.parser;

import com.ansorgit.plugins.bash.lang.parser.util.ParserUtil;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;




















public class FileParsing
  implements ParsingTool
{
  public boolean parseFile(BashPsiBuilder builder) {
    builder.readOptionalNewlines();
    if (builder.getTokenType() == SHEBANG) {
      ParserUtil.markTokenAndAdvance(builder, SHEBANG_ELEMENT);
    }
    
    builder.remapShebangToComment();
    
    boolean success = true;
    while (!builder.eof()) {
      builder.readOptionalNewlines();

      
      builder.readOptionalNewlines();
      if (builder.eof()) {
        break;
      }
      
      boolean toplevelExitCommand = isToplevelExit(builder);
      
      boolean ok = Parsing.list.parseSimpleList(builder);
      toplevelExitCommand &= ok;
      
      if (!builder.eof() && Parsing.list.isSimpleListTerminator(builder.getTokenType())) {
        builder.advanceLexer();
      }

      
      if (toplevelExitCommand) {
        PsiBuilder.Marker binaryMarker = builder.mark();
        
        while (!builder.eof()) {
          builder.advanceLexer();
        }
        
        binaryMarker.done(BashElementTypes.BINARY_DATA);
      } 
      
      if (!ok && !builder.eof()) {
        builder.advanceLexer();
        success = false;
      } 
    } 
    
    return success;
  }
  
  private boolean isToplevelExit(BashPsiBuilder builder) {
    if (builder.getTokenType() != WORD || !"exit".equals(builder.getTokenText())) {
      return false;
    }
    
    IElementType prev = builder.rawLookup(-1);
    if (prev != null && prev != LINE_FEED && prev != SHEBANG) {
      return false;
    }

    
    IElementType type = builder.lookAhead(1);
    if (type != null && type != WORD && type != STRING2 && type != INTEGER_LITERAL && type != LINE_FEED) {
      return false;
    }
    
    return true;
  }
}
