package com.ansorgit.plugins.bash.lang.psi.api;

import com.ansorgit.plugins.bash.lang.psi.api.function.BashFunctionDef;
import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiFile;
import java.util.List;
import org.jetbrains.annotations.Nullable;


















public interface BashFile
  extends PsiFile, BashPsiElement
{
  public static final Key<Boolean> LANGUAGE_CONSOLE_MARKER = new Key("Language console marker");
  
  boolean hasShebangLine();
  
  @Nullable
  BashShebang findShebang();
  
  List<BashFunctionDef> allFunctionDefinitions();
}
