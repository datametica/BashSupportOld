package com.ansorgit.plugins.bash.lang.lexer;

import com.ansorgit.plugins.bash.file.BashFileType;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;



















public class BashElementType
  extends IElementType
{
  public BashElementType(@NotNull String debugName) {
    super(debugName, BashFileType.BASH_LANGUAGE);
  }
  
  public String toString() {
    return "[Bash] " + super.toString();
  }
}
