package com.ansorgit.plugins.bash.lang.psi.impl.command;

import com.ansorgit.plugins.bash.lang.psi.api.function.BashFunctionDef;
import com.ansorgit.plugins.bash.lang.psi.util.BashAbstractProcessor;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import com.google.common.collect.Lists;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import java.util.List;


















public class BashFunctionVariantsProcessor
  extends BashAbstractProcessor
{
  private final List<BashFunctionDef> functionDefs = Lists.newArrayList();
  private PsiElement startElement;
  
  public BashFunctionVariantsProcessor(PsiElement startElement) {
    super(true);
    this.startElement = startElement;
  }
  
  public boolean execute(PsiElement element, ResolveState resolveState) {
    ProgressManager.checkCanceled();
    
    if (element instanceof BashFunctionDef) {
      BashFunctionDef f = (BashFunctionDef)element;
      
      if (BashPsiUtils.isValidReferenceScope(this.startElement, element)) {
        this.functionDefs.add(f);
      }
    } 
    
    return true;
  }
  
  public <T> T getHint(Key<T> tKey) {
    return null;
  }
  
  public List<BashFunctionDef> getFunctionDefs() {
    return this.functionDefs;
  }
}
