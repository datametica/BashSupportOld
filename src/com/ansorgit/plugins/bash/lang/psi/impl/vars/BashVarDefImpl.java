package com.ansorgit.plugins.bash.lang.psi.impl.vars;

import com.ansorgit.plugins.bash.lang.LanguageBuiltins;
import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.BashCharSequence;
import com.ansorgit.plugins.bash.lang.psi.api.BashPsiElement;
import com.ansorgit.plugins.bash.lang.psi.api.BashReference;
import com.ansorgit.plugins.bash.lang.psi.api.command.BashCommand;
import com.ansorgit.plugins.bash.lang.psi.api.function.BashFunctionDef;
import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVar;
import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVarDef;
import com.ansorgit.plugins.bash.lang.psi.impl.BashBaseStubElementImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.BashElementSharedImpl;
import com.ansorgit.plugins.bash.lang.psi.stubs.api.BashVarDefStub;
import com.ansorgit.plugins.bash.lang.psi.util.BashCommandUtil;
import com.ansorgit.plugins.bash.lang.psi.util.BashIdentifierUtil;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiElementFactory;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import com.ansorgit.plugins.bash.settings.BashProjectSettings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiReference;
import com.intellij.psi.ResolveState;
import com.intellij.psi.StubBasedPsiElement;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;














public class BashVarDefImpl
  extends BashBaseStubElementImpl<BashVarDefStub>
  implements BashVarDef, BashVar, StubBasedPsiElement<BashVarDefStub>
{
  private static final TokenSet accepted = TokenSet.create(new IElementType[] { BashTokenTypes.WORD, BashTokenTypes.ASSIGNMENT_WORD });
  private static final Set<String> commandsWithReadonlyOption = Sets.newHashSet((Object[])new String[] { "declare", "typeset", "local" });
  private static final Set<String> commandsWithArrayOption = Sets.newHashSet((Object[])new String[] { "declare", "typeset", "read", "local" });
  private static final Set<String> localVarDefCommands = commandsWithArrayOption;
  private static final Set<String> typeArrayDeclarationParams = Collections.singleton("-a");
  private static final Set<String> typeReadOnlyParams = Collections.singleton("-r");
  
  private final BashReference reference = new SmartVarDefReference(this);
  private final BashReference dumbReference = new DumbVarDefReference(this);
  
  private final Object stateLock = new Object();
  
  private volatile Boolean cachedFunctionScopeLocal;
  private volatile String name;
  private volatile PsiElement assignmentWord;
  private volatile TextRange nameTextRange;
  
  public BashVarDefImpl(ASTNode astNode) {
    super(astNode, "Bash var def");
  }
  
  public BashVarDefImpl(@NotNull BashVarDefStub stub, @NotNull IStubElementType nodeType) {
    super((StubElement)stub, nodeType, "Bash var def");
  }

  
  public void subtreeChanged() {
    super.subtreeChanged();
    
    synchronized (this.stateLock) {
      this.cachedFunctionScopeLocal = null;
      this.name = null;
      this.assignmentWord = null;
      this.nameTextRange = null;
    } 
  }
  
  public String getName() {
    BashVarDefStub stub = (BashVarDefStub)getStub();
    if (stub != null) {
      return stub.getName();
    }
    
    if (this.name == null)
    {
      synchronized (this.stateLock) {
        if (this.name == null) {
          String newName; PsiElement element = findAssignmentWord();

          
          if (element instanceof BashCharSequence) {
            newName = ((BashCharSequence)element).getUnwrappedCharSequence();
          } else {
            newName = element.getText();
          } 
          
          this.name = newName;
        } 
      } 
    }
    
    return this.name;
  }
  
  public PsiElement setName(@NotNull @NonNls String newName) throws IncorrectOperationException {
    if (newName == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "newName", "com/ansorgit/plugins/bash/lang/psi/impl/vars/BashVarDefImpl", "setName" }));  if (!BashIdentifierUtil.isValidNewVariableName(newName)) {
      throw new IncorrectOperationException("Invalid variable name");
    }
    
    PsiElement original = findAssignmentWord();
    PsiElement replacement = BashPsiElementFactory.createAssignmentWord(getProject(), newName);
    return BashPsiUtils.replaceElement(original, replacement);
  }








  
  public boolean isArray() {
    PsiElement assignmentValue = findAssignmentValue();

    
    if (assignmentValue instanceof com.ansorgit.plugins.bash.lang.psi.api.vars.BashAssignmentList) {
      return true;
    }

    
    PsiElement parentElement = getParent();
    if (parentElement instanceof BashCommand) {
      BashCommand command = (BashCommand)parentElement;
      
      return ("mapfile".equals(command.getReferencedCommandName()) || "readarray"
        .equals(command.getReferencedCommandName()) || 
        isCommandWithParameter(command, commandsWithArrayOption, typeArrayDeclarationParams));
    } 
    
    return false;
  }





  
  @NotNull
  public PsiElement findAssignmentWord() {
    if (this.assignmentWord == null)
    {
      synchronized (this.stateLock) {
        if (this.assignmentWord == null) {
          PsiElement newAssignmentWord, element = findChildByType(accepted);

          
          if (element != null) {
            newAssignmentWord = element;
          }
          else {
            
            PsiElement firstChild = getFirstChild();
            ASTNode childNode = (firstChild != null) ? firstChild.getNode() : null;
            
            ASTNode node = (childNode != null) ? childNode.findChildByType(accepted) : null;
            newAssignmentWord = (node != null) ? node.getPsi() : firstChild;
          } 
          
          this.assignmentWord = newAssignmentWord;
        } 
      } 
    }
    
    if (this.assignmentWord == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/vars/BashVarDefImpl", "findAssignmentWord" }));  return this.assignmentWord;
  }
  
  @Nullable
  public PsiElement findAssignmentValue() {
    PsiElement last = getLastChild();
    return (last != this) ? last : null;
  }
  
  public boolean isFunctionScopeLocal() {
    if (this.cachedFunctionScopeLocal == null) {
      boolean newCachedFunctionScopeLocal = doIsFunctionScopeLocal();
      
      synchronized (this.stateLock) {
        this.cachedFunctionScopeLocal = Boolean.valueOf(newCachedFunctionScopeLocal);
      } 
    } 
    
    return this.cachedFunctionScopeLocal.booleanValue();
  }
  
  private boolean doIsFunctionScopeLocal() {
    if (isLocalVarDef()) {
      return true;
    }










    
    BashFunctionDef scope = BashPsiUtils.findNextVarDefFunctionDefScope((PsiElement)this);
    while (scope != null) {
      if (scope.findLocalScopeVariables().contains(getReferenceName())) {
        return true;
      }
      
      scope = BashPsiUtils.findNextVarDefFunctionDefScope(PsiTreeUtil.getStubOrPsiParent((PsiElement)scope));
    } 
    
    return false;
  }

  
  public boolean isLocalVarDef() {
    PsiElement context = getContext();
    if (context instanceof BashCommand) {
      BashCommand parentCmd = (BashCommand)context;
      String commandName = parentCmd.getReferencedCommandName();

      
      if (parentCmd.isVarDefCommand() && LanguageBuiltins.localVarDefCommands.contains(commandName)) {
        return true;
      }

      
      if (localVarDefCommands.contains(commandName) && BashPsiUtils.findNextVarDefFunctionDefScope(context) != null) {
        return true;
      }
    } 
    
    return false;
  }
  
  public boolean hasAssignmentValue() {
    return (findAssignmentValue() != null);
  }
  
  public boolean isCommandLocal() {
    PsiElement context = getContext();
    if (context instanceof BashCommand) {
      BashCommand parentCmd = (BashCommand)context;
      return (!parentCmd.isPureAssignment() && !parentCmd.isVarDefCommand());
    } 
    
    return false;
  }




  
  public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState resolveState, PsiElement lastParent, @NotNull PsiElement place) {
    if (processor == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "processor", "com/ansorgit/plugins/bash/lang/psi/impl/vars/BashVarDefImpl", "processDeclarations" }));  if (resolveState == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "resolveState", "com/ansorgit/plugins/bash/lang/psi/impl/vars/BashVarDefImpl", "processDeclarations" }));  if (place == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "place", "com/ansorgit/plugins/bash/lang/psi/impl/vars/BashVarDefImpl", "processDeclarations" }));  if (!processor.execute((PsiElement)this, resolveState)) {
      return false;
    }
    
    return BashElementSharedImpl.walkDefinitionScope((PsiElement)this, processor, resolveState, lastParent, place);
  }
  
  public PsiElement getNameIdentifier() {
    return findAssignmentWord();
  }
  
  public final String getReferenceName() {
    return getName();
  }
  
  public PsiElement getElement() {
    return (PsiElement)this;
  }

  
  @NotNull
  public BashReference getReference() {
    if ((DumbService.isDumb(getProject()) ? this.dumbReference : this.reference) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/vars/BashVarDefImpl", "getReference" }));  return DumbService.isDumb(getProject()) ? this.dumbReference : this.reference;
  }

  
  @Nullable
  public BashReference getNeighborhoodReference() {
    return null;
  }

  
  public final boolean isVarDefinition() {
    return true;
  }

  
  public int getPrefixLength() {
    return 0;
  }

  
  public boolean isStaticAssignmentWord() {
    PsiElement word = findAssignmentWord();
    if (word instanceof BashCharSequence) {
      return ((BashCharSequence)word).isStatic();
    }
    
    return true;
  }
  
  public BashFunctionDef findFunctionScope() {
    return (BashFunctionDef)PsiTreeUtil.getStubOrPsiParentOfType((PsiElement)this, BashFunctionDef.class);
  }

  
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "visitor", "com/ansorgit/plugins/bash/lang/psi/impl/vars/BashVarDefImpl", "accept" }));  if (visitor instanceof BashVisitor) {
      ((BashVisitor)visitor).visitVarDef(this);
    } else {
      visitor.visitElement((PsiElement)this);
    } 
  }
  
  public boolean isBuiltinVar() {
    boolean isBash_v4 = BashProjectSettings.storedSettings(getProject()).isSupportBash4();
    
    String varName = getReferenceName();
    boolean v3_var = (LanguageBuiltins.bashShellVars.contains(varName) || LanguageBuiltins.bourneShellVars.contains(varName));
    
    return isBash_v4 ? ((v3_var || LanguageBuiltins.bashShellVars_v4.contains(varName))) : v3_var;
  }
  
  public boolean isParameterExpansion() {
    return false;
  }
  
  public boolean isParameterReference() {
    return false;
  }
  
  public boolean isArrayUse() {
    return false;
  }
  
  public TextRange getAssignmentNameTextRange() {
    if (this.nameTextRange == null) {
      synchronized (this.stateLock) {
        if (this.nameTextRange == null) {
          TextRange newNameTextRange; PsiElement wordElement = findAssignmentWord();

          
          if (wordElement instanceof BashCharSequence) {
            newNameTextRange = ((BashCharSequence)wordElement).getTextContentRange();
          } else {
            newNameTextRange = TextRange.from(0, wordElement.getTextLength());
          } 
          
          this.nameTextRange = newNameTextRange;
        } 
      } 
    }
    
    return this.nameTextRange;
  }
  
  public boolean isReadonly() {
    PsiElement context = getParent();
    if (context instanceof BashCommand) {
      BashCommand command = (BashCommand)context;
      
      if (command.isInternalCommand() && LanguageBuiltins.readonlyVarDefCommands.contains(command.getReferencedCommandName())) {
        return true;
      }

      
      if (isCommandWithParameter(command, commandsWithReadonlyOption, typeReadOnlyParams)) {
        return true;
      }
    } 
    
    return false;
  }
  
  private boolean isCommandWithParameter(BashCommand command, Set<String> validCommands, Set<String> validParams) {
    String commandName = command.getReferencedCommandName();
    
    if (commandName != null && validCommands.contains(commandName)) {
      List<BashPsiElement> parameters = Lists.newArrayList(command.parameters());
      
      for (BashPsiElement argValue : parameters) {
        for (String paramName : validParams) {
          if (BashCommandUtil.isParameterDefined(paramName, argValue.getText())) {
            return true;
          }
        } 
      } 
    } 
    
    return false;
  }
  
  @Nullable
  public List<PsiComment> findAttachedComment() {
    return BashPsiUtils.findDocumentationElementComments((PsiElement)this);
  }
}
