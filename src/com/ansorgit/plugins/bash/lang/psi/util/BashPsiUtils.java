package com.ansorgit.plugins.bash.lang.psi.util;

import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.BashFileReference;
import com.ansorgit.plugins.bash.lang.psi.api.command.BashCommand;
import com.ansorgit.plugins.bash.lang.psi.api.command.BashGenericCommand;
import com.ansorgit.plugins.bash.lang.psi.api.command.BashIncludeCommand;
import com.ansorgit.plugins.bash.lang.psi.api.function.BashFunctionDef;
import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVar;
import com.ansorgit.plugins.bash.lang.psi.api.word.BashWord;
import com.ansorgit.plugins.bash.lang.psi.eval.BashEvalBlock;
import com.ansorgit.plugins.bash.lang.psi.stubs.index.BashIncludeCommandIndex;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.intellij.lang.ASTNode;
import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.ElementManipulator;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.IncorrectOperationException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
























public final class BashPsiUtils
{
  public static PsiFile findFileContext(PsiElement element) {
    return element.getContainingFile();
  }






  
  public static int blockNestingLevel(PsiElement element) {
    int depth = 0;
    
    PsiElement current = findEnclosingBlock(element);
    while (current != null) {
      depth++;
      current = findEnclosingBlock(current);
    } 
    
    return depth;
  }






  
  @Nullable
  public static BashFunctionDef findBroadestFunctionScope(PsiElement startElement) {
    BashFunctionDef lastValidScope = null;
    
    PsiElement element = PsiTreeUtil.getStubOrPsiParent(startElement);
    while (element != null) {
      if (element instanceof BashFunctionDef) {
        lastValidScope = (BashFunctionDef)element;
      }
      
      element = PsiTreeUtil.getStubOrPsiParent(element);
      if (element == null) {
        return lastValidScope;
      }
    } 
    
    return null;
  }






  
  public static BashFunctionDef findNextVarDefFunctionDefScope(PsiElement varDef) {
    PsiElement element = PsiTreeUtil.getStubOrPsiParent(varDef);
    while (element != null) {
      if (element instanceof BashFunctionDef) {
        return (BashFunctionDef)element;
      }
      
      element = PsiTreeUtil.getStubOrPsiParent(element);
    } 
    
    return null;
  }






  
  public static PsiElement findEnclosingBlock(PsiElement element) {
    while (element != null) {
      element = PsiTreeUtil.getStubOrPsiParent(element);
      
      if (isValidContainer(element)) {
        return element;
      }
    } 
    
    return null;
  }
  
  public static int getElementLineNumber(PsiElement element) {
    FileViewProvider fileViewProvider = element.getContainingFile().getViewProvider();
    if (fileViewProvider.getDocument() != null) {
      return fileViewProvider.getDocument().getLineNumber(element.getTextOffset()) + 1;
    }
    
    return 0;
  }
  
  public static int getElementEndLineNumber(PsiElement element) {
    FileViewProvider fileViewProvider = element.getContainingFile().getViewProvider();
    if (fileViewProvider.getDocument() != null) {
      return fileViewProvider.getDocument().getLineNumber(element.getTextOffset() + element.getTextLength()) + 1;
    }
    
    return 0;
  }
  
  public static PsiElement findNextSibling(PsiElement start, IElementType ignoreType) {
    if (start == null) {
      return null;
    }
    
    PsiElement current = start.getNextSibling();
    while (current != null) {
      if (ignoreType != PsiUtilCore.getElementType(current)) {
        return current;
      }
      
      current = current.getNextSibling();
    } 
    
    return null;
  }
  
  public static PsiElement findPreviousSibling(PsiElement start, IElementType ignoreType) {
    if (start == null) {
      return null;
    }
    
    PsiElement current = start.getPrevSibling();
    while (current != null) {
      if (ignoreType != PsiUtilCore.getElementType(current)) {
        return current;
      }
      
      current = current.getPrevSibling();
    } 
    
    return null;
  }







  
  public static <T extends PsiElement> T replaceElement(T original, PsiElement replacement) throws IncorrectOperationException {
    try {
      return (T)original.replace(replacement);
    } catch (IncorrectOperationException incorrectOperationException) {
    
    } catch (UnsupportedOperationException unsupportedOperationException) {}


    
    PsiElement parent = original.getParent();
    if (parent != null) {
      PsiElement inserted = parent.addBefore(replacement, (PsiElement)original);
      original.delete();
      return (T)inserted;
    } 
    
    original.getNode().replaceAllChildrenToChildrenOf(replacement.getNode());
    return original;
  }

  
  @Nullable
  public static TextRange rangeInParent(PsiElement parent, PsiElement child) {
    if (!parent.getTextRange().contains(child.getTextRange())) {
      return null;
    }
    
    return TextRange.from(child.getTextOffset() - parent.getTextOffset(), child.getTextLength());
  }
  
  public static boolean isStaticWordExpr(PsiElement child) {
    while (child != null) {
      if (child instanceof BashVar || child instanceof com.ansorgit.plugins.bash.lang.psi.api.expression.BashSubshellCommand) {
        return false;
      }

      
      if (!isStaticWordExpr(child.getFirstChild())) {
        return false;
      }
      
      child = child.getNextSibling();
    } 
    
    return true;
  }












  
  public static boolean varResolveTreeWalkUp(@NotNull PsiScopeProcessor processor, @NotNull BashVar entrance, @Nullable PsiElement maxScope, @NotNull ResolveState state) {
    int i;
    if (processor == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "processor", "com/ansorgit/plugins/bash/lang/psi/util/BashPsiUtils", "varResolveTreeWalkUp" }));  if (entrance == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "entrance", "com/ansorgit/plugins/bash/lang/psi/util/BashPsiUtils", "varResolveTreeWalkUp" }));  if (state == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "state", "com/ansorgit/plugins/bash/lang/psi/util/BashPsiUtils", "varResolveTreeWalkUp" }));  BashVar bashVar1 = entrance;
    BashVar bashVar2 = entrance;
    
    boolean hasResult = false;
    
    while (bashVar2 != null) {
      i = hasResult | (!bashVar2.processDeclarations(processor, state, (PsiElement)bashVar1, (PsiElement)entrance) ? 1 : 0);
      
      if (bashVar2 == maxScope) {
        break;
      }
      
      bashVar1 = bashVar2;
      PsiElement psiElement = PsiTreeUtil.getStubOrPsiParent((PsiElement)bashVar1);
    } 
    
    return (i == 0);
  }
  
  @Nullable
  public static PsiFile findIncludedFile(BashCommand bashCommand) {
    if (bashCommand instanceof BashIncludeCommand) {
      BashFileReference reference = ((BashIncludeCommand)bashCommand).getFileReference();
      
      if (reference != null) {
        return reference.findReferencedFile();
      }
    } 
    
    return null;
  }






  
  public static Set<PsiFile> findIncludedFiles(PsiFile file, boolean followNestedFiles) {
    Set<PsiFile> files = Sets.newLinkedHashSet();
    
    collectIncludedFiles(file, files, followNestedFiles);
    
    return files;
  }
  
  public static void collectIncludedFiles(PsiFile file, Set<PsiFile> files, boolean followNestedFiles) {
    String filePath = file.getVirtualFile().getPath();
    
    Collection<BashIncludeCommand> commands = StubIndex.getElements(BashIncludeCommandIndex.KEY, filePath, file.getProject(), GlobalSearchScope.fileScope(file), BashIncludeCommand.class);
    for (BashIncludeCommand command : commands) {
      PsiFile includedFile = findIncludedFile((BashCommand)command);
      if (includedFile != null) {
        boolean followFile = (followNestedFiles && !files.contains(includedFile));
        files.add(includedFile);
        
        if (followFile) {
          collectIncludedFiles(includedFile, files, true);
        }
      } 
    } 
  }







  
  public static List<BashIncludeCommand> findIncludeCommands(PsiFile file, @Nullable PsiFile filterByFile) {
    String filePath = file.getVirtualFile().getPath();
    
    List<BashIncludeCommand> result = Lists.newLinkedList();
    
    Collection<BashIncludeCommand> commands = StubIndex.getElements(BashIncludeCommandIndex.KEY, filePath, file.getProject(), GlobalSearchScope.fileScope(file), BashIncludeCommand.class);
    for (BashIncludeCommand command : commands) {
      if (filterByFile == null || filterByFile.equals(findIncludedFile((BashCommand)command))) {
        result.add(command);
      }
    } 
    
    return result;
  }
  
  public static void visitRecursively(PsiElement element, BashVisitor visitor) {
    element.accept((PsiElementVisitor)visitor);


    
    PsiElement child = element.getFirstChild();
    while (child != null) {
      if (child.getNode() instanceof com.intellij.psi.impl.source.tree.CompositeElement) {
        visitRecursively(child, visitor);
      }
      
      child = child.getNextSibling();
    } 
  }
  
  public static boolean hasContext(PsiElement element, PsiElement contextCandidate) {
    for (PsiElement ref = element; ref != null; ref = ref.getContext()) {
      if (ref == contextCandidate) {
        return true;
      }
    } 
    
    return false;
  }
  
  public static boolean isValidReferenceScope(PsiElement referenceToDefCandidate, PsiElement variableDefinition) {
    PsiFile definitionFile = findFileContext(variableDefinition);
    PsiFile referenceFile = findFileContext(referenceToDefCandidate);
    
    boolean sameFile = definitionFile.equals(referenceFile);
    
    if (sameFile) {
      return isValidGlobalOffset(referenceToDefCandidate, variableDefinition);
    }




    
    List<BashIncludeCommand> includeCommands = findIncludeCommands(referenceFile, definitionFile);

    
    for (BashCommand includeCommand : includeCommands) {
      if (!isValidGlobalOffset(referenceToDefCandidate, (PsiElement)includeCommand)) {
        return false;
      }
    } 
    
    return true;
  }
  
  public static List<PsiComment> findDocumentationElementComments(PsiElement element) {
    PsiElement command = (PsiElement)findStubParent(element, BashCommand.class);
    if (command == null) {
      command = findStubParent(element, BashFunctionDef.class);
    }
    
    if (command == null) {
      return Collections.emptyList();
    }
    
    int previousLine = getElementLineNumber(element);
    
    PsiElement current = command.getPrevSibling();
    
    List<PsiComment> result = Lists.newLinkedList();
    
    while (current != null && current.getNode() != null && current.getNode().getElementType() == BashTokenTypes.LINE_FEED) {
      current = current.getPrevSibling();
      
      if (current instanceof PsiComment && getElementEndLineNumber(current) + 1 == previousLine) {
        result.add(0, (PsiComment)current);
        previousLine = getElementLineNumber(current);
        current = current.getPrevSibling();
      } 
    } 


    
    return result;
  }
  
  @Nullable
  public static <T extends PsiElement> T findStubParent(@Nullable PsiElement start, Class<T> parentType) {
    if (start == null) {
      return null;
    }
    
    Class<PsiFile> clazz = PsiFile.class;
    
    for (PsiElement current = start; current != null; current = PsiTreeUtil.getStubOrPsiParent(current)) {
      if (parentType.isInstance(current)) {
        return (T)current;
      }
      
      if (clazz.isInstance(current)) {
        return null;
      }
    } 
    
    return null;
  }
  
  @Nullable
  public static <T extends PsiElement> T findParent(@Nullable PsiElement start, Class<T> parentType) {
    return findParent(start, parentType, (Class)PsiFile.class);
  }
  
  @Nullable
  public static <T extends PsiElement> T findParent(@Nullable PsiElement start, Class<T> parentType, Class<? extends PsiElement> breakPoint) {
    if (start == null) {
      return null;
    }
    
    for (PsiElement current = start; current != null; current = current.getParent()) {
      if (parentType.isInstance(current)) {
        return (T)current;
      }
      
      if (breakPoint != null && breakPoint.isInstance(current)) {
        return null;
      }
    } 
    
    return null;
  }
  
  public static boolean hasParentOfType(PsiElement start, Class<? extends PsiElement> parentType, int maxSteps) {
    return hasParentOfType(start, parentType, maxSteps, (Class)PsiFile.class);
  }
  
  public static boolean isCommandParameterWord(PsiElement start) {
    BashCommand command = (BashCommand)PsiTreeUtil.getParentOfType(start, BashCommand.class);
    if (command == null) {
      return false;
    }
    
    BashWord word = (BashWord)PsiTreeUtil.getParentOfType(start, BashWord.class);
    
    return (word != null && PsiTreeUtil.getPrevSiblingOfType((PsiElement)word, BashGenericCommand.class) != null);
  }

  
  public static boolean hasParentOfType(PsiElement start, Class<? extends PsiElement> parentType, int maxSteps, Class<? extends PsiElement> breakPoint) {
    for (PsiElement current = start; current != null && maxSteps-- >= 0; current = current.getParent()) {
      if (parentType.isInstance(current)) {
        return true;
      }
      
      if (breakPoint != null && breakPoint.isInstance(current)) {
        return false;
      }
    } 
    
    return false;
  }
  
  public static boolean isInjectedElement(@NotNull PsiElement element) {
    if (element == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "element", "com/ansorgit/plugins/bash/lang/psi/util/BashPsiUtils", "isInjectedElement" }));  InjectedLanguageManager languageManager = InjectedLanguageManager.getInstance(element.getProject());
    return (languageManager.isInjectedFragment(element.getContainingFile()) || hasInjectionHostParent(element));
  }







  
  public static int getFileTextOffset(@NotNull PsiElement element) {
    if (element == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "element", "com/ansorgit/plugins/bash/lang/psi/util/BashPsiUtils", "getFileTextOffset" }));  int offset = element.getTextOffset();
    if (isInjectedElement(element)) {
      
      InjectedLanguageManager languageManager = InjectedLanguageManager.getInstance(element.getProject());
      
      PsiLanguageInjectionHost injectionHost = languageManager.getInjectionHost(element);
      if (injectionHost != null) {
        offset += injectionHost.getTextOffset();
      }
    } 
    
    return offset;
  }
  
  public static int getFileTextEndOffset(@NotNull PsiElement element) {
    if (element == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "element", "com/ansorgit/plugins/bash/lang/psi/util/BashPsiUtils", "getFileTextEndOffset" }));  return getFileTextOffset(element) + element.getTextLength();
  }
  
  public static TextRange getTextRangeInFile(PsiElement element) {
    int offset = getFileTextOffset(element);
    
    return TextRange.from(offset, element.getTextLength());
  }







  
  @NotNull
  public static ASTNode getDeepestEquivalent(ASTNode parent) {
    ASTNode element = parent;
    while (element.getFirstChildNode() != null && element.getFirstChildNode() == element.getLastChildNode() && element.getTextRange().equals(parent.getTextRange())) {
      element = element.getFirstChildNode();
    }
    
    if (element == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/util/BashPsiUtils", "getDeepestEquivalent" }));  return element;
  }
  
  @Nullable
  public static ASTNode findEquivalentParent(@NotNull ASTNode node, @Nullable IElementType stopAt) {
    if (node == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "node", "com/ansorgit/plugins/bash/lang/psi/util/BashPsiUtils", "findEquivalentParent" }));  TextRange sourceRange = node.getTextRange();
    
    ASTNode current = node;
    do {
      ASTNode parent = current.getTreeParent();
      if (parent == null || !parent.getTextRange().equals(sourceRange)) {
        return (stopAt != null && current.getElementType() != stopAt) ? null : current;
      }
      
      current = parent;
    } while (stopAt != null && !stopAt.equals(current.getElementType()));
    return current;
  }


  
  @Nullable
  public static PsiElement findEquivalentParent(@NotNull PsiElement node, @Nullable IElementType stopAt) {
    if (node == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "node", "com/ansorgit/plugins/bash/lang/psi/util/BashPsiUtils", "findEquivalentParent" }));  ASTNode parent = findEquivalentParent(node.getNode(), stopAt);
    if (parent != null) {
      return parent.getPsi();
    }
    return null;
  }
  
  @Nullable
  public static PsiReference selfReference(PsiElement element) {
    ElementManipulator<PsiElement> manipulator = ElementManipulators.getManipulator(element);
    if (manipulator == null) {
      return null;
    }
    
    return (PsiReference)new PsiReferenceBase.Immediate(element, manipulator.getRangeInElement(element), true, element);
  }
  
  public static boolean isSingleChildParent(PsiElement psi) {
    if (psi == null) {
      return false;
    }
    
    ASTNode child = psi.getNode();
    return (child.getTreePrev() == null && child.getTreeNext() == null);
  }
  
  public static boolean isSingleChildParent(PsiElement psi, @NotNull IElementType childType) {
    if (childType == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "childType", "com/ansorgit/plugins/bash/lang/psi/util/BashPsiUtils", "isSingleChildParent" }));  if (psi == null) {
      return false;
    }
    
    ASTNode child = getDeepestEquivalent(psi.getNode());
    return (child.getTreePrev() == null && child.getTreeNext() == null && child.getElementType() == childType);
  }
  
  public static boolean isSingleChildParent(PsiElement psi, @NotNull Class<? extends PsiElement> childType) {
    if (childType == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "childType", "com/ansorgit/plugins/bash/lang/psi/util/BashPsiUtils", "isSingleChildParent" }));  if (psi == null) {
      return false;
    }
    
    ASTNode child = getDeepestEquivalent(psi.getNode());
    PsiElement childPsi = child.getPsi();
    
    return childType.isInstance(childPsi);
  }
  
  public static boolean isInEvalBlock(PsiElement element) {
    return (findParent(element, BashEvalBlock.class) != null);
  }
  
  private static boolean isValidContainer(PsiElement element) {
    if (element instanceof com.ansorgit.plugins.bash.lang.psi.api.loops.BashFor)
    {
      return false;
    }
    
    return (element instanceof com.ansorgit.plugins.bash.lang.psi.api.BashBlock || element instanceof BashFunctionDef || element instanceof com.ansorgit.plugins.bash.lang.psi.api.BashFile);
  }











  
  private static boolean isValidGlobalOffset(PsiElement referenceElement, PsiElement definition) {
    BashFunctionDef refScope = findNextVarDefFunctionDefScope(referenceElement);
    BashFunctionDef defScope = findNextVarDefFunctionDefScope(definition);
    
    int refOffset = referenceElement.getTextOffset();
    int defOffset = definition.getTextOffset();

    
    if (refScope == defScope || (refScope != null && refScope.isEquivalentTo((PsiElement)defScope)))
    {
      return (refOffset > defOffset);
    }

    
    if (refScope != null && defScope != null) {
      return true;
    }



    
    return (refOffset > defOffset);
  }
  
  private static boolean hasInjectionHostParent(PsiElement element) {
    return hasParentOfType(element, (Class)PsiLanguageInjectionHost.class, 10);
  }
}
