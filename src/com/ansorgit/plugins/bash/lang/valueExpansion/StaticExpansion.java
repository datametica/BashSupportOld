package com.ansorgit.plugins.bash.lang.valueExpansion;





















class StaticExpansion
  implements Expansion
{
  private final String value;
  private boolean lastFlipped;
  
  public StaticExpansion(String value) {
    this.value = value;
  }
  
  public String findNext(boolean previousFlipped) {
    this.lastFlipped = previousFlipped;
    return this.value;
  }
  
  public boolean isFlipped() {
    return this.lastFlipped;
  }
  
  public boolean hasNext() {
    return false;
  }
}
