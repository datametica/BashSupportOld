package com.ansorgit.plugins.bash.lang.lexer;

import com.intellij.util.containers.Stack;























final class StringLexingstate
{
  private final Stack<SubshellState> subshells = new Stack(5);
  
  void enterString() {
    if (!this.subshells.isEmpty()) {
      ((SubshellState)this.subshells.peek()).enterString();
    }
  }
  
  void leaveString() {
    if (!this.subshells.isEmpty()) {
      ((SubshellState)this.subshells.peek()).leaveString();
    }
  }
  
  boolean isInSubstring() {
    return (!this.subshells.isEmpty() && ((SubshellState)this.subshells.peek()).isInString());
  }
  
  boolean isSubstringAllowed() {
    return (!this.subshells.isEmpty() && !((SubshellState)this.subshells.peek()).isInString());
  }
  
  boolean isInSubshell() {
    return !this.subshells.isEmpty();
  }
  
  void enterSubshell() {
    this.subshells.push(new SubshellState());
  }
  
  void leaveSubshell() {
    assert !this.subshells.isEmpty();
    
    this.subshells.pop();
  }
  
  private static final class SubshellState {
    private int inString = 0;
    
    boolean isInString() {
      return (this.inString > 0);
    }
    
    void enterString() {
      this.inString++;
    }
    
    void leaveString() {
      assert this.inString > 0 : "The inString stack should not be empty";
      this.inString--;
    }
    
    private SubshellState() {}
  }
}
