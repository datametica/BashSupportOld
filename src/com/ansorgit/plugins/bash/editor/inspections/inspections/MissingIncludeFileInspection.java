package com.ansorgit.plugins.bash.editor.inspections.inspections;

import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.BashFileReference;
import com.ansorgit.plugins.bash.lang.psi.api.command.BashIncludeCommand;
import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVar;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import com.ansorgit.plugins.bash.settings.BashProjectSettings;
import com.ansorgit.plugins.bash.util.BashFiles;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicBoolean;
import org.jetbrains.annotations.NotNull;





















public class MissingIncludeFileInspection
  extends LocalInspectionTool
{
  @NotNull
  public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
    if (holder == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "holder", "com/ansorgit/plugins/bash/editor/inspections/inspections/MissingIncludeFileInspection", "buildVisitor" }));  if (new IncludeFileVisitor(holder) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/inspections/inspections/MissingIncludeFileInspection", "buildVisitor" }));  return (PsiElementVisitor)new IncludeFileVisitor(holder);
  }
  
  private static boolean containsUnsupportedVars(PsiElement fileReference) {
    final AtomicBoolean otherVars = new AtomicBoolean(false);
    BashPsiUtils.visitRecursively(fileReference, new BashVisitor()
        {
          public void visitVarUse(BashVar var) {
            if (!"HOME".equals(var.getReferenceName())) {
              otherVars.set(true);
            }
          }
        });
    
    return otherVars.get();
  }
  
  private static class IncludeFileVisitor extends BashVisitor {
    private final ProblemsHolder holder;
    
    public IncludeFileVisitor(ProblemsHolder holder) {
      this.holder = holder;
    }

    
    public void visitIncludeCommand(BashIncludeCommand bashCommand) {
      BashFileReference fileReference = bashCommand.getFileReference();
      if (fileReference == null || fileReference.findReferencedFile() != null) {
        return;
      }
      
      String filename = fileReference.getFilename();
      if (fileReference.isDynamic() || BashFiles.containsSupportedPlaceholders(filename)) {
        if (!BashProjectSettings.storedSettings(this.holder.getProject()).isValidateWithCurrentEnv()) {
          return;
        }


        
        if (MissingIncludeFileInspection.containsUnsupportedVars((PsiElement)fileReference)) {
          return;
        }
      } 
      
      filename = BashFiles.replaceHomePlaceholders(filename);

      
      try {
        Path path = Paths.get(filename, new String[0]);
        boolean absoluteAndExists = (path.isAbsolute() && Files.exists(path, new java.nio.file.LinkOption[0]));
        
        if (!absoluteAndExists) {
          this.holder.registerProblem((PsiElement)fileReference, String.format("The file '%s' does not exist.", new Object[] { filename }), new com.intellij.codeInspection.LocalQuickFix[0]);
        } else if (Files.isDirectory(path, new java.nio.file.LinkOption[0])) {
          
          this.holder.registerProblem((PsiElement)fileReference, "Unable to include a directory.", new com.intellij.codeInspection.LocalQuickFix[0]);
        } 
      } catch (InvalidPathException e) {
        this.holder.registerProblem((PsiElement)fileReference, String.format("Unable to parse file reference '%s'", new Object[] { filename }), new com.intellij.codeInspection.LocalQuickFix[0]);
      } 
    }
  }
}
