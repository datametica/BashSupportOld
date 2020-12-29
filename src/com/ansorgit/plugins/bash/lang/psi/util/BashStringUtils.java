package com.ansorgit.plugins.bash.lang.psi.util;

import org.apache.commons.lang.ArrayUtils;


















public final class BashStringUtils
{
  private static final char[] EMPTY = new char[0];



  
  public static int countPrefixChars(String data, char prefixChar) {
    int count = 0;
    for (int i = 0; i < data.length() && data.charAt(i) == prefixChar; i++) {
      count++;
    }
    
    return count;
  }
  
  public static String escape(CharSequence content, char escapedChar) {
    return escape(content, escapedChar, EMPTY);
  }








  
  public static String escape(CharSequence content, char escapedChar, char[] ignoredIfFollowedBy) {
    StringBuilder builder = new StringBuilder();
    
    char last = Character.MIN_VALUE;
    for (int i = 0; i < content.length(); i++) {
      char current = content.charAt(i);
      
      if (current == escapedChar && last != '\\' && (
        i == content.length() - 1 || !ArrayUtils.contains(ignoredIfFollowedBy, content.charAt(i + 1)))) {
        builder.append('\\');
      }

      
      builder.append(current);
      
      last = (escapedChar == '\\' && current == '\\') ? Character.MIN_VALUE : current;
    } 
    
    return builder.toString();
  }
}
