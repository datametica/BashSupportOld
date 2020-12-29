package com.ansorgit.plugins.bash.lang.psi.impl.word;

import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.ansorgit.plugins.bash.lang.parser.BashElementTypes;
import com.ansorgit.plugins.bash.lang.parser.eval.BashEnhancedLiteralTextEscaper;
import com.ansorgit.plugins.bash.lang.parser.eval.BashIdentityStringLiteralEscaper;
import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.word.BashWord;
import com.ansorgit.plugins.bash.lang.psi.impl.BashBaseElement;
import com.ansorgit.plugins.bash.lang.psi.impl.BashElementSharedImpl;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.LiteralTextEscaper;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;







public class BashWordImpl
  extends BashBaseElement
  implements BashWord, PsiLanguageInjectionHost
{
  private static final TokenSet nonWrappableChilds = TokenSet.create(new IElementType[] { BashElementTypes.STRING_ELEMENT, BashTokenTypes.STRING2, BashTokenTypes.WORD });
  
  private final Object stateLock = new Object();
  private volatile Boolean isWrapped;
  private volatile boolean singleChildParentComputed = false;
  private volatile boolean singleChildParent;
  
  public BashWordImpl(ASTNode astNode) {
    super(astNode, "bash combined word");
  }

  
  public void subtreeChanged() {
    super.subtreeChanged();
    
    synchronized (this.stateLock) {
      this.isWrapped = null;
      this.singleChildParentComputed = false;
    } 
  }

  
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "visitor", "com/ansorgit/plugins/bash/lang/psi/impl/word/BashWordImpl", "accept" }));  if (visitor instanceof BashVisitor) {
      ((BashVisitor)visitor).visitCombinedWord(this);
    } else {
      visitor.visitElement((PsiElement)this);
    } 
  }
  
  public boolean isWrappable() {
    if (isSingleChildParent()) {
      return false;
    }
    
    ASTNode node = getNode();
    for (ASTNode child = node.getFirstChildNode(); child != null; child = child.getTreeNext()) {
      if (nonWrappableChilds.contains(child.getElementType())) {
        return false;
      }
    } 
    
    return true;
  }
  
  private boolean isSingleChildParent() {
    if (!this.singleChildParentComputed) {
      synchronized (this.stateLock) {
        if (!this.singleChildParentComputed) {
          this.singleChildParent = BashPsiUtils.isSingleChildParent((PsiElement)this);
          this.singleChildParentComputed = true;
        } 
      } 
    }
    
    return this.singleChildParent;
  }

  
  public boolean isWrapped() {
    if (this.isWrapped == null) {
      synchronized (this.stateLock) {
        if (this.isWrapped == null) {
          boolean newIsWrapped = false;
          if (getTextLength() >= 2) {
            ASTNode firstChildNode = getNode().getFirstChildNode();
            if (firstChildNode != null && firstChildNode.getTextLength() >= 2) {
              String text = firstChildNode.getText();
              
              newIsWrapped = ((text.startsWith("$'") || text.startsWith("'")) && text.endsWith("'"));
            } 
          } 
          
          this.isWrapped = Boolean.valueOf(newIsWrapped);
        } 
      } 
    }
    
    return this.isWrapped.booleanValue();
  }

  
  public String createEquallyWrappedString(String newContent) {
    if (!isWrapped()) {
      return newContent;
    }
    
    String firstText = getNode().getFirstChildNode().getText();
    if (firstText.startsWith("$'")) {
      return "$'" + newContent + "'";
    }
    return "'" + newContent + "'";
  }

  
  public String getUnwrappedCharSequence() {
    return getTextContentRange().substring(getText());
  }
  
  public boolean isStatic() {
    return (isWrapped() || BashPsiUtils.isStaticWordExpr(getFirstChild()));
  }
  
  @NotNull
  public TextRange getTextContentRange() {
    if (!isWrapped()) {
      if (TextRange.from(0, getTextLength()) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/word/BashWordImpl", "getTextContentRange" }));  return TextRange.from(0, getTextLength());
    } 
    
    ASTNode node = getNode();
    String first = node.getFirstChildNode().getText();
    String last = node.getLastChildNode().getText();
    
    int textLength = getTextLength();
    
    if (first.startsWith("$'") && last.endsWith("'")) {
      if (TextRange.from(2, textLength - 3) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/word/BashWordImpl", "getTextContentRange" }));  return TextRange.from(2, textLength - 3);
    } 
    
    if (TextRange.from(1, textLength - 2) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/word/BashWordImpl", "getTextContentRange" }));  return TextRange.from(1, textLength - 2);
  }


  
  public boolean isValidHost() {
    return isWrapped();
  }

  
  public PsiLanguageInjectionHost updateText(@NotNull String text) {
    if (text == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "text", "com/ansorgit/plugins/bash/lang/psi/impl/word/BashWordImpl", "updateText" }));  return (PsiLanguageInjectionHost)ElementManipulators.handleContentChange((PsiElement)this, text);
  }

  
  public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement place) {
    if (processor == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "processor", "com/ansorgit/plugins/bash/lang/psi/impl/word/BashWordImpl", "processDeclarations" }));  if (state == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "state", "com/ansorgit/plugins/bash/lang/psi/impl/word/BashWordImpl", "processDeclarations" }));  if (place == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "place", "com/ansorgit/plugins/bash/lang/psi/impl/word/BashWordImpl", "processDeclarations" }));  if (!processor.execute((PsiElement)this, state)) {
      return false;
    }
    
    if (isSingleChildParent() && isWrapped()) {
      return true;
    }
    
    return BashElementSharedImpl.walkDefinitionScope((PsiElement)this, processor, state, lastParent, place);
  }


  
  @NotNull
  public LiteralTextEscaper<? extends PsiLanguageInjectionHost> createLiteralTextEscaper() {
    if (getText().startsWith("$'")) {
      if (new BashEnhancedLiteralTextEscaper(this) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/word/BashWordImpl", "createLiteralTextEscaper" }));  return (LiteralTextEscaper<? extends PsiLanguageInjectionHost>)new BashEnhancedLiteralTextEscaper(this);
    } 

    
    if (new BashIdentityStringLiteralEscaper(this) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/word/BashWordImpl", "createLiteralTextEscaper" }));  return (LiteralTextEscaper<? extends PsiLanguageInjectionHost>)new BashIdentityStringLiteralEscaper(this);
  }
}
