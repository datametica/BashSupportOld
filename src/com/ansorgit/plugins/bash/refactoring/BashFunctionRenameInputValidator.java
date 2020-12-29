package com.ansorgit.plugins.bash.refactoring;

import com.ansorgit.plugins.bash.lang.psi.api.function.BashFunctionDef;
import com.ansorgit.plugins.bash.lang.psi.util.BashIdentifierUtil;
import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.refactoring.rename.RenameInputValidator;
import com.intellij.util.ProcessingContext;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;













@Metadata(mv = {1, 1, 1}, bv = {1, 0, 0}, k = 1, d1 = {"\000*\n\002\030\002\n\002\030\002\n\002\b\002\n\002\030\002\n\002\030\002\n\000\n\002\020\013\n\000\n\002\020\016\n\002\b\002\n\002\030\002\n\000\030\0002\0020\001B\005¢\006\002\020\002J\020\020\003\032\n\022\006\b\001\022\0020\0050\004H\026J \020\006\032\0020\0072\006\020\b\032\0020\t2\006\020\n\032\0020\0052\006\020\013\032\0020\fH\026¨\006\r"}, d2 = {"Lcom/ansorgit/plugins/bash/refactoring/BashFunctionRenameInputValidator;", "Lcom/intellij/refactoring/rename/RenameInputValidator;", "()V", "getPattern", "Lcom/intellij/patterns/ElementPattern;", "Lcom/intellij/psi/PsiElement;", "isInputValid", "", "newName", "", "element", "context", "Lcom/intellij/util/ProcessingContext;", "BashSupport1_main"})
public final class BashFunctionRenameInputValidator
  implements RenameInputValidator
{
  public boolean isInputValid(@NotNull String newName, @NotNull PsiElement element, @NotNull ProcessingContext context) {
    Intrinsics.checkParameterIsNotNull(newName, "newName"); Intrinsics.checkParameterIsNotNull(element, "element"); Intrinsics.checkParameterIsNotNull(context, "context"); return BashIdentifierUtil.isValidFunctionName(newName);
  }
  @NotNull
  public ElementPattern<? extends PsiElement> getPattern() {
    Intrinsics.checkExpressionValueIsNotNull(PlatformPatterns.psiElement(BashFunctionDef.class), "PlatformPatterns.psiElem…hFunctionDef::class.java)"); return (ElementPattern<? extends PsiElement>)PlatformPatterns.psiElement(BashFunctionDef.class);
  }
}
