package com.ansorgit.plugins.bash.lang.psi;

import com.ansorgit.plugins.bash.lang.psi.api.BashBackquote;
import com.ansorgit.plugins.bash.lang.psi.api.BashFile;
import com.ansorgit.plugins.bash.lang.psi.api.BashFileReference;
import com.ansorgit.plugins.bash.lang.psi.api.BashShebang;
import com.ansorgit.plugins.bash.lang.psi.api.BashString;
import com.ansorgit.plugins.bash.lang.psi.api.arithmetic.ArithmeticExpression;
import com.ansorgit.plugins.bash.lang.psi.api.command.BashCommand;
import com.ansorgit.plugins.bash.lang.psi.api.command.BashIncludeCommand;
import com.ansorgit.plugins.bash.lang.psi.api.expression.BashFiledescriptor;
import com.ansorgit.plugins.bash.lang.psi.api.expression.BashRedirectExpr;
import com.ansorgit.plugins.bash.lang.psi.api.expression.BashRedirectList;
import com.ansorgit.plugins.bash.lang.psi.api.expression.BashSubshellCommand;
import com.ansorgit.plugins.bash.lang.psi.api.function.BashFunctionDef;
import com.ansorgit.plugins.bash.lang.psi.api.heredoc.BashHereDoc;
import com.ansorgit.plugins.bash.lang.psi.api.heredoc.BashHereDocEndMarker;
import com.ansorgit.plugins.bash.lang.psi.api.heredoc.BashHereDocStartMarker;
import com.ansorgit.plugins.bash.lang.psi.api.shell.BashConditionalCommand;
import com.ansorgit.plugins.bash.lang.psi.api.vars.BashComposedVar;
import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVar;
import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVarDef;
import com.ansorgit.plugins.bash.lang.psi.api.word.BashExpansion;
import com.ansorgit.plugins.bash.lang.psi.api.word.BashWord;
import com.ansorgit.plugins.bash.lang.psi.impl.shell.BashExtendedConditionalCommandImpl;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;












public class BashVisitor
  extends PsiElementVisitor
{
  public void visitFile(BashFile file) {
    visitElement((PsiElement)file);
  }
  
  public void visitFunctionDef(BashFunctionDef functionDef) {
    visitElement((PsiElement)functionDef);
  }
  
  public void visitVarDef(BashVarDef varDef) {
    visitElement((PsiElement)varDef);
  }
  
  public void visitVarUse(BashVar var) {
    visitElement((PsiElement)var);
  }
  
  public void visitComposedVariable(BashComposedVar composedVar) {
    visitElement((PsiElement)composedVar);
  }
  
  public void visitShebang(BashShebang shebang) {
    visitElement((PsiElement)shebang);
  }
  
  public void visitCombinedWord(BashWord word) {
    visitElement((PsiElement)word);
  }
  
  public void visitBackquoteCommand(BashBackquote backquote) {
    visitElement((PsiElement)backquote);
  }
  
  public void visitSubshell(BashSubshellCommand subshellCommand) {
    visitElement((PsiElement)subshellCommand);
  }
  
  public void visitConditional(BashConditionalCommand conditionalCommand) {
    visitElement((PsiElement)conditionalCommand);
  }
  
  public void visitExtendedConditional(BashExtendedConditionalCommandImpl extendedConditionalCommand) {
    visitElement((PsiElement)extendedConditionalCommand);
  }
  
  public void visitInternalCommand(BashCommand bashCommand) {
    visitElement((PsiElement)bashCommand);
  }
  
  public void visitGenericCommand(BashCommand bashCommand) {
    visitElement((PsiElement)bashCommand);
  }
  
  public void visitExpansion(BashExpansion bashExpansion) {
    visitElement((PsiElement)bashExpansion);
  }
  
  public void visitIncludeCommand(BashIncludeCommand fileReference) {
    visitElement((PsiElement)fileReference);
  }
  
  public void visitFileReference(BashFileReference fileReference) {
    visitElement((PsiElement)fileReference);
  }







  
  public void visitString(BashString bashString) {
    visitElement((PsiElement)bashString);
  }
  
  public void visitHereDocEndMarker(BashHereDocEndMarker marker) {
    visitElement((PsiElement)marker);
  }
  
  public void visitHereDocStartMarker(BashHereDocStartMarker marker) {
    visitElement((PsiElement)marker);
  }
  
  public void visitHereDoc(BashHereDoc doc) {
    visitElement((PsiElement)doc);
  }
  
  public void visitArithmeticExpression(ArithmeticExpression expression) {
    visitElement((PsiElement)expression);
  }
  
  public void visitRedirectExpression(BashRedirectExpr redirect) {
    visitElement((PsiElement)redirect);
  }
  
  public void visitRedirectExpressionList(BashRedirectList redirectList) {
    visitElement((PsiElement)redirectList);
  }
  
  public void visitFiledescriptor(BashFiledescriptor filedescriptor) {
    visitElement((PsiElement)filedescriptor);
  }
}
