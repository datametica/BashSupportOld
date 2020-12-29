package com.ansorgit.plugins.bash.lang.parser.builtin;

import com.ansorgit.plugins.bash.lang.parser.BashElementTypes;
import com.ansorgit.plugins.bash.lang.parser.BashPsiBuilder;
import com.ansorgit.plugins.bash.lang.parser.ParsingFunction;
import com.ansorgit.plugins.bash.lang.parser.ParsingTool;
import com.ansorgit.plugins.bash.lang.parser.command.CommandParsingUtil;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;




















class LetCommand
  implements ParsingFunction, ParsingTool
{
  public static final TokenSet VALID_EXTRA_TOKENS = TokenSet.create(new IElementType[] { EQ, ADD_EQ, ARITH_NUMBER, ARITH_PLUS, ARITH_ASS_PLUS });

  
  public boolean isValid(BashPsiBuilder builder) {
    return (builder.getTokenType() == LET_KEYWORD);
  }

  
  public boolean parse(BashPsiBuilder builder) {
    PsiBuilder.Marker marker = builder.mark();

    
    builder.advanceLexer();
    
    PsiBuilder.Marker letExpressionMarker = builder.mark();

    
    boolean paramsAreFine = CommandParsingUtil.readCommandParams(builder, VALID_EXTRA_TOKENS);
    
    if (paramsAreFine) {
      letExpressionMarker.collapse(BashElementTypes.LET_LAZY_EXPRESSION);
      marker.done(LET_COMMAND);
    } else {
      letExpressionMarker.drop();
      marker.drop();
      builder.error("Expected an arithmetic expression");
    } 
    
    return true;
  }
}
