package com.ansorgit.plugins.bash.lang.psi.stubs.impl;

import com.ansorgit.plugins.bash.lang.psi.api.function.BashFunctionDef;
import com.ansorgit.plugins.bash.lang.psi.stubs.api.BashFunctionDefStub;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import com.intellij.util.io.StringRef;
















public class BashFunctionDefStubImpl
  extends StubBase<BashFunctionDef>
  implements BashFunctionDefStub
{
  private final StringRef myName;
  
  public BashFunctionDefStubImpl(StubElement parent, StringRef name, IStubElementType elementType) {
    super(parent, elementType);
    this.myName = name;
  }
  
  public String getName() {
    return StringRef.toString(this.myName);
  }
}
