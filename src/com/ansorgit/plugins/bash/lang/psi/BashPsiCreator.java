package com.ansorgit.plugins.bash.lang.psi;
import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.ansorgit.plugins.bash.lang.psi.impl.BashBinaryDataElement;
import com.ansorgit.plugins.bash.lang.psi.impl.BashFileReferenceImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.BashFunctionDefNameImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.BashProcessSubstitutionImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.BashShebangImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.arithmetic.AssignmentChainImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.arithmetic.BitwiseAndExpressionsImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.arithmetic.BitwiseOrExpressionsImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.arithmetic.EqualityExprImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.arithmetic.LogicalOrmpl;
import com.ansorgit.plugins.bash.lang.psi.impl.arithmetic.ParenthesesExpressionsImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.arithmetic.PreIncrementExpressionsImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.arithmetic.ProductExpressionsImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.arithmetic.ShiftExpressionsImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.arithmetic.SimpleExpressionsImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.arithmetic.SumExpressionsImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.arithmetic.TernaryExpressionsImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.arithmetic.VariableOperatorImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.command.BashArithmeticCommandImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.command.BashGenericCommandImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.command.BashIncludeCommandImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.command.BashPipelineImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.command.BashSimpleCommandImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.expression.BashFiledescriptorImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.expression.BashRedirectExprImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.expression.BashRedirectListImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.function.BashFunctionDefImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.heredoc.BashHereDocEndMarkerImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.heredoc.BashHereDocImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.heredoc.BashHereDocStartMarkerImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.shell.BashCasePatternListElementImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.vars.BashAssignmentListImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.vars.BashComposedVarImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.vars.BashParameterExpansionImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.vars.BashVarDefImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.vars.BashVarImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.word.BashExpansionImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.word.BashStringImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.word.BashWordImpl;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;

public class BashPsiCreator implements BashElementTypes {
  private static final Logger log = Logger.getInstance("#BashPsiCreator");
  
  public static PsiElement createElement(ASTNode node) {
    IElementType elementType = node.getElementType();

    
    if (elementType == SHEBANG_ELEMENT) {
      return (PsiElement)new BashShebangImpl(node);
    }

    
    if (elementType == FUNCTION_DEF_COMMAND) {
      return (PsiElement)new BashFunctionDefImpl(node);
    }
    if (elementType == PIPELINE_COMMAND) {
      return (PsiElement)new BashPipelineImpl(node);
    }
    if (elementType == COMPOSED_COMMAND) {
      return (PsiElement)new BashComposedCommandImpl(node);
    }

    
    if (elementType == CASE_PATTERN_ELEMENT) {
      return (PsiElement)new BashCasePatternImpl(node);
    }
    if (elementType == CASE_PATTERN_LIST_ELEMENT) {
      return (PsiElement)new BashCasePatternListElementImpl(node);
    }
    if (elementType == REDIRECT_ELEMENT) {
      return (PsiElement)new BashRedirectExprImpl(node);
    }
    if (elementType == REDIRECT_LIST_ELEMENT) {
      return (PsiElement)new BashRedirectListImpl(node);
    }
    if (elementType == BashTokenTypes.FILEDESCRIPTOR) {
      return (PsiElement)new BashFiledescriptorImpl(node);
    }
    if (elementType == FILE_REFERENCE) {
      return (PsiElement)new BashFileReferenceImpl(node);
    }

    
    if (elementType == VAR_DEF_ELEMENT) {
      return (PsiElement)new BashVarDefImpl(node);
    }
    if (elementType == VAR_ELEMENT) {
      return (PsiElement)new BashVarImpl(node);
    }
    if (elementType == PARAM_EXPANSION_ELEMENT) {
      return (PsiElement)new BashParameterExpansionImpl(node);
    }
    if (elementType == VAR_COMPOSED_VAR_ELEMENT) {
      return (PsiElement)new BashComposedVarImpl(node);
    }
    if (elementType == VAR_ASSIGNMENT_LIST) {
      return (PsiElement)new BashAssignmentListImpl(node);
    }

    
    if (elementType == SIMPLE_COMMAND_ELEMENT) {
      return (PsiElement)new BashSimpleCommandImpl(node);
    }
    if (elementType == INCLUDE_COMMAND_ELEMENT) {
      return (PsiElement)new BashIncludeCommandImpl(node);
    }

    
    if (elementType == STRING_ELEMENT) {
      return (PsiElement)new BashStringImpl(node);
    }
    if (elementType == FUNCTION_DEF_NAME_ELEMENT) {
      return (PsiElement)new BashFunctionDefNameImpl(node);
    }
    
    if (elementType == PARSED_WORD_ELEMENT) {
      return (PsiElement)new BashWordImpl(node);
    }
    
    if (elementType == EXPANSION_ELEMENT) {
      return (PsiElement)new BashExpansionImpl(node);
    }
    
    if (elementType == ARITHMETIC_COMMAND) {
      return (PsiElement)new BashArithmeticCommandImpl(node);
    }
    
    if (elementType == GENERIC_COMMAND_ELEMENT) {
      return (PsiElement)new BashGenericCommandImpl(node);
    }
    
    if (elementType == HEREDOC_START_ELEMENT) {
      return (PsiElement)new BashHereDocStartMarkerImpl(node);
    }
    
    if (elementType == HEREDOC_CONTENT_ELEMENT) {
      return (PsiElement)new BashHereDocImpl(node);
    }
    
    if (elementType == HEREDOC_END_ELEMENT || elementType == HEREDOC_END_IGNORING_TABS_ELEMENT) {
      return (PsiElement)new BashHereDocEndMarkerImpl(node);
    }
    
    if (elementType == ARITH_ASSIGNMENT_ELEMENT) {
      return (PsiElement)new AssignmentExpressionsImpl(node);
    }
    
    if (elementType == ARITH_VARIABLE_OPERATOR_ELEMENT) {
      return (PsiElement)new VariableOperatorImpl(node);
    }
    
    if (elementType == ARITH_BIT_AND_ELEMENT) {
      return (PsiElement)new BitwiseAndExpressionsImpl(node);
    }
    
    if (elementType == ARITH_BIT_OR_ELEMENT) {
      return (PsiElement)new BitwiseOrExpressionsImpl(node);
    }
    
    if (elementType == ARITH_BIT_XOR_ELEMENT) {
      return (PsiElement)new BitwiseXorExpressionsImpl(node);
    }
    
    if (elementType == ARITH_EQUALITY_ELEMENT) {
      return (PsiElement)new EqualityExprImpl(node);
    }
    
    if (elementType == ARITH_LOGIC_AND_ELEMENT) {
      return (PsiElement)new LogicalAndImpl(node);
    }
    
    if (elementType == ARITH_LOGIC_OR_ELEMENT) {
      return (PsiElement)new LogicalOrmpl(node);
    }
    
    if (elementType == ARITH_COMPUND_COMPARISION_ELEMENT) {
      return (PsiElement)new CompoundComparisionExpressionsImpl(node);
    }
    
    if (elementType == ARITH_EXPONENT_ELEMENT) {
      return (PsiElement)new ExponentExprImpl(node);
    }
    
    if (elementType == ARITH_NEGATION_ELEMENT) {
      return (PsiElement)new NegationExpressionImpl(node);
    }
    
    if (elementType == ARITH_PARENS_ELEMENT) {
      return (PsiElement)new ParenthesesExpressionsImpl(node);
    }
    
    if (elementType == ARITH_POST_INCR_ELEMENT) {
      return (PsiElement)new PostIncrementExpressionsImpl(node);
    }
    
    if (elementType == ARITH_PRE_INC_ELEMENT) {
      return (PsiElement)new PreIncrementExpressionsImpl(node);
    }
    
    if (elementType == ARITH_MULTIPLICACTION_ELEMENT) {
      return (PsiElement)new ProductExpressionsImpl(node);
    }
    
    if (elementType == ARITH_SHIFT_ELEMENT) {
      return (PsiElement)new ShiftExpressionsImpl(node);
    }
    
    if (elementType == ARITH_SIMPLE_ELEMENT) {
      return (PsiElement)new SimpleExpressionsImpl(node);
    }
    
    if (elementType == ARITH_SUM_ELEMENT) {
      return (PsiElement)new SumExpressionsImpl(node);
    }
    
    if (elementType == ARITH_TERNERAY_ELEMENT) {
      return (PsiElement)new TernaryExpressionsImpl(node);
    }
    
    if (elementType == ARITH_ASSIGNMENT_CHAIN_ELEMENT) {
      return (PsiElement)new AssignmentChainImpl(node);
    }
    
    if (elementType == PROCESS_SUBSTITUTION_ELEMENT) {
      return (PsiElement)new BashProcessSubstitutionImpl(node);
    }
    
    if (elementType == EVAL_BLOCK) {
      return (PsiElement)new BashEvalBlock(node);
    }
    
    if (elementType == BINARY_DATA) {
      return (PsiElement)new BashBinaryDataElement(node);
    }
    
    log.warn("MISSING PSI for" + node);
    
    return (PsiElement)new ASTWrapperPsiElement(node);
  }
}
