package com.ansorgit.plugins.bash.lang.util;

import com.intellij.openapi.util.Pair;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;




















public class HeredocSharedImpl
{
  public static String cleanMarker(String marker, boolean ignoredLeadingTabs) {
    String markerText = trimNewline(marker);
    if (markerText.equals("$")) {
      return markerText;
    }
    
    Pair<Integer, Integer> offsets = getStartEndOffsets(markerText, ignoredLeadingTabs);
    int start = ((Integer)offsets.first).intValue();
    int end = ((Integer)offsets.second).intValue();
    
    return (end <= markerText.length() && start < end) ? markerText.substring(start, end) : marker;
  }
  
  public static int startMarkerTextOffset(String markerText, boolean ignoredLeadingTabs) {
    return ((Integer)(getStartEndOffsets(markerText, ignoredLeadingTabs)).first).intValue();
  }
  
  public static int endMarkerTextOffset(String markerText) {
    return ((Integer)(getStartEndOffsets(markerText, false)).second).intValue();
  }
  
  public static boolean isEvaluatingMarker(String marker) {
    String markerText = trimNewline(marker);
    
    return (!markerText.startsWith("\"") && !markerText.startsWith("'") && !markerText.startsWith("\\") && !markerText.startsWith("$"));
  }
  
  private static String trimNewline(String marker) {
    return StringUtils.removeEnd(marker, "\n");
  }
  
  public static String wrapMarker(String newName, String originalMarker) {
    Pair<Integer, Integer> offsets = getStartEndOffsets(originalMarker, true);
    int start = ((Integer)offsets.first).intValue();
    int end = ((Integer)offsets.second).intValue();
    
    return (end <= originalMarker.length() && start < end) ? (originalMarker
      .substring(0, start) + newName + originalMarker.substring(end)) : newName;
  }

  
  private static Pair<Integer, Integer> getStartEndOffsets(@NotNull String markerText, boolean ignoredLeadingTabs) {
    if (markerText == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "markerText", "com/ansorgit/plugins/bash/lang/util/HeredocSharedImpl", "getStartEndOffsets" }));  if (markerText.isEmpty()) {
      return Pair.create(Integer.valueOf(0), Integer.valueOf(0));
    }
    
    if (markerText.length() == 1) {
      return Pair.create(Integer.valueOf(0), Integer.valueOf(1));
    }
    
    if (markerText.charAt(0) == '\\' && markerText.length() > 1) {
      return Pair.create(Integer.valueOf(1), Integer.valueOf(markerText.length()));
    }
    
    int length = markerText.length();
    int start = 0;
    int end = length - 1;
    
    while (ignoredLeadingTabs && start < length - 1 && markerText.charAt(start) == '\t') {
      start++;
    }
    
    if (markerText.charAt(start) == '$' && length > start + 2 && (markerText.charAt(start + 1) == '"' || markerText.charAt(end) == '\'')) {
      start++;
      length--;
    } 
    
    while (end > 0 && markerText.charAt(end) == '\n') {
      end--;
    }
    
    if (length > 0 && (markerText.charAt(start) == '\'' || markerText.charAt(start) == '"') && markerText.charAt(end) == markerText.charAt(start)) {
      start++;
      end--;
      length -= 2;
    } 
    
    return Pair.create(Integer.valueOf(start), Integer.valueOf(end + 1));
  }
}
