package com.ansorgit.plugins.bash.structureview;

import com.intellij.ide.structureView.StructureViewBuilder;
import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.TreeBasedStructureViewBuilder;
import com.intellij.lang.PsiStructureViewFactory;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;



















public class BashStructureViewFactory
  implements PsiStructureViewFactory
{
  public StructureViewBuilder getStructureViewBuilder(PsiFile psiFile) {
    return (StructureViewBuilder)new BashTreeBasedStructureViewBuilder(psiFile);
  }
  
  private static class BashTreeBasedStructureViewBuilder extends TreeBasedStructureViewBuilder {
    private final PsiFile psiFile;
    
    BashTreeBasedStructureViewBuilder(PsiFile psiFile) {
      this.psiFile = psiFile;
    }

    
    @NotNull
    public StructureViewModel createStructureViewModel(@Nullable Editor editor) {
      if (new BashStructureViewModel(editor, this.psiFile) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/structureview/BashStructureViewFactory$BashTreeBasedStructureViewBuilder", "createStructureViewModel" }));  return new BashStructureViewModel(editor, this.psiFile);
    }
    
    public boolean isRootNodeShown() {
      return false;
    }
  }
}
