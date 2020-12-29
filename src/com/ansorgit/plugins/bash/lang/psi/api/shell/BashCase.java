package com.ansorgit.plugins.bash.lang.psi.api.shell;

import com.ansorgit.plugins.bash.lang.psi.api.BashConditionalBlock;
import com.ansorgit.plugins.bash.lang.psi.api.BashPsiElement;
import com.intellij.psi.PsiElement;
import java.util.Collection;

public interface BashCase extends BashPsiElement, BashConditionalBlock {
  Collection<? extends PsiElement> patternList();
}
