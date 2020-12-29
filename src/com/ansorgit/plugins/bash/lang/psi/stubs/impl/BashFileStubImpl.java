package com.ansorgit.plugins.bash.lang.psi.stubs.impl;

import com.ansorgit.plugins.bash.lang.parser.BashElementTypes;
import com.ansorgit.plugins.bash.lang.psi.api.BashFile;
import com.ansorgit.plugins.bash.lang.psi.stubs.api.BashFileStub;
import com.intellij.psi.PsiFile;
import com.intellij.psi.stubs.PsiFileStubImpl;
import com.intellij.psi.tree.IStubFileElementType;
import com.intellij.psi.tree.StubFileElementType;
import com.intellij.util.io.StringRef;














public class BashFileStubImpl
  extends PsiFileStubImpl<BashFile>
  implements BashFileStub
{
  private final StringRef myName;
  
  public BashFileStubImpl(BashFile file) {
    this(file, StringRef.fromNullableString(file.getName()));
  }
  
  public BashFileStubImpl(BashFile file, StringRef name) {
    super((PsiFile)file);
    this.myName = name;
  }

  
  public IStubFileElementType getType() {
    return BashElementTypes.FILE;
  }
  
  public StringRef getName() {
    return this.myName;
  }
}
