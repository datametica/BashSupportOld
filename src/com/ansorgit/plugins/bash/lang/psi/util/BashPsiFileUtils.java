package com.ansorgit.plugins.bash.lang.psi.util;

import com.ansorgit.plugins.bash.file.BashFileType;
import com.intellij.openapi.util.io.FileUtilRt;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.util.text.StringUtilRt;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;



























public class BashPsiFileUtils
{
  @Nullable
  public static PsiFile findRelativeFile(PsiFile start, String relativePath) {
    PsiDirectory startDirectory = BashPsiUtils.findFileContext((PsiElement)start).getContainingDirectory();
    if (startDirectory == null || StringUtil.isEmptyOrSpaces(relativePath)) {
      return null;
    }

    
    PsiDirectory currentDir = startDirectory;
    
    List<String> parts = StringUtil.split(relativePath, "/");
    String filePart = (parts.size() > 0) ? parts.get(parts.size() - 1) : "";
    
    for (int i = 0, partsLength = parts.size() - 1; i < partsLength && currentDir != null; i++) {
      String part = parts.get(i);
      
      if (!".".equals(part))
      {
        if ("..".equals(part)) {
          currentDir = currentDir.getParentDirectory();
        } else {
          currentDir = currentDir.findSubdirectory(part);
        } 
      }
    } 
    if (currentDir != null) {
      return currentDir.findFile(filePart);
    }
    
    return null;
  }
  
  @Nullable
  public static String findRelativeFilePath(PsiFile base, PsiFile targetFile) {
    PsiFile currentFile = BashPsiUtils.findFileContext((PsiElement)base);
    VirtualFile baseVirtualFile = currentFile.getVirtualFile();
    if (!(baseVirtualFile.getFileSystem() instanceof com.intellij.openapi.vfs.LocalFileSystem)) {
      throw new IncorrectOperationException("Can not rename file refeferences in non-local files");
    }
    
    VirtualFile targetVirtualFile = BashPsiUtils.findFileContext((PsiElement)targetFile).getVirtualFile();
    if (!(targetVirtualFile.getFileSystem() instanceof com.intellij.openapi.vfs.LocalFileSystem)) {
      throw new IncorrectOperationException("Can not bind to non-local files");
    }
    
    VirtualFile baseParent = baseVirtualFile.getParent();
    VirtualFile targetParent = targetVirtualFile.getParent();
    if (baseParent == null || targetParent == null) {
      throw new IllegalStateException("parent directories not found");
    }
    
    char separator = '/';
    
    String baseDirPath = ensureEnds(baseParent.getPath(), separator);
    String targetDirPath = ensureEnds(targetParent.getPath(), separator);
    
    String targetRelativePath = FileUtilRt.getRelativePath(baseDirPath, targetDirPath, separator, true);
    if (targetRelativePath == null) {
      return null;
    }
    
    if (".".equals(targetRelativePath))
    {
      return targetVirtualFile.getName();
    }
    
    return ensureEnds(targetRelativePath, separator) + targetVirtualFile.getName();
  }

  
  public static boolean isSpecialBashFile(String name) {
    for (String bashSpecialFileName : BashFileType.BASH_SPECIAL_FILES) {
      if (bashSpecialFileName.equals(name)) {
        return true;
      }
    } 
    return false;
  }

  
  private static String ensureEnds(@NotNull String s, char endsWith) {
    if (s == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "s", "com/ansorgit/plugins/bash/lang/psi/util/BashPsiFileUtils", "ensureEnds" }));  return StringUtilRt.endsWithChar(s, endsWith) ? s : (s + endsWith);
  }
}
