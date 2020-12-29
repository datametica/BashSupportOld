package com.ansorgit.plugins.bash.lang.parser.arithmetic;

import com.ansorgit.plugins.bash.lang.parser.BashPsiBuilder;
import com.ansorgit.plugins.bash.lang.parser.util.ParserUtil;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;




























class AbstractRepeatedExpr
  implements ArithmeticParsingFunction
{
  private final ArithmeticParsingFunction expressionParser;
  private final TokenSet operators;
  private boolean prefixOperator;
  private final IElementType partMarker;
  private int maxRepeats;
  private final String debugInfo;
  private final boolean checkWhitespace;
  
  AbstractRepeatedExpr(ArithmeticParsingFunction expressionParser, TokenSet operators, boolean prefixOperator, IElementType partMarker, int maxRepeats, String debugInfo) {
    this.expressionParser = expressionParser;
    this.operators = operators;
    this.prefixOperator = prefixOperator;
    this.partMarker = partMarker;
    this.maxRepeats = maxRepeats;
    this.debugInfo = debugInfo;
    
    this.checkWhitespace = operators.contains(WHITESPACE);
  }
  
  public boolean isValid(BashPsiBuilder builder) {
    if (this.prefixOperator && this.operators.contains(builder.getTokenType(this.checkWhitespace))) {
      return true;
    }
    
    if (this.expressionParser.isValid(builder)) {
      return true;
    }
    
    return isValidParentesisExpression(builder);
  }
  
  private boolean isValidParentesisExpression(BashPsiBuilder builder) {
    PsiBuilder.Marker marker = builder.mark();
    
    ArithmeticParsingFunction parenthesisParser = ArithmeticFactory.parenthesisParser();

    
    boolean ok = (parenthesisParser.isValid(builder) && parenthesisParser.parse(builder) && this.operators.contains(builder.getTokenType(this.checkWhitespace)));
    
    marker.rollbackTo();
    return ok;
  }
  
  public boolean parse(BashPsiBuilder builder) {
    return this.prefixOperator ? 
      parseWithPrefixOperator(builder) : 
      parseWithNonPrefixOperator(builder);
  }
  private boolean parseWithPrefixOperator(BashPsiBuilder builder) {
    boolean ok;
    PsiBuilder.Marker marker = builder.mark();
    
    int count = 0;

    
    do {
      ok = ParserUtil.conditionalRead((PsiBuilder)builder, this.operators);
      count++;
    } while (ok && (this.maxRepeats <= 0 || count < this.maxRepeats));
    
    if (this.expressionParser.isValid(builder)) {
      ok = this.expressionParser.parse(builder);
    } else {
      ok = ArithmeticFactory.parenthesisParser().parse(builder);
    } 
    
    if (ok && count > 1 && this.partMarker != null) {
      marker.done(this.partMarker);
    } else {
      marker.drop();
    } 
    
    return ok;
  }
  private boolean parseWithNonPrefixOperator(BashPsiBuilder builder) {
    boolean ok;
    PsiBuilder.Marker marker = builder.mark();
    
    int count = 0;

    
    do {
      if (this.expressionParser.isValid(builder)) {
        ok = this.expressionParser.parse(builder);
      } else {
        ok = ArithmeticFactory.parenthesisParser().parse(builder);
      } 
      
      count++;
    } while (ok && (this.maxRepeats <= 0 || count < this.maxRepeats) && ParserUtil.conditionalRead((PsiBuilder)builder, this.operators));
    
    if (ok && count > 1 && this.partMarker != null) {
      marker.done(this.partMarker);
    } else {
      marker.drop();
    } 
    
    return ok;
  }

  
  public String toString() {
    return "RepeatedExpr:" + this.debugInfo + ": " + super.toString();
  }
}
