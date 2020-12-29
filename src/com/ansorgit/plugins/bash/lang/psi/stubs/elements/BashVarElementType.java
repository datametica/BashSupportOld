package com.ansorgit.plugins.bash.lang.psi.stubs.elements;

import com.ansorgit.plugins.bash.lang.parser.BashElementTypes;
import com.ansorgit.plugins.bash.lang.psi.BashStubElementType;
import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVar;
import com.ansorgit.plugins.bash.lang.psi.impl.vars.BashVarImpl;
import com.ansorgit.plugins.bash.lang.psi.stubs.api.BashVarStub;
import com.ansorgit.plugins.bash.lang.psi.stubs.impl.BashVarStubImpl;
import com.ansorgit.plugins.bash.lang.psi.stubs.index.BashVarIndex;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.Stub;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.util.io.StringRef;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;















public class BashVarElementType
  extends BashStubElementType<BashVarStub, BashVar>
{
  public BashVarElementType() {
    super("var-use-element");
  }

  
  @NotNull
  public String getExternalId() {
    if ("bash.var" == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/stubs/elements/BashVarElementType", "getExternalId" }));  return "bash.var";
  }
  
  public void serialize(@NotNull BashVarStub stub, @NotNull StubOutputStream dataStream) throws IOException {
    if (stub == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "stub", "com/ansorgit/plugins/bash/lang/psi/stubs/elements/BashVarElementType", "serialize" }));  if (dataStream == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "dataStream", "com/ansorgit/plugins/bash/lang/psi/stubs/elements/BashVarElementType", "serialize" }));  dataStream.writeName(stub.getName());
    dataStream.writeInt(stub.getPrefixLength());
  }
  
  @NotNull
  public BashVarStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    if (dataStream == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "dataStream", "com/ansorgit/plugins/bash/lang/psi/stubs/elements/BashVarElementType", "deserialize" }));  StringRef ref = dataStream.readName();
    int prefixLength = dataStream.readInt();
    
    if (new BashVarStubImpl(parentStub, ref, (IStubElementType)this, prefixLength) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/stubs/elements/BashVarElementType", "deserialize" }));  return (BashVarStub)new BashVarStubImpl(parentStub, ref, (IStubElementType)this, prefixLength);
  }
  
  public BashVar createPsi(@NotNull BashVarStub stub) {
    if (stub == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "stub", "com/ansorgit/plugins/bash/lang/psi/stubs/elements/BashVarElementType", "createPsi" }));  return (BashVar)new BashVarImpl(stub, BashElementTypes.VAR_ELEMENT);
  }
  
  public BashVarStub createStub(@NotNull BashVar psi, StubElement parentStub) {
    if (psi == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "psi", "com/ansorgit/plugins/bash/lang/psi/stubs/elements/BashVarElementType", "createStub" }));  return (BashVarStub)new BashVarStubImpl(parentStub, StringRef.fromString(psi.getName()), BashElementTypes.VAR_ELEMENT, psi.getPrefixLength());
  }

  
  public void indexStub(@NotNull BashVarStub stub, @NotNull IndexSink sink) {
    if (stub == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "stub", "com/ansorgit/plugins/bash/lang/psi/stubs/elements/BashVarElementType", "indexStub" }));  if (sink == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "sink", "com/ansorgit/plugins/bash/lang/psi/stubs/elements/BashVarElementType", "indexStub" }));  String name = stub.getName();
    if (name != null)
      sink.occurrence(BashVarIndex.KEY, name); 
  }
}
