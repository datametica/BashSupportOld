package com.ansorgit.plugins.bash.actions;

import com.ansorgit.plugins.bash.file.BashFileTypeDetector;
import com.ansorgit.plugins.bash.util.BashIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeRegistry;
import com.intellij.openapi.fileTypes.ex.FileTypeManagerEx;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.SystemInfoRt;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;




@Metadata(mv = {1, 1, 1}, bv = {1, 0, 0}, k = 1, d1 = {"\000\030\n\002\030\002\n\002\030\002\n\002\b\002\n\002\020\002\n\000\n\002\030\002\n\000\030\0002\0020\001B\005¢\006\002\020\002J\020\020\003\032\0020\0042\006\020\005\032\0020\006H\026¨\006\007"}, d2 = {"Lcom/ansorgit/plugins/bash/actions/ReportFileTypeAction;", "Lcom/intellij/openapi/actionSystem/AnAction;", "()V", "actionPerformed", "", "e", "Lcom/intellij/openapi/actionSystem/AnActionEvent;", "BashSupport1_main"})
public final class ReportFileTypeAction
  extends AnAction
{
  public ReportFileTypeAction() {
    super("Bash: file type detection report", "", BashIcons.BASH_FILE_ICON);
  } public void actionPerformed(@NotNull AnActionEvent e) {
    Intrinsics.checkParameterIsNotNull(e, "e"); if (e.getData(CommonDataKeys.PROJECT) == null) throw new TypeCastException("null cannot be cast to non-null type com.intellij.openapi.project.Project");  Project project = (Project)e.getData(CommonDataKeys.PROJECT);
    Editor editor = (Editor)CommonDataKeys.EDITOR.getData(e.getDataContext());
    if (editor == null) {
      Messages.showInfoMessage("No open file found.", "Bash file type detection");
      
      return;
    } 
    PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(editor.getDocument());
    if (psiFile == null) {
      Messages.showInfoMessage("No PSI file found.", "Bash file type detection");
      
      return;
    } 
    VirtualFile virtualFile = psiFile.getVirtualFile();
    
    FileTypeRegistry typeRegistry = FileTypeRegistry.getInstance();
    FileType detectedType = typeRegistry.getFileTypeByFile(virtualFile);
    boolean isIgnored = typeRegistry.isFileIgnored(virtualFile);
    FileType typeByName = typeRegistry.getFileTypeByFileName(psiFile.getName());
    
    FileType psiFileType = psiFile.getFileType();
    FileType vfsFileType = (virtualFile != null) ? virtualFile.getFileType() : null;
    
    FileTypeManagerEx typeManager = FileTypeManagerEx.getInstanceEx();
    
    String initialContent = VfsUtilCore.loadText(virtualFile, 512);
    String firstLine = (String)CollectionsKt.firstOrNull(StringsKt.lines(initialContent));
    String str1 = firstLine; FileType bashDetectedType = 
      Intrinsics.areEqual(str1, null) ? (FileType)null : 
      BashFileTypeDetector.detect(virtualFile, firstLine);

    
    String report = 



















      
      StringsKt.trimIndent("\n" + "            Please report an issue at https://github.com/BashSupport/BashSupport/issues" + "\n" + "            if the Bash file isn't properly displayed. You can select and copy the information below." + "\n" + "\n" + "            File name: " + virtualFile.getPath() + "\n" + "            File extension: " + virtualFile.getExtension() + "\n" + "            First line: " + firstLine + "\n" + "\n" + "            Attached PSI file type: " + psiFileType + "\n" + "            Attached VFS file type: " + vfsFileType + "\n" + "            Detected file type: " + detectedType + "\n" + "            Ignored: " + isIgnored + "\n" + "            Type by filename: " + typeByName + "\n" + "\n" + "            Ignored files: " + typeManager.getIgnoredFilesList() + "\n" + "            Detected by bash detector: " + bashDetectedType + "\n" + "\n" + "            IDE: " + ApplicationInfo.getInstance().getBuild() + "\n" + "            OS: " + SystemInfoRt.OS_NAME + "\n" + "            OS version: " + SystemInfoRt.OS_VERSION + "\n" + "            File system case sensitive: " + SystemInfoRt.isFileSystemCaseSensitive + "\n" + "        ");
    
    Messages.showInfoMessage(report, "Bash file type detection");
  }
}
