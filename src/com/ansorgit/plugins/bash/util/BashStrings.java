package com.ansorgit.plugins.bash.util;

import com.intellij.CommonBundle;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.ResourceBundle;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.PropertyKey;






















public class BashStrings
{
  private static Reference<ResourceBundle> ourBundle;
  @NonNls
  private static final String BUNDLE = "com.ansorgit.plugins.bash.bash";
  
  public static String message(@PropertyKey(resourceBundle = "com.ansorgit.plugins.bash.bash") String key, Object... params) {
    return CommonBundle.message(getBundle(), key, params);
  }
  
  private static ResourceBundle getBundle() {
    ResourceBundle bundle = null;
    
    if (ourBundle != null) {
      bundle = ourBundle.get();
    }
    
    if (bundle == null) {
      bundle = ResourceBundle.getBundle("com.ansorgit.plugins.bash.bash");
      ourBundle = new SoftReference<>(bundle);
    } 
    
    return bundle;
  }
}
