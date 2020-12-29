package com.ansorgit.plugins.bash.lang.lexer;

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;





















public interface BashTokenTypes
{
  public static final IElementType BAD_CHARACTER = TokenType.BAD_CHARACTER;

  
  public static final IElementType WHITESPACE = TokenType.WHITE_SPACE;
  public static final IElementType LINE_CONTINUATION = new BashElementType("line continuation \\");
  public static final TokenSet whitespaceTokens = TokenSet.create(new IElementType[] { WHITESPACE, LINE_CONTINUATION });
  
  public static final IElementType ARITH_NUMBER = new BashElementType("number");
  public static final IElementType WORD = new BashElementType("word");
  public static final IElementType ASSIGNMENT_WORD = new BashElementType("assignment_word");
  public static final IElementType DOLLAR = new BashElementType("$");
  
  public static final IElementType LEFT_PAREN = new BashElementType("(");
  public static final IElementType RIGHT_PAREN = new BashElementType(")");
  
  public static final IElementType LEFT_CURLY = new BashElementType("{");
  public static final IElementType RIGHT_CURLY = new BashElementType("}");
  
  public static final IElementType LEFT_SQUARE = new BashElementType("[ (left square)");
  public static final IElementType RIGHT_SQUARE = new BashElementType("] (right square)");
  public static final TokenSet bracketSet = TokenSet.create(new IElementType[] { LEFT_SQUARE, RIGHT_SQUARE });

  
  public static final IElementType COMMENT = new BashElementType("Comment");
  public static final IElementType SHEBANG = new BashElementType("Shebang");
  
  public static final TokenSet commentTokens = TokenSet.create(new IElementType[] { COMMENT });

  
  public static final IElementType CASE_KEYWORD = new BashElementType("case");
  public static final IElementType DO_KEYWORD = new BashElementType("do");
  public static final IElementType DONE_KEYWORD = new BashElementType("done");
  public static final IElementType ELIF_KEYWORD = new BashElementType("elif");
  public static final IElementType ELSE_KEYWORD = new BashElementType("else");
  public static final IElementType ESAC_KEYWORD = new BashElementType("esac");
  public static final IElementType FI_KEYWORD = new BashElementType("fi");
  public static final IElementType FOR_KEYWORD = new BashElementType("for");
  public static final IElementType FUNCTION_KEYWORD = new BashElementType("function");
  public static final IElementType IF_KEYWORD = new BashElementType("if");
  public static final IElementType IN_KEYWORD_REMAPPED = new BashElementType("in");
  public static final IElementType SELECT_KEYWORD = new BashElementType("select");
  public static final IElementType THEN_KEYWORD = new BashElementType("then");
  public static final IElementType UNTIL_KEYWORD = new BashElementType("until");
  public static final IElementType WHILE_KEYWORD = new BashElementType("while");
  public static final IElementType TIME_KEYWORD = new BashElementType("time");
  public static final IElementType TRAP_KEYWORD = new BashElementType("trap");
  public static final IElementType LET_KEYWORD = new BashElementType("let");
  public static final IElementType BRACKET_KEYWORD = new BashElementType("[[ (left bracket)");
  public static final IElementType _BRACKET_KEYWORD = new BashElementType("]] (right bracket)");

  
  public static final IElementType CASE_END = new BashElementType(";;");

  
  public static final IElementType EXPR_ARITH = new BashElementType("((");
  public static final IElementType _EXPR_ARITH = new BashElementType("))");
  public static final IElementType EXPR_ARITH_SQUARE = new BashElementType("[ for arithmetic");
  public static final IElementType _EXPR_ARITH_SQUARE = new BashElementType("] for arithmetic");

  
  public static final IElementType EXPR_CONDITIONAL = new BashElementType("[ (left conditional)");
  public static final IElementType _EXPR_CONDITIONAL = new BashElementType(" ] (right conditional)");
  
  public static final TokenSet keywords = TokenSet.create(new IElementType[] { CASE_KEYWORD, DO_KEYWORD, DONE_KEYWORD, ELIF_KEYWORD, ELSE_KEYWORD, ESAC_KEYWORD, FI_KEYWORD, FOR_KEYWORD, FUNCTION_KEYWORD, IF_KEYWORD, IN_KEYWORD_REMAPPED, SELECT_KEYWORD, THEN_KEYWORD, UNTIL_KEYWORD, WHILE_KEYWORD, TIME_KEYWORD, BRACKET_KEYWORD, _BRACKET_KEYWORD, CASE_END, DOLLAR, EXPR_ARITH, _EXPR_ARITH, EXPR_CONDITIONAL, _EXPR_CONDITIONAL });





  
  public static final TokenSet internalCommands = TokenSet.create(new IElementType[] { TRAP_KEYWORD, LET_KEYWORD });


  
  public static final TokenSet identifierKeywords = TokenSet.create(new IElementType[] { CASE_KEYWORD, DO_KEYWORD, DONE_KEYWORD, ELIF_KEYWORD, ELSE_KEYWORD, ESAC_KEYWORD, FI_KEYWORD, FOR_KEYWORD, FUNCTION_KEYWORD, IF_KEYWORD, IN_KEYWORD_REMAPPED, SELECT_KEYWORD, THEN_KEYWORD, UNTIL_KEYWORD, WHILE_KEYWORD, TIME_KEYWORD });




  
  public static final IElementType BACKSLASH = new BashElementType("\\");
  public static final IElementType AMP = new BashElementType("&");
  public static final IElementType AT = new BashElementType("@");
  public static final IElementType COLON = new BashElementType(":");
  public static final IElementType COMMA = new BashElementType(",");
  public static final IElementType EQ = new BashElementType("=");
  public static final IElementType ADD_EQ = new BashElementType("+=");
  public static final IElementType SEMI = new BashElementType(";");
  public static final IElementType SHIFT_RIGHT = new BashElementType(">>");
  public static final IElementType LESS_THAN = new BashElementType("<");
  public static final IElementType GREATER_THAN = new BashElementType(">");
  
  public static final IElementType PIPE = new BashElementType("|");
  public static final IElementType PIPE_AMP = new BashElementType("|&");
  public static final IElementType AND_AND = new BashElementType("&&");
  public static final IElementType OR_OR = new BashElementType("||");
  
  public static final IElementType LINE_FEED = new BashElementType("linefeed");
  
  public static final TokenSet pipeTokens = TokenSet.create(new IElementType[] { PIPE, PIPE_AMP });

  
  public static final IElementType ARITH_PLUS_PLUS = new BashElementType("++");
  public static final IElementType ARITH_PLUS = new BashElementType("+");

  
  public static final IElementType ARITH_MINUS_MINUS = new BashElementType("--");
  public static final IElementType ARITH_MINUS = new BashElementType("-");
  
  public static final TokenSet arithmeticPostOps = TokenSet.create(new IElementType[] { ARITH_PLUS_PLUS, ARITH_MINUS_MINUS });
  public static final TokenSet arithmeticPreOps = TokenSet.create(new IElementType[] { ARITH_PLUS_PLUS, ARITH_MINUS_MINUS });
  public static final TokenSet arithmeticAdditionOps = TokenSet.create(new IElementType[] { ARITH_PLUS, ARITH_MINUS });

  
  public static final IElementType ARITH_EXPONENT = new BashElementType("**");
  public static final IElementType ARITH_MULT = new BashElementType("*");
  public static final IElementType ARITH_DIV = new BashElementType("/");
  public static final IElementType ARITH_MOD = new BashElementType("%");
  public static final IElementType ARITH_SHIFT_LEFT = new BashElementType("<<");
  public static final IElementType ARITH_SHIFT_RIGHT = new BashElementType(">>");
  public static final IElementType ARITH_NEGATE = new BashElementType("negation !");
  public static final IElementType ARITH_BITWISE_NEGATE = new BashElementType("bitwise negation ~");
  
  public static final TokenSet arithmeticShiftOps = TokenSet.create(new IElementType[] { ARITH_SHIFT_LEFT, ARITH_SHIFT_RIGHT });
  
  public static final TokenSet arithmeticNegationOps = TokenSet.create(new IElementType[] { ARITH_NEGATE, ARITH_BITWISE_NEGATE });
  
  public static final TokenSet arithmeticProduct = TokenSet.create(new IElementType[] { ARITH_MULT, ARITH_DIV, ARITH_MOD });

  
  public static final IElementType ARITH_LE = new BashElementType("<=");
  public static final IElementType ARITH_GE = new BashElementType(">=");
  public static final IElementType ARITH_GT = new BashElementType("arith >");
  public static final IElementType ARITH_LT = new BashElementType("arith <");
  
  public static final TokenSet arithmeticCmpOp = TokenSet.create(new IElementType[] { ARITH_LE, ARITH_GE, ARITH_LT, ARITH_GT });
  
  public static final IElementType ARITH_EQ = new BashElementType("arith ==");
  public static final IElementType ARITH_NE = new BashElementType("!=");
  public static final TokenSet arithmeticEqualityOps = TokenSet.create(new IElementType[] { ARITH_NE, ARITH_EQ });

  
  public static final IElementType ARITH_QMARK = new BashElementType("?");
  public static final IElementType ARITH_COLON = new BashElementType(":");
  public static final IElementType ARITH_BITWISE_XOR = new BashElementType("^");
  public static final IElementType ARITH_BITWISE_AND = new BashElementType("&");


  
  public static final IElementType ARITH_ASS_MUL = new BashElementType("*= arithmetic");
  public static final IElementType ARITH_ASS_DIV = new BashElementType("/= arithmetic");
  public static final IElementType ARITH_ASS_MOD = new BashElementType("%= arithmetic");
  public static final IElementType ARITH_ASS_PLUS = new BashElementType("+= arithmetic");
  public static final IElementType ARITH_ASS_MINUS = new BashElementType("-= arithmetic");
  public static final IElementType ARITH_ASS_SHIFT_RIGHT = new BashElementType(">>= arithmetic");
  public static final IElementType ARITH_ASS_SHIFT_LEFT = new BashElementType("<<= arithmetic");
  public static final IElementType ARITH_ASS_BIT_AND = new BashElementType("&= arithmetic");
  public static final IElementType ARITH_ASS_BIT_OR = new BashElementType("|= arithmetic");
  public static final IElementType ARITH_ASS_BIT_XOR = new BashElementType("^= arithmetic");

  
  public static final TokenSet arithmeticAssign = TokenSet.create(new IElementType[] { ARITH_ASS_MUL, ARITH_ASS_DIV, ARITH_ASS_MOD, ARITH_ASS_PLUS, ARITH_ASS_MINUS, ARITH_ASS_SHIFT_LEFT, ARITH_ASS_SHIFT_RIGHT, ARITH_ASS_BIT_AND, ARITH_ASS_BIT_OR, ARITH_ASS_BIT_XOR });



  
  public static final IElementType ARITH_HEX_NUMBER = new BashElementType("0x hex literal");
  public static final IElementType ARITH_OCTAL_NUMBER = new BashElementType("octal literal");
  
  public static final IElementType ARITH_BASE_CHAR = new BashElementType("arithmetic base char (#)");
  
  public static final TokenSet arithLiterals = TokenSet.create(new IElementType[] { ARITH_NUMBER, ARITH_OCTAL_NUMBER, ARITH_HEX_NUMBER });

  
  public static final IElementType COMMAND_TOKEN = new BashElementType("command");
  public static final TokenSet commands = TokenSet.create(new IElementType[] { COMMAND_TOKEN });

  
  public static final IElementType VARIABLE = new BashElementType("variable");

  
  public static final IElementType PARAM_EXPANSION_OP_UNKNOWN = new BashElementType("Parameter expansion operator (unknown)");
  public static final IElementType PARAM_EXPANSION_OP_EXCL = new BashElementType("Parameter expansion operator '!'");
  public static final IElementType PARAM_EXPANSION_OP_COLON_EQ = new BashElementType("Parameter expansion operator ':='");
  public static final IElementType PARAM_EXPANSION_OP_COLON_QMARK = new BashElementType("Parameter expansion operator ':?'");
  public static final IElementType PARAM_EXPANSION_OP_EQ = new BashElementType("Parameter expansion operator '='");
  public static final IElementType PARAM_EXPANSION_OP_COLON = new BashElementType("Parameter expansion operator ':'");
  public static final IElementType PARAM_EXPANSION_OP_COLON_MINUS = new BashElementType("Parameter expansion operator ':-'");
  public static final IElementType PARAM_EXPANSION_OP_MINUS = new BashElementType("Parameter expansion operator '-'");
  public static final IElementType PARAM_EXPANSION_OP_COLON_PLUS = new BashElementType("Parameter expansion operator ':+'");
  public static final IElementType PARAM_EXPANSION_OP_PLUS = new BashElementType("Parameter expansion operator '+'");
  public static final IElementType PARAM_EXPANSION_OP_HASH = new BashElementType("Parameter expansion operator '#'");
  public static final IElementType PARAM_EXPANSION_OP_HASH_HASH = new BashElementType("Parameter expansion operator '##'");
  public static final IElementType PARAM_EXPANSION_OP_AT = new BashElementType("Parameter expansion operator '@'");
  public static final IElementType PARAM_EXPANSION_OP_STAR = new BashElementType("Parameter expansion operator '*'");
  public static final IElementType PARAM_EXPANSION_OP_QMARK = new BashElementType("Parameter expansion operator '?'");
  public static final IElementType PARAM_EXPANSION_OP_DOT = new BashElementType("Parameter expansion operator '.'");
  public static final IElementType PARAM_EXPANSION_OP_PERCENT = new BashElementType("Parameter expansion operator '%'");
  public static final IElementType PARAM_EXPANSION_OP_SLASH = new BashElementType("Parameter expansion operator '/'");
  public static final IElementType PARAM_EXPANSION_OP_SLASH_SLASH = new BashElementType("Parameter expansion operator '//'");
  public static final IElementType PARAM_EXPANSION_OP_LOWERCASE_FIRST = new BashElementType("Parameter expansion operator ','");
  public static final IElementType PARAM_EXPANSION_OP_LOWERCASE_ALL = new BashElementType("Parameter expansion operator ',,'");
  public static final IElementType PARAM_EXPANSION_OP_UPPERCASE_FIRST = new BashElementType("Parameter expansion operator '^'");
  public static final IElementType PARAM_EXPANSION_OP_UPPERCASE_ALL = new BashElementType("Parameter expansion operator '^^'");
  public static final IElementType PARAM_EXPANSION_PATTERN = new BashElementType("Parameter expansion regex pattern");
  public static final TokenSet paramExpansionOperators = TokenSet.create(new IElementType[] { PARAM_EXPANSION_OP_UNKNOWN, PARAM_EXPANSION_OP_EXCL, PARAM_EXPANSION_OP_COLON_EQ, PARAM_EXPANSION_OP_COLON_QMARK, PARAM_EXPANSION_OP_EQ, PARAM_EXPANSION_OP_COLON, PARAM_EXPANSION_OP_COLON_MINUS, PARAM_EXPANSION_OP_MINUS, PARAM_EXPANSION_OP_PLUS, PARAM_EXPANSION_OP_COLON_PLUS, PARAM_EXPANSION_OP_HASH, PARAM_EXPANSION_OP_HASH_HASH, PARAM_EXPANSION_OP_AT, PARAM_EXPANSION_OP_STAR, PARAM_EXPANSION_OP_PERCENT, PARAM_EXPANSION_OP_QMARK, PARAM_EXPANSION_OP_DOT, PARAM_EXPANSION_OP_SLASH, PARAM_EXPANSION_OP_SLASH_SLASH, PARAM_EXPANSION_OP_LOWERCASE_ALL, PARAM_EXPANSION_OP_LOWERCASE_FIRST, PARAM_EXPANSION_OP_UPPERCASE_ALL, PARAM_EXPANSION_OP_UPPERCASE_FIRST, PARAM_EXPANSION_PATTERN });






  
  public static final TokenSet paramExpansionAssignmentOps = TokenSet.create(new IElementType[] { PARAM_EXPANSION_OP_EQ, PARAM_EXPANSION_OP_COLON_EQ });


  
  public static final IElementType STRING_BEGIN = new BashElementType("string begin");
  public static final IElementType STRING_DATA = new BashElementType("string data");
  public static final IElementType STRING_END = new BashElementType("string end");
  
  public static final IElementType STRING_CONTENT = new BashElementType("string content");
  
  public static final IElementType STRING2 = new BashElementType("unevaluated string (STRING2)");
  public static final IElementType BACKQUOTE = new BashElementType("backquote `");
  
  public static final IElementType INTEGER_LITERAL = new BashElementType("int literal");
  
  public static final TokenSet stringLiterals = TokenSet.create(new IElementType[] { WORD, STRING2, INTEGER_LITERAL, COLON });
  
  public static final IElementType HEREDOC_MARKER_TAG = new BashElementType("heredoc marker tag");
  public static final IElementType HEREDOC_MARKER_START = new BashElementType("heredoc start marker");
  public static final IElementType HEREDOC_MARKER_END = new BashElementType("heredoc end marker");
  public static final IElementType HEREDOC_MARKER_IGNORING_TABS_END = new BashElementType("heredoc end marker (ignoring tabs)");
  public static final IElementType HEREDOC_LINE = new BashElementType("heredoc line (temporary)");
  public static final IElementType HEREDOC_CONTENT = new BashElementType("here doc content");

  
  public static final IElementType COND_OP = new BashElementType("cond_op");
  public static final IElementType COND_OP_EQ_EQ = new BashElementType("cond_op ==");
  public static final IElementType COND_OP_REGEX = new BashElementType("cond_op =~");
  public static final IElementType COND_OP_NOT = new BashElementType("cond_op !");
  public static final TokenSet conditionalOperators = TokenSet.create(new IElementType[] { COND_OP, OR_OR, AND_AND, COND_OP_EQ_EQ, COND_OP_REGEX });

  
  public static final IElementType REDIRECT_HERE_STRING = new BashElementType("<<<");
  public static final IElementType REDIRECT_LESS_AMP = new BashElementType("<&");
  public static final IElementType REDIRECT_GREATER_AMP = new BashElementType(">&");
  public static final IElementType REDIRECT_LESS_GREATER = new BashElementType("<>");
  public static final IElementType REDIRECT_GREATER_BAR = new BashElementType(">|");
  public static final IElementType FILEDESCRIPTOR = new BashElementType("&[0-9] filedescriptor");

  
  public static final IElementType REDIRECT_AMP_GREATER_GREATER = new BashElementType("&>>");
  public static final IElementType REDIRECT_AMP_GREATER = new BashElementType("&>");

  
  public static final TokenSet redirectionSet = TokenSet.create(new IElementType[] { GREATER_THAN, LESS_THAN, SHIFT_RIGHT, REDIRECT_HERE_STRING, REDIRECT_LESS_GREATER, REDIRECT_GREATER_BAR, REDIRECT_GREATER_AMP, REDIRECT_AMP_GREATER, REDIRECT_LESS_AMP, REDIRECT_AMP_GREATER_GREATER, HEREDOC_MARKER_TAG });




  
  public static final TokenSet EQ_SET = TokenSet.create(new IElementType[] { EQ });
}
