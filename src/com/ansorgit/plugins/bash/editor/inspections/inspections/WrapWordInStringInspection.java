package com.ansorgit.plugins.bash.editor.inspections.inspections;

import com.ansorgit.plugins.bash.editor.inspections.quickfix.WordToDoublequotedStringQuickfix;
import com.ansorgit.plugins.bash.editor.inspections.quickfix.WordToSinglequotedStringQuickfix;
import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.word.BashWord;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;





















public class WrapWordInStringInspection
  extends LocalInspectionTool
{
  @NotNull
  public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder problemsHolder, boolean b) {
    if (problemsHolder == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "problemsHolder", "com/ansorgit/plugins/bash/editor/inspections/inspections/WrapWordInStringInspection", "buildVisitor" }));  if (new BashVisitor()
      {
        public void visitCombinedWord(BashWord word) {
          if (word.getParent() instanceof com.ansorgit.plugins.bash.lang.psi.api.BashString) {
            return;
          }
          
          if (word.isWrappable())
            problemsHolder.registerProblem((PsiElement)word, "Unquoted string", new LocalQuickFix[] { (LocalQuickFix)new WordToDoublequotedStringQuickfix(word), (LocalQuickFix)new WordToSinglequotedStringQuickfix(word) });  } } == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/inspections/inspections/WrapWordInStringInspection", "buildVisitor" }));  return (PsiElementVisitor)new BashVisitor() { public void visitCombinedWord(BashWord word) { if (word.getParent() instanceof com.ansorgit.plugins.bash.lang.psi.api.BashString) return;  if (word.isWrappable()) problemsHolder.registerProblem((PsiElement)word, "Unquoted string", new LocalQuickFix[] { (LocalQuickFix)new WordToDoublequotedStringQuickfix(word), (LocalQuickFix)new WordToSinglequotedStringQuickfix(word) });  }
         }
      ;
  }
}
