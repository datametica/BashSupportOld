package com.ansorgit.plugins.bash.lang.psi.impl.function;

import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.BashBlock;
import com.ansorgit.plugins.bash.lang.psi.api.BashFunctionDefName;
import com.ansorgit.plugins.bash.lang.psi.api.BashPsiElement;
import com.ansorgit.plugins.bash.lang.psi.api.function.BashFunctionDef;
import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVar;
import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVarDef;
import com.ansorgit.plugins.bash.lang.psi.impl.BashBaseStubElementImpl;
import com.ansorgit.plugins.bash.lang.psi.stubs.api.BashFunctionDefStub;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiElementFactory;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import com.ansorgit.plugins.bash.lang.psi.util.BashResolveUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.ResolveState;
import com.intellij.psi.StubBasedPsiElement;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.PlatformIcons;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import javax.swing.Icon;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;












public class BashFunctionDefImpl
  extends BashBaseStubElementImpl<BashFunctionDefStub>
  implements BashFunctionDef, StubBasedPsiElement<BashFunctionDefStub>
{
  private final Object stateLock = new Object();
  private final FunctionDefPresentation presentation = new FunctionDefPresentation(this);
  private volatile BashBlock body;
  private volatile boolean computedBody = false;
  private volatile List<BashPsiElement> referencedParameters;
  private volatile Set<String> localScopeVariables;
  
  public BashFunctionDefImpl(ASTNode astNode) {
    super(astNode, "bash function()");
  }
  
  public BashFunctionDefImpl(@NotNull BashFunctionDefStub stub, @NotNull IStubElementType nodeType) {
    super((StubElement)stub, nodeType, null);
  }

  
  public void subtreeChanged() {
    super.subtreeChanged();
    
    synchronized (this.stateLock) {
      this.computedBody = false;
      this.body = null;
      this.referencedParameters = null;
      this.localScopeVariables = null;
    } 
  }
  
  public PsiElement setName(@NotNull @NonNls String name) throws IncorrectOperationException {
    if (name == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "name", "com/ansorgit/plugins/bash/lang/psi/impl/function/BashFunctionDefImpl", "setName" }));  if (StringUtil.isEmpty(name)) {
      return null;
    }


    
    BashFunctionDefName bashFunctionDefName = getNameSymbol();
    if (bashFunctionDefName == null) {
      throw new IncorrectOperationException("invalid name");
    }
    
    PsiElement newNameSymbol = BashPsiElementFactory.createSymbol(getProject(), name);
    
    getNode().replaceChild(bashFunctionDefName.getNode(), newNameSymbol.getNode());
    return (PsiElement)this;
  }

  
  public String getName() {
    return getDefinedName();
  }
  
  public BashBlock functionBody() {
    if (!this.computedBody) {
      synchronized (this.stateLock) {
        if (!this.computedBody) {
          this.body = (BashBlock)findChildByClass(BashBlock.class);
          this.computedBody = true;
        } 
      } 
    }
    
    return this.body;
  }
  
  public BashFunctionDefName getNameSymbol() {
    return (BashFunctionDefName)findChildByClass(BashFunctionDefName.class);
  }
  
  @Nullable
  public List<PsiComment> findAttachedComment() {
    return BashPsiUtils.findDocumentationElementComments((PsiElement)this);
  }
  
  @NotNull
  public List<BashPsiElement> findReferencedParameters() {
    if (this.referencedParameters == null) {
      synchronized (this.stateLock) {
        if (this.referencedParameters == null) {
          
          List<BashPsiElement> newReferencedParameters = Lists.newLinkedList();
          
          for (BashVar var : PsiTreeUtil.collectElementsOfType((PsiElement)this, new Class[] { BashVar.class })) {
            if (var.isParameterReference() && equals(BashPsiUtils.findParent((PsiElement)var, BashFunctionDef.class, BashFunctionDef.class))) {
              newReferencedParameters.add(var);
            }
          } 
          
          this.referencedParameters = newReferencedParameters;
        } 
      } 
    }
    
    if (this.referencedParameters == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/function/BashFunctionDefImpl", "findReferencedParameters" }));  return this.referencedParameters;
  }

  
  @NotNull
  public Set<String> findLocalScopeVariables() {
    if (this.localScopeVariables == null) {
      synchronized (this.stateLock) {
        if (this.localScopeVariables == null) {
          this.localScopeVariables = Sets.newLinkedHashSetWithExpectedSize(10);
          
          Collection<BashVarDef> varDefs = PsiTreeUtil.findChildrenOfType((PsiElement)this, BashVarDef.class);
          for (BashVarDef varDef : varDefs) {
            if (varDef.isLocalVarDef() && isEquivalentTo((PsiElement)BashPsiUtils.findNextVarDefFunctionDefScope((PsiElement)varDef))) {
              this.localScopeVariables.add(varDef.getReferenceName());
            }
          } 
        } 
      } 
    }
    
    if (this.localScopeVariables == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/function/BashFunctionDefImpl", "findLocalScopeVariables" }));  return this.localScopeVariables;
  }
  
  public String getDefinedName() {
    BashFunctionDefStub stub = (BashFunctionDefStub)getStub();
    if (stub != null) {
      return stub.getName();
    }
    
    BashFunctionDefName symbol = getNameSymbol();
    
    return (symbol == null) ? "" : symbol.getNameString();
  }

  
  public Icon getIcon(int flags) {
    return PlatformIcons.METHOD_ICON;
  }
  
  public int getTextOffset() {
    ASTNode name = getNameSymbol().getNode();
    return (name != null) ? name.getStartOffset() : super.getTextOffset();
  }

  
  public ItemPresentation getPresentation() {
    return this.presentation;
  }
  
  public PsiElement getNameIdentifier() {
    return (PsiElement)getNameSymbol();
  }

  
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "visitor", "com/ansorgit/plugins/bash/lang/psi/impl/function/BashFunctionDefImpl", "accept" }));  if (visitor instanceof BashVisitor) {
      ((BashVisitor)visitor).visitFunctionDef(this);
    } else {
      visitor.visitElement((PsiElement)this);
    } 
  }

  
  public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement place) {
    if (processor == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "processor", "com/ansorgit/plugins/bash/lang/psi/impl/function/BashFunctionDefImpl", "processDeclarations" }));  if (state == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "state", "com/ansorgit/plugins/bash/lang/psi/impl/function/BashFunctionDefImpl", "processDeclarations" }));  if (place == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "place", "com/ansorgit/plugins/bash/lang/psi/impl/function/BashFunctionDefImpl", "processDeclarations" }));  if (lastParent != null && lastParent.equals(functionBody())) {
      return processor.execute((PsiElement)this, state);
    }
    
    return BashResolveUtil.processContainerDeclarations((BashPsiElement)this, processor, state, lastParent, place);
  }
  
  private static class FunctionDefPresentation implements ItemPresentation {
    private final BashFunctionDef function;
    
    FunctionDefPresentation(BashFunctionDefImpl functionDef) {
      this.function = functionDef;
    }
    
    public String getPresentableText() {
      return this.function.getName() + "()";
    }
    
    public String getLocationString() {
      return null;
    }
    
    public Icon getIcon(boolean open) {
      return null;
    }
  }
}
