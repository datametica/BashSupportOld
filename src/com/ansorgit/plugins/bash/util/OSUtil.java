package com.ansorgit.plugins.bash.util;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

























public final class OSUtil
{
  private static final String CYGWIN_PREFIX = "/cygdrive/";
  
  public static String toBashCompatible(String path) {
    path = StringUtils.replace(path, File.separator, "/");
    if (path.length() > 3 && path.substring(1, 3).equals(":/")) {
      path = "/cygdrive/" + path.substring(0, 1) + path.substring(2);
    }
    
    return path;
  }
  
  public static String bashCompatibleToNative(String cygwinPath) {
    if (cygwinPath.startsWith("/cygdrive/") && cygwinPath.length() > "/cygdrive/".length() + 2) {
      String driveLetter = cygwinPath.substring("/cygdrive/".length(), "/cygdrive/".length() + 1);
      
      return driveLetter + ":" + File.separator + StringUtils.replace(cygwinPath.substring("/cygwin/".length() + 4), "/", File.separator);
    } 
    
    return cygwinPath;
  }
  
  @Nullable
  public static String findBestExecutable(@NotNull String commandName) {
    if (commandName == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "commandName", "com/ansorgit/plugins/bash/util/OSUtil", "findBestExecutable" }));  String[] pathElements = StringUtils.split(System.getenv("PATH"), File.pathSeparatorChar);
    if (pathElements == null) {
      return null;
    }
    
    return findBestExecutable(commandName, Arrays.asList(pathElements));
  }
  
  @Nullable
  public static String findBestExecutable(@NotNull String commandName, @NotNull List<String> paths) {
    if (commandName == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "commandName", "com/ansorgit/plugins/bash/util/OSUtil", "findBestExecutable" }));  if (paths == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "paths", "com/ansorgit/plugins/bash/util/OSUtil", "findBestExecutable" }));  for (String path : paths) {
      String executablePath = findExecutable(commandName, path);
      if (executablePath != null) {
        return executablePath;
      }
    } 
    
    return null;
  }
  
  @Nullable
  public static String findExecutable(@NotNull String commandName, String path) {
    if (commandName == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "commandName", "com/ansorgit/plugins/bash/util/OSUtil", "findExecutable" }));  String fullPath = path + File.separatorChar + commandName;
    File command = new File(fullPath);
    return command.exists() ? command.getAbsolutePath() : null;
  }
}
