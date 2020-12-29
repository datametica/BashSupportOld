package com.ansorgit.plugins.bash.lang.parser;


















public final class BackquoteData
{
  private boolean inBackquote = false;
  
  public void enterBackquote() {
    this.inBackquote = true;
  }
  
  public void leaveBackquote() {
    this.inBackquote = false;
  }
  
  public boolean isInBackquote() {
    return this.inBackquote;
  }
}
