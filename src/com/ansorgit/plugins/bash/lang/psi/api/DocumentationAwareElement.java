package com.ansorgit.plugins.bash.lang.psi.api;

import com.intellij.psi.PsiComment;
import java.util.List;
import org.jetbrains.annotations.Nullable;

public interface DocumentationAwareElement {
  @Nullable
  List<PsiComment> findAttachedComment();
}
