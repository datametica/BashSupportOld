package com.ansorgit.plugins.bash.util;

import com.google.common.collect.Lists;
import com.intellij.openapi.util.SystemInfoRt;
import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import org.jetbrains.annotations.NotNull;






























public final class CompletionUtil
{
  @NotNull
  public static List<String> completeAbsolutePath(@NotNull String prefix, Predicate<File> accept) {
    File basePath;
    String matchPrefix;
    if (prefix == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "prefix", "com/ansorgit/plugins/bash/util/CompletionUtil", "completeAbsolutePath" }));  String nativePath = (prefix.startsWith("/") && SystemInfoRt.isWindows) ? OSUtil.bashCompatibleToNative(prefix) : prefix;
    
    File base = new File(nativePath);

    
    boolean dotSuffix = (prefix.endsWith(".") && !prefix.startsWith("."));
    if (!base.exists() || dotSuffix) {
      base = base.getParentFile();
      if (base == null || !base.exists()) {
        if (Collections.emptyList() == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/util/CompletionUtil", "completeAbsolutePath" }));  return (List)Collections.emptyList();
      } 
    } 


    
    if (base.isDirectory()) {
      basePath = base;
      matchPrefix = "";
    } else {
      basePath = base.getParentFile();
      matchPrefix = base.getName();
    } 

    
    List<String> result = Lists.newLinkedList();
    
    for (File fileCandidate : collectFiles(basePath, matchPrefix)) {
      String resultPath; if (!accept.test(fileCandidate)) {
        continue;
      }

      
      if (fileCandidate.isDirectory()) {
        resultPath = fileCandidate.getAbsolutePath() + File.separator;
      } else {
        resultPath = fileCandidate.getAbsolutePath();
      } 
      
      result.add(OSUtil.toBashCompatible(resultPath));
    } 
    
    if (result == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/util/CompletionUtil", "completeAbsolutePath" }));  return result;
  }








  
  @NotNull
  public static List<String> completeRelativePath(@NotNull String baseDir, @NotNull String shownBaseDir, @NotNull String relativePath) {
    if (baseDir == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "baseDir", "com/ansorgit/plugins/bash/util/CompletionUtil", "completeRelativePath" }));  if (shownBaseDir == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "shownBaseDir", "com/ansorgit/plugins/bash/util/CompletionUtil", "completeRelativePath" }));  if (relativePath == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "relativePath", "com/ansorgit/plugins/bash/util/CompletionUtil", "completeRelativePath" }));  List<String> result = Lists.newLinkedList();
    
    String bashBaseDir = OSUtil.toBashCompatible(baseDir);
    
    for (String path : completeAbsolutePath(baseDir + File.separator + relativePath, file -> true)) {
      if (path.startsWith(bashBaseDir)) {
        result.add(shownBaseDir + path.substring(bashBaseDir.length()));
      }
    } 
    
    if (result == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/util/CompletionUtil", "completeRelativePath" }));  return result;
  }
  
  @NotNull
  private static List<File> collectFiles(File basePath, @NotNull final String matchPart) {
    if (matchPart == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "matchPart", "com/ansorgit/plugins/bash/util/CompletionUtil", "collectFiles" }));  File[] filtered = basePath.listFiles(new FileFilter() {
          public boolean accept(File pathname) {
            return (!".".equals(pathname.getName()) && (matchPart
              .isEmpty() || pathname.getName().startsWith(matchPart)));
          }
        });

    
    if (((filtered == null) ? (List)Collections.emptyList() : Arrays.asList(filtered)) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/util/CompletionUtil", "collectFiles" }));  return (filtered == null) ? (List)Collections.emptyList() : Arrays.asList(filtered);
  }
}
