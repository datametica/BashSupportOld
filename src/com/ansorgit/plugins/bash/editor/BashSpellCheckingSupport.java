package com.ansorgit.plugins.bash.editor;

import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.ansorgit.plugins.bash.lang.psi.api.BashString;
import com.ansorgit.plugins.bash.lang.psi.api.heredoc.BashHereDoc;
import com.ansorgit.plugins.bash.lang.psi.api.word.BashWord;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.spellchecker.inspections.Splitter;
import com.intellij.spellchecker.inspections.TextSplitter;
import com.intellij.spellchecker.tokenizer.SpellcheckingStrategy;
import com.intellij.spellchecker.tokenizer.TokenConsumer;
import com.intellij.spellchecker.tokenizer.Tokenizer;
import org.jetbrains.annotations.NotNull;




















public class BashSpellCheckingSupport
  extends SpellcheckingStrategy
{
  @NotNull
  public Tokenizer getTokenizer(PsiElement psiElement) {
    if (psiElement instanceof BashString) {
      if (new BashStringTokenizer() == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/BashSpellCheckingSupport", "getTokenizer" }));  return new BashStringTokenizer();
    } 
    
    if (psiElement instanceof BashWord) {
      if (((BashWord)psiElement).isWrapped()) {
        if (new BashWordTokenizer() == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/BashSpellCheckingSupport", "getTokenizer" }));  return new BashWordTokenizer();
      } 
    } else if (psiElement instanceof BashHereDoc) {
      if (new BashHeredocTokenizer() == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/BashSpellCheckingSupport", "getTokenizer" }));  return new BashHeredocTokenizer();
    } 
    
    if (super.getTokenizer(psiElement) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/BashSpellCheckingSupport", "getTokenizer" }));  return super.getTokenizer(psiElement);
  }

  
  private static class BashStringTokenizer
    extends Tokenizer<BashString>
  {
    private BashStringTokenizer() {}
    
    public void tokenize(@NotNull BashString element, TokenConsumer consumer) {
      if (element == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "element", "com/ansorgit/plugins/bash/editor/BashSpellCheckingSupport$BashStringTokenizer", "tokenize" }));  ASTNode[] contentNodes = element.getNode().getChildren(TokenSet.create(new IElementType[] { BashTokenTypes.STRING_CONTENT }));
      for (ASTNode node : contentNodes) {
        consumer.consumeToken(node.getPsi(), false, (Splitter)TextSplitter.getInstance());
      }
    }
  }
  
  private static class BashWordTokenizer
    extends Tokenizer<BashWord>
  {
    private BashWordTokenizer() {}
    
    public void tokenize(@NotNull BashWord element, TokenConsumer tokenConsumer) {
      if (element == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "element", "com/ansorgit/plugins/bash/editor/BashSpellCheckingSupport$BashWordTokenizer", "tokenize" }));  tokenConsumer.consumeToken((PsiElement)element, element.getText(), false, 0, element.getTextContentRange(), (Splitter)TextSplitter.getInstance());
    }
  }

  
  private static class BashHeredocTokenizer
    extends Tokenizer<BashHereDoc>
  {
    private BashHeredocTokenizer() {}
    
    public void tokenize(@NotNull BashHereDoc element, TokenConsumer consumer) {
      if (element == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "element", "com/ansorgit/plugins/bash/editor/BashSpellCheckingSupport$BashHeredocTokenizer", "tokenize" }));  ASTNode[] contentNodes = element.getNode().getChildren(TokenSet.create(new IElementType[] { BashTokenTypes.HEREDOC_CONTENT }));
      for (ASTNode node : contentNodes)
        consumer.consumeToken(node.getPsi(), false, (Splitter)TextSplitter.getInstance()); 
    }
  }
}
