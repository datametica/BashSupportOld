package com.ansorgit.plugins.bash.lang.psi.impl;

import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.BashShebang;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiElementFactory;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
















public class BashShebangImpl
  extends BashBaseElement
  implements BashShebang
{
  private static final Logger log = Logger.getInstance("#bash.BashShebang");
  
  public BashShebangImpl(ASTNode astNode) {
    super(astNode, "bash shebang");
    log.debug("Created BashShebangImpl");
  }
  
  public String shellCommand(boolean withParams) {
    String allText = getText();
    if (StringUtils.isEmpty(allText))
    {
      return null;
    }

    
    int commandOffset = getShellCommandOffset();
    
    String line = allText.substring(commandOffset);
    String commandLine = hasNewline() ? line.substring(0, line.length() - 1) : line;
    
    if (!withParams) {
      
      int cmdEndIndex = commandLine.indexOf(' ', 0);
      
      if (cmdEndIndex > 0) {
        commandLine = commandLine.substring(0, cmdEndIndex);
      }
    } 
    
    return commandLine.trim();
  }

  
  public String shellCommandParams() {
    String withParams = shellCommand(true);
    String withoutParams = shellCommand(false);
    
    if (withoutParams.length() < withParams.length()) {
      return withParams.substring(withoutParams.length()).trim();
    }
    
    return "";
  }
  
  public int getShellCommandOffset() {
    String line = getText();
    if (!line.startsWith("#!")) {
      return 0;
    }
    
    int offset = 2;
    for (int i = 2; i < line.length() && line.charAt(i) == ' '; i++) {
      offset++;
    }
    
    return offset;
  }
  
  @NotNull
  public TextRange commandRange() {
    if (TextRange.from(getShellCommandOffset(), shellCommand(false).length()) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/BashShebangImpl", "commandRange" }));  return TextRange.from(getShellCommandOffset(), shellCommand(false).length());
  }

  
  @NotNull
  public TextRange commandAndParamsRange() {
    if (TextRange.from(getShellCommandOffset(), shellCommand(true).length()) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/BashShebangImpl", "commandAndParamsRange" }));  return TextRange.from(getShellCommandOffset(), shellCommand(true).length());
  }
  
  public void updateCommand(String command, @Nullable TextRange replacementRange) {
    log.debug("Updating command to " + command);
    
    PsiFile file = getContainingFile();
    if (file == null) {
      return;
    }
    
    Document document = file.getViewProvider().getDocument();
    if (document != null) {
      TextRange textRange = (replacementRange != null) ? replacementRange : commandRange();
      document.replaceString(textRange.getStartOffset(), textRange.getEndOffset(), command);
    } else {
      
      PsiElement newElement = BashPsiElementFactory.createShebang(getProject(), command, hasNewline());
      getNode().replaceChild(getNode().getFirstChildNode(), newElement.getNode());
    } 
  }
  
  private boolean hasNewline() {
    return getText().endsWith("\n");
  }

  
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "visitor", "com/ansorgit/plugins/bash/lang/psi/impl/BashShebangImpl", "accept" }));  if (visitor instanceof BashVisitor) {
      ((BashVisitor)visitor).visitShebang(this);
    } else {
      visitor.visitElement((PsiElement)this);
    } 
  }
}
