package com.ansorgit.plugins.bash.lang.psi.util;

import com.ansorgit.plugins.bash.file.BashFileType;
import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.BashString;
import com.ansorgit.plugins.bash.lang.psi.api.command.BashGenericCommand;
import com.ansorgit.plugins.bash.lang.psi.api.command.BashIncludeCommand;
import com.ansorgit.plugins.bash.lang.psi.api.heredoc.BashHereDoc;
import com.ansorgit.plugins.bash.lang.psi.api.heredoc.BashHereDocEndMarker;
import com.ansorgit.plugins.bash.lang.psi.api.heredoc.BashHereDocStartMarker;
import com.ansorgit.plugins.bash.lang.psi.api.vars.BashComposedVar;
import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVar;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.util.PsiTreeUtil;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;


















public class BashPsiElementFactory
{
  private static final String TEMP_FILE_NAME = "__.sh";
  
  public static PsiFile createDummyBashFile(Project project, String text) {
    return createFileFromText(project, "__.sh", (FileType)BashFileType.BASH_FILE_TYPE, text);
  }
  
  public static PsiElement createFileReference(Project project, String content) {
    PsiElement firstChild = createDummyBashFile(project, ". " + content).getFirstChild();
    
    return (PsiElement)((BashIncludeCommand)firstChild).getFileReference();
  }
  
  public static PsiElement createSymbol(Project project, String name) {
    PsiFile psiFile = createDummyBashFile(project, name + "() { x; }");
    return psiFile.getFirstChild().getFirstChild();
  }
  
  public static PsiElement createWord(Project project, String name) {
    return createDummyBashFile(project, name).getFirstChild().getFirstChild().getFirstChild();
  }
  
  public static BashGenericCommand createCommand(Project project, String command) {
    return (BashGenericCommand)createDummyBashFile(project, command).getFirstChild().getFirstChild();
  }
  
  public static BashString createString(Project project, String content) {
    String fileContent = (content.startsWith("\"") && content.endsWith("\"")) ? content : ("\"" + content + "\"");
    return (BashString)PsiTreeUtil.findChildOfType((PsiElement)createDummyBashFile(project, fileContent), BashString.class);
  }
  
  public static PsiElement createAssignmentWord(Project project, String name) {
    PsiElement assignmentCommand = createDummyBashFile(project, name + "=a").getFirstChild();
    
    return assignmentCommand.getFirstChild().getFirstChild();
  }
  
  public static PsiElement createVariable(Project project, String name, boolean withBraces) {
    if (withBraces) {
      final PsiElement[] result = new PsiElement[1];
      
      BashPsiUtils.visitRecursively(createComposedVar(project, name), new BashVisitor()
          {
            public void visitVarUse(BashVar var) {
              result[0] = (PsiElement)var;
            }
          });
      
      return result[0];
    } 
    
    String text = "$" + name;
    PsiElement command = createDummyBashFile(project, text).getFirstChild().getFirstChild();
    
    return command.getFirstChild().getFirstChild();
  }
  
  public static PsiElement createComposedVar(Project project, String varName) {
    String text = "${" + varName + "}";
    PsiElement command = createDummyBashFile(project, text).getFirstChild().getFirstChild();
    
    final PsiElement[] result = new PsiElement[1];
    
    BashPsiUtils.visitRecursively(command, new BashVisitor()
        {
          public void visitComposedVariable(BashComposedVar composedVar) {
            result[0] = (PsiElement)composedVar;
          }
        });
    
    return result[0];
  }
  
  public static PsiElement createShebang(Project project, String command, boolean addNewline) {
    String text = "#!" + command + (addNewline ? "\n" : "");
    return createDummyBashFile(project, text).getFirstChild();
  }
  
  public static PsiElement createNewline(Project project) {
    String text = "\n";
    return createDummyBashFile(project, text).getFirstChild();
  }
  
  public static PsiComment createComment(Project project, String comment) {
    String text = "#" + comment + "\n";
    
    PsiFile file = createDummyBashFile(project, text);
    return (PsiComment)PsiTreeUtil.getChildOfType((PsiElement)file, PsiComment.class);
  }
  
  public static PsiElement createHeredocStartMarker(Project project, String name) {
    String data = String.format("cat << %s\n%s", new Object[] { name, name });
    return PsiTreeUtil.findChildOfType((PsiElement)createDummyBashFile(project, data), BashHereDocStartMarker.class);
  }
  
  public static PsiElement createHeredocEndMarker(Project project, String name, int leadingTabs) {
    String data = String.format("cat <<- %s\n%s", new Object[] { name, StringUtils.repeat("\t", leadingTabs) + name });
    return PsiTreeUtil.findChildOfType((PsiElement)createDummyBashFile(project, data), BashHereDocEndMarker.class);
  }
  
  public static PsiElement createHeredocContent(Project project, String content) {
    String markerName = "_BASH_EOF_";
    
    String data = String.format("cat << %s\n%s\n%s", new Object[] { markerName, content, markerName });
    return PsiTreeUtil.findChildOfType((PsiElement)createDummyBashFile(project, data), BashHereDoc.class);
  }
  
  @NotNull
  private static PsiFile createFileFromText(@NotNull Project project, @NotNull String name, @NotNull FileType fileType, @NotNull String text) {
    if (project == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "project", "com/ansorgit/plugins/bash/lang/psi/util/BashPsiElementFactory", "createFileFromText" }));  if (name == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "name", "com/ansorgit/plugins/bash/lang/psi/util/BashPsiElementFactory", "createFileFromText" }));  if (fileType == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "fileType", "com/ansorgit/plugins/bash/lang/psi/util/BashPsiElementFactory", "createFileFromText" }));  if (text == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "text", "com/ansorgit/plugins/bash/lang/psi/util/BashPsiElementFactory", "createFileFromText" }));  if (PsiFileFactory.getInstance(project).createFileFromText(name, fileType, text) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/util/BashPsiElementFactory", "createFileFromText" }));  return PsiFileFactory.getInstance(project).createFileFromText(name, fileType, text);
  }
}
