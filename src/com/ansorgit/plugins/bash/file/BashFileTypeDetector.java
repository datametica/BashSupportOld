package com.ansorgit.plugins.bash.file;

import com.ansorgit.plugins.bash.util.BashInterpreterDetection;
import com.google.common.collect.Lists;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeRegistry;
import com.intellij.openapi.util.io.ByteSequence;
import com.intellij.openapi.vfs.VirtualFile;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;





















public class BashFileTypeDetector
  implements FileTypeRegistry.FileTypeDetector
{
  private static final List<String> VALID_SHEBANGS = Lists.newArrayList();
  
  static {
    for (String location : BashInterpreterDetection.POSSIBLE_LOCATIONS) {
      VALID_SHEBANGS.add("#!" + location);
    }
  }

  
  @Nullable
  public FileType detect(@NotNull VirtualFile file, @NotNull ByteSequence firstBytes, @Nullable CharSequence textContent) {
    if (file == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "file", "com/ansorgit/plugins/bash/file/BashFileTypeDetector", "detect" }));  if (firstBytes == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "firstBytes", "com/ansorgit/plugins/bash/file/BashFileTypeDetector", "detect" }));  return detect(file, textContent);
  }
  
  @Nullable
  public static FileType detect(@NotNull VirtualFile file, @Nullable CharSequence textContent) {
    if (file == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "file", "com/ansorgit/plugins/bash/file/BashFileTypeDetector", "detect" }));  if (textContent == null || file.getExtension() != null) {
      return null;
    }
    
    String content = textContent.toString();
    
    for (String shebang : VALID_SHEBANGS) {

      
      if (content.contains(shebang)) {
        return (FileType)BashFileType.BASH_FILE_TYPE;
      }
    } 
    
    return null;
  }

  
  public int getVersion() {
    return 1;
  }
}
