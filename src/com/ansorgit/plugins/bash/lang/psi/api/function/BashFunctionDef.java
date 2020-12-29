package com.ansorgit.plugins.bash.lang.psi.api.function;

import com.ansorgit.plugins.bash.lang.psi.api.BashBlock;
import com.ansorgit.plugins.bash.lang.psi.api.BashFunctionDefName;
import com.ansorgit.plugins.bash.lang.psi.api.BashPsiElement;
import com.ansorgit.plugins.bash.lang.psi.api.DocumentationAwareElement;
import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVarDefContainer;
import com.intellij.navigation.NavigationItem;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.PsiNamedElement;
import java.util.List;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface BashFunctionDef extends BashPsiElement, PsiNamedElement, NavigationItem, PsiNameIdentifierOwner, BashVarDefContainer, DocumentationAwareElement {
  @Nullable
  BashBlock functionBody();
  
  @Nullable
  BashFunctionDefName getNameSymbol();
  
  @NotNull
  List<BashPsiElement> findReferencedParameters();
  
  @NotNull
  Set<String> findLocalScopeVariables();
}
