package com.ansorgit.plugins.bash.editor.formatting.processor;

import com.ansorgit.plugins.bash.editor.formatting.BashBlock;
import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.ansorgit.plugins.bash.lang.parser.BashElementTypes;
import com.intellij.formatting.Spacing;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;



























public abstract class BashSpacingProcessorBasic
  implements BashElementTypes, BashTokenTypes
{
  private static final Logger log = Logger.getInstance("SpacingProcessorBasic");
  private static final TokenSet commandSet = TokenSet.create(new IElementType[] { GENERIC_COMMAND_ELEMENT, (IElementType)SIMPLE_COMMAND_ELEMENT });
  private static final Spacing NO_SPACING_WITH_NEWLINE = Spacing.createSpacing(0, 0, 0, true, 1);
  private static final Spacing NO_SPACING = Spacing.createSpacing(0, 0, 0, false, 0);
  private static final Spacing COMMON_SPACING = Spacing.createSpacing(1, 1, 0, true, 100);
  private static final Spacing COMMON_SPACING_WITH_NL = Spacing.createSpacing(1, 1, 1, true, 100);
  private static final Spacing LAZY_SPACING = Spacing.createSpacing(0, 239, 0, true, 100);
  private static TokenSet subshellSet = TokenSet.create(new IElementType[] { SUBSHELL_COMMAND, ARITHMETIC_COMMAND, PARAM_EXPANSION_ELEMENT, VAR_COMPOSED_VAR_ELEMENT });
  
  public static Spacing getSpacing(BashBlock child1, BashBlock child2, CodeStyleSettings settings) {
    ASTNode leftNode = child1.getNode();
    ASTNode rightNode = child2.getNode();
    
    IElementType leftType = leftNode.getElementType();
    IElementType rightType = rightNode.getElementType();
    
    PsiElement leftPsi = leftNode.getPsi();
    PsiElement rightPsi = rightNode.getPsi();
    
    IElementType leftParentElement = (leftPsi != null && leftPsi.getParent() != null) ? leftPsi.getParent().getNode().getElementType() : null;
    IElementType rightParentElement = (rightPsi != null && rightPsi.getParent() != null) ? rightPsi.getParent().getNode().getElementType() : null;

    
    IElementType leftGrandParentElement = (leftPsi != null && leftPsi.getParent() != null && leftPsi.getParent().getParent() != null && leftPsi.getParent().getParent().getNode() != null) ? leftPsi.getParent().getParent().getNode().getElementType() : null;

    
    IElementType rightGrandParentElement = (rightPsi != null && rightPsi.getParent() != null && rightPsi.getParent().getParent() != null && rightPsi.getParent().getParent().getNode() != null) ? rightPsi.getParent().getParent().getNode().getElementType() : null;








    
    if ((leftType == WHITESPACE || rightType == WHITESPACE) && leftPsi.getParent() instanceof com.ansorgit.plugins.bash.lang.psi.eval.BashEvalBlock && rightPsi.getParent() instanceof com.ansorgit.plugins.bash.lang.psi.eval.BashEvalBlock) {
      return Spacing.getReadOnlySpacing();
    }

    
    if (leftPsi.getParent() instanceof com.ansorgit.plugins.bash.lang.psi.api.heredoc.BashHereDoc && rightPsi.getParent() instanceof com.ansorgit.plugins.bash.lang.psi.api.heredoc.BashHereDoc) {
      return Spacing.getReadOnlySpacing();
    }
    
    if (leftPsi instanceof com.ansorgit.plugins.bash.lang.psi.api.heredoc.BashHereDoc || rightPsi instanceof com.ansorgit.plugins.bash.lang.psi.api.heredoc.BashHereDoc) {
      return Spacing.getReadOnlySpacing();
    }
    
    if (rightPsi instanceof com.ansorgit.plugins.bash.lang.psi.api.heredoc.BashHereDocEndMarker) {
      return Spacing.getReadOnlySpacing();
    }
    
    if (leftType == STRING_BEGIN && rightPsi instanceof com.ansorgit.plugins.bash.lang.psi.api.heredoc.BashHereDocMarker) {
      return NO_SPACING;
    }
    
    if (leftPsi instanceof com.ansorgit.plugins.bash.lang.psi.api.heredoc.BashHereDocMarker && rightType == STRING_END) {
      return NO_SPACING;
    }
    
    if (leftType == LINE_FEED && rightType instanceof com.ansorgit.plugins.bash.lang.psi.api.heredoc.BashHereDocEndMarker) {
      return Spacing.getReadOnlySpacing();
    }

    
    if (leftNode != null && leftNode.getLastChildNode() != null && leftNode.getLastChildNode().getElementType() == HEREDOC_END_ELEMENT) {
      return Spacing.getReadOnlySpacing();
    }

    
    if (leftPsi.getParent() instanceof com.ansorgit.plugins.bash.lang.psi.api.word.BashWord && rightPsi.getParent() instanceof com.ansorgit.plugins.bash.lang.psi.api.word.BashWord) {
      return NO_SPACING;
    }

    
    if (leftType == EXPR_CONDITIONAL || rightType == _EXPR_CONDITIONAL) {
      return NO_SPACING;
    }
    if (leftType == BRACKET_KEYWORD || rightType == _BRACKET_KEYWORD) {
      return NO_SPACING;
    }

    
    if (leftType == DOLLAR && (rightType == SUBSHELL_COMMAND || rightType == ARITHMETIC_COMMAND || rightType == PARAM_EXPANSION_ELEMENT || rightType == VAR_COMPOSED_VAR_ELEMENT))
    {


      
      return NO_SPACING;
    }

    
    if (leftType == LEFT_PAREN && leftParentElement == SUBSHELL_COMMAND && leftGrandParentElement == FUNCTION_DEF_COMMAND) {
      return COMMON_SPACING_WITH_NL;
    }

    
    if (rightType == RIGHT_PAREN && rightParentElement == SUBSHELL_COMMAND && rightGrandParentElement == FUNCTION_DEF_COMMAND) {
      return COMMON_SPACING_WITH_NL;
    }

    
    if ((leftType == LEFT_PAREN || leftType == EXPR_ARITH) && subshellSet.contains(rightParentElement)) {
      return NO_SPACING;
    }
    
    if ((rightType == RIGHT_PAREN || rightType == _EXPR_ARITH) && subshellSet.contains(leftParentElement)) {
      return NO_SPACING;
    }
    
    if (leftType == DOLLAR && rightType == LEFT_CURLY && rightParentElement == VAR_COMPOSED_VAR_ELEMENT) {
      return NO_SPACING;
    }

    
    if ((leftType == LEFT_CURLY || rightType == RIGHT_CURLY) && (leftPsi
      .getParent().getNode().getElementType() == PARAM_EXPANSION_ELEMENT || leftPsi
      .getParent().getNode().getElementType() == VAR_COMPOSED_VAR_ELEMENT)) {
      return NO_SPACING;
    }
    
    if (isNodeInParameterExpansion(leftNode) && isNodeInParameterExpansion(rightNode)) {
      return NO_SPACING;
    }

    
    if ((leftType == BACKQUOTE || rightType == BACKQUOTE) && leftPsi.getParent() instanceof com.ansorgit.plugins.bash.lang.psi.api.BashBackquote) {
      return NO_SPACING;
    }

    
    if (LEFT_PAREN.equals(rightNode.getElementType()) && rightNode
      .getPsi().getParent().getNode() != null && FUNCTION_DEF_COMMAND == rightPsi
      .getParent().getNode().getElementType()) {
      return NO_SPACING;
    }

    
    if (RIGHT_PAREN.equals(rightNode.getElementType()) && FUNCTION_DEF_COMMAND == rightParentElement) {
      return NO_SPACING;
    }
    
    if (FUNCTION_DEF_COMMAND == leftType) {
      return Spacing.createSpacing(0, 0, settings.BLANK_LINES_AROUND_METHOD + 1, settings.KEEP_LINE_BREAKS, 100);
    }

    
    if (leftType == IF_COMMAND) {
      return COMMON_SPACING_WITH_NL;
    }
    
    if (leftType == THEN_KEYWORD && leftNode.getPsi().getParent() instanceof com.ansorgit.plugins.bash.lang.psi.api.shell.BashIf) {
      log.debug("Formatting if-then-else: then");
      return COMMON_SPACING_WITH_NL;
    } 
    
    if (rightType == ELIF_KEYWORD && rightNode.getPsi().getParent() instanceof com.ansorgit.plugins.bash.lang.psi.api.shell.BashIf) {
      log.debug("Formatting if-then: else");
      return COMMON_SPACING_WITH_NL;
    } 
    
    if ((leftType == ELSE_KEYWORD && leftNode.getPsi().getParent() instanceof com.ansorgit.plugins.bash.lang.psi.api.shell.BashIf) || (rightType == ELSE_KEYWORD && rightNode
      .getPsi().getParent() instanceof com.ansorgit.plugins.bash.lang.psi.api.shell.BashIf)) {
      log.debug("Formatting if-then: else");
      return COMMON_SPACING_WITH_NL;
    } 
    
    if ((leftType == FI_KEYWORD && leftNode.getPsi().getParent() instanceof com.ansorgit.plugins.bash.lang.psi.api.shell.BashIf) || (rightType == FI_KEYWORD && rightNode
      .getPsi().getParent() instanceof com.ansorgit.plugins.bash.lang.psi.api.shell.BashIf)) {
      log.debug("Formatting if-then: fi");
      return COMMON_SPACING_WITH_NL;
    } 

    
    if (leftType == ASSIGNMENT_WORD && rightType == EQ) {
      return NO_SPACING;
    }
    
    if (leftType == EQ && leftNode.getTreePrev() != null && leftNode.getTreePrev().getElementType() == ASSIGNMENT_WORD) {
      
      if (rightPsi instanceof com.ansorgit.plugins.bash.lang.psi.api.command.BashGenericCommand) {
        return COMMON_SPACING;
      }
      return NO_SPACING;
    } 
    
    if (leftType == VAR_DEF_ELEMENT && leftParentElement == VAR_COMPOSED_VAR_ELEMENT && rightNode == PARAM_EXPANSION_OP_COLON_EQ) {
      return NO_SPACING;
    }
    
    if (leftParentElement == VAR_COMPOSED_VAR_ELEMENT && rightParentElement == VAR_COMPOSED_VAR_ELEMENT) {
      return NO_SPACING;
    }

    
    if (leftType == ASSIGNMENT_WORD && rightType == ADD_EQ) {
      return NO_SPACING;
    }
    
    if (leftType == ADD_EQ && leftNode.getTreePrev() != null && leftNode.getTreePrev().getElementType() == ASSIGNMENT_WORD) {
      
      if (rightPsi instanceof com.ansorgit.plugins.bash.lang.psi.api.command.BashGenericCommand) {
        return COMMON_SPACING;
      }
      return NO_SPACING;
    } 

    
    if ((leftPsi instanceof com.ansorgit.plugins.bash.lang.psi.api.command.BashCommand || leftPsi instanceof BashBlock) && ";".equals(rightNode.getText())) {
      return NO_SPACING;
    }

    
    if (rightType == SEMI) {
      return NO_SPACING;
    }

    
    if (leftType == CASE_PATTERN_ELEMENT || rightType == CASE_PATTERN_ELEMENT) {
      return NO_SPACING;
    }

    
    if (leftType == IN_KEYWORD_REMAPPED && leftPsi.getParent() instanceof com.ansorgit.plugins.bash.lang.psi.api.shell.BashCase) {
      return COMMON_SPACING_WITH_NL;
    }

    
    if (leftType == DO_KEYWORD) {
      return COMMON_SPACING_WITH_NL;
    }

    
    if (rightType == DONE_KEYWORD) {
      return COMMON_SPACING_WITH_NL;
    }

    
    if (leftType == SHEBANG_ELEMENT) {
      return COMMON_SPACING_WITH_NL;
    }

    
    if (leftPsi.getParent() instanceof com.ansorgit.plugins.bash.lang.psi.api.expression.BashRedirectExpr && rightPsi.getParent() instanceof com.ansorgit.plugins.bash.lang.psi.api.expression.BashRedirectExpr) {
      if (rightType == BashElementTypes.PARSED_WORD_ELEMENT) {
        return COMMON_SPACING;
      }

      
      return NO_SPACING;
    } 
    
    if (leftType == LESS_THAN && rightType == LEFT_PAREN && leftParentElement == BashElementTypes.PROCESS_SUBSTITUTION_ELEMENT) {
      return NO_SPACING;
    }





    
    if (leftType == SEMI && leftNode.getTreePrev().getPsi() instanceof com.ansorgit.plugins.bash.lang.psi.api.command.BashCommand && rightPsi instanceof com.ansorgit.plugins.bash.lang.psi.api.command.BashCommand) {
      return COMMON_SPACING_WITH_NL;
    }

    
    if (isNodeInString(leftNode) && isNodeInString(rightNode)) {
      return Spacing.getReadOnlySpacing();
    }
    
    return COMMON_SPACING;
  }






  
  private static boolean isNodeInString(ASTNode node) {
    if (node.getElementType() == STRING_CONTENT || node.getElementType() == STRING_DATA) {
      return true;
    }
    
    PsiElement psiElement = node.getPsi();
    PsiElement parent = (psiElement != null) ? psiElement.getParent() : null;
    
    while (parent != null) {
      if (parent instanceof com.ansorgit.plugins.bash.lang.psi.api.BashString) {
        return true;
      }
      
      parent = parent.getParent();
    } 
    
    return false;
  }






  
  private static boolean isNodeInParameterExpansion(ASTNode node) {
    if (paramExpansionOperators.contains(node.getElementType())) {
      return true;
    }
    
    PsiElement psiElement = node.getPsi();
    PsiElement parent = (psiElement != null) ? psiElement.getParent() : null;
    
    while (parent != null) {
      if (parent instanceof com.ansorgit.plugins.bash.lang.psi.api.vars.BashParameterExpansion) {
        return true;
      }
      
      parent = parent.getParent();
    } 
    
    return false;
  }







  
  private static boolean hasAncestorNodeType(@Nullable ASTNode node, int levelsUp, @NotNull IElementType parentNodeType) {
    if (parentNodeType == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "parentNodeType", "com/ansorgit/plugins/bash/editor/formatting/processor/BashSpacingProcessorBasic", "hasAncestorNodeType" }));  if (node == null) {
      return false;
    }
    
    if (levelsUp <= 0) {
      return (node.getElementType() == parentNodeType);
    }
    
    return hasAncestorNodeType(node.getTreeParent(), levelsUp - 1, parentNodeType);
  }
}
