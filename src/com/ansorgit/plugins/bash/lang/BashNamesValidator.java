package com.ansorgit.plugins.bash.lang;

import com.ansorgit.plugins.bash.lang.psi.util.BashIdentifierUtil;
import com.intellij.lang.refactoring.NamesValidator;
import com.intellij.openapi.project.Project;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;













@Metadata(mv = {1, 1, 1}, bv = {1, 0, 0}, k = 1, d1 = {"\000 \n\002\030\002\n\002\030\002\n\002\b\002\n\002\020\013\n\000\n\002\020\016\n\000\n\002\030\002\n\002\b\002\030\0002\0020\001B\005¢\006\002\020\002J\030\020\003\032\0020\0042\006\020\005\032\0020\0062\006\020\007\032\0020\bH\026J\030\020\t\032\0020\0042\006\020\005\032\0020\0062\006\020\007\032\0020\bH\026¨\006\n"}, d2 = {"Lcom/ansorgit/plugins/bash/lang/BashNamesValidator;", "Lcom/intellij/lang/refactoring/NamesValidator;", "()V", "isIdentifier", "", "name", "", "project", "Lcom/intellij/openapi/project/Project;", "isKeyword", "BashSupport1_main"})
public final class BashNamesValidator
  implements NamesValidator
{
  public boolean isKeyword(@NotNull String name, @NotNull Project project) {
    Intrinsics.checkParameterIsNotNull(name, "name"); Intrinsics.checkParameterIsNotNull(project, "project"); return LanguageBuiltins.completionKeywords.contains(name);
  }
  
  public boolean isIdentifier(@NotNull String name, @NotNull Project project) {
    Intrinsics.checkParameterIsNotNull(name, "name"); Intrinsics.checkParameterIsNotNull(project, "project"); return BashIdentifierUtil.isValidVariableName(name);
  }
}
