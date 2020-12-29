package com.ansorgit.plugins.bash.lang.parser;

import java.util.HashSet;
import java.util.Set;























public final class ParsingStateData
{
  private int inSimpleCommand = 0;
  private int heredocMarkers = 0;
  private final Set<Integer> heredocMarkersIndexSet = new HashSet<>();
  
  public void enterSimpleCommand() {
    this.inSimpleCommand++;
  }
  
  public void leaveSimpleCommand() {
    this.inSimpleCommand--;
  }
  
  public boolean isInSimpleCommand() {
    return (this.inSimpleCommand > 0);
  }
  
  public void pushHeredocMarker(int id) {
    if (!this.heredocMarkersIndexSet.contains(Integer.valueOf(id))) {
      this.heredocMarkers++;
      this.heredocMarkersIndexSet.add(Integer.valueOf(id));
    } 
  }
  
  public boolean expectsHeredocMarker() {
    return (this.heredocMarkers > 0);
  }
  
  public void popHeredocMarker() {
    this.heredocMarkers--;
    if (this.heredocMarkers <= 0) {
      this.heredocMarkersIndexSet.clear();
    }
  }
  
  public Set<Integer> getHeredocMarkersIndexSet() {
    return this.heredocMarkersIndexSet;
  }
}
