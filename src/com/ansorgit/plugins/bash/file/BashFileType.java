package com.ansorgit.plugins.bash.file;

import com.ansorgit.plugins.bash.lang.BashLanguage;
import com.ansorgit.plugins.bash.util.BashIcons;
import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.LanguageFileType;
import javax.swing.Icon;
import org.jetbrains.annotations.NotNull;




















public class BashFileType
  extends LanguageFileType
{
  public static final BashFileType BASH_FILE_TYPE = new BashFileType();
  public static final Language BASH_LANGUAGE = BASH_FILE_TYPE.getLanguage();
  
  public static final String SH_EXTENSION = "sh";
  
  static final String BASH_EXTENSION = "bash";
  
  static final String BASHRC_FILENAME = ".bashrc";
  
  static final String PROFILE_FILENAME = ".profile";
  
  static final String BASH_PROFILE_FILENAME = ".bash_profile";
  
  static final String BASH_LOGOUT_FILENAME = ".bash_logout";
  static final String BASH_ALIASES_FILENAME = ".bash_aliases";
  public static final String[] BASH_SPECIAL_FILES = new String[] { ".bashrc", ".profile", ".bash_profile", ".bash_logout", ".bash_aliases" };

  
  protected BashFileType() {
    super((Language)new BashLanguage());
  }

  
  public String toString() {
    return "BashFileType";
  }
  
  @NotNull
  public String getName() {
    if ("Bash" == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/file/BashFileType", "getName" }));  return "Bash";
  }
  
  @NotNull
  public String getDescription() {
    if ("Bourne Again Shell" == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/file/BashFileType", "getDescription" }));  return "Bourne Again Shell";
  }
  
  @NotNull
  public String getDefaultExtension() {
    if ("sh" == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/file/BashFileType", "getDefaultExtension" }));  return "sh";
  }
  
  public Icon getIcon() {
    return BashIcons.BASH_FILE_ICON;
  }
}
