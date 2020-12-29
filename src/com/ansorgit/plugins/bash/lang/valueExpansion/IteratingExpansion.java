package com.ansorgit.plugins.bash.lang.valueExpansion;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;



















class IteratingExpansion
  implements Expansion
{
  private final List<String> values = new LinkedList<>();
  private Iterator<String> valueIterator;
  private String currentValue;
  private int index = 0;
  private int count = 0;
  private boolean isFlipped = false;
  
  public IteratingExpansion(List<String> values) {
    this.values.addAll(values);
    this.valueIterator = this.values.iterator();
    this.currentValue = this.valueIterator.hasNext() ? this.valueIterator.next() : null;
  }






  
  public String findNext(boolean previousFlipped) {
    boolean resetIsFlipped = true;
    
    if (previousFlipped && this.count > 0) {
      if (!this.valueIterator.hasNext()) {
        this.valueIterator = this.values.iterator();
        this.index = 0;
        this.isFlipped = true;
        resetIsFlipped = false;
      } else {
        this.index++;
      } 
      
      this.currentValue = this.valueIterator.next();
    } 
    
    this.count++;
    
    if (resetIsFlipped) {
      this.isFlipped = false;
    }
    
    return this.currentValue;
  }
  
  public boolean isFlipped() {
    return (this.index == 0 && this.isFlipped);
  }
  
  public boolean hasNext() {
    return this.valueIterator.hasNext();
  }
}
