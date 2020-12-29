package com.ansorgit.plugins.bash.lang.parser.misc;

import com.ansorgit.plugins.bash.lang.parser.BashPsiBuilder;




















final class RecursionGuard
{
  private final int max;
  private int level = 0;
  
  private RecursionGuard(int max) {
    this.max = max;
  }


  
  public static RecursionGuard initial() {
    return initial(1000);
  }
  
  public static RecursionGuard initial(int maxNestingLevel) {
    return new RecursionGuard(maxNestingLevel);
  }
  
  public boolean next(BashPsiBuilder builder) {
    if (++this.level > this.max) {
      builder.error("Internal parser error: Maximum level '" + this.max + "' of nested calls reached. Please report this at https://github.com/jansorg/BashSupport/issues");
      return false;
    } 
    
    return true;
  }
}
