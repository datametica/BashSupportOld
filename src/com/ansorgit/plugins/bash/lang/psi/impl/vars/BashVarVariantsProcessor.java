package com.ansorgit.plugins.bash.lang.psi.impl.vars;

import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVarDef;
import com.ansorgit.plugins.bash.lang.psi.util.BashAbstractProcessor;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.ResolveState;
import java.util.List;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

















public class BashVarVariantsProcessor
  extends BashAbstractProcessor
  implements BashVarCollectorProcessor
{
  private final List<BashVarDef> variables = Lists.newLinkedList();
  private final Set<String> variableNames = Sets.newHashSet();
  
  private final PsiElement startElement;
  
  private final boolean allowSameFile;
  
  private final boolean allowOtherFiles;
  
  private final PsiFile startFile;

  
  public BashVarVariantsProcessor(PsiElement startElement, boolean allowSameFile, boolean allowOtherFiles) {
    super(false);
    
    this.startElement = startElement;
    this.allowSameFile = allowSameFile;
    this.allowOtherFiles = allowOtherFiles;
    
    this.startFile = startElement.getContainingFile();
  }
  
  public boolean execute(@NotNull PsiElement psiElement, @NotNull ResolveState resolveState) {
    if (psiElement == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "psiElement", "com/ansorgit/plugins/bash/lang/psi/impl/vars/BashVarVariantsProcessor", "execute" }));  if (resolveState == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "resolveState", "com/ansorgit/plugins/bash/lang/psi/impl/vars/BashVarVariantsProcessor", "execute" }));  ProgressManager.checkCanceled();
    
    if (this.startFile != null) {
      PsiFile file = psiElement.getContainingFile();
      if (!this.allowSameFile && this.startFile.isEquivalentTo((PsiElement)file)) {
        return true;
      }
      if (!this.allowOtherFiles && !this.startFile.isEquivalentTo((PsiElement)file)) {
        return true;
      }
    } 
    
    if (psiElement instanceof BashVarDef) {
      BashVarDef varDef = (BashVarDef)psiElement;
      if (varDef.isStaticAssignmentWord() && 
        !varDef.isCommandLocal() && 
        !this.variableNames.contains(varDef.getName()) && 
        BashVarUtils.isInDefinedScope(this.startElement, varDef)) {
        this.variables.add(varDef);
        this.variableNames.add(varDef.getName());
      } 
    } 
    
    return true;
  }
  
  public List<BashVarDef> getVariables() {
    return this.variables;
  }
  
  public <T> T getHint(@NotNull Key<T> tKey) {
    if (tKey == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "tKey", "com/ansorgit/plugins/bash/lang/psi/impl/vars/BashVarVariantsProcessor", "getHint" }));  return null;
  }
}
