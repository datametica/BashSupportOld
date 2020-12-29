package com.ansorgit.plugins.bash.editor.usages;

import com.ansorgit.plugins.bash.lang.lexer.BashLexer;
import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.ansorgit.plugins.bash.lang.parser.BashElementTypes;
import com.ansorgit.plugins.bash.lang.psi.api.command.BashCommand;
import com.intellij.lang.cacheBuilder.DefaultWordsScanner;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.lexer.Lexer;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;


























public class BashFindUsagesProvider
  implements FindUsagesProvider, BashTokenTypes
{
  public WordsScanner getWordsScanner() {
    return (WordsScanner)new BashWordsScanner();
  }
  
  public boolean canFindUsagesFor(@NotNull PsiElement psi) {
    if (psi == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "psi", "com/ansorgit/plugins/bash/editor/usages/BashFindUsagesProvider", "canFindUsagesFor" }));  return (psi instanceof com.ansorgit.plugins.bash.lang.psi.api.vars.BashVar || psi instanceof com.ansorgit.plugins.bash.lang.psi.api.BashFile || psi instanceof com.ansorgit.plugins.bash.lang.psi.api.BashFileReference || psi instanceof BashCommand || psi instanceof com.ansorgit.plugins.bash.lang.psi.api.heredoc.BashHereDocMarker || psi instanceof com.ansorgit.plugins.bash.lang.psi.api.function.BashFunctionDef || psi instanceof com.ansorgit.plugins.bash.lang.psi.api.BashFunctionDefName);
  }






  
  public String getHelpId(@NotNull PsiElement psiElement) {
    if (psiElement == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "psiElement", "com/ansorgit/plugins/bash/editor/usages/BashFindUsagesProvider", "getHelpId" }));  return null;
  }
  
  @NotNull
  public String getType(@NotNull PsiElement element) {
    if (element == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "element", "com/ansorgit/plugins/bash/editor/usages/BashFindUsagesProvider", "getType" }));  if (element instanceof com.ansorgit.plugins.bash.lang.psi.api.function.BashFunctionDef || element instanceof com.ansorgit.plugins.bash.lang.psi.api.BashFunctionDefName) {
      if ("function" == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/usages/BashFindUsagesProvider", "getType" }));  return "function";
    } 
    
    if (element instanceof BashCommand) {
      if (((BashCommand)element).isFunctionCall()) {
        if ("function" == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/usages/BashFindUsagesProvider", "getType" }));  return "function";
      } 
      
      if (((BashCommand)element).isBashScriptCall()) {
        if ("bash script call" == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/usages/BashFindUsagesProvider", "getType" }));  return "bash script call";
      } 
      
      if ("command" == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/usages/BashFindUsagesProvider", "getType" }));  return "command";
    } 
    
    if (element instanceof com.ansorgit.plugins.bash.lang.psi.api.vars.BashVar) {
      if ("variable" == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/usages/BashFindUsagesProvider", "getType" }));  return "variable";
    } 
    
    if (element instanceof com.ansorgit.plugins.bash.lang.psi.api.heredoc.BashHereDocMarker) {
      if ("heredoc" == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/usages/BashFindUsagesProvider", "getType" }));  return "heredoc";
    } 
    
    if (element instanceof com.ansorgit.plugins.bash.lang.psi.api.BashFile) {
      if ("Bash file" == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/usages/BashFindUsagesProvider", "getType" }));  return "Bash file";
    } 
    
    if ("unknown type" == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/usages/BashFindUsagesProvider", "getType" }));  return "unknown type";
  }
  
  @NotNull
  public String getDescriptiveName(@NotNull PsiElement element) {
    if (element == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "element", "com/ansorgit/plugins/bash/editor/usages/BashFindUsagesProvider", "getDescriptiveName" }));  if (element instanceof BashCommand) {
      if (StringUtils.stripToEmpty(((BashCommand)element).getReferencedCommandName()) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/usages/BashFindUsagesProvider", "getDescriptiveName" }));  return StringUtils.stripToEmpty(((BashCommand)element).getReferencedCommandName());
    } 
    
    if (element instanceof PsiNamedElement) {
      if (StringUtils.stripToEmpty(((PsiNamedElement)element).getName()) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/usages/BashFindUsagesProvider", "getDescriptiveName" }));  return StringUtils.stripToEmpty(((PsiNamedElement)element).getName());
    } 
    
    if ("" == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/usages/BashFindUsagesProvider", "getDescriptiveName" }));  return "";
  }
  
  @NotNull
  public String getNodeText(@NotNull PsiElement element, boolean useFullName) {
    if (element == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "element", "com/ansorgit/plugins/bash/editor/usages/BashFindUsagesProvider", "getNodeText" }));  if (getDescriptiveName(element) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/usages/BashFindUsagesProvider", "getNodeText" }));  return getDescriptiveName(element);
  }
  
  private static final class BashWordsScanner extends DefaultWordsScanner {
    private static final TokenSet literals = TokenSet.create(new IElementType[] { BashElementTypes.STRING_ELEMENT, BashTokenTypes.STRING2, BashTokenTypes.INTEGER_LITERAL, BashTokenTypes.WORD, BashTokenTypes.STRING_CONTENT });
    private static final TokenSet identifiers = TokenSet.create(new IElementType[] { BashTokenTypes.VARIABLE });
    
    BashWordsScanner() {
      super((Lexer)new BashLexer(), identifiers, BashTokenTypes.commentTokens, literals);
      setMayHaveFileRefsInLiterals(true);
    }

    
    public int getVersion() {
      return 82 + super.getVersion();
    }
  }
}
