package com.ansorgit.plugins.bash.editor.inspections.inspections;

import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.FileInclusionManager;
import com.ansorgit.plugins.bash.lang.psi.api.BashFileReference;
import com.ansorgit.plugins.bash.lang.psi.api.command.BashIncludeCommand;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import java.util.Set;
import org.jetbrains.annotations.NotNull;






















public class RecursiveIncludeFileInspection
  extends LocalInspectionTool
{
  @NotNull
  public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
    if (holder == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "holder", "com/ansorgit/plugins/bash/editor/inspections/inspections/RecursiveIncludeFileInspection", "buildVisitor" }));  if (new BashVisitor()
      {
        public void visitIncludeCommand(BashIncludeCommand includeCommand) {
          BashFileReference fileReference = includeCommand.getFileReference();
          if (fileReference == null) {
            return;
          }
          
          PsiFile referencedFile = fileReference.findReferencedFile();
          if (includeCommand.getContainingFile().equals(referencedFile))
          { holder.registerProblem((PsiElement)fileReference, "A file should not include itself.", new com.intellij.codeInspection.LocalQuickFix[0]); }
          else if (referencedFile instanceof com.ansorgit.plugins.bash.lang.psi.api.BashFile)
          
          { 
            Set<PsiFile> includedFiles = FileInclusionManager.findIncludedFiles(referencedFile, true, true);
            if (includedFiles.contains(includeCommand.getContainingFile()))
              holder.registerProblem((PsiElement)fileReference, "Possible recursive inclusion", new com.intellij.codeInspection.LocalQuickFix[0]);  }  } } == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/inspections/inspections/RecursiveIncludeFileInspection", "buildVisitor" }));  return (PsiElementVisitor)new BashVisitor() { public void visitIncludeCommand(BashIncludeCommand includeCommand) { BashFileReference fileReference = includeCommand.getFileReference(); if (fileReference == null) return;  PsiFile referencedFile = fileReference.findReferencedFile(); if (includeCommand.getContainingFile().equals(referencedFile)) { holder.registerProblem((PsiElement)fileReference, "A file should not include itself.", new com.intellij.codeInspection.LocalQuickFix[0]); } else if (referencedFile instanceof com.ansorgit.plugins.bash.lang.psi.api.BashFile) { Set<PsiFile> includedFiles = FileInclusionManager.findIncludedFiles(referencedFile, true, true); if (includedFiles.contains(includeCommand.getContainingFile())) holder.registerProblem((PsiElement)fileReference, "Possible recursive inclusion", new com.intellij.codeInspection.LocalQuickFix[0]);  }
           }
         }
      ;
  }
}
