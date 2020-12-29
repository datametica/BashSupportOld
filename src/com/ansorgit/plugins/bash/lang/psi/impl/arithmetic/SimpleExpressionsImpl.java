package com.ansorgit.plugins.bash.lang.psi.impl.arithmetic;

import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.ansorgit.plugins.bash.lang.parser.BashElementTypes;
import com.ansorgit.plugins.bash.lang.psi.api.arithmetic.ArithmeticExpression;
import com.ansorgit.plugins.bash.lang.psi.api.arithmetic.SimpleExpression;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.DebugUtil;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiUtilCore;
import java.util.List;
import org.jetbrains.annotations.Nullable;




















public class SimpleExpressionsImpl
  extends AbstractExpression
  implements SimpleExpression
{
  private static final char[] literalChars = new char[64]; static {
    int index = 0;
    
    char c;
    for (c = '0'; c <= '9'; c = (char)(c + 1)) {
      literalChars[index++] = c;
    }

    
    for (c = 'a'; c <= 'z'; c = (char)(c + 1)) {
      literalChars[index++] = c;
    }

    
    for (c = 'A'; c <= 'Z'; c = (char)(c + 1)) {
      literalChars[index++] = c;
    }

    
    literalChars[index++] = '@';

    
    literalChars[index] = '_';
  }
  
  private final Object stateLock = new Object();
  private volatile SimpleExpression.LiteralType literalType;
  private volatile Boolean isStatic = null;
  
  public SimpleExpressionsImpl(ASTNode astNode) {
    super(astNode, "ArithSimpleExpr", ArithmeticExpression.Type.NoOperands);
  }
  
  public SimpleExpression.LiteralType literalType() {
    if (this.literalType == null)
    {
      synchronized (this.stateLock) {
        if (this.literalType == null) {
          SimpleExpression.LiteralType newType = SimpleExpression.LiteralType.Other;
          
          PsiElement child = getFirstChild();
          if (child != null && BashTokenTypes.arithmeticAdditionOps.contains(PsiUtilCore.getElementType(child)))
          {
            child = child.getNextSibling();
          }
          
          if (child != null) {
            IElementType elementType = PsiUtilCore.getElementType(child);
            
            PsiElement second = child.getNextSibling();
            IElementType typeSecond = (second != null) ? PsiUtilCore.getElementType(second) : null;
            
            if (elementType == BashTokenTypes.ARITH_HEX_NUMBER) {
              newType = SimpleExpression.LiteralType.HexLiteral;
            } else if (elementType == BashTokenTypes.ARITH_OCTAL_NUMBER) {
              newType = SimpleExpression.LiteralType.OctalLiteral;
            } else if (elementType == BashTokenTypes.ARITH_NUMBER) {
              if (typeSecond == BashTokenTypes.ARITH_BASE_CHAR) {
                newType = SimpleExpression.LiteralType.BaseLiteral;
              } else {
                newType = SimpleExpression.LiteralType.DecimalLiteral;
              } 
            } 
          } 
          
          this.literalType = newType;
        } 
      } 
    }
    
    return this.literalType;
  }

  
  public boolean isStatic() {
    if (this.isStatic == null)
    {
      synchronized (this.stateLock) {
        if (this.isStatic == null) {


          
          ASTNode[] children = getNode().getChildren(null);
          boolean newIsStatic = false;
          
          if (children.length > 0) {
            IElementType first = BashPsiUtils.getDeepestEquivalent(children[0]).getElementType();
            
            if (SimpleExpression.LiteralType.BaseLiteral.equals(literalType())) {
              newIsStatic = (children.length == 3);
              if (newIsStatic) {
                IElementType secondType = BashPsiUtils.getDeepestEquivalent(children[2]).getElementType();


                
                newIsStatic = (secondType == BashTokenTypes.WORD || secondType == BashElementTypes.PARSED_WORD_ELEMENT || BashTokenTypes.arithLiterals.contains(secondType));
              } 
            } else if (children.length == 2 && BashTokenTypes.arithmeticAdditionOps.contains(first)) {
              List<ArithmeticExpression> subexpressions = subexpressions();
              newIsStatic = (subexpressions.size() == 1 && ((ArithmeticExpression)subexpressions.get(0)).isStatic());
            } else if (children.length == 1) {
              newIsStatic = BashTokenTypes.arithLiterals.contains(first);
            } 
          } 
          
          this.isStatic = Boolean.valueOf(newIsStatic);
        } 
      } 
    }
    
    return this.isStatic.booleanValue();
  }

  
  public void subtreeChanged() {
    super.subtreeChanged();
    
    synchronized (this.stateLock) {
      this.isStatic = null;
      this.literalType = null;
    } 
  }

  
  @Nullable
  protected Long compute(long currentValue, IElementType operator, Long nextExpressionValue) {
    throw new IllegalStateException("SimpleExpressionImpl: Unsupported for " + getText());
  }

  
  public long computeNumericValue() throws InvalidExpressionValue {
    if (isStatic()) {
      ASTNode[] children = getNode().getChildren(null);

      
      if (literalType() == SimpleExpression.LiteralType.BaseLiteral) {
        if (children.length != 3) {
          throw new IllegalStateException("unexpected number of children for a numeric valid with base");
        }
        
        String baseText = children[0].getText();
        String numericText = children[2].getText();
        try {
          return baseLiteralValue(Long.valueOf(baseText).longValue(), numericText);
        } catch (NumberFormatException e) {
          
          throw new InvalidExpressionValue("Invalid numeric base value: " + baseText);
        } 
      }  if (children.length == 1) {
        String asString = getText();
        
        try {
          SimpleExpression.LiteralType currentLiteralType = literalType();
          
          switch (currentLiteralType) {
            case DecimalLiteral:
              return Long.valueOf(asString).longValue();

            
            case HexLiteral:
              return Long.valueOf(asString.substring(2), 16).longValue();
            
            case OctalLiteral:
              return Long.valueOf(asString, 8).longValue();
          } 
          
          throw new IllegalStateException("Illegal state, neither decimal, hex nor base literal: " + currentLiteralType + ", " + asString + ", " + DebugUtil.psiToString(getParent(), false, true));
        }
        catch (NumberFormatException e) {
          
          return 0L;
        } 
      } 
      if (children.length == 0) {
        throw new IllegalStateException("Unexpected number of child elements: " + getText());
      }

      
      ASTNode first = children[0];
      if (children.length > 1 && !(children[1].getPsi() instanceof ArithmeticExpression)) {
        throw new IllegalStateException("invalid expression found");
      }
      
      AbstractExpression second = (AbstractExpression)children[1].getPsi();
      
      IElementType nodeType = first.getElementType();
      if (nodeType == BashTokenTypes.ARITH_MINUS) {
        return -1L * second.computeNumericValue();
      }
      
      if (nodeType == BashTokenTypes.ARITH_PLUS) {
        return second.computeNumericValue();
      }
      
      throw new IllegalStateException("Invalid state found (invalid prefix operator); " + getText());
    } 

    
    throw new InvalidExpressionValue("unsupported expression state: " + getText());
  }
  
  static long baseLiteralValue(long base, String value) throws InvalidExpressionValue {
    long result = 0L;
    
    int index = value.length() - 1;
    for (char c : value.toCharArray()) {
      long digitValue = baseLiteralValue(base, c);
      if (digitValue == -1L) {
        throw new InvalidExpressionValue("Digit " + c + " is invalid with base " + base);
      }
      
      result = (long)(result + Math.pow(base, index) * digitValue);
      index--;
    } 
    
    return result;
  }









  
  static long baseLiteralValue(long base, char value) {
    long result = -1L;
    
    for (int i = 0; i < literalChars.length; i++) {
      char c = literalChars[i];
      
      if (c == value) {
        if (base <= 36L && i >= 36 && i <= 61) {
          result = (i - 36 + 10); break;
        }  if (i <= base) {
          result = i;
        }
        
        break;
      } 
    } 
    
    return (result < base) ? result : -1L;
  }
}
