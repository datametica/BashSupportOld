package com.ansorgit.plugins.bash.lang.psi.api.shell;

import com.ansorgit.plugins.bash.lang.psi.api.BashKeyword;
import com.intellij.psi.PsiElement;
import java.util.List;
import org.jetbrains.annotations.Nullable;

public interface BashTrapCommand extends PsiElement, BashKeyword {
  @Nullable
  PsiElement getSignalHandlerElement();
  
  List<PsiElement> getSignalSpec();
}
