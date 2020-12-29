package com.ansorgit.plugins.bash.lang.psi.impl.vars;

import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVar;
import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVarDef;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;


























public class BashVarUtils
{
  public static boolean isInDefinedScope(@NotNull PsiElement referenceElement, @NotNull BashVarDef definition) {
    if (referenceElement == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "referenceElement", "com/ansorgit/plugins/bash/lang/psi/impl/vars/BashVarUtils", "isInDefinedScope" }));  if (definition == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "definition", "com/ansorgit/plugins/bash/lang/psi/impl/vars/BashVarUtils", "isInDefinedScope" }));  if (definition.isFunctionScopeLocal())
    {
      return PsiTreeUtil.isAncestor(definition.findFunctionScope(), referenceElement, false);
    }
    
    if (referenceElement instanceof BashVarDef && ((BashVarDef)referenceElement).isFunctionScopeLocal())
    {
      
      return PsiTreeUtil.isAncestor(definition.findFunctionScope(), referenceElement, false);
    }

    
    if (referenceElement instanceof BashVar) {
      BashVar var = (BashVar)referenceElement;
      BashVarDef referencingDefinition = (BashVarDef)var.getReference().resolve();
      
      if (referencingDefinition != null && referencingDefinition.isFunctionScopeLocal()) {
        return isInDefinedScope((PsiElement)referencingDefinition, definition);
      }
    } 

    
    if (BashPsiUtils.hasContext(referenceElement, (PsiElement)definition)) {
      return false;
    }


    
    if (!BashPsiUtils.isValidReferenceScope(referenceElement, (PsiElement)definition)) {
      return false;
    }

    
    return true;
  }
}
