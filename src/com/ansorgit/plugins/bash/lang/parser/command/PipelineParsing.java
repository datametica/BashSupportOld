package com.ansorgit.plugins.bash.lang.parser.command;

import com.ansorgit.plugins.bash.lang.parser.BashPsiBuilder;
import com.ansorgit.plugins.bash.lang.parser.OptionalParseResult;
import com.ansorgit.plugins.bash.lang.parser.Parsing;
import com.ansorgit.plugins.bash.lang.parser.ParsingTool;
import com.ansorgit.plugins.bash.lang.parser.util.ParserUtil;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;






























public class PipelineParsing
  implements ParsingTool
{
  private TimespecState parseOptionalTimespec(BashPsiBuilder builder) {
    boolean hasTimespec = (builder.getTokenType() == TIME_KEYWORD);
    if (hasTimespec && !parseTimespecPart(builder)) {
      return TimespecState.Error;
    }
    
    return hasTimespec ? TimespecState.Ok : TimespecState.NotAvailable;
  }
  
  public OptionalParseResult parsePipelineCommand(BashPsiBuilder builder, boolean errorOnMissingCommand) {
    if (builder.eof() || Parsing.list.isListTerminator(builder.getTokenType())) {
      return OptionalParseResult.Invalid;
    }
    
    PsiBuilder.Marker pipelineCommandMarker = builder.mark();
    
    boolean hasBang = ParserUtil.isWord(builder, "!");
    if (hasBang) {
      builder.advanceLexer();
    }
    
    TimespecState timespecState = parseOptionalTimespec(builder);
    if (timespecState.equals(TimespecState.Error)) {
      pipelineCommandMarker.drop();
      return OptionalParseResult.ParseError;
    } 
    
    if (!hasBang && ParserUtil.isWord(builder, "!")) {
      builder.advanceLexer();
      hasBang = true;
      
      if (timespecState.equals(TimespecState.NotAvailable)) {
        timespecState = parseOptionalTimespec(builder);
      }
    } 
    
    if (Parsing.list.isListTerminator(builder.getTokenType())) {

      
      if (!hasBang && timespecState.equals(TimespecState.Ok)) {
        
        builder.advanceLexer();
        pipelineCommandMarker.drop();
        
        return OptionalParseResult.Ok;
      } 
      if (errorOnMissingCommand) {
        builder.error("Expected a command.");
      }
      pipelineCommandMarker.drop();
      builder.advanceLexer();
      
      return OptionalParseResult.ParseError;
    } 

    
    ParseState parseState = parsePipeline(builder);
    switch (parseState) {
      case OK_PIPELINE:
        pipelineCommandMarker.done(PIPELINE_COMMAND);
        return OptionalParseResult.Ok;
      case OK_NO_PIPELINE:
        pipelineCommandMarker.drop();
        return OptionalParseResult.Ok;
      case ERROR:
        pipelineCommandMarker.drop();
        if (errorOnMissingCommand) {
          builder.error("Expected a command.");
        }
        return OptionalParseResult.ParseError;
    } 
    pipelineCommandMarker.drop();
    throw new IllegalStateException("Unexpected ParseState value" + parseState);
  }








  
  private ParseState parsePipeline(BashPsiBuilder builder) {
    OptionalParseResult result = Parsing.command.parseIfValid(builder);
    if (!result.isParsedSuccessfully()) {
      return ParseState.ERROR;
    }
    
    boolean withPipeline = pipeTokens.contains(builder.getTokenType());
    if (!withPipeline) {
      return ParseState.OK_NO_PIPELINE;
    }
    
    while (result.isParsedSuccessfully() && pipeTokens.contains(builder.getTokenType())) {
      builder.advanceLexer();
      builder.readOptionalNewlines();
      
      result = Parsing.command.parseIfValid(builder);
    } 
    
    if (!result.isParsedSuccessfully()) {
      return ParseState.ERROR;
    }
    
    return ParseState.OK_PIPELINE;
  }
  
  boolean parseTimespecPart(BashPsiBuilder builder) {
    PsiBuilder.Marker time = builder.mark();
    
    IElementType timeToken = ParserUtil.getTokenAndAdvance((PsiBuilder)builder);
    if (timeToken != TIME_KEYWORD) {
      ParserUtil.error(time, "parser.time.expected.time");
      return false;
    } 
    
    if (ParserUtil.isWordToken(builder.getTokenType()) && "-p".equals(builder.getTokenText())) {
      builder.advanceLexer();
    }
    
    time.done(TIME_COMMAND);
    return true;
  }
  
  private enum ParseState {
    OK_PIPELINE,
    OK_NO_PIPELINE,
    ERROR;
  }
  
  private enum TimespecState {
    Ok, NotAvailable, Error;
  }
}
