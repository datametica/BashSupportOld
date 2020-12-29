package com.ansorgit.plugins.bash.util;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(mv = {1, 1, 1}, bv = {1, 0, 0}, k = 1, d1 = {"\000\032\n\002\030\002\n\002\020\000\n\002\b\002\n\002\020\013\n\000\n\002\020\016\n\002\b\003\bÆ\002\030\0002\0020\001B\007\b\002¢\006\002\020\002J\020\020\003\032\0020\0042\006\020\005\032\0020\006H\007J\020\020\007\032\0020\0062\006\020\005\032\0020\006H\007J\b\020\b\032\0020\006H\007¨\006\t"}, d2 = {"Lcom/ansorgit/plugins/bash/util/BashFiles;", "", "()V", "containsSupportedPlaceholders", "", "value", "", "replaceHomePlaceholders", "userHomeDir", "BashSupport1_main"})
public final class BashFiles {
  private BashFiles() {
    INSTANCE = this;
  }
  public static final BashFiles INSTANCE;
  @JvmStatic
  @NotNull
  public static final String replaceHomePlaceholders(@NotNull String value) {
    Intrinsics.checkParameterIsNotNull(value, "value"); String result = value;
    if (StringsKt.startsWith$default(result, '~', false, 2, null)) {
      String str1 = value; boolean bool = true; StringBuilder stringBuilder = (new StringBuilder()).append(userHomeDir()); if (str1 == null) throw new TypeCastException("null cannot be cast to non-null type java.lang.String");  Intrinsics.checkExpressionValueIsNotNull(str1.substring(bool), "(this as java.lang.String).substring(startIndex)"); String str2 = str1.substring(bool); result = stringBuilder.append(str2).toString();
    } 
    return StringsKt.replace(result, "$HOME", userHomeDir(), false);
  }
  @JvmStatic
  public static final boolean containsSupportedPlaceholders(@NotNull String value) {
    Intrinsics.checkParameterIsNotNull(value, "value"); return (StringsKt.startsWith$default(value, "~", false, 2, null) || StringsKt.contains$default(value, "$HOME", false, 2, null));
  }
  @JvmStatic
  @NotNull
  public static final String userHomeDir() {
    Intrinsics.checkExpressionValueIsNotNull(System.getenv("USERPROFILE"), "System.getenv(\"USERPROFILE\")");
    
    Intrinsics.checkExpressionValueIsNotNull(System.getenv("HOME"), "System.getenv(\"HOME\")"); return SystemInfo.isWindows ? System.getenv("USERPROFILE") : System.getenv("HOME");
  }
}
