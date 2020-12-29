package com.ansorgit.plugins.bash.lang.lexer;

import com.ansorgit.plugins.bash.lang.BashVersion;
import com.ansorgit.plugins.bash.util.IntStack;
import com.intellij.psi.tree.IElementType;
import java.io.IOException;
import java.io.Reader;













final class _BashLexer
  extends _BashLexerBase
  implements BashLexerDef
{
  private final IntStack lastStates = new IntStack(25);
  
  private final StringLexingstate string = new StringLexingstate();
  private final HeredocLexingState heredocState = new HeredocLexingState();
  
  private boolean paramExpansionHash = false;
  private boolean paramExpansionWord = false;
  private boolean paramExpansionOther = false;
  private int openParenths = 0;
  
  private boolean isBash4 = false;
  
  private boolean inCaseBody = false;
  private boolean emptyConditionalCommand = false;
  private boolean inHereString = false;
  
  _BashLexer(BashVersion version, Reader in) {
    super(in);
    
    this.isBash4 = BashVersion.Bash_v4.equals(version);
  }


  
  public IElementType advance() throws IOException {
    try {
      return super.advance();
    } catch (Error e) {
      
      throw new Error("Error lexing Bash file:\n" + getBuffer(), e);
    } 
  }

  
  public HeredocLexingState heredocState() {
    return this.heredocState;
  }

  
  public boolean isInHereStringContent() {
    return this.inHereString;
  }

  
  public void enterHereStringContent() {
    assert !this.inHereString : "inHereString must be false when entering a here string";
    
    this.inHereString = true;
  }

  
  public void leaveHereStringContent() {
    this.inHereString = false;
  }

  
  public boolean isEmptyConditionalCommand() {
    return this.emptyConditionalCommand;
  }

  
  public void setEmptyConditionalCommand(boolean emptyConditionalCommand) {
    this.emptyConditionalCommand = emptyConditionalCommand;
  }

  
  public StringLexingstate stringParsingState() {
    return this.string;
  }

  
  public boolean isInCaseBody() {
    return this.inCaseBody;
  }

  
  public void setInCaseBody(boolean inCaseBody) {
    this.inCaseBody = inCaseBody;
  }

  
  public boolean isBash4() {
    return this.isBash4;
  }




  
  public void goToState(int newState) {
    this.lastStates.push(yystate());
    yybegin(newState);
  }





  
  public void backToPreviousState() {
    yybegin(this.lastStates.pop());
  }

  
  public void popStates(int lastStateToPop) {
    if (yystate() == lastStateToPop) {
      backToPreviousState();
      
      return;
    } 
    while (isInState(lastStateToPop)) {
      boolean finished = (yystate() == lastStateToPop);
      backToPreviousState();
      
      if (finished) {
        break;
      }
    } 
  }

  
  public boolean isInState(int state) {
    return ((state == 0 && this.lastStates.empty()) || this.lastStates.contains(state));
  }

  
  public int openParenthesisCount() {
    return this.openParenths;
  }

  
  public void incOpenParenthesisCount() {
    this.openParenths++;
  }

  
  public void decOpenParenthesisCount() {
    this.openParenths--;
  }

  
  public boolean isParamExpansionWord() {
    return this.paramExpansionWord;
  }

  
  public void setParamExpansionWord(boolean paramExpansionWord) {
    this.paramExpansionWord = paramExpansionWord;
  }

  
  public boolean isParamExpansionOther() {
    return this.paramExpansionOther;
  }

  
  public void setParamExpansionOther(boolean paramExpansionOther) {
    this.paramExpansionOther = paramExpansionOther;
  }

  
  public boolean isParamExpansionHash() {
    return this.paramExpansionHash;
  }

  
  public void setParamExpansionHash(boolean paramExpansionHash) {
    this.paramExpansionHash = paramExpansionHash;
  }
}
