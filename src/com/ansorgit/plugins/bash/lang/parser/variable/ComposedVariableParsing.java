package com.ansorgit.plugins.bash.lang.parser.variable;

import com.ansorgit.plugins.bash.lang.parser.BashPsiBuilder;
import com.ansorgit.plugins.bash.lang.parser.OptionalParseResult;
import com.ansorgit.plugins.bash.lang.parser.Parsing;
import com.ansorgit.plugins.bash.lang.parser.ParsingFunction;
import com.ansorgit.plugins.bash.lang.parser.misc.ShellCommandParsing;
import com.ansorgit.plugins.bash.lang.parser.util.ParserUtil;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;




















public class ComposedVariableParsing
  implements ParsingFunction
{
  private static final TokenSet acceptedStarts = TokenSet.create(new IElementType[] { LEFT_CURLY, LEFT_PAREN, EXPR_ARITH, EXPR_CONDITIONAL, EXPR_ARITH_SQUARE });


  
  public boolean isValid(BashPsiBuilder builder) {
    if (builder.rawLookup(0) != DOLLAR) {
      return false;
    }
    
    return acceptedStarts.contains(builder.rawLookup(1));
  }
  public boolean parse(BashPsiBuilder builder) {
    boolean ok;
    PsiBuilder.Marker varMarker = builder.mark();
    builder.advanceLexer();
    
    if (builder.getTokenType(true) == WHITESPACE) {
      varMarker.drop();
      return false;
    } 


    
    OptionalParseResult result = Parsing.parameterExpansionParsing.parseIfValid(builder);
    if (result.isValid()) {
      ok = result.isParsedSuccessfully();
    } else {
      result = ShellCommandParsing.arithmeticParser.parseIfValid(builder);
      if (result.isValid()) {
        ok = result.isParsedSuccessfully();
      } else {
        result = Parsing.shellCommand.subshellParser.parseIfValid(builder);
        if (result.isValid()) {
          ok = result.isParsedSuccessfully();
        } else {
          ParserUtil.error(varMarker, "parser.unexpected.token");
          return false;
        } 
      } 
    } 
    
    if (ok) {
      varMarker.done(VAR_COMPOSED_VAR_ELEMENT);
    } else {
      varMarker.drop();
    } 
    
    return true;
  }
}
