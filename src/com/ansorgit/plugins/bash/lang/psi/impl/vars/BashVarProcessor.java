package com.ansorgit.plugins.bash.lang.psi.impl.vars;

import com.ansorgit.plugins.bash.lang.psi.api.function.BashFunctionDef;
import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVar;
import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVarDef;
import com.ansorgit.plugins.bash.lang.psi.impl.Keys;
import com.ansorgit.plugins.bash.lang.psi.util.BashAbstractProcessor;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import com.ansorgit.plugins.bash.settings.BashProjectSettings;
import com.google.common.collect.Sets;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.util.PsiTreeUtil;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

















public class BashVarProcessor
  extends BashAbstractProcessor
  implements Keys
{
  private final BashVar startElement;
  private final BashFunctionDef startElementScope;
  private final boolean collectAllDefinitions;
  private final boolean checkLocalness;
  private final String varName;
  private boolean ignoreGlobals;
  private final boolean functionVarDefsAreGlobal;
  private final int startElementTextOffset;
  private final Set<PsiElement> globalVariables = Sets.newLinkedHashSet();
  
  public BashVarProcessor(BashVar startElement, String variableName, boolean checkLocalness, boolean preferNeighbourhood, boolean collectAllDefinitions) {
    super(preferNeighbourhood);
    
    this.startElement = startElement;
    this.checkLocalness = checkLocalness;
    this.varName = variableName;
    this.startElementScope = BashPsiUtils.findNextVarDefFunctionDefScope((PsiElement)startElement);
    this.collectAllDefinitions = collectAllDefinitions;
    
    this.ignoreGlobals = false;
    this.functionVarDefsAreGlobal = BashProjectSettings.storedSettings(startElement.getProject()).isGlobalFunctionVarDefs();
    this.startElementTextOffset = BashPsiUtils.getFileTextOffset((PsiElement)startElement);
  }
  
  public boolean execute(@NotNull PsiElement psiElement, @NotNull ResolveState resolveState) {
    if (psiElement == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "psiElement", "com/ansorgit/plugins/bash/lang/psi/impl/vars/BashVarProcessor", "execute" }));  if (resolveState == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "resolveState", "com/ansorgit/plugins/bash/lang/psi/impl/vars/BashVarProcessor", "execute" }));  ProgressManager.checkCanceled();
    
    if (psiElement instanceof BashVarDef) {
      BashVarDef varDef = (BashVarDef)psiElement;
      
      if (!this.varName.equals(varDef.getName()) || this.startElement == psiElement || this.startElement.equals(psiElement))
      {
        return true;
      }
      
      PsiElement includeCommand = (PsiElement)resolveState.get(resolvingIncludeCommand);

      
      boolean localVarDef = varDef.isFunctionScopeLocal();

      
      boolean isValid = (this.checkLocalness && localVarDef) ? isValidLocalDefinition(varDef) : isValidDefinition(varDef, resolveState);

      
      this.ignoreGlobals = (this.ignoreGlobals || (isValid && this.checkLocalness && localVarDef));
      
      if (isValid) {
        PsiElement defAnchor = (includeCommand != null) ? includeCommand : (PsiElement)varDef;
        
        storeResult((PsiElement)varDef, Integer.valueOf(BashPsiUtils.blockNestingLevel(defAnchor)), includeCommand);
        
        if (!localVarDef) {
          this.globalVariables.add(varDef);
        }
        
        return this.collectAllDefinitions;
      } 
    } 
    
    return true;
  }
  
  protected boolean isValidDefinition(BashVarDef varDef, ResolveState resolveState) {
    if (varDef.isCommandLocal()) {
      return false;
    }
    
    if (!varDef.isStaticAssignmentWord()) {
      return false;
    }

    
    if (this.startElement.isVarDefinition() && ((BashVarDef)this.startElement).isLocalVarDef()) {
      return false;
    }
    
    BashFunctionDef varDefScope = BashPsiUtils.findNextVarDefFunctionDefScope((PsiElement)varDef);
    if (this.ignoreGlobals && varDefScope == null) {
      return false;
    }






    
    boolean sameFiles = BashPsiUtils.findFileContext((PsiElement)this.startElement).isEquivalentTo((PsiElement)BashPsiUtils.findFileContext((PsiElement)varDef));
    if (sameFiles) {
      int textOffsetVarDef = BashPsiUtils.getFileTextOffset((PsiElement)varDef);
      if (this.startElementTextOffset >= textOffsetVarDef) {
        return isDefinitionOffsetValid(varDefScope);
      }


      
      if (varDefScope == null)
      {
        return (this.startElementScope != null);
      }




      
      if (this.functionVarDefsAreGlobal && this.startElementScope != null && !this.startElement.isVarDefinition() && !varDefScope.equals(this.startElementScope)) {
        return true;
      }
      
      if (this.startElementScope != null) {
        return PsiTreeUtil.isAncestor((PsiElement)varDefScope, (PsiElement)this.startElementScope, true);
      }
    } else {
      
      PsiElement includeCommand = (PsiElement)resolveState.get(resolvingIncludeCommand);
      if (includeCommand == null) {
        return false;
      }
      
      BashFunctionDef includeCommandScope = BashPsiUtils.findNextVarDefFunctionDefScope(includeCommand);

      
      int definitionOffset = BashPsiUtils.getFileTextOffset((PsiElement)this.startElement);
      int includeCommandOffset = BashPsiUtils.getFileTextOffset(includeCommand);
      if (definitionOffset >= includeCommandOffset) {
        return isDefinitionOffsetValid(includeCommandScope);
      }

      
      if (includeCommandScope == null) {
        return (BashPsiUtils.findNextVarDefFunctionDefScope(includeCommand) != null);
      }
      
      if (this.startElementScope != null) {
        return PsiTreeUtil.isAncestor((PsiElement)varDefScope, (PsiElement)includeCommandScope, true);
      }
    } 
    
    return false;
  }


  
  private boolean isDefinitionOffsetValid(BashFunctionDef varDefScope) {
    if (this.startElementScope == null)
    {
      
      return (varDefScope == null || !this.startElement.isVarDefinition());
    }
    
    return (varDefScope == null || varDefScope.equals(this.startElementScope) || !PsiTreeUtil.isAncestor((PsiElement)this.startElementScope, (PsiElement)varDefScope, true));
  }









  
  protected boolean isValidLocalDefinition(BashVarDef varDef) {
    boolean validScope = PsiTreeUtil.isAncestor(BashPsiUtils.findEnclosingBlock((PsiElement)varDef), (PsiElement)this.startElement, false);



    
    return (validScope && BashPsiUtils.getFileTextOffset((PsiElement)varDef) < BashPsiUtils.getFileTextOffset((PsiElement)this.startElement));
  }

  
  public void prepareResults() {
    if (this.ignoreGlobals) {
      for (PsiElement globalVar : this.globalVariables) {
        removeResult(globalVar);
      }
    }
  }
  
  public <T> T getHint(@NotNull Key<T> key) {
    if (key == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "key", "com/ansorgit/plugins/bash/lang/psi/impl/vars/BashVarProcessor", "getHint" }));  return null;
  }
  
  protected Set<PsiElement> getGlobalVariables() {
    return this.globalVariables;
  }
}
