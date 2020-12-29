package com.ansorgit.plugins.bash.util;

import com.google.common.collect.Lists;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.util.SystemInfoRt;
import java.io.File;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.Nullable;



















public class BashInterpreterDetection
{
  public static final List<String> POSSIBLE_LOCATIONS = Collections.unmodifiableList(Lists.newArrayList((Object[])new String[] { "/sbin/bash", "/bin/bash", "/usr/bin/bash", "/usr/local/bin/bash", "/opt/local/bin/bash", "/opt/bin/bash", "/sbin/sh", "/bin/sh", "/usr/bin/sh", "/opt/local/bin/sh", "/opt/bin/sh", "/usr/bin/env bash", "/usr/bin/env sh" }));














  
  private static final List<String> POSSIBLE_EXE_LOCATIONS = Collections.unmodifiableList(Lists.newArrayList((Object[])new String[] { "/sbin/bash", "/bin/bash", "/usr/bin/bash", "/usr/local/bin/bash", "/opt/local/bin/bash", "/opt/bin/bash", "/sbin/sh", "/bin/sh", "/usr/bin/sh", "/opt/local/bin/sh", "/opt/bin/sh", "/usr/bin/env" }));













  
  private static final List<String> POSSIBLE_EXE_LOCATIONS_WINDOWS = Collections.unmodifiableList(Lists.newArrayList((Object[])new String[] { "c:\\cygwin\\bin\\bash.exe", "d:\\cygwin\\bin\\bash.exe" }));


  
  public static final BashInterpreterDetection INSTANCE = new BashInterpreterDetection();
  
  public static BashInterpreterDetection instance() {
    return INSTANCE;
  }
  
  @Nullable
  public String findBestLocation() {
    List<String> locations = SystemInfo.isWindows ? POSSIBLE_EXE_LOCATIONS_WINDOWS : POSSIBLE_EXE_LOCATIONS;
    
    for (String guessLocation : locations) {
      if (isSuitable(guessLocation)) {
        return guessLocation;
      }
    } 
    
    String pathLocation = OSUtil.findBestExecutable(SystemInfoRt.isWindows ? "bash.exe" : "bash");
    return isSuitable(pathLocation) ? pathLocation : null;
  }
  
  public boolean isSuitable(String guessLocation) {
    if (guessLocation == null) {
      return false;
    }
    
    File f = new File(guessLocation);
    return (f.isFile() && f.canRead() && f.canExecute());
  }
}
