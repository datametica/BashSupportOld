package com.ansorgit.plugins.bash.lang.parser;

import com.ansorgit.plugins.bash.lang.BashVersion;
import com.ansorgit.plugins.bash.lang.lexer.BashLexer;
import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.ansorgit.plugins.bash.lang.psi.BashPsiCreator;
import com.ansorgit.plugins.bash.lang.psi.impl.BashFileImpl;
import com.ansorgit.plugins.bash.settings.BashProjectSettings;
import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;





















public class BashParserDefinition
  implements ParserDefinition, BashElementTypes
{
  private static final TokenSet stringLiterals = TokenSet.create(new IElementType[] { BashTokenTypes.STRING2, BashTokenTypes.INTEGER_LITERAL, BashTokenTypes.COLON, BashElementTypes.STRING_ELEMENT });
  
  @NotNull
  public Lexer createLexer(Project project) {
    if (createBashLexer(project) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/parser/BashParserDefinition", "createLexer" }));  return createBashLexer(project);
  }
  
  public static Lexer createBashLexer(Project project) {
    return (Lexer)new BashLexer(findLanguageLevel(project));
  }
  
  public PsiParser createParser(Project project) {
    return new BashParser(project, findLanguageLevel(project));
  }
  
  private static BashVersion findLanguageLevel(Project project) {
    boolean supportBash4 = BashProjectSettings.storedSettings(project).isSupportBash4();
    return supportBash4 ? BashVersion.Bash_v4 : BashVersion.Bash_v3;
  }
  
  public IFileElementType getFileNodeType() {
    return (IFileElementType)FILE;
  }
  
  @NotNull
  public TokenSet getWhitespaceTokens() {
    if (BashTokenTypes.whitespaceTokens == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/parser/BashParserDefinition", "getWhitespaceTokens" }));  return BashTokenTypes.whitespaceTokens;
  }
  
  @NotNull
  public TokenSet getCommentTokens() {
    if (BashTokenTypes.commentTokens == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/parser/BashParserDefinition", "getCommentTokens" }));  return BashTokenTypes.commentTokens;
  }
  
  @NotNull
  public TokenSet getStringLiteralElements() {
    if (stringLiterals == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/parser/BashParserDefinition", "getStringLiteralElements" }));  return stringLiterals;
  }
  
  private static final TokenSet heredocTokens = TokenSet.create(new IElementType[] { BashTokenTypes.HEREDOC_CONTENT, BashTokenTypes.HEREDOC_MARKER_START, BashTokenTypes.HEREDOC_MARKER_END, BashTokenTypes.HEREDOC_MARKER_IGNORING_TABS_END });

  
  public ParserDefinition.SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode leftAst, ASTNode rightAst) {
    IElementType left = leftAst.getElementType();
    IElementType right = rightAst.getElementType();
    
    if (left == BashTokenTypes.LINE_FEED || right == BashTokenTypes.LINE_FEED || left == BashTokenTypes.ASSIGNMENT_WORD)
    {
      
      return ParserDefinition.SpaceRequirements.MUST_NOT;
    }

    
    if (heredocTokens.contains(left) || heredocTokens.contains(right)) {
      return ParserDefinition.SpaceRequirements.MUST_NOT;
    }
    
    if (left == BashTokenTypes.LEFT_PAREN || right == BashTokenTypes.RIGHT_PAREN || left == BashTokenTypes.RIGHT_PAREN || right == BashTokenTypes.LEFT_PAREN || left == BashTokenTypes.LEFT_CURLY || right == BashTokenTypes.LEFT_CURLY || left == BashTokenTypes.RIGHT_CURLY || right == BashTokenTypes.RIGHT_CURLY || (left == BashTokenTypes.WORD && right == BashTokenTypes.PARAM_EXPANSION_OP_UNKNOWN) || left == BashTokenTypes.LEFT_SQUARE || right == BashTokenTypes.RIGHT_SQUARE || left == BashTokenTypes.RIGHT_SQUARE || right == BashTokenTypes.LEFT_SQUARE || left == BashTokenTypes.VARIABLE || right == BashTokenTypes.VARIABLE)
    {

















      
      return ParserDefinition.SpaceRequirements.MAY;
    }
    
    return ParserDefinition.SpaceRequirements.MUST;
  }
  
  @NotNull
  public PsiElement createElement(ASTNode node) {
    if (BashPsiCreator.createElement(node) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/parser/BashParserDefinition", "createElement" }));  return BashPsiCreator.createElement(node);
  }
  
  public PsiFile createFile(FileViewProvider viewProvider) {
    return (PsiFile)new BashFileImpl(viewProvider);
  }
}
