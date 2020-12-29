package com.ansorgit.plugins.bash.util;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.IconLoader;
import com.intellij.util.PlatformIcons;
import java.net.URL;
import javax.swing.Icon;
import kotlin.Metadata;
import kotlin.jvm.JvmField;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(mv = {1, 1, 1}, bv = {1, 0, 0}, k = 1, d1 = {"\000\"\n\002\030\002\n\002\020\000\n\002\b\002\n\002\030\002\n\002\b\005\n\002\030\002\n\002\b\004\n\002\020\016\n\000\bÆ\002\030\0002\0020\001B\007\b\002¢\006\002\020\002J\020\020\r\032\0020\0042\006\020\016\032\0020\017H\002R\020\020\003\032\0020\0048\006X\004¢\006\002\n\000R\020\020\005\032\0020\0048\006X\004¢\006\002\n\000R\020\020\006\032\0020\0048\006X\004¢\006\002\n\000R\020\020\007\032\0020\0048\006X\004¢\006\002\n\000R\020\020\b\032\0020\0048\006X\004¢\006\002\n\000R\026\020\t\032\n \013*\004\030\0010\n0\nX\004¢\006\002\n\000R\020\020\f\032\0020\0048\006X\004¢\006\002\n\000¨\006\020"}, d2 = {"Lcom/ansorgit/plugins/bash/util/BashIcons;", "", "()V", "BASH_FILE_ICON", "Ljavax/swing/Icon;", "BASH_VAR_ICON", "BOURNE_VAR_ICON", "FUNCTION_ICON", "GLOBAL_VAR_ICON", "LOG", "Lcom/intellij/openapi/diagnostic/Logger;", "kotlin.jvm.PlatformType", "VAR_ICON", "load", "resourcePath", "", "BashSupport1_main"})
public final class BashIcons
{
  private static final Logger LOG;
  @JvmField
  @NotNull
  public static final Icon BASH_FILE_ICON;
  @JvmField
  @NotNull
  public static final Icon FUNCTION_ICON;
  @JvmField
  @NotNull
  public static final Icon VAR_ICON;
  
  private BashIcons() {
    INSTANCE = this;
    LOG = Logger.getInstance("#bash.icons");

    
    BASH_FILE_ICON = load("/icons/fileTypes/BashFileIcon.png");


    
    FUNCTION_ICON = load("/icons/fileTypes/BashFileIcon.png");


    
    VAR_ICON = load("/icons/fileTypes/BashFileIcon.png");


    
    GLOBAL_VAR_ICON = load("/icons/global-var-16.png");


    
    BASH_VAR_ICON = load("/icons/bash-var-16.png");


    
    BOURNE_VAR_ICON = load("/icons/bash-var-16.png"); } @JvmField
  @NotNull
  public static final Icon GLOBAL_VAR_ICON; @JvmField
  @NotNull
  public static final Icon BASH_VAR_ICON; @JvmField
  @NotNull
  public static final Icon BOURNE_VAR_ICON; public static final BashIcons INSTANCE; private final Icon load(String resourcePath) {
    Icon icon1;
    try {
      URL url = BashIcons.class.getResource(resourcePath);
      icon1 = IconLoader.findIcon(url, true);
    } catch (Exception e) {
      LOG.warn("Error while loading icon " + resourcePath, e);
      icon1 = (Icon)null;
    } 
    Icon icon = icon1;
    if (icon == null) Intrinsics.checkExpressionValueIsNotNull(PlatformIcons.FILE_ICON, "PlatformIcons.FILE_ICON");  return PlatformIcons.FILE_ICON;
  }
}
