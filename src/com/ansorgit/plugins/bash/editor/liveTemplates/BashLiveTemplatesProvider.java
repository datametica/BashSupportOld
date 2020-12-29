package com.ansorgit.plugins.bash.editor.liveTemplates;

import com.intellij.codeInsight.template.impl.DefaultLiveTemplatesProvider;
import org.jetbrains.annotations.Nullable;


















public class BashLiveTemplatesProvider
  implements DefaultLiveTemplatesProvider
{
  private static final String[] EMPTY = new String[0];

  
  public String[] getDefaultLiveTemplateFiles() {
    return new String[] { "/liveTemplates/Bash" };
  }

  
  @Nullable
  public String[] getHiddenLiveTemplateFiles() {
    return EMPTY;
  }
}
