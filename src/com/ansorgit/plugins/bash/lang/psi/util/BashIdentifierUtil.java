package com.ansorgit.plugins.bash.lang.psi.util;

import java.util.regex.Pattern;


















public final class BashIdentifierUtil
{
  private static final Pattern newVariablePattern = Pattern.compile("[a-zA-Z_][a-zA-Z0-9_]*");
  private static final Pattern singleIdentifier = Pattern.compile("[@$?!*#-]");
  private static final Pattern anyIdentifier = Pattern.compile("[0-9]+|([a-zA-Z_][a-zA-Z0-9_]*)");
  private static final Pattern functionIdentifier = Pattern.compile("[a-zA-Z0-9_-]+");
  private static final Pattern heredocMarker = Pattern.compile("[^ ]+");
  
  private static final Pattern number = Pattern.compile("[0-9]+");



  
  public static boolean isValidNewVariableName(String text) {
    return (text != null && newVariablePattern.matcher(text).matches());
  }
  
  public static boolean isValidFunctionName(String text) {
    return (text != null && functionIdentifier.matcher(text).matches() && !number.matcher(text).matches());
  }
  
  public static boolean isValidVariableName(CharSequence text) {
    return (text != null && (singleIdentifier.matcher(text).matches() || anyIdentifier.matcher(text).matches()));
  }
  
  public static boolean isValidHeredocIdentifier(CharSequence text) {
    return (text != null && heredocMarker.matcher(text).matches());
  }
}
