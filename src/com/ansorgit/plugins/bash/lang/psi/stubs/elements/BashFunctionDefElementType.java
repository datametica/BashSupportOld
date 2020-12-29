package com.ansorgit.plugins.bash.lang.psi.stubs.elements;

import com.ansorgit.plugins.bash.lang.parser.BashElementTypes;
import com.ansorgit.plugins.bash.lang.psi.BashStubElementType;
import com.ansorgit.plugins.bash.lang.psi.api.function.BashFunctionDef;
import com.ansorgit.plugins.bash.lang.psi.impl.function.BashFunctionDefImpl;
import com.ansorgit.plugins.bash.lang.psi.stubs.api.BashFunctionDefStub;
import com.ansorgit.plugins.bash.lang.psi.stubs.impl.BashFunctionDefStubImpl;
import com.ansorgit.plugins.bash.lang.psi.stubs.index.BashFunctionNameIndex;
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
















public class BashFunctionDefElementType
  extends BashStubElementType<BashFunctionDefStub, BashFunctionDef>
{
  public BashFunctionDefElementType() {
    super("function-def-element");
  }

  
  @NotNull
  public String getExternalId() {
    if ("bash.functionDef" == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/stubs/elements/BashFunctionDefElementType", "getExternalId" }));  return "bash.functionDef";
  }
  
  public void serialize(@NotNull BashFunctionDefStub stub, @NotNull StubOutputStream dataStream) throws IOException {
    if (stub == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "stub", "com/ansorgit/plugins/bash/lang/psi/stubs/elements/BashFunctionDefElementType", "serialize" }));  if (dataStream == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "dataStream", "com/ansorgit/plugins/bash/lang/psi/stubs/elements/BashFunctionDefElementType", "serialize" }));  dataStream.writeName(stub.getName());
  }
  
  @NotNull
  public BashFunctionDefStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    if (dataStream == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "dataStream", "com/ansorgit/plugins/bash/lang/psi/stubs/elements/BashFunctionDefElementType", "deserialize" }));  StringRef ref = dataStream.readName();
    if (new BashFunctionDefStubImpl(parentStub, ref, (IStubElementType)this) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/stubs/elements/BashFunctionDefElementType", "deserialize" }));  return (BashFunctionDefStub)new BashFunctionDefStubImpl(parentStub, ref, (IStubElementType)this);
  }
  
  public BashFunctionDef createPsi(@NotNull BashFunctionDefStub stub) {
    if (stub == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "stub", "com/ansorgit/plugins/bash/lang/psi/stubs/elements/BashFunctionDefElementType", "createPsi" }));  return (BashFunctionDef)new BashFunctionDefImpl(stub, BashElementTypes.FUNCTION_DEF_COMMAND);
  }
  
  public BashFunctionDefStub createStub(@NotNull BashFunctionDef psi, StubElement parentStub) {
    if (psi == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "psi", "com/ansorgit/plugins/bash/lang/psi/stubs/elements/BashFunctionDefElementType", "createStub" }));  return (BashFunctionDefStub)new BashFunctionDefStubImpl(parentStub, StringRef.fromString(psi.getName()), BashElementTypes.FUNCTION_DEF_COMMAND);
  }

  
  public void indexStub(@NotNull BashFunctionDefStub stub, @NotNull IndexSink sink) {
    if (stub == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "stub", "com/ansorgit/plugins/bash/lang/psi/stubs/elements/BashFunctionDefElementType", "indexStub" }));  if (sink == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "sink", "com/ansorgit/plugins/bash/lang/psi/stubs/elements/BashFunctionDefElementType", "indexStub" }));  String name = stub.getName();
    if (name != null)
      sink.occurrence(BashFunctionNameIndex.KEY, name); 
  }
}
