package com.ansorgit.plugins.bash.lang.parser.misc;

import com.ansorgit.plugins.bash.lang.parser.BashPsiBuilder;
import com.ansorgit.plugins.bash.lang.parser.OptionalParseResult;
import com.ansorgit.plugins.bash.lang.parser.Parsing;
import com.ansorgit.plugins.bash.lang.parser.ParsingTool;
import com.ansorgit.plugins.bash.lang.parser.util.ParserUtil;
import com.ansorgit.plugins.bash.util.NullMarker;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;































public final class ListParsing
  implements ParsingTool
{
  public boolean isListTerminator(IElementType token) {
    return (token == LINE_FEED || token == SEMI || token == null);
  }
  
  public boolean isSimpleListTerminator(IElementType token) {
    return (token == LINE_FEED || token == null);
  }








  
  public boolean parseCompoundList(BashPsiBuilder builder, boolean markAsFoldable) {
    return parseCompoundList(builder, false, markAsFoldable);
  }














  
  public boolean parseCompoundList(BashPsiBuilder builder, boolean optionalTerminator, boolean markAsFoldable) {
    PsiBuilder.Marker optionalMarker = markAsFoldable ? builder.mark() : NullMarker.get();

    
    builder.readOptionalNewlines();

    
    if (!parseList1(builder, false, true)) {
      optionalMarker.drop();
      
      return false;
    } 

    
    IElementType token = builder.getTokenType();

    
    if (token == SEMI || token == LINE_FEED || token == AMP) {
      optionalMarker.done(LOGICAL_BLOCK_ELEMENT);
      
      builder.advanceLexer();
      builder.readOptionalNewlines();
      
      return true;
    } 
    
    optionalMarker.done(LOGICAL_BLOCK_ELEMENT);
    
    return (builder.eof() || optionalTerminator);
  }
  
  public boolean parseList(BashPsiBuilder builder) {
    return parseCompoundList(builder, false, false);
  }










  
  boolean parseList1(BashPsiBuilder builder, boolean simpleMode, boolean markComposedCommand) {
    PsiBuilder.Marker startMarker = markComposedCommand ? builder.mark() : NullMarker.get();
    
    boolean success = parseList1Element(builder, true);
    boolean markCommand = success;
    
    while (success) {
      IElementType next = builder.getTokenType();
      
      if (next == AND_AND || next == OR_OR) {
        builder.advanceLexer();
        parseOptionalHeredocContent(builder);
        builder.readOptionalNewlines();
        
        success = parseList1Element(builder, true);
        markCommand = success; continue;
      }  if (next == SEMI || next == LINE_FEED || next == AMP) {
        if (builder.getParsingState().expectsHeredocMarker() && next != LINE_FEED) {
          builder.advanceLexer();
        }
        
        boolean hasHeredoc = parseOptionalHeredocContent(builder);

        
        if (builder.getTokenType() == LINE_FEED && simpleMode) {
          markCommand = hasHeredoc;
          success = true;
          break;
        } 
        if (hasHeredoc && builder.getTokenType() != LINE_FEED && !builder.eof()) {

          
          markCommand = true;
          
          break;
        } 
        PsiBuilder.Marker start = builder.mark();
        
        builder.advanceLexer();
        builder.readOptionalNewlines();
        
        success = parseList1Element(builder, false);
        if (success) {
          start.drop(); continue;
        } 
        start.rollbackTo();
        success = true;
        markCommand = hasHeredoc;
        
        break;
      } 
      
      markCommand = false;



      
      if (next != null && simpleMode) {
        ParserUtil.errorToken((PsiBuilder)builder, "parser.unexpected.token");
        success = false;
      } 
    } 


    
    if (markCommand) {
      startMarker.done(COMPOSED_COMMAND);
    } else {
      startMarker.drop();
    } 
    
    return success;
  }
  
  private boolean parseList1Element(BashPsiBuilder builder, boolean errorOnMissingCommand) {
    OptionalParseResult result = Parsing.pipeline.parsePipelineCommand(builder, errorOnMissingCommand);
    if (!result.isValid()) {
      if (errorOnMissingCommand) {
        builder.error("Expected a command");
      }
      
      return false;
    } 
    
    return result.isParsedSuccessfully();
  }









  
  private boolean parseOptionalHeredocContent(BashPsiBuilder builder) {
    if (builder.getTokenType() == LINE_FEED && builder.getParsingState().expectsHeredocMarker()) {
      int startOffset = builder.getCurrentOffset();

      
      builder.advanceLexer();




      
      while (true) {
        while (builder.getTokenType() == LINE_FEED)
          builder.advanceLexer(); 
        if (builder.getTokenType() == HEREDOC_CONTENT) {
          ParserUtil.markTokenAndAdvance((PsiBuilder)builder, HEREDOC_CONTENT_ELEMENT); continue;
        } 
        OptionalParseResult varResult = Parsing.var.parseIfValid(builder);
        if (varResult.isValid() ? 
          !varResult.isParsedSuccessfully() : (

          
          !Parsing.shellCommand.subshellParser.isValid(builder) || 
          !Parsing.shellCommand.subshellParser.parse(builder)))
        
        { 





          
          if (builder.getTokenType() == HEREDOC_MARKER_END) {
            ParserUtil.markTokenAndAdvance((PsiBuilder)builder, HEREDOC_END_ELEMENT);
            builder.getParsingState().popHeredocMarker();
          } else if (builder.getTokenType() == HEREDOC_MARKER_IGNORING_TABS_END) {
            ParserUtil.markTokenAndAdvance((PsiBuilder)builder, HEREDOC_END_IGNORING_TABS_ELEMENT);
            builder.getParsingState().popHeredocMarker();
          } else {
            if (builder.getParsingState().expectsHeredocMarker()) {
              builder.error("Unexpected token");
            }
            
            break;
          } 
          if (!builder.getParsingState().expectsHeredocMarker())
            break;  } 
      }  return (builder.getCurrentOffset() - startOffset > 0);
    } 
    
    return false;
  }

























  
  public boolean parseSimpleList(BashPsiBuilder builder) {
    if (!parseList1(builder, true, true)) {
      return false;
    }

    
    IElementType tokenType = builder.getTokenType();
    if (tokenType == AMP || tokenType == SEMI) {
      builder.advanceLexer();
    }
    
    return true;
  }
}
