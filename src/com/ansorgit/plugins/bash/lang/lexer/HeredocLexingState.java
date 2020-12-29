package com.ansorgit.plugins.bash.lang.lexer;

import com.ansorgit.plugins.bash.lang.util.HeredocSharedImpl;
import com.google.common.collect.Lists;
import java.util.LinkedList;




















final class HeredocLexingState
{
  private final LinkedList<HeredocMarkerInfo> expectedHeredocs = Lists.newLinkedList();
  
  public boolean isEmpty() {
    return this.expectedHeredocs.isEmpty();
  }
  
  boolean isNextMarker(CharSequence markerText) {
    return (!this.expectedHeredocs.isEmpty() && ((HeredocMarkerInfo)this.expectedHeredocs.peekFirst()).nameEquals(markerText));
  }
  
  boolean isExpectingEvaluatingHeredoc() {
    if (isEmpty()) {
      throw new IllegalStateException("isExpectingEvaluatingHeredoc called on an empty marker stack");
    }
    
    return (!this.expectedHeredocs.isEmpty() && ((HeredocMarkerInfo)this.expectedHeredocs.peekFirst()).evaluating);
  }
  
  boolean isIgnoringTabs() {
    if (isEmpty()) {
      throw new IllegalStateException("isIgnoringTabs called on an empty marker stack");
    }
    
    return (!this.expectedHeredocs.isEmpty() && ((HeredocMarkerInfo)this.expectedHeredocs.peekFirst()).ignoreLeadingTabs);
  }

  
  void removeMarker(long offset) {
    this.expectedHeredocs.removeIf(info -> (info.offset == offset));
  }
  
  void pushMarker(long offset, CharSequence marker, boolean ignoreTabs) {
    if (offset >= 0L)
    {
      removeMarker(offset);
    }
    this.expectedHeredocs.add(new HeredocMarkerInfo(offset, marker, ignoreTabs));
  }
  
  void popMarker(CharSequence marker) {
    if (!isNextMarker(HeredocSharedImpl.cleanMarker(marker.toString(), false))) {
      throw new IllegalStateException("Heredoc marker isn't expected to be removed: " + marker);
    }
    
    this.expectedHeredocs.removeFirst();
  }
  
  private static class HeredocMarkerInfo {
    final boolean ignoreLeadingTabs;
    final boolean evaluating;
    final CharSequence markerName;
    private final long offset;
    
    HeredocMarkerInfo(long offset, CharSequence markerText, boolean ignoreLeadingTabs) {
      String markerTextString = markerText.toString();
      
      this.offset = offset;
      this.markerName = HeredocSharedImpl.cleanMarker(markerTextString, ignoreLeadingTabs);
      this.evaluating = HeredocSharedImpl.isEvaluatingMarker(markerTextString);
      this.ignoreLeadingTabs = ignoreLeadingTabs;
    }
    
    boolean nameEquals(CharSequence markerText) {
      return this.markerName.equals(HeredocSharedImpl.cleanMarker(markerText.toString(), this.ignoreLeadingTabs));
    }

    
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      
      HeredocMarkerInfo that = (HeredocMarkerInfo)o;
      
      if (this.ignoreLeadingTabs != that.ignoreLeadingTabs) {
        return false;
      }
      if (this.evaluating != that.evaluating) {
        return false;
      }
      return (this.markerName != null) ? this.markerName.equals(that.markerName) : ((that.markerName == null));
    }


    
    public int hashCode() {
      int result = this.ignoreLeadingTabs ? 1 : 0;
      result = 31 * result + (this.evaluating ? 1 : 0);
      result = 31 * result + ((this.markerName != null) ? this.markerName.hashCode() : 0);
      return result;
    }

    
    public String toString() {
      return "HeredocMarkerInfo{offset=" + this.offset + ", markerName=" + this.markerName + '}';
    }
  }
}
