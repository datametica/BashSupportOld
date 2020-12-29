package com.ansorgit.plugins.bash.editor.codecompletion;

import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionInitializationContext;
import com.intellij.codeInsight.completion.OffsetMap;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;




















public class BashCompletionContributor
  extends CompletionContributor
{
  private static final TokenSet endTokens = TokenSet.create(new IElementType[] { BashTokenTypes.STRING_END, BashTokenTypes.RIGHT_CURLY });
  private static final TokenSet wordTokens = TokenSet.create(new IElementType[] { BashTokenTypes.WORD });
  
  public BashCompletionContributor() {
    BashPathCompletionService completionService = BashPathCompletionService.getInstance();
    
    (new VariableNameCompletionProvider()).addTo(this);
    (new CommandNameCompletionProvider(completionService)).addTo(this);
    (new AbsolutePathCompletionProvider()).addTo(this);
    (new ShebangPathCompletionProvider()).addTo(this);
    (new DynamicPathCompletionProvider()).addTo(this);
    (new BashKeywordCompletionProvider()).addTo(this);
  }

  
  public void beforeCompletion(@NotNull CompletionInitializationContext context) {
    if (context == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "context", "com/ansorgit/plugins/bash/editor/codecompletion/BashCompletionContributor", "beforeCompletion" }));  context.setDummyIdentifier("ZZZ");
  }

  
  public void duringCompletion(@NotNull CompletionInitializationContext context) {
    if (context == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "context", "com/ansorgit/plugins/bash/editor/codecompletion/BashCompletionContributor", "duringCompletion" }));  fixComposedWordEndOffset(context);
  }





  
  protected void fixComposedWordEndOffset(CompletionInitializationContext context) {
    PsiElement element = context.getFile().findElementAt(context.getStartOffset());
    if (element == null) {
      return;
    }
    
    OffsetMap offsetMap = context.getOffsetMap();


    
    if (endTokens.contains(element.getNode().getElementType())) {
      offsetMap.addOffset(CompletionInitializationContext.START_OFFSET, element.getTextOffset());
      offsetMap.addOffset(CompletionInitializationContext.IDENTIFIER_END_OFFSET, element.getTextOffset());
      
      return;
    } 
    if (wordTokens.contains(element.getNode().getElementType()) && 
      fixReplacementOffsetInString(element, offsetMap)) {
      return;
    }


    
    if (!(element instanceof com.ansorgit.plugins.bash.lang.psi.api.word.BashWord)) {
      element = element.getParent();
    }
    
    if (element instanceof com.ansorgit.plugins.bash.lang.psi.api.word.BashWord) {
      offsetMap.addOffset(CompletionInitializationContext.START_OFFSET, element.getTextOffset());




      
      if (!fixReplacementOffsetInString(element, offsetMap)) {
        offsetMap.addOffset(CompletionInitializationContext.IDENTIFIER_END_OFFSET, element.getTextRange().getEndOffset());
      }
    } 
  }







  
  private boolean fixReplacementOffsetInString(PsiElement element, OffsetMap offsetMap) {
    int endCharIndex = StringUtils.indexOfAny(element.getText(), new char[] { '\n', ' ' });
    if (endCharIndex > 0) {
      offsetMap.addOffset(CompletionInitializationContext.IDENTIFIER_END_OFFSET, element.getTextOffset() + endCharIndex);
      return true;
    } 
    
    return false;
  }
}
