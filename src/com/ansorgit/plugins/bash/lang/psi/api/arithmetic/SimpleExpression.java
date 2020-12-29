package com.ansorgit.plugins.bash.lang.psi.api.arithmetic;


















public interface SimpleExpression
  extends ArithmeticExpression
{
  LiteralType literalType();
  
  public enum LiteralType
  {
    HexLiteral, BaseLiteral, OctalLiteral, DecimalLiteral, Other;
  }
}
