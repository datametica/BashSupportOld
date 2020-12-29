package com.ansorgit.plugins.bash.lang.valueExpansion;

import com.intellij.openapi.util.text.StringUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;


























public final class ValueExpansionUtil
{
  public static boolean isValid(String spec, boolean enhancedSyntax) {
    if (StringUtils.isEmpty(spec)) {
      return false;
    }
    
    try {
      split(spec, enhancedSyntax);
    } catch (IllegalArgumentException e) {
      return false;
    } 
    
    return true;
  }







  
  public static String expand(String spec, boolean enhancedSyntax) {
    if (!isValid(spec, enhancedSyntax)) {
      return spec;
    }
    
    return expand(split(spec, enhancedSyntax)).toString();
  }
  
  static List<Expansion> split(String spec, boolean enhancedSyntax) {
    List<Expansion> result = new LinkedList<>();
    List<String> parts = StringUtil.split(spec, "{");
    
    for (String part : parts) {
      if (part.contains("}")) {
        int endOffset = part.indexOf('}');
        
        List<String> values = evaluateExpansionPattern(part.substring(0, endOffset), enhancedSyntax);
        if (values.isEmpty())
        {
          throw new IllegalArgumentException("Invalid pattern syntax in " + spec);
        }
        
        result.add(new IteratingExpansion(values));
        
        if (endOffset + 1 < part.length())
          result.add(new StaticExpansion(part.substring(endOffset + 1))); 
        continue;
      } 
      result.add(new StaticExpansion(part));
    } 

    
    return result;
  }











  
  static List<String> evaluateExpansionPattern(String part, boolean enhancedSyntax) {
    List<String> stringList = StringUtil.split(part, ",", true, false);
    List<String> result = new LinkedList<>();

    
    if (stringList.size() == 1 && ((String)stringList.get(0)).contains("..")) {
      if (!evaluateRangeExpression(stringList.get(0), result, enhancedSyntax)) {
        return Collections.emptyList();
      }
    } else {
      for (String e : stringList) {
        result.add(e);
      }
    } 
    
    return result;
  }

  
  private static boolean evaluateRangeExpression(String rangeText, List<String> result, boolean enhancedSyntax) {
    List<String> startEnd = StringUtil.split(rangeText, "..");
    if (enhancedSyntax && (startEnd.size() > 3 || startEnd.size() < 2))
      return false; 
    if (!enhancedSyntax && startEnd.size() != 2) {
      return false;
    }
    
    String first = startEnd.get(0);
    String second = startEnd.get(1);
    String stepSpec = (enhancedSyntax && startEnd.size() == 3) ? startEnd.get(2) : "";
    int step = NumberUtils.toInt(stepSpec, 1);
    if (NumberUtils.toInt(first, 1) > NumberUtils.toInt(second, 1) && stepSpec.isEmpty()) {
      step = -1;
    }
    boolean hasValidStep = (stepSpec.isEmpty() || NumberUtils.isNumber(stepSpec));
    
    if (NumberUtils.isNumber(first) && NumberUtils.isNumber(second) && hasValidStep) {
      
      int current = NumberUtils.toInt(first);
      int end = NumberUtils.toInt(second);

      
      int padTargetWidth = -1;
      if (enhancedSyntax) {
        boolean hasPadding = (first.startsWith("0") || second.startsWith("0"));
        padTargetWidth = hasPadding ? Math.max(first.length(), second.length()) : -1;
      } 
      
      while ((step > 0) ? (current <= end) : (current >= end)) {
        String value = String.valueOf(current);
        if (padTargetWidth > value.length())
        {
          value = StringUtil.repeatSymbol('0', padTargetWidth - value.length()) + value;
        }
        
        result.add(value);
        current += step;
      } 
    } else if (first.length() == 1 && second.length() == 1 && hasValidStep) {
      
      char current = first.charAt(0);
      char end = second.charAt(0);
      
      while (current <= end) {
        result.add(String.valueOf(current));
        current = (char)(current + step);
      } 
    } else {
      
      result.add(rangeText);
    } 
    
    return true;
  }
  
  private static StringBuilder expand(List<Expansion> expansions) {
    List<Expansion> reversed = new ArrayList<>(expansions);
    Collections.reverse(reversed);
    
    StringBuilder result = new StringBuilder();
    do {
      result.append(makeLine(reversed));
      
      if (!stillMore(reversed))
        continue;  result.append(" ");
    }
    while (stillMore(reversed));
    
    return result;
  }
  
  private static boolean stillMore(List<Expansion> expansions) {
    for (Expansion expansion : expansions) {
      if (expansion.hasNext()) return true;
    
    } 
    return false;
  }
  
  private static StringBuilder makeLine(List<Expansion> reversed) {
    boolean lastFlipped = true;
    StringBuilder line = new StringBuilder();
    for (Expansion e : reversed) {
      line.insert(0, e.findNext(lastFlipped));
      lastFlipped = e.isFlipped();
    } 
    return line;
  }
}
