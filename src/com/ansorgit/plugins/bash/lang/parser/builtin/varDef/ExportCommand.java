package com.ansorgit.plugins.bash.lang.parser.builtin.varDef;

import com.ansorgit.plugins.bash.lang.parser.BashPsiBuilder;
import com.ansorgit.plugins.bash.lang.parser.Parsing;
import com.ansorgit.plugins.bash.lang.parser.ParsingTool;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;




















class ExportCommand
  extends AbstractVariableDefParsing
  implements ParsingTool
{
  ExportCommand() {
    super(true, GENERIC_COMMAND_ELEMENT, "export", true, false);
  }


  
  boolean argumentValueExpected(String name) {
    return "-f".equals(name);
  }

  
  protected boolean parseArgumentValue(String argName, BashPsiBuilder builder) {
    if ("-f".equals(argName)) {
      IElementType token = builder.getTokenType();
      if (token == STRING2 || token == WORD || Parsing.word.isSimpleComposedString(builder, false)) {
        return parseFunctionName(builder);
      }
    } 
    
    return super.parseArgumentValue(argName, builder);
  }
  
  private boolean parseFunctionName(BashPsiBuilder builder) {
    PsiBuilder.Marker marker = builder.mark();
    PsiBuilder.Marker markerInner = builder.mark();
    
    if (Parsing.word.parseWordIfValid(builder).isParsedSuccessfully()) {
      markerInner.done(GENERIC_COMMAND_ELEMENT);
      marker.done((IElementType)SIMPLE_COMMAND_ELEMENT);
      return true;
    } 
    
    markerInner.drop();
    marker.drop();
    return false;
  }
}
