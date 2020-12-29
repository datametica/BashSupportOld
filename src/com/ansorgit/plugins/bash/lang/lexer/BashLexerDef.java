package com.ansorgit.plugins.bash.lang.lexer;

import com.intellij.lexer.FlexLexer;

public interface BashLexerDef extends BashTokenTypes, FlexLexer {
  boolean isBash4();
  
  void goToState(int paramInt);
  
  void backToPreviousState();
  
  void popStates(int paramInt);
  
  boolean isInState(int paramInt);
  
  int openParenthesisCount();
  
  void incOpenParenthesisCount();
  
  void decOpenParenthesisCount();
  
  boolean isParamExpansionHash();
  
  void setParamExpansionHash(boolean paramBoolean);
  
  boolean isParamExpansionWord();
  
  void setParamExpansionWord(boolean paramBoolean);
  
  boolean isParamExpansionOther();
  
  void setParamExpansionOther(boolean paramBoolean);
  
  boolean isInCaseBody();
  
  void setInCaseBody(boolean paramBoolean);
  
  StringLexingstate stringParsingState();
  
  boolean isEmptyConditionalCommand();
  
  void setEmptyConditionalCommand(boolean paramBoolean);
  
  HeredocLexingState heredocState();
  
  boolean isInHereStringContent();
  
  void enterHereStringContent();
  
  void leaveHereStringContent();
}
