package com.ansorgit.plugins.bash.editor.accessDetector;

import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVarDef;
import com.intellij.codeInsight.highlighting.ReadWriteAccessDetector;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;




















public class BashReadWriteAccessDetector
  extends ReadWriteAccessDetector
{
  public boolean isReadWriteAccessible(PsiElement element) {
    return (element instanceof com.ansorgit.plugins.bash.lang.psi.api.vars.BashVar || element instanceof com.ansorgit.plugins.bash.lang.psi.api.BashFile);
  }

  
  public boolean isDeclarationWriteAccess(PsiElement element) {
    if (element instanceof BashVarDef) {
      BashVarDef varDef = (BashVarDef)element;
      return varDef.hasAssignmentValue();
    } 
    
    return false;
  }

  
  public ReadWriteAccessDetector.Access getReferenceAccess(PsiElement referencedElement, PsiReference reference) {
    return getExpressionAccess(referencedElement);
  }

  
  public ReadWriteAccessDetector.Access getExpressionAccess(PsiElement expression) {
    if (expression instanceof BashVarDef) {
      return ReadWriteAccessDetector.Access.Write;
    }
    
    return ReadWriteAccessDetector.Access.Read;
  }
}
