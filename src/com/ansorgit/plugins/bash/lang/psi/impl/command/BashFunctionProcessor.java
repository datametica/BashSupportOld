package com.ansorgit.plugins.bash.lang.psi.impl.command;

import com.ansorgit.plugins.bash.lang.psi.api.function.BashFunctionDef;
import com.ansorgit.plugins.bash.lang.psi.impl.Keys;
import com.ansorgit.plugins.bash.lang.psi.util.BashAbstractProcessor;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import com.google.common.collect.Sets;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import java.util.Set;
import org.jetbrains.annotations.NotNull;















public class BashFunctionProcessor
  extends BashAbstractProcessor
{
  private final String symboleName;
  private final boolean ignoreExecuteResult;
  private Set<PsiElement> visitedElements = Sets.newIdentityHashSet();
  
  public BashFunctionProcessor(String symboleName) {
    this(symboleName, false);
  }
  
  public BashFunctionProcessor(String symboleName, boolean ignoreExecuteResult) {
    super(true);
    
    this.symboleName = symboleName;
    this.ignoreExecuteResult = ignoreExecuteResult;
  }
  
  public boolean execute(@NotNull PsiElement element, @NotNull ResolveState resolveState) {
    if (element == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "element", "com/ansorgit/plugins/bash/lang/psi/impl/command/BashFunctionProcessor", "execute" }));  if (resolveState == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "resolveState", "com/ansorgit/plugins/bash/lang/psi/impl/command/BashFunctionProcessor", "execute" }));  ProgressManager.checkCanceled();
    
    if (element instanceof BashFunctionDef) {
      BashFunctionDef funcDef = (BashFunctionDef)element;
      
      if (this.symboleName.equals(funcDef.getName())) {
        storeResult(element, Integer.valueOf(BashPsiUtils.blockNestingLevel((PsiElement)funcDef)), null);
        return this.ignoreExecuteResult;
      } 
    } 
    
    return true;
  }
  
  public <T> T getHint(@NotNull Key<T> key) {
    if (key == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "key", "com/ansorgit/plugins/bash/lang/psi/impl/command/BashFunctionProcessor", "getHint" }));  if (key.equals(Keys.VISITED_SCOPES_KEY)) {
      return (T)this.visitedElements;
    }
    
    if (key.equals(Keys.FILE_WALK_GO_DEEP)) {
      return (T)Boolean.FALSE;
    }
    
    return null;
  }
}
