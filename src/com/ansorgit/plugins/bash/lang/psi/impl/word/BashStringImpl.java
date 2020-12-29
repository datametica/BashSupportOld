package com.ansorgit.plugins.bash.lang.psi.impl.word;

import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.ansorgit.plugins.bash.lang.parser.eval.BashSimpleTextLiteralEscaper;
import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.BashCharSequence;
import com.ansorgit.plugins.bash.lang.psi.api.BashString;
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
import org.jetbrains.annotations.NotNull;














public class BashStringImpl
  extends BashBaseElement
  implements BashString, BashCharSequence, PsiLanguageInjectionHost
{
  private final Object stateLock = new Object();
  private volatile TextRange contentRange;
  private volatile Boolean isWrapped;
  
  public BashStringImpl(ASTNode node) {
    super(node, "Bash string");
  }

  
  public void subtreeChanged() {
    super.subtreeChanged();
    
    synchronized (this.stateLock) {
      this.contentRange = null;
      this.isWrapped = null;
    } 
  }

  
  public boolean isWrapped() {
    if (this.isWrapped == null) {
      synchronized (this.stateLock) {
        if (this.isWrapped == null) {
          boolean newIsWrapped = false;
          
          if (getTextLength() >= 2) {
            ASTNode node = getNode();
            IElementType firstType = node.getFirstChildNode().getElementType();
            IElementType lastType = node.getLastChildNode().getElementType();
            
            newIsWrapped = (firstType == BashTokenTypes.STRING_BEGIN && lastType == BashTokenTypes.STRING_END);
          } 
          
          this.isWrapped = Boolean.valueOf(newIsWrapped);
        } 
      } 
    }
    
    return this.isWrapped.booleanValue();
  }

  
  public String createEquallyWrappedString(String newContent) {
    ASTNode node = getNode();
    ASTNode firstChild = node.getFirstChildNode();
    ASTNode lastChild = node.getLastChildNode();
    
    StringBuilder result = new StringBuilder(firstChild.getTextLength() + newContent.length() + lastChild.getTextLength());
    return result.append(firstChild.getText()).append(newContent).append(lastChild.getText()).toString();
  }
  
  public String getUnwrappedCharSequence() {
    return getTextContentRange().substring(getText());
  }
  
  public boolean isStatic() {
    return (getTextContentRange().getLength() == 0 || BashPsiUtils.isStaticWordExpr(getFirstChild()));
  }
  
  @NotNull
  public TextRange getTextContentRange() {
    if (this.contentRange == null)
    {
      synchronized (this.stateLock) {
        if (this.contentRange == null) {
          TextRange newContentRange; ASTNode node = getNode();
          ASTNode firstChild = node.getFirstChildNode();

          
          if (firstChild != null && firstChild.getText().equals("$\"")) {
            newContentRange = TextRange.from(2, getTextLength() - 3);
          } else {
            newContentRange = TextRange.from(1, getTextLength() - 2);
          } 
          
          this.contentRange = newContentRange;
        } 
      } 
    }
    
    if (this.contentRange == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/word/BashStringImpl", "getTextContentRange" }));  return this.contentRange;
  }

  
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "visitor", "com/ansorgit/plugins/bash/lang/psi/impl/word/BashStringImpl", "accept" }));  if (visitor instanceof BashVisitor) {
      ((BashVisitor)visitor).visitString(this);
    } else {
      visitor.visitElement((PsiElement)this);
    } 
  }

  
  public boolean isValidHost() {
    return true;
  }

  
  public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement place) {
    if (processor == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "processor", "com/ansorgit/plugins/bash/lang/psi/impl/word/BashStringImpl", "processDeclarations" }));  if (state == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "state", "com/ansorgit/plugins/bash/lang/psi/impl/word/BashStringImpl", "processDeclarations" }));  if (place == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "place", "com/ansorgit/plugins/bash/lang/psi/impl/word/BashStringImpl", "processDeclarations" }));  if (!processor.execute((PsiElement)this, state)) {
      return false;
    }
    
    boolean walkOn = (isStatic() || BashElementSharedImpl.walkDefinitionScope((PsiElement)this, processor, state, lastParent, place));




    
    return walkOn;
  }

  
  public PsiLanguageInjectionHost updateText(@NotNull String text) {
    if (text == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "text", "com/ansorgit/plugins/bash/lang/psi/impl/word/BashStringImpl", "updateText" }));  return (PsiLanguageInjectionHost)ElementManipulators.handleContentChange((PsiElement)this, text);
  }

  
  @NotNull
  public LiteralTextEscaper<? extends PsiLanguageInjectionHost> createLiteralTextEscaper() {
    if (new BashSimpleTextLiteralEscaper(this) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/word/BashStringImpl", "createLiteralTextEscaper" }));  return (LiteralTextEscaper<? extends PsiLanguageInjectionHost>)new BashSimpleTextLiteralEscaper(this);
  }
}
