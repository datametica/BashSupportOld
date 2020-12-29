package com.ansorgit.plugins.bash.editor.annotator;

import com.ansorgit.plugins.bash.editor.highlighting.BashSyntaxHighlighter;
import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.ansorgit.plugins.bash.lang.parser.BashElementTypes;
import com.ansorgit.plugins.bash.lang.psi.api.BashBackquote;
import com.ansorgit.plugins.bash.lang.psi.api.BashFunctionDefName;
import com.ansorgit.plugins.bash.lang.psi.api.BashString;
import com.ansorgit.plugins.bash.lang.psi.api.arithmetic.ArithmeticExpression;
import com.ansorgit.plugins.bash.lang.psi.api.arithmetic.IncrementExpression;
import com.ansorgit.plugins.bash.lang.psi.api.command.BashCommand;
import com.ansorgit.plugins.bash.lang.psi.api.expression.BashRedirectExpr;
import com.ansorgit.plugins.bash.lang.psi.api.expression.BashSubshellCommand;
import com.ansorgit.plugins.bash.lang.psi.api.heredoc.BashHereDoc;
import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVar;
import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVarDef;
import com.ansorgit.plugins.bash.lang.psi.impl.BashBinaryDataElement;
import com.ansorgit.plugins.bash.lang.psi.util.BashIdentifierUtil;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import com.intellij.lang.ASTNode;
import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiRecursiveElementVisitor;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import java.util.List;
import org.jetbrains.annotations.NotNull;





























public class BashAnnotator
  implements Annotator
{
  private static final TokenSet noWordHighlightErase = TokenSet.orSet(new TokenSet[] {
        TokenSet.create(new IElementType[] { BashTokenTypes.STRING2 }), BashTokenTypes.arithLiterals, 
        
        TokenSet.create(new IElementType[] { (IElementType)BashElementTypes.VAR_ELEMENT }) });
  
  public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder annotationHolder) {
    if (element == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "element", "com/ansorgit/plugins/bash/editor/annotator/BashAnnotator", "annotate" }));  if (annotationHolder == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "annotationHolder", "com/ansorgit/plugins/bash/editor/annotator/BashAnnotator", "annotate" }));  if (element instanceof BashHereDoc) {
      annotateHereDoc((BashHereDoc)element, annotationHolder);
    } else if (element instanceof com.ansorgit.plugins.bash.lang.psi.api.heredoc.BashHereDocStartMarker) {
      annotateHereDocStart(element, annotationHolder);
    } else if (element instanceof com.ansorgit.plugins.bash.lang.psi.api.heredoc.BashHereDocEndMarker) {
      annotateHereDocEnd(element, annotationHolder);
    } else if (element instanceof BashCommand) {
      annotateCommand((BashCommand)element, annotationHolder);
    } else if (element instanceof BashVarDef) {
      annotateVarDef((BashVarDef)element, annotationHolder);
      annotateIdentifier((BashVar)element, annotationHolder);
    } else if (element instanceof BashVar) {
      highlightVariable((BashVar)element, annotationHolder);
      annotateIdentifier((BashVar)element, annotationHolder);
    } else if (element instanceof com.ansorgit.plugins.bash.lang.psi.api.word.BashWord) {
      annotateWord(element, annotationHolder);
    } else if (element instanceof IncrementExpression) {
      annotateArithmeticIncrement((IncrementExpression)element, annotationHolder);
    } else if (element instanceof BashFunctionDefName) {
      annotateFunctionDef((BashFunctionDefName)element, annotationHolder);
    } else if (element instanceof BashRedirectExpr) {
      annotateRedirectExpression((BashRedirectExpr)element, annotationHolder);
    } else if (element instanceof BashBinaryDataElement) {
      annotateBinaryData((BashBinaryDataElement)element, annotationHolder);
    } 
    
    highlightKeywordTokens(element, annotationHolder);
  }
  
  private void highlightKeywordTokens(@NotNull PsiElement element, @NotNull AnnotationHolder annotationHolder) {
    if (element == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "element", "com/ansorgit/plugins/bash/editor/annotator/BashAnnotator", "highlightKeywordTokens" }));  if (annotationHolder == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "annotationHolder", "com/ansorgit/plugins/bash/editor/annotator/BashAnnotator", "highlightKeywordTokens" }));  ASTNode node = element.getNode();
    if (node == null) {
      return;
    }
    
    IElementType elementType = node.getElementType();
    
    boolean isKeyword = (elementType == BashTokenTypes.IN_KEYWORD_REMAPPED || (elementType == BashTokenTypes.WORD && "!".equals(element.getText())));
    
    if (isKeyword) {
      Annotation annotation = annotationHolder.createInfoAnnotation(element, null);
      annotation.setTextAttributes(BashSyntaxHighlighter.KEYWORD);
    } 
  }
  
  private void annotateBinaryData(BashBinaryDataElement element, AnnotationHolder annotationHolder) {
    Annotation annotation = annotationHolder.createInfoAnnotation((PsiElement)element, null);
    annotation.setEnforcedTextAttributes(TextAttributes.ERASE_MARKER);
    
    annotation = annotationHolder.createInfoAnnotation((PsiElement)element, null);
    annotation.setTextAttributes(BashSyntaxHighlighter.BINARY_DATA);
    annotation.setNeedsUpdateOnTyping(false);
  }
  
  protected void highlightVariable(@NotNull BashVar element, @NotNull AnnotationHolder annotationHolder) {
    if (element == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "element", "com/ansorgit/plugins/bash/editor/annotator/BashAnnotator", "highlightVariable" }));  if (annotationHolder == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "annotationHolder", "com/ansorgit/plugins/bash/editor/annotator/BashAnnotator", "highlightVariable" }));  if (element.isBuiltinVar()) {
      
      Annotation annotation = annotationHolder.createInfoAnnotation((PsiElement)element, null);
      annotation.setTextAttributes(BashSyntaxHighlighter.VAR_USE_BUILTIN);
    } else if (element.isParameterExpansion()) {
      
      Annotation annotation = annotationHolder.createInfoAnnotation((PsiElement)element, null);
      annotation.setTextAttributes(BashSyntaxHighlighter.VAR_USE_COMPOSED);
    } 
  }
  
  private void annotateRedirectExpression(BashRedirectExpr element, AnnotationHolder annotationHolder) {
    Annotation annotation = annotationHolder.createInfoAnnotation((PsiElement)element, null);
    annotation.setTextAttributes(BashSyntaxHighlighter.REDIRECTION);
  }
  
  private void annotateFunctionDef(BashFunctionDefName functionName, AnnotationHolder annotationHolder) {
    Annotation annotation = annotationHolder.createInfoAnnotation((PsiElement)functionName, null);
    annotation.setTextAttributes(BashSyntaxHighlighter.FUNCTION_DEF_NAME);
  }
  
  private void annotateArithmeticIncrement(IncrementExpression element, AnnotationHolder annotationHolder) {
    List<ArithmeticExpression> subexpressions = element.subexpressions();
    if (subexpressions.isEmpty()) {
      return;
    }
    
    ArithmeticExpression first = subexpressions.get(0);
    if (first instanceof com.ansorgit.plugins.bash.lang.psi.api.arithmetic.SimpleExpression && !(first.getFirstChild() instanceof BashVar)) {
      PsiElement operator = element.findOperatorElement();
      if (operator != null) {
        annotationHolder.createErrorAnnotation(operator, "This operator only works on a variable and not on a value.");
      }
    } 
  }

  
  private void annotateWord(PsiElement bashWord, AnnotationHolder annotationHolder) {
    PsiElement child = bashWord.getFirstChild();
    
    if (child != null);
  }










  
  private void highlightVariables(PsiElement containerElement, final AnnotationHolder holder) {
    (new PsiRecursiveElementVisitor()
      {
        public void visitElement(PsiElement element) {
          if (element instanceof BashVar) {
            Annotation infoAnnotation = holder.createInfoAnnotation(element, null);
            infoAnnotation.setTextAttributes(BashSyntaxHighlighter.VAR_USE);
          } 
          
          super.visitElement(element);
        }
      }).visitElement(containerElement);
  }
  
  private void annotateCommand(BashCommand bashCommand, AnnotationHolder annotationHolder) {
    PsiElement cmdElement = null;
    TextAttributesKey attributesKey = null;


    
    if (BashPsiUtils.isSingleChildParent((PsiElement)bashCommand, BashTokenTypes.VARIABLE)) {
      return;
    }
    
    if (BashPsiUtils.isSingleChildParent((PsiElement)bashCommand, BashString.class)) {
      return;
    }
    
    if (BashPsiUtils.isSingleChildParent((PsiElement)bashCommand, BashBackquote.class)) {
      return;
    }
    
    if (BashPsiUtils.isSingleChildParent((PsiElement)bashCommand, BashSubshellCommand.class)) {
      return;
    }
    
    if (bashCommand.isFunctionCall()) {
      cmdElement = bashCommand.commandElement();
      attributesKey = BashSyntaxHighlighter.FUNCTION_CALL;
    } else if (bashCommand.isExternalCommand()) {
      cmdElement = bashCommand.commandElement();
      attributesKey = BashSyntaxHighlighter.EXTERNAL_COMMAND;
    } else if (bashCommand.isInternalCommand()) {
      cmdElement = bashCommand.commandElement();
      attributesKey = BashSyntaxHighlighter.INTERNAL_COMMAND;
    } 
    
    if (cmdElement != null && attributesKey != null) {
      Annotation annotation = annotationHolder.createInfoAnnotation(cmdElement, null);
      annotation.setTextAttributes(attributesKey);
    } 
  }
  
  private void annotateHereDoc(BashHereDoc element, AnnotationHolder annotationHolder) {
    Annotation annotation = annotationHolder.createInfoAnnotation((PsiElement)element, null);
    annotation.setTextAttributes(BashSyntaxHighlighter.HERE_DOC);
    annotation.setNeedsUpdateOnTyping(false);
    
    if (element.isEvaluatingVariables()) {
      highlightVariables((PsiElement)element, annotationHolder);
    }
  }
  
  private void annotateHereDocStart(PsiElement element, AnnotationHolder annotationHolder) {
    Annotation annotation = annotationHolder.createInfoAnnotation(element, null);
    annotation.setTextAttributes(BashSyntaxHighlighter.HERE_DOC_START);
  }
  
  private void annotateHereDocEnd(PsiElement element, AnnotationHolder annotationHolder) {
    Annotation annotation = annotationHolder.createInfoAnnotation(element, null);
    annotation.setTextAttributes(BashSyntaxHighlighter.HERE_DOC_END);
  }
  
  private void annotateVarDef(BashVarDef bashVarDef, AnnotationHolder annotationHolder) {
    PsiElement identifier = bashVarDef.findAssignmentWord();
    if (identifier != null) {
      Annotation annotation = annotationHolder.createInfoAnnotation(identifier, null);
      annotation.setTextAttributes(BashSyntaxHighlighter.VAR_DEF);
    } 
  }






  
  private void annotateIdentifier(BashVar var, AnnotationHolder annotationHolder) {
    String varName = var.getReferenceName();
    
    if (!BashIdentifierUtil.isValidVariableName(varName) && !BashPsiUtils.isInEvalBlock((PsiElement)var))
      annotationHolder.createErrorAnnotation(var.getReference().getRangeInElement().shiftRight(var.getTextOffset()), String.format("'%s': not a valid identifier", new Object[] { varName })); 
  }
}
