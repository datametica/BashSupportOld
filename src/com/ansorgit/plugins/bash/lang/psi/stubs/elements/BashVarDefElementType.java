package com.ansorgit.plugins.bash.lang.psi.stubs.elements;

import com.ansorgit.plugins.bash.lang.parser.BashElementTypes;
import com.ansorgit.plugins.bash.lang.psi.BashStubElementType;
import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVarDef;
import com.ansorgit.plugins.bash.lang.psi.impl.vars.BashVarDefImpl;
import com.ansorgit.plugins.bash.lang.psi.stubs.api.BashVarDefStub;
import com.ansorgit.plugins.bash.lang.psi.stubs.impl.BashVarDefStubImpl;
import com.ansorgit.plugins.bash.lang.psi.stubs.index.BashVarDefIndex;
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















public class BashVarDefElementType
  extends BashStubElementType<BashVarDefStub, BashVarDef>
{
  public BashVarDefElementType() {
    super("var-def-element");
  }

  
  @NotNull
  public String getExternalId() {
    if ("bash.varDef" == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/stubs/elements/BashVarDefElementType", "getExternalId" }));  return "bash.varDef";
  }
  
  public void serialize(@NotNull BashVarDefStub stub, @NotNull StubOutputStream dataStream) throws IOException {
    if (stub == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "stub", "com/ansorgit/plugins/bash/lang/psi/stubs/elements/BashVarDefElementType", "serialize" }));  if (dataStream == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "dataStream", "com/ansorgit/plugins/bash/lang/psi/stubs/elements/BashVarDefElementType", "serialize" }));  dataStream.writeName(stub.getName());
    dataStream.writeBoolean(stub.isReadOnly());
  }
  
  @NotNull
  public BashVarDefStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    if (dataStream == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "dataStream", "com/ansorgit/plugins/bash/lang/psi/stubs/elements/BashVarDefElementType", "deserialize" }));  StringRef ref = dataStream.readName();
    boolean readOnly = dataStream.readBoolean();
    
    if (new BashVarDefStubImpl(parentStub, ref, (IStubElementType)this, readOnly) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/stubs/elements/BashVarDefElementType", "deserialize" }));  return (BashVarDefStub)new BashVarDefStubImpl(parentStub, ref, (IStubElementType)this, readOnly);
  }
  
  public BashVarDef createPsi(@NotNull BashVarDefStub stub) {
    if (stub == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "stub", "com/ansorgit/plugins/bash/lang/psi/stubs/elements/BashVarDefElementType", "createPsi" }));  return (BashVarDef)new BashVarDefImpl(stub, BashElementTypes.VAR_DEF_ELEMENT);
  }
  
  public BashVarDefStub createStub(@NotNull BashVarDef psi, StubElement parentStub) {
    if (psi == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "psi", "com/ansorgit/plugins/bash/lang/psi/stubs/elements/BashVarDefElementType", "createStub" }));  return (BashVarDefStub)new BashVarDefStubImpl(parentStub, StringRef.fromString(psi.getName()), BashElementTypes.VAR_DEF_ELEMENT, psi.isReadonly());
  }

  
  public void indexStub(@NotNull BashVarDefStub stub, @NotNull IndexSink sink) {
    if (stub == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "stub", "com/ansorgit/plugins/bash/lang/psi/stubs/elements/BashVarDefElementType", "indexStub" }));  if (sink == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "sink", "com/ansorgit/plugins/bash/lang/psi/stubs/elements/BashVarDefElementType", "indexStub" }));  String name = stub.getName();
    if (name != null)
      sink.occurrence(BashVarDefIndex.KEY, name); 
  }
}
