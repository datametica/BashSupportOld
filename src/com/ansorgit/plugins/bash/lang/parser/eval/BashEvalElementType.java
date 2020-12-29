package com.ansorgit.plugins.bash.lang.parser.eval;

import com.ansorgit.plugins.bash.file.BashFileType;
import com.ansorgit.plugins.bash.settings.BashProjectSettings;
import com.intellij.lang.ASTNode;
import com.intellij.lang.LanguageParserDefinitions;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiBuilder;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.ILazyParseableElementType;
import org.jetbrains.annotations.NotNull;













public class BashEvalElementType
  extends ILazyParseableElementType
{
  public BashEvalElementType() {
    super("eval block", BashFileType.BASH_LANGUAGE);
  }
  
  protected ASTNode doParseContents(@NotNull ASTNode chameleon, @NotNull PsiElement psi) {
    TextPreprocessor textProcessor;
    if (chameleon == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "chameleon", "com/ansorgit/plugins/bash/lang/parser/eval/BashEvalElementType", "doParseContents" }));  if (psi == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "psi", "com/ansorgit/plugins/bash/lang/parser/eval/BashEvalElementType", "doParseContents" }));  Project project = psi.getProject();
    boolean supportEvalEscapes = BashProjectSettings.storedSettings(project).isEvalEscapesEnabled();
    
    String originalText = chameleon.getChars().toString();
    ParserDefinition def = (ParserDefinition)LanguageParserDefinitions.INSTANCE.forLanguage(BashFileType.BASH_LANGUAGE);
    
    boolean isDoubleQuoted = (originalText.startsWith("\"") && originalText.endsWith("\""));
    boolean isSingleQuoted = (originalText.startsWith("'") && originalText.endsWith("'"));
    boolean isEscapingSingleQuoted = (originalText.startsWith("$'") && originalText.endsWith("'"));
    boolean isUnquoted = (!isDoubleQuoted && !isSingleQuoted && !isEscapingSingleQuoted);
    
    String prefix = isUnquoted ? "" : originalText.subSequence(0, isEscapingSingleQuoted ? 2 : 1).toString();
    String content = isUnquoted ? originalText : originalText.subSequence(isEscapingSingleQuoted ? 2 : 1, originalText.length() - 1).toString();
    String suffix = isUnquoted ? "" : originalText.subSequence(originalText.length() - 1, originalText.length()).toString();

    
    if (supportEvalEscapes) {
      if (isEscapingSingleQuoted) {
        textProcessor = new BashEnhancedTextPreprocessor(TextRange.from(prefix.length(), content.length()));
      } else if (isSingleQuoted) {
        
        textProcessor = new BashIdentityTextPreprocessor(TextRange.from(prefix.length(), content.length()));
      } else {
        
        textProcessor = new BashSimpleTextPreprocessor(TextRange.from(prefix.length(), content.length()));
      } 
    } else {
      textProcessor = new BashIdentityTextPreprocessor(TextRange.from(prefix.length(), content.length()));
    } 
    
    StringBuilder unescapedContent = new StringBuilder(content.length());
    textProcessor.decode(content, unescapedContent);


    
    Lexer lexer = isUnquoted ? def.createLexer(project) : (Lexer)new PrefixSuffixAddingLexer(def.createLexer(project), prefix, TokenType.WHITE_SPACE, suffix, TokenType.WHITE_SPACE);
    
    UnescapingPsiBuilder unescapingPsiBuilder = new UnescapingPsiBuilder(project, def, lexer, chameleon, originalText, prefix + unescapedContent + suffix, textProcessor);






    
    return def.createParser(project).parse((IElementType)this, (PsiBuilder)unescapingPsiBuilder).getFirstChildNode();
  }
}
