package com.ansorgit.plugins.bash.lang.psi.impl.arithmetic;

import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.arithmetic.ArithmeticExpression;
import com.ansorgit.plugins.bash.lang.psi.impl.BashBaseElement;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiUtilCore;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;





















public abstract class AbstractExpression
  extends BashBaseElement
  implements ArithmeticExpression
{
  private final ArithmeticExpression.Type type;
  private final Object stateLock = new Object();
  private volatile Boolean isStatic = null;
  
  public AbstractExpression(ASTNode astNode, String name, ArithmeticExpression.Type type) {
    super(astNode, name);
    this.type = type;
  }

  
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "visitor", "com/ansorgit/plugins/bash/lang/psi/impl/arithmetic/AbstractExpression", "accept" }));  if (visitor instanceof BashVisitor) {
      ((BashVisitor)visitor).visitArithmeticExpression(this);
    } else {
      visitor.visitElement((PsiElement)this);
    } 
  }
  
  public boolean isStatic() {
    if (this.isStatic == null)
    {
      synchronized (this.stateLock) {
        if (this.isStatic == null) {
          Iterator<ArithmeticExpression> iterator = subexpressions().iterator();
          
          boolean allStatic = iterator.hasNext();
          while (allStatic && iterator.hasNext()) {
            allStatic = ((ArithmeticExpression)iterator.next()).isStatic();
          }
          
          this.isStatic = Boolean.valueOf(allStatic);
        } 
      } 
    }
    
    return this.isStatic.booleanValue();
  }

  
  public void subtreeChanged() {
    super.subtreeChanged();
    
    synchronized (this.stateLock) {
      this.isStatic = null;
    } 
  }

  
  @NotNull
  public List<ArithmeticExpression> subexpressions() {
    if (getFirstChild() == null) {
      if (Collections.emptyList() == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/arithmetic/AbstractExpression", "subexpressions" }));  return (List)Collections.emptyList();
    } 
    
    if (Arrays.asList(findChildrenByClass(ArithmeticExpression.class)) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/arithmetic/AbstractExpression", "subexpressions" }));  return Arrays.asList(findChildrenByClass(ArithmeticExpression.class));
  }

  
  @Nullable
  protected abstract Long compute(long paramLong, IElementType paramIElementType, Long paramLong1);
  
  public long computeNumericValue() throws InvalidExpressionValue {
    List<ArithmeticExpression> childExpressions = subexpressions();
    
    int childSize = childExpressions.size();
    if (childSize == 0) {
      throw new UnsupportedOperationException("unsupported, zero children are not supported");
    }
    
    ArithmeticExpression firstChild = childExpressions.get(0);
    long result = firstChild.computeNumericValue();
    
    if (this.type == ArithmeticExpression.Type.PostfixOperand || this.type == ArithmeticExpression.Type.PrefixOperand) {
      Long computed = compute(result, findOperator(), (Long)null);
      if (computed == null) {
        throw new UnsupportedOperationException("Can't calculate value for " + getText());
      }
      return computed.longValue();
    } 
    
    if (this.type == ArithmeticExpression.Type.TwoOperands) {
      int i = 1;
      while (i < childSize) {
        ArithmeticExpression c = childExpressions.get(i);
        long nextValue = c.computeNumericValue();
        
        PsiElement opElement = BashPsiUtils.findPreviousSibling((PsiElement)c, BashTokenTypes.WHITESPACE);
        if (opElement != null) {
          IElementType operator = PsiUtilCore.getElementType(opElement);
          
          Long computed = compute(result, operator, Long.valueOf(nextValue));
          if (computed == null) {
            throw new UnsupportedOperationException("Can't calculate value for " + getText());
          }
          result = computed.longValue();
        } 
        
        i++;
      } 
      
      return result;
    } 
    
    throw new UnsupportedOperationException("unsupported computation for expression " + getText());
  }
  
  public ArithmeticExpression findParentExpression() {
    PsiElement context = getParent();
    if (context instanceof ArithmeticExpression) {
      return (ArithmeticExpression)context;
    }
    
    return null;
  }





  
  public IElementType findOperator() {
    return PsiUtilCore.getElementType(findOperatorElement());
  }

  
  public PsiElement findOperatorElement() {
    List<ArithmeticExpression> childs = subexpressions();
    int childSize = childs.size();
    if (childSize == 0) {
      return null;
    }
    
    ArithmeticExpression firstChild = childs.get(0);
    
    if (this.type == ArithmeticExpression.Type.PostfixOperand) {
      return BashPsiUtils.findNextSibling((PsiElement)firstChild, BashTokenTypes.WHITESPACE);
    }
    
    if (this.type == ArithmeticExpression.Type.PrefixOperand) {
      return BashPsiUtils.findPreviousSibling((PsiElement)firstChild, BashTokenTypes.WHITESPACE);
    }
    
    if (this.type == ArithmeticExpression.Type.TwoOperands) {
      int i = 1;
      while (i < childSize) {
        PsiElement opElement = BashPsiUtils.findPreviousSibling((PsiElement)childs.get(i), BashTokenTypes.WHITESPACE);
        if (opElement != null)
        {
          return opElement;
        }
        
        i++;
      } 
    } 
    
    return null;
  }
}
