package com.ansorgit.plugins.bash.lang.parser.builtin.varDef;

import com.ansorgit.plugins.bash.lang.parser.BashElementTypes;
import com.ansorgit.plugins.bash.lang.parser.BashPsiBuilder;
import com.ansorgit.plugins.bash.lang.parser.OptionalParseResult;
import com.ansorgit.plugins.bash.lang.parser.ParsingFunction;
import com.ansorgit.plugins.bash.lang.parser.command.CommandParsingUtil;
import com.google.common.collect.Maps;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import java.util.Map;
























public class BuiltinVarCommandParser
  implements ParsingFunction
{
  private final Map<String, AbstractVariableDefParsing> cmdMapping = Maps.newHashMap();
  
  public BuiltinVarCommandParser() {
    add(new ExportCommand());
    add(new ReadonlyCommand());
    add(new DeclareCommand());
    add(new MapfileCommand());
    add(new ReadarrayCommand());
    add(new TypesetCommand());
    add(new ReadCommand());
    add(new LocalCommand());
  }
  
  private void add(AbstractVariableDefParsing cmd) {
    this.cmdMapping.put(cmd.getCommandName(), cmd);
  }

  
  public boolean isValid(BashPsiBuilder builder) {
    throw new UnsupportedOperationException("use parseIfValid");
  }

  
  public boolean parse(BashPsiBuilder builder) {
    return parseIfValid(builder).isParsedSuccessfully();
  }

  
  public OptionalParseResult parseIfValid(BashPsiBuilder builder) {
    PsiBuilder.Marker cmdMarker = builder.mark();


    
    OptionalParseResult result = CommandParsingUtil.readAssignmentsAndRedirectsIfValid(builder, false, CommandParsingUtil.Mode.StrictAssignmentMode, true);
    if (result.isValid() && !result.isParsedSuccessfully()) {
      
      cmdMarker.drop();
      return OptionalParseResult.ParseError;
    } 


    
    String cmdName = builder.getTokenText();
    
    AbstractVariableDefParsing parsingFunction = this.cmdMapping.get(cmdName);
    if (parsingFunction == null) {
      cmdMarker.rollbackTo();
      return OptionalParseResult.Invalid;
    } 

    
    cmdMarker.rollbackTo();
    cmdMarker = builder.mark();
    
    result = parsingFunction.parseIfValid(builder);
    if (!result.isParsedSuccessfully()) {
      cmdMarker.drop();
    } else {
      cmdMarker.done((IElementType)BashElementTypes.SIMPLE_COMMAND_ELEMENT);
    } 
    return result;
  }
}
