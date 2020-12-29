package com.ansorgit.plugins.bash.file;

import com.intellij.openapi.fileTypes.ExactFileNameMatcher;
import com.intellij.openapi.fileTypes.FileNameMatcher;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import org.jetbrains.annotations.NotNull;

















public class BashFileTypeLoader
  extends FileTypeFactory
{
  public void createFileTypes(@NotNull FileTypeConsumer consumer) {
    if (consumer == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "consumer", "com/ansorgit/plugins/bash/file/BashFileTypeLoader", "createFileTypes" }));  consumer.consume((FileType)BashFileType.BASH_FILE_TYPE, "sh");
    consumer.consume((FileType)BashFileType.BASH_FILE_TYPE, "bash");
    
    consumer.consume((FileType)BashFileType.BASH_FILE_TYPE, new FileNameMatcher[] { (FileNameMatcher)new ExactFileNameMatcher(".bashrc"), (FileNameMatcher)new ExactFileNameMatcher(".profile"), (FileNameMatcher)new ExactFileNameMatcher(".bash_logout"), (FileNameMatcher)new ExactFileNameMatcher(".bash_profile"), (FileNameMatcher)new ExactFileNameMatcher(".bash_aliases") });
  }
}
