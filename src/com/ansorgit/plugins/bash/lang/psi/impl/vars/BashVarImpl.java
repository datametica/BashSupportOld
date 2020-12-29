package com.ansorgit.plugins.bash.lang.psi.impl.vars;

import com.ansorgit.plugins.bash.lang.LanguageBuiltins;
import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.ansorgit.plugins.bash.lang.parser.BashElementTypes;
import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.BashReference;
import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVar;
import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVarUse;
import com.ansorgit.plugins.bash.lang.psi.impl.BashBaseStubElementImpl;
import com.ansorgit.plugins.bash.lang.psi.stubs.api.BashVarStub;
import com.ansorgit.plugins.bash.lang.psi.util.BashIdentifierUtil;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiElementFactory;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiReference;
import com.intellij.psi.ResolveState;
import com.intellij.psi.StubBasedPsiElement;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import org.apache.commons.lang.math.NumberUtils;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;






















public class BashVarImpl
  extends BashBaseStubElementImpl<BashVarStub>
  implements BashVar, BashVarUse, StubBasedPsiElement<BashVarStub>
{
  private final BashReference varReference = new SmartBashVarReference(this);
  private final BashReference dumbVarReference = new DumbBashVarReference(this);
  
  private final BashReference varNeighborhoodReference = new SmartBashVarReference(this, true);
  private final BashReference dumbVarNeighborhoodReference = new DumbBashVarReference(this, true);
  
  private final Object stateLock = new Object();
  private volatile int prefixLength = -1;
  private volatile String referencedName;
  private volatile TextRange nameTextRange;
  
  public BashVarImpl(ASTNode astNode) {
    super(astNode, "Bash-var");
  }
  
  public BashVarImpl(@NotNull BashVarStub stub, @NotNull IStubElementType nodeType) {
    super((StubElement)stub, nodeType, "Bash var def");
  }

  
  public void subtreeChanged() {
    super.subtreeChanged();
    
    synchronized (this.stateLock) {
      this.prefixLength = -1;
      this.referencedName = null;
      this.nameTextRange = null;
    } 
  }

  
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "visitor", "com/ansorgit/plugins/bash/lang/psi/impl/vars/BashVarImpl", "accept" }));  if (visitor instanceof BashVisitor) {
      ((BashVisitor)visitor).visitVarUse(this);
    } else {
      visitor.visitElement((PsiElement)this);
    } 
  }

  
  public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement place) {
    if (processor == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "processor", "com/ansorgit/plugins/bash/lang/psi/impl/vars/BashVarImpl", "processDeclarations" }));  if (state == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "state", "com/ansorgit/plugins/bash/lang/psi/impl/vars/BashVarImpl", "processDeclarations" }));  if (place == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "place", "com/ansorgit/plugins/bash/lang/psi/impl/vars/BashVarImpl", "processDeclarations" }));  return processor.execute((PsiElement)this, state);
  }

  
  @NotNull
  public BashReference getReference() {
    if ((DumbService.isDumb(getProject()) ? this.dumbVarReference : this.varReference) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/vars/BashVarImpl", "getReference" }));  return DumbService.isDumb(getProject()) ? this.dumbVarReference : this.varReference;
  }

  
  @NotNull
  public BashReference getNeighborhoodReference() {
    if ((DumbService.isDumb(getProject()) ? this.dumbVarNeighborhoodReference : this.varNeighborhoodReference) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/vars/BashVarImpl", "getNeighborhoodReference" }));  return DumbService.isDumb(getProject()) ? this.dumbVarNeighborhoodReference : this.varNeighborhoodReference;
  }

  
  public final boolean isVarDefinition() {
    return false;
  }
  
  public PsiElement getElement() {
    return (PsiElement)this;
  }

  
  public String getName() {
    return getReferenceName();
  }

  
  public PsiElement setName(@NonNls @NotNull String newName) throws IncorrectOperationException {
    if (newName == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "newName", "com/ansorgit/plugins/bash/lang/psi/impl/vars/BashVarImpl", "setName" }));  if (!BashIdentifierUtil.isValidNewVariableName(newName)) {
      throw new IncorrectOperationException("Invalid variable name");
    }
    
    PsiElement replacement = BashPsiElementFactory.createVariable(getProject(), newName, isParameterExpansion());
    return BashPsiUtils.replaceElement((PsiElement)this, replacement);
  }
  
  public String getReferenceName() {
    BashVarStub stub = (BashVarStub)getStub();
    if (stub != null) {
      return stub.getName();
    }
    
    if (this.referencedName == null) {
      synchronized (this.stateLock) {
        if (this.referencedName == null) {
          this.referencedName = getNameTextRange().substring(getText());
        }
      } 
    }
    
    return this.referencedName;
  }






  
  public int getPrefixLength() {
    BashVarStub stub = (BashVarStub)getStub();
    if (stub != null) {
      return stub.getPrefixLength();
    }
    
    if (this.prefixLength == -1) {
      synchronized (this.stateLock) {
        if (this.prefixLength == -1) {
          String text = getText();
          this.prefixLength = text.startsWith("\\$") ? 2 : (text.startsWith("$") ? 1 : 0);
        } 
      } 
    }
    
    return this.prefixLength;
  }
  
  public boolean isBuiltinVar() {
    String name = getReferenceName();
    return (LanguageBuiltins.bashShellVars.contains(name) || LanguageBuiltins.bourneShellVars.contains(name));
  }
  
  public boolean isParameterExpansion() {
    return (getPrefixLength() == 0 && (getParent() instanceof com.ansorgit.plugins.bash.lang.psi.api.vars.BashComposedVar || getParent() instanceof com.ansorgit.plugins.bash.lang.psi.api.vars.BashParameterExpansion));
  }
  
  public boolean isParameterReference() {
    if (getTextLength() > 2) {
      return false;
    }
    
    if (LanguageBuiltins.bashShellParamReferences.contains(getReferenceName())) {
      return true;
    }

    
    return (NumberUtils.toInt(getReferenceName(), -1) >= 0);
  }
  
  public boolean isArrayUse() {
    if (!isParameterExpansion()) {
      return false;
    }
    
    PsiElement nextLeafNode = PsiTreeUtil.nextLeaf((PsiElement)this);
    if (nextLeafNode == null) {
      return false;
    }
    
    ASTNode nextLeaf = nextLeafNode.getNode();
    ASTNode nextNode = getNextSibling().getNode();
    boolean nextLeafIsSquare = (nextLeaf.getElementType() == BashTokenTypes.LEFT_SQUARE);
    
    ASTNode prev = getNode().getTreePrev();
    
    if (prev != null && (prev.getElementType() == BashTokenTypes.PARAM_EXPANSION_OP_HASH || prev.getElementType() == BashTokenTypes.PARAM_EXPANSION_OP_HASH_HASH)) {
      return true;
    }

    
    if (nextNode.getElementType() == BashElementTypes.ARITHMETIC_COMMAND && nextLeafIsSquare) {
      return true;
    }

    
    PsiElement nextLeaf2nd = PsiTreeUtil.nextLeaf(nextLeafNode);
    if (nextLeafIsSquare && nextLeaf2nd != null) {
      IElementType next2 = nextLeaf2nd.getNode().getElementType();
      return (next2 == BashTokenTypes.PARAM_EXPANSION_OP_STAR || next2 == BashTokenTypes.PARAM_EXPANSION_OP_AT);
    } 
    
    return false;
  }
  
  protected TextRange getNameTextRange() {
    if (this.nameTextRange == null) {
      synchronized (this.stateLock) {
        if (this.nameTextRange == null) {
          this.nameTextRange = TextRange.create(getPrefixLength(), getTextLength());
        }
      } 
    }
    
    return this.nameTextRange;
  }
}
