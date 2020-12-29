package com.ansorgit.plugins.bash.editor;

import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVar;
import com.intellij.codeInsight.editorActions.AutoHardWrapHandler;
import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegate;
import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegateAdapter;
import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.util.Ref;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;























public class EnterInStringLiteralHandler
  extends EnterHandlerDelegateAdapter
{
  public EnterHandlerDelegate.Result preprocessEnter(@NotNull PsiFile file, @NotNull Editor editor, @NotNull Ref<Integer> caretOffset, @NotNull Ref<Integer> caretAdvance, @NotNull DataContext dataContext, @Nullable EditorActionHandler originalHandler) {
    if (file == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "file", "com/ansorgit/plugins/bash/editor/EnterInStringLiteralHandler", "preprocessEnter" }));  if (editor == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "editor", "com/ansorgit/plugins/bash/editor/EnterInStringLiteralHandler", "preprocessEnter" }));  if (caretOffset == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "caretOffset", "com/ansorgit/plugins/bash/editor/EnterInStringLiteralHandler", "preprocessEnter" }));  if (caretAdvance == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "caretAdvance", "com/ansorgit/plugins/bash/editor/EnterInStringLiteralHandler", "preprocessEnter" }));  if (dataContext == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "dataContext", "com/ansorgit/plugins/bash/editor/EnterInStringLiteralHandler", "preprocessEnter" }));  if (!(file instanceof com.ansorgit.plugins.bash.lang.psi.api.BashFile)) {
      return EnterHandlerDelegate.Result.Continue;
    }

    
    int offset = ((Integer)caretOffset.get()).intValue();

    
    CharSequence content = editor.getDocument().getCharsSequence();
    if (offset >= content.length() || content.charAt(offset) == '\n') {
      return EnterHandlerDelegate.Result.Continue;
    }
    
    if (PsiDocumentManager.getInstance(file.getProject()).isUncommited(editor.getDocument()))
    {
      
      return EnterHandlerDelegate.Result.Continue;
    }
    
    PsiElement psi = file.findElementAt(offset);
    if (psi == null || psi.getNode() == null) {
      return EnterHandlerDelegate.Result.Continue;
    }
    
    boolean isUserTyping = !Boolean.TRUE.equals(DataManager.getInstance().loadFromDataContext(dataContext, AutoHardWrapHandler.AUTO_WRAP_LINE_IN_PROGRESS_KEY));
    if (isUserTyping && !isInString(psi)) {
      return EnterHandlerDelegate.Result.Continue;
    }
    
    if (offset >= psi.getTextOffset() && psi.getNode().getElementType() != BashTokenTypes.LINE_FEED) {
      EditorModificationUtil.insertStringAtCaret(editor, "\\\n");
      return EnterHandlerDelegate.Result.Stop;
    } 
    
    return EnterHandlerDelegate.Result.Continue;
  }
  
  private boolean isInString(PsiElement start) {
    PsiElement parent = PsiTreeUtil.skipParentsOfType(start, new Class[] { LeafPsiElement.class, BashVar.class });
    if (parent != null) {
      return parent instanceof com.ansorgit.plugins.bash.lang.psi.api.BashString;
    }
    return start instanceof com.ansorgit.plugins.bash.lang.psi.api.BashString;
  }
}
