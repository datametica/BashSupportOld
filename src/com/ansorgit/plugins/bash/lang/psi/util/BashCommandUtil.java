package com.ansorgit.plugins.bash.lang.psi.util;


























public final class BashCommandUtil
{
  public static boolean isParameterDefined(String parameterName, String argumentValue) {
    if (parameterName.equals(argumentValue)) {
      return true;
    }

    
    if (isSimpleArg(parameterName) && isSimpleArg(argumentValue) && parameterName.length() == 2) {
      return argumentValue.contains(parameterName.substring(1));
    }
    
    return false;
  }
  
  private static boolean isSimpleArg(String parameterName) {
    return (parameterName.startsWith("-") && !parameterName.startsWith("--"));
  }
}
