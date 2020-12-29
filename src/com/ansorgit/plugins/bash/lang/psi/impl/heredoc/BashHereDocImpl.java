package com.ansorgit.plugins.bash.lang.psi.impl.heredoc;

import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.heredoc.BashHereDoc;
import com.ansorgit.plugins.bash.lang.psi.api.heredoc.BashHereDocEndMarker;
import com.ansorgit.plugins.bash.lang.psi.api.heredoc.BashHereDocStartMarker;
import com.ansorgit.plugins.bash.lang.psi.impl.BashBaseElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.LiteralTextEscaper;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.impl.source.tree.LeafElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
















public class BashHereDocImpl
  extends BashBaseElement
  implements BashHereDoc, PsiLanguageInjectionHost
{
  public BashHereDocImpl(ASTNode astNode) {
    super(astNode, "bash here doc");
  }

  
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "visitor", "com/ansorgit/plugins/bash/lang/psi/impl/heredoc/BashHereDocImpl", "accept" }));  if (visitor instanceof BashVisitor) {
      ((BashVisitor)visitor).visitHereDoc(this);
    } else {
      visitor.visitElement((PsiElement)this);
    } 
  }
  
  @Nullable
  private PsiElement findRedirectElement() {
    BashHereDocStartMarker start = findStartMarkerElement();
    if (start == null) {
      return null;
    }
    
    for (PsiElement sibling = start.getPrevSibling(); sibling != null; sibling = sibling.getPrevSibling()) {
      if (sibling.getNode().getElementType() == BashTokenTypes.HEREDOC_MARKER_TAG) {
        return sibling;
      }
    } 
    
    return null;
  }
  
  @Nullable
  private BashHereDocStartMarker findStartMarkerElement() {
    BashHereDocEndMarker end = findEndMarkerElement();
    if (end == null || end.getReference() == null) {
      return null;
    }
    
    PsiElement start = end.getReference().resolve();
    if (start instanceof BashHereDocStartMarker) {
      return (BashHereDocStartMarker)start;
    }
    
    return null;
  }
  
  @Nullable
  private BashHereDocEndMarker findEndMarkerElement() {
    PsiElement last = getNextSibling();
    return (last instanceof BashHereDocEndMarker) ? (BashHereDocEndMarker)last : null;
  }
  
  public boolean isEvaluatingVariables() {
    BashHereDocStartMarker start = findStartMarkerElement();
    return (start != null && start.isEvaluatingVariables());
  }
  
  public boolean isStrippingLeadingWhitespace() {
    PsiElement redirectElement = findRedirectElement();
    
    return (redirectElement != null && "<<-".equals(redirectElement.getText()));
  }

  
  public boolean isValidHost() {
    return !isEvaluatingVariables();
  }

  
  public PsiLanguageInjectionHost updateText(@NotNull String text) {
    if (text == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "text", "com/ansorgit/plugins/bash/lang/psi/impl/heredoc/BashHereDocImpl", "updateText" }));  ASTNode valueNode = getNode().getFirstChildNode();
    assert valueNode instanceof LeafElement;
    ((LeafElement)valueNode).replaceWithText(text);
    return this;
  }

  
  @NotNull
  public LiteralTextEscaper<? extends PsiLanguageInjectionHost> createLiteralTextEscaper() {
    if (new HeredocLiteralEscaper<>(this) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/heredoc/BashHereDocImpl", "createLiteralTextEscaper" }));  return (LiteralTextEscaper)new HeredocLiteralEscaper<>(this);
  }
}
