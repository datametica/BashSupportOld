package com.ansorgit.plugins.bash.lang.psi.impl.command;

import com.ansorgit.plugins.bash.lang.psi.api.function.BashFunctionDef;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import java.util.List;
import org.jetbrains.annotations.Nullable;





















class DumbFunctionReference
  extends AbstractFunctionReference
{
  public DumbFunctionReference(AbstractBashCommand<?> cmd) {
    super(cmd);
  }

  
  @Nullable
  public PsiElement resolveInner() {
    String referencedName = this.cmd.getReferencedCommandName();
    if (referencedName == null) {
      return null;
    }


    
    List<BashFunctionDef> functionDefs = this.cmd.getContainingFile().allFunctionDefinitions();
    
    ResolveState initial = ResolveState.initial();
    
    BashFunctionProcessor bashFunctionProcessor = new BashFunctionProcessor(referencedName);
    for (BashFunctionDef functionDef : functionDefs) {
      bashFunctionProcessor.execute((PsiElement)functionDef, initial);
    }
    
    bashFunctionProcessor.prepareResults();
    
    return bashFunctionProcessor.hasResults() ? bashFunctionProcessor.getBestResult(true, (PsiElement)this.cmd) : null;
  }
}
