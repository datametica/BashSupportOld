package com.ansorgit.plugins.bash.editor.inspections.inspections;

import com.ansorgit.plugins.bash.editor.inspections.SupressionUtil;
import com.ansorgit.plugins.bash.editor.inspections.quickfix.AddShebangQuickfix;
import com.ansorgit.plugins.bash.editor.inspections.quickfix.SupressAddShebangInspectionQuickfix;
import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.BashFile;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiFileUtils;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import com.intellij.codeInspection.BatchSuppressableTool;
import com.intellij.codeInspection.CustomSuppressableInspectionTool;
import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.codeInspection.SuppressIntentionAction;
import com.intellij.codeInspection.SuppressIntentionActionFromFix;
import com.intellij.codeInspection.SuppressQuickFix;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;













public class AddShebangInspection
  extends LocalInspectionTool
  implements CustomSuppressableInspectionTool, BatchSuppressableTool
{
  public ProblemDescriptor[] checkFile(@NotNull PsiFile file, @NotNull InspectionManager manager, boolean isOnTheFly) {
    if (file == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "file", "com/ansorgit/plugins/bash/editor/inspections/inspections/AddShebangInspection", "checkFile" }));  if (manager == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "manager", "com/ansorgit/plugins/bash/editor/inspections/inspections/AddShebangInspection", "checkFile" }));  PsiFile checkedFile = BashPsiUtils.findFileContext((PsiElement)file);
    
    if (checkedFile instanceof BashFile && !BashPsiUtils.isInjectedElement((PsiElement)file) && 
      !BashPsiFileUtils.isSpecialBashFile(checkedFile.getName())) {
      BashFile bashFile = (BashFile)checkedFile;
      Boolean isLanguageConsole = (Boolean)checkedFile.getUserData(BashFile.LANGUAGE_CONSOLE_MARKER);
      
      if ((isLanguageConsole == null || !isLanguageConsole.booleanValue()) && !bashFile.hasShebangLine()) {
        return new ProblemDescriptor[] { manager
            .createProblemDescriptor((PsiElement)checkedFile, "Add shebang line", (LocalQuickFix)new AddShebangQuickfix(), ProblemHighlightType.GENERIC_ERROR_OR_WARNING, isOnTheFly) };
      }
    } 

    
    return null;
  }

  
  @Nullable
  public SuppressIntentionAction[] getSuppressActions(PsiElement element) {
    return SuppressIntentionActionFromFix.convertBatchToSuppressIntentionActions(getBatchSuppressActions(element));
  }

  
  public boolean isSuppressedFor(@NotNull PsiElement element) {
    if (element == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "element", "com/ansorgit/plugins/bash/editor/inspections/inspections/AddShebangInspection", "isSuppressedFor" }));  PsiComment suppressionComment = SupressionUtil.findSuppressionComment(element);
    return (suppressionComment != null && SupressionUtil.isSuppressionComment(suppressionComment, getID()));
  }

  
  @NotNull
  public SuppressQuickFix[] getBatchSuppressActions(@Nullable PsiElement element) {
    if (element != null && element.getContainingFile() instanceof BashFile) {
      (new SuppressQuickFix[1])[0] = (SuppressQuickFix)new SupressAddShebangInspectionQuickfix(
          getID()); if (new SuppressQuickFix[1] == null)
        throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/inspections/inspections/AddShebangInspection", "getBatchSuppressActions" })); 
      return new SuppressQuickFix[1];
    } 
    if (SuppressQuickFix.EMPTY_ARRAY == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/inspections/inspections/AddShebangInspection", "getBatchSuppressActions" }));  return SuppressQuickFix.EMPTY_ARRAY;
  }

  
  @NotNull
  public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, final boolean isOnTheFly) {
    if (holder == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "holder", "com/ansorgit/plugins/bash/editor/inspections/inspections/AddShebangInspection", "buildVisitor" }));  if (new BashVisitor()
      {
        public void visitFile(BashFile file) {
          addDescriptors(AddShebangInspection.this.checkFile((PsiFile)file, holder.getManager(), isOnTheFly));
        }
        
        private void addDescriptors(ProblemDescriptor[] descriptors) {
          if (descriptors != null)
            for (ProblemDescriptor descriptor : descriptors)
              holder.registerProblem(descriptor);   } } == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/inspections/inspections/AddShebangInspection", "buildVisitor" }));  return (PsiElementVisitor)new BashVisitor() { private void addDescriptors(ProblemDescriptor[] descriptors) { if (descriptors != null) for (ProblemDescriptor descriptor : descriptors) holder.registerProblem(descriptor);   }

        
        public void visitFile(BashFile file) {
          addDescriptors(AddShebangInspection.this.checkFile((PsiFile)file, holder.getManager(), isOnTheFly));
        } }
      ;
  }
}
