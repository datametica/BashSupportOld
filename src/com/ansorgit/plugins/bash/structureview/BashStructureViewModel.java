package com.ansorgit.plugins.bash.structureview;

import com.ansorgit.plugins.bash.lang.psi.api.function.BashFunctionDef;
import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.structureView.TextEditorBasedStructureViewModel;
import com.intellij.ide.util.treeView.smartTree.Filter;
import com.intellij.ide.util.treeView.smartTree.Grouper;
import com.intellij.ide.util.treeView.smartTree.Sorter;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;














class BashStructureViewModel
  extends TextEditorBasedStructureViewModel
  implements StructureViewModel
{
  private static final Class[] CLASSS = new Class[] { BashFunctionDef.class };
  private static final Sorter[] SORTERS = new Sorter[] { Sorter.ALPHA_SORTER };
  
  private final PsiFile myFile;
  
  BashStructureViewModel(Editor editor, PsiFile file) {
    super(editor, file);
    this.myFile = file;
  }
  
  protected PsiFile getPsiFile() {
    return this.myFile;
  }
  
  @NotNull
  public StructureViewTreeElement getRoot() {
    if (new BashStructureViewElement((PsiElement)this.myFile) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/structureview/BashStructureViewModel", "getRoot" }));  return new BashStructureViewElement((PsiElement)this.myFile);
  }
  
  @NotNull
  public Grouper[] getGroupers() {
    if (Grouper.EMPTY_ARRAY == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/structureview/BashStructureViewModel", "getGroupers" }));  return Grouper.EMPTY_ARRAY;
  }

  
  @NotNull
  protected Class[] getSuitableClasses() {
    if (CLASSS == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/structureview/BashStructureViewModel", "getSuitableClasses" }));  return CLASSS;
  }
  
  @NotNull
  public Sorter[] getSorters() {
    if (SORTERS == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/structureview/BashStructureViewModel", "getSorters" }));  return SORTERS;
  }
  
  @NotNull
  public Filter[] getFilters() {
    if (Filter.EMPTY_ARRAY == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/structureview/BashStructureViewModel", "getFilters" }));  return Filter.EMPTY_ARRAY;
  }
}
