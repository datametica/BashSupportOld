package com.ansorgit.plugins.bash.editor.highlighting;

import com.ansorgit.plugins.bash.lang.lexer.BashLexer;
import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.ansorgit.plugins.bash.lang.parser.BashElementTypes;
import com.google.common.collect.Maps;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import java.util.Map;
import org.jetbrains.annotations.NotNull;





















public class BashSyntaxHighlighter
  extends SyntaxHighlighterBase
{
  public static final TextAttributesKey KEYWORD = TextAttributesKey.createTextAttributesKey("BASH.KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);
  
  public static final TextAttributesKey LINE_COMMENT = TextAttributesKey.createTextAttributesKey("BASH.LINE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
  
  public static final TextAttributesKey SHEBANG_COMMENT = TextAttributesKey.createTextAttributesKey("BASH.SHEBANG", LINE_COMMENT);
  
  public static final TextAttributesKey PAREN = TextAttributesKey.createTextAttributesKey("BASH.PAREN", DefaultLanguageHighlighterColors.PARENTHESES);
  public static final TextAttributesKey BRACE = TextAttributesKey.createTextAttributesKey("BASH.BRACE", DefaultLanguageHighlighterColors.BRACES);
  public static final TextAttributesKey BRACKET = TextAttributesKey.createTextAttributesKey("BASH.BRACKET", DefaultLanguageHighlighterColors.BRACKETS);
  
  public static final TextAttributesKey NUMBER = TextAttributesKey.createTextAttributesKey("BASH.NUMBER", DefaultLanguageHighlighterColors.NUMBER);
  public static final TextAttributesKey REDIRECTION = TextAttributesKey.createTextAttributesKey("BASH.REDIRECTION", DefaultLanguageHighlighterColors.OPERATION_SIGN);
  public static final TextAttributesKey CONDITIONAL = TextAttributesKey.createTextAttributesKey("BASH.CONDITIONAL", DefaultLanguageHighlighterColors.KEYWORD);
  
  public static final TextAttributesKey STRING2 = TextAttributesKey.createTextAttributesKey("BASH.STRING2", DefaultLanguageHighlighterColors.STRING);
  
  public static final TextAttributesKey BACKQUOTE = TextAttributesKey.createTextAttributesKey("BASH.BACKQUOTE", DefaultLanguageHighlighterColors.KEYWORD);

  
  public static final TextAttributesKey BINARY_DATA = TextAttributesKey.createTextAttributesKey("BASH.BINARY_DATA");
  
  public static final TextAttributesKey STRING = TextAttributesKey.createTextAttributesKey("BASH.STRING", DefaultLanguageHighlighterColors.STRING);
  
  public static final TextAttributesKey HERE_DOC = TextAttributesKey.createTextAttributesKey("BASH.HERE_DOC", DefaultLanguageHighlighterColors.STRING);
  public static final TextAttributesKey HERE_DOC_START = TextAttributesKey.createTextAttributesKey("BASH.HERE_DOC_START", DefaultLanguageHighlighterColors.KEYWORD);
  public static final TextAttributesKey HERE_DOC_END = TextAttributesKey.createTextAttributesKey("BASH.HERE_DOC_END", DefaultLanguageHighlighterColors.KEYWORD);
  
  public static final TextAttributesKey EXTERNAL_COMMAND = TextAttributesKey.createTextAttributesKey("BASH.EXTERNAL_COMMAND", DefaultLanguageHighlighterColors.IDENTIFIER);
  public static final TextAttributesKey INTERNAL_COMMAND = TextAttributesKey.createTextAttributesKey("BASH.INTERNAL_COMMAND", EXTERNAL_COMMAND);
  
  public static final TextAttributesKey FUNCTION_DEF_NAME = TextAttributesKey.createTextAttributesKey("BASH.FUNCTION_DEF_NAME", DefaultLanguageHighlighterColors.FUNCTION_DECLARATION);
  public static final TextAttributesKey FUNCTION_CALL = TextAttributesKey.createTextAttributesKey("BASH.FUNCTION_CALL", DefaultLanguageHighlighterColors.FUNCTION_CALL);
  
  public static final TextAttributesKey VAR_USE = TextAttributesKey.createTextAttributesKey("BASH.VAR_USE", DefaultLanguageHighlighterColors.GLOBAL_VARIABLE);
  public static final TextAttributesKey VAR_USE_BUILTIN = TextAttributesKey.createTextAttributesKey("BASH.VAR_USE_BUILTIN", VAR_USE);
  public static final TextAttributesKey VAR_USE_COMPOSED = TextAttributesKey.createTextAttributesKey("BASH.VAR_USE_COMPOSED", VAR_USE);
  
  public static final TextAttributesKey VAR_DEF = TextAttributesKey.createTextAttributesKey("BASH.VAR_DEF", VAR_USE);
  
  @NotNull
  public Lexer getHighlightingLexer() {
    if (new BashLexer() == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/highlighting/BashSyntaxHighlighter", "getHighlightingLexer" }));  return (Lexer)new BashLexer();
  }
  
  private static final Map<IElementType, TextAttributesKey> attributes1 = Maps.newHashMap();
  
  private static final TokenSet parenthesisSet = TokenSet.create(new IElementType[] { BashTokenTypes.LEFT_PAREN, BashTokenTypes.RIGHT_PAREN });
  private static final TokenSet bracesSet = TokenSet.create(new IElementType[] { BashTokenTypes.LEFT_CURLY, BashTokenTypes.RIGHT_CURLY });
  private static final TokenSet bracketSet = TokenSet.create(new IElementType[] { BashTokenTypes.LEFT_SQUARE, BashTokenTypes.RIGHT_SQUARE });
  private static final TokenSet numberSet = TokenSet.orSet(new TokenSet[] { BashTokenTypes.arithLiterals, TokenSet.create(new IElementType[] { BashTokenTypes.INTEGER_LITERAL }) });
  private static final TokenSet lineCommentSet = TokenSet.create(new IElementType[] { BashTokenTypes.COMMENT });
  private static final TokenSet shebangSet = TokenSet.create(new IElementType[] { BashTokenTypes.SHEBANG });
  private static final TokenSet backquoteSet = TokenSet.create(new IElementType[] { BashTokenTypes.BACKQUOTE });
  private static final TokenSet stringTokens = TokenSet.create(new IElementType[] { BashTokenTypes.STRING_BEGIN, BashTokenTypes.STRING_CONTENT, BashTokenTypes.STRING_END });
  
  private static final TokenSet badCharacterSet = TokenSet.create(new IElementType[] { BashTokenTypes.BAD_CHARACTER });
  
  static {
    fillMap(attributes1, BashTokenTypes.keywords, KEYWORD);
    fillMap(attributes1, BashTokenTypes.internalCommands, INTERNAL_COMMAND);
    
    fillMap(attributes1, BINARY_DATA, new IElementType[] { BashElementTypes.BINARY_DATA });
    
    fillMap(attributes1, backquoteSet, BACKQUOTE);
    
    fillMap(attributes1, lineCommentSet, LINE_COMMENT);
    
    fillMap(attributes1, shebangSet, SHEBANG_COMMENT);
    
    fillMap(attributes1, stringTokens, STRING);
    fillMap(attributes1, STRING2, new IElementType[] { BashTokenTypes.STRING2 });
    
    fillMap(attributes1, VAR_USE, new IElementType[] { BashTokenTypes.VARIABLE });
    
    fillMap(attributes1, parenthesisSet, PAREN);
    fillMap(attributes1, bracesSet, BRACE);
    fillMap(attributes1, bracketSet, BRACKET);
    fillMap(attributes1, numberSet, NUMBER);
    
    fillMap(attributes1, BashTokenTypes.redirectionSet, REDIRECTION);
    fillMap(attributes1, BashTokenTypes.conditionalOperators, CONDITIONAL);
    
    fillMap(attributes1, badCharacterSet, HighlighterColors.BAD_CHARACTER);
  }
  
  @NotNull
  public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
    if (pack(attributes1.get(tokenType)) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/highlighting/BashSyntaxHighlighter", "getTokenHighlights" }));  return pack(attributes1.get(tokenType));
  }
}
