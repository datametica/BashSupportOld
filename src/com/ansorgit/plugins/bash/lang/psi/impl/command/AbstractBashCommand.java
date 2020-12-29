package com.ansorgit.plugins.bash.lang.psi.impl.command;

import com.ansorgit.plugins.bash.jetbrains.PsiScopesUtil;
import com.ansorgit.plugins.bash.lang.LanguageBuiltins;
import com.ansorgit.plugins.bash.lang.parser.BashElementTypes;
import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.BashFile;
import com.ansorgit.plugins.bash.lang.psi.api.BashPsiElement;
import com.ansorgit.plugins.bash.lang.psi.api.command.BashCommand;
import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVarDef;
import com.ansorgit.plugins.bash.lang.psi.impl.BashBaseStubElementImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.Keys;
import com.ansorgit.plugins.bash.lang.psi.stubs.api.BashCommandStub;
import com.ansorgit.plugins.bash.lang.psi.stubs.api.BashCommandStubBase;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import com.ansorgit.plugins.bash.lang.psi.util.BashResolveUtil;
import com.ansorgit.plugins.bash.settings.BashProjectSettings;
import com.google.common.collect.Lists;
import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiInvalidElementAccessException;
import com.intellij.psi.PsiReference;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.tree.IElementType;
import java.util.Collections;
import java.util.List;
import javax.swing.Icon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;








public class AbstractBashCommand<T extends BashCommandStubBase>
  extends BashBaseStubElementImpl<T>
  implements BashCommand, Keys
{
  private final PsiReference functionReference = (PsiReference)new SmartFunctionReference(this);
  private final PsiReference dumbFunctionReference = (PsiReference)new DumbFunctionReference(this);
  
  private final PsiReference bashFileReference = (PsiReference)new SmartBashFileReference(this);
  private final PsiReference dumbBashFileReference = (PsiReference)new DumbBashFileReference(this);
  
  private final Object stateLock = new Object();
  private volatile boolean hasReferencedCommandName = false;
  private volatile String referencedCommandName;
  private volatile Boolean isInternalCommandBash3;
  private volatile Boolean isInternalCommandBash4;
  private volatile List<BashPsiElement> parameters;
  private volatile ASTNode genericCommandElement;
  
  public AbstractBashCommand(ASTNode astNode, String name) {
    super(astNode, name);
  }
  
  public AbstractBashCommand(T stub, IStubElementType nodeType, String name) {
    super((StubElement)stub, nodeType, name);
  }


  
  @NotNull
  public BashFile getContainingFile() {
    if ((BashFile)super.getContainingFile() == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/command/AbstractBashCommand", "getContainingFile" }));  return (BashFile)super.getContainingFile();
  }

  
  public void subtreeChanged() {
    super.subtreeChanged();
    
    synchronized (this.stateLock) {
      this.genericCommandElement = null;
      this.hasReferencedCommandName = false;
      this.referencedCommandName = null;
      this.isInternalCommandBash3 = null;
      this.isInternalCommandBash4 = null;
      this.parameters = null;
    } 
  }
  
  public boolean isGenericCommand() {
    BashCommandStubBase bashCommandStubBase = (BashCommandStubBase)getStub();
    if (bashCommandStubBase != null) {
      return bashCommandStubBase.isGenericCommand();
    }
    
    return (commandElementNode() != null);
  }
  
  public boolean isFunctionCall() {
    if (isSlowResolveRequired()) {
      return (isGenericCommand() && this.dumbFunctionReference.resolve() != null);
    }
    
    return (isGenericCommand() && this.functionReference.resolve() != null);
  }

  
  public boolean isInternalCommand(boolean bash4) {
    BashCommandStubBase bashCommandStubBase = (BashCommandStubBase)getStub();
    if (bashCommandStubBase != null) {
      return bashCommandStubBase.isInternalCommand(bash4);
    }
    
    if (this.isInternalCommandBash3 == null || this.isInternalCommandBash4 == null)
    {
      synchronized (this.stateLock) {
        if (this.isInternalCommandBash3 == null || this.isInternalCommandBash4 == null) {
          boolean isBash3 = false;
          boolean isBash4 = false;
          
          if (isGenericCommand()) {
            String commandText = getReferencedCommandName();
            isBash3 = LanguageBuiltins.isInternalCommand(commandText, false);
            isBash4 = LanguageBuiltins.isInternalCommand(commandText, true);
          } 
          
          this.isInternalCommandBash3 = Boolean.valueOf(isBash3);
          this.isInternalCommandBash4 = Boolean.valueOf(isBash4);
        } 
      } 
    }
    
    return (bash4 ? this.isInternalCommandBash4 : this.isInternalCommandBash3).booleanValue();
  }
  
  public boolean isInternalCommand() {
    return isInternalCommand(BashProjectSettings.storedSettings(getProject()).isSupportBash4());
  }





  
  public boolean isExternalCommand() {
    return (!isInternalCommand() && !isFunctionCall());
  }

  
  public boolean isBashScriptCall() {
    if (isSlowResolveRequired()) {
      return (this.dumbBashFileReference.resolve() != null);
    }
    
    return (this.bashFileReference.resolve() != null);
  }

  
  public boolean isPureAssignment() {
    return (commandElement() == null && hasAssignments());
  }
  
  public boolean isVarDefCommand() {
    return (isInternalCommand() && (LanguageBuiltins.varDefCommands
      .contains(getReferencedCommandName()) || LanguageBuiltins.localVarDefCommands
      .contains(getReferencedCommandName())));
  }
  
  public boolean hasAssignments() {
    return (findChildByType((IElementType)BashElementTypes.VAR_DEF_ELEMENT) != null);
  }
  
  @Nullable
  public PsiElement commandElement() {
    ASTNode node = commandElementNode();
    return (node != null) ? node.getPsi() : null;
  }
  
  @Nullable
  private ASTNode commandElementNode() {
    if (this.genericCommandElement == null)
    {
      synchronized (this.stateLock) {
        if (this.genericCommandElement == null) {
          this.genericCommandElement = getNode().findChildByType(BashElementTypes.GENERIC_COMMAND_ELEMENT);
        }
      } 
    }
    
    return this.genericCommandElement;
  }
  
  public List<BashPsiElement> parameters() {
    if (this.parameters == null)
    {
      synchronized (this.stateLock) {
        if (this.parameters == null) {
          List<BashPsiElement> newParameters; PsiElement cmd = commandElement();

          
          if (cmd == null) {
            newParameters = Collections.emptyList();
          } else {
            newParameters = Lists.newLinkedList();
            
            PsiElement nextSibling = cmd.getNextSibling();
            while (nextSibling != null) {
              if (nextSibling instanceof BashPsiElement && !(nextSibling instanceof com.ansorgit.plugins.bash.lang.psi.api.expression.BashRedirectList)) {
                newParameters.add((BashPsiElement)nextSibling);
              }
              
              nextSibling = nextSibling.getNextSibling();
            } 
          } 
          
          this.parameters = newParameters;
        } 
      } 
    }
    
    return this.parameters;
  }
  
  public BashVarDef[] assignments() {
    return (BashVarDef[])findChildrenByClass(BashVarDef.class);
  }

  
  public PsiReference getReference() {
    boolean slowFallback = isSlowResolveRequired();
    
    if (isFunctionCall()) {
      return slowFallback ? this.dumbFunctionReference : this.functionReference;
    }
    
    if (isInternalCommand())
    {
      return BashPsiUtils.selfReference((PsiElement)this);
    }
    
    return slowFallback ? this.dumbBashFileReference : this.bashFileReference;
  }
  
  @Nullable
  public String getReferencedCommandName() {
    BashCommandStubBase bashCommandStubBase = (BashCommandStubBase)getStub();
    if (bashCommandStubBase instanceof BashCommandStub) {
      return ((BashCommandStub)bashCommandStubBase).getBashCommandName();
    }
    
    if (!this.hasReferencedCommandName)
    {
      synchronized (this.stateLock) {
        if (!this.hasReferencedCommandName) {
          ASTNode command = commandElementNode();
          String newCommandName = (command != null) ? command.getText() : null;
          
          this.hasReferencedCommandName = true;
          this.referencedCommandName = newCommandName;
        } 
      } 
    }
    
    return this.referencedCommandName;
  }

  
  public boolean canNavigate() {
    return (isFunctionCall() || isBashScriptCall());
  }

  
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "visitor", "com/ansorgit/plugins/bash/lang/psi/impl/command/AbstractBashCommand", "accept" }));  if (visitor instanceof BashVisitor) {
      BashVisitor v = (BashVisitor)visitor;
      if (isInternalCommand()) {
        v.visitInternalCommand(this);
      } else {
        v.visitGenericCommand(this);
      } 
    } else {
      visitor.visitElement((PsiElement)this);
    } 
  }


  
  public ItemPresentation getPresentation() {
    return new ItemPresentation() {
        public String getPresentableText() {
          PsiElement element = AbstractBashCommand.this.commandElement();
          return (element == null) ? "unknown" : element.getText();
        }
        
        public String getLocationString() {
          return null;
        }
        
        public Icon getIcon(boolean open) {
          return null;
        }
      };
  }


  
  public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement place) {
    if (processor == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "processor", "com/ansorgit/plugins/bash/lang/psi/impl/command/AbstractBashCommand", "processDeclarations" }));  if (state == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "state", "com/ansorgit/plugins/bash/lang/psi/impl/command/AbstractBashCommand", "processDeclarations" }));  if (place == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "place", "com/ansorgit/plugins/bash/lang/psi/impl/command/AbstractBashCommand", "processDeclarations" }));  return PsiScopesUtil.walkChildrenScopes((PsiElement)this, processor, state, lastParent, place);
  }
  
  public boolean isIncludeCommand() {
    return false;
  }



  
  private boolean isSlowResolveRequired() {
    Project project = getProject();
    BashFile bashFile = getContainingFile();
    
    return (DumbService.isDumb(project) || BashResolveUtil.isScratchFile((PsiFile)bashFile) || BashResolveUtil.isNotIndexedFile(project, bashFile.getVirtualFile()));
  }
}
