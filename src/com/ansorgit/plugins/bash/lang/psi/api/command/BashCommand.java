package com.ansorgit.plugins.bash.lang.psi.api.command;

import com.ansorgit.plugins.bash.lang.psi.api.BashPsiElement;
import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVarDef;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import java.util.List;
import org.jetbrains.annotations.Nullable;

public interface BashCommand extends BashPsiElement {
  @Nullable
  PsiReference getReference();
  
  boolean isFunctionCall();
  
  boolean isIncludeCommand();
  
  boolean isInternalCommand();
  
  boolean isInternalCommand(boolean paramBoolean);
  
  boolean isExternalCommand();
  
  boolean isBashScriptCall();
  
  boolean isPureAssignment();
  
  boolean isVarDefCommand();
  
  boolean hasAssignments();
  
  @Nullable
  PsiElement commandElement();
  
  boolean isGenericCommand();
  
  List<BashPsiElement> parameters();
  
  BashVarDef[] assignments();
  
  @Nullable
  String getReferencedCommandName();
}
