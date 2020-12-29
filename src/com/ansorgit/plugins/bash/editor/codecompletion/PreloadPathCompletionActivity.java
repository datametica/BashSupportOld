package com.ansorgit.plugins.bash.editor.codecompletion;

import com.intellij.openapi.application.PreloadingActivity;
import com.intellij.openapi.progress.ProgressIndicator;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(mv = {1, 1, 1}, bv = {1, 0, 0}, k = 1, d1 = {"\000\030\n\002\030\002\n\002\030\002\n\002\b\002\n\002\020\002\n\000\n\002\030\002\n\000\030\0002\0020\001B\005¢\006\002\020\002J\020\020\003\032\0020\0042\006\020\005\032\0020\006H\026¨\006\007"}, d2 = {"Lcom/ansorgit/plugins/bash/editor/codecompletion/PreloadPathCompletionActivity;", "Lcom/intellij/openapi/application/PreloadingActivity;", "()V", "preload", "", "progress", "Lcom/intellij/openapi/progress/ProgressIndicator;", "BashSupport1_main"})
public final class PreloadPathCompletionActivity
  extends PreloadingActivity
{
  public void preload(@NotNull ProgressIndicator progress) {
    Intrinsics.checkParameterIsNotNull(progress, "progress"); BashPathCompletionService.Companion.getInstance().findCommands("test");
  }
}
