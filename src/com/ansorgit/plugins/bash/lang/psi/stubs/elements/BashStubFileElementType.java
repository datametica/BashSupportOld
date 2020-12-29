package com.ansorgit.plugins.bash.lang.psi.stubs.elements;

import com.ansorgit.plugins.bash.file.BashFileType;
import com.ansorgit.plugins.bash.lang.psi.api.BashFile;
import com.ansorgit.plugins.bash.lang.psi.stubs.api.BashFileStub;
import com.ansorgit.plugins.bash.lang.psi.stubs.impl.BashFileStubImpl;
import com.ansorgit.plugins.bash.lang.psi.stubs.index.BashScriptNameIndex;
import com.intellij.psi.PsiFile;
import com.intellij.psi.StubBuilder;
import com.intellij.psi.stubs.DefaultStubBuilder;
import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.PsiFileStub;
import com.intellij.psi.stubs.Stub;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.psi.tree.IStubFileElementType;
import com.intellij.util.io.StringRef;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;











public class BashStubFileElementType
  extends IStubFileElementType<BashFileStub>
{
  public BashStubFileElementType() {
    super(BashFileType.BASH_LANGUAGE);
  }

  
  public StubBuilder getBuilder() {
    return (StubBuilder)new DefaultStubBuilder()
      {
        @NotNull
        protected StubElement createStubForFile(@NotNull PsiFile file) {
          if (file == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "file", "com/ansorgit/plugins/bash/lang/psi/stubs/elements/BashStubFileElementType$1", "createStubForFile" }));  if (file instanceof BashFile) {
            if (new BashFileStubImpl((BashFile)file) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/stubs/elements/BashStubFileElementType$1", "createStubForFile" }));  return (StubElement)new BashFileStubImpl((BashFile)file);
          } 
          
          if (super.createStubForFile(file) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/stubs/elements/BashStubFileElementType$1", "createStubForFile" }));  return super.createStubForFile(file);
        }
      };
  }

  
  public int getStubVersion() {
    return super.getStubVersion() + 72;
  }
  
  @NotNull
  public String getExternalId() {
    if ("bash.FILE" == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/stubs/elements/BashStubFileElementType", "getExternalId" }));  return "bash.FILE";
  }

  
  public void indexStub(@NotNull PsiFileStub stub, @NotNull IndexSink sink) {
    if (stub == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "stub", "com/ansorgit/plugins/bash/lang/psi/stubs/elements/BashStubFileElementType", "indexStub" }));  if (sink == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "sink", "com/ansorgit/plugins/bash/lang/psi/stubs/elements/BashStubFileElementType", "indexStub" }));  super.indexStub(stub, sink);
    assert stub instanceof BashFileStub;


    
    String name = ((BashFileStub)stub).getName().toString();
    sink.occurrence(BashScriptNameIndex.KEY, name);
  }


  
  public void serialize(@NotNull BashFileStub stub, @NotNull StubOutputStream dataStream) throws IOException {
    if (stub == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "stub", "com/ansorgit/plugins/bash/lang/psi/stubs/elements/BashStubFileElementType", "serialize" }));  if (dataStream == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "dataStream", "com/ansorgit/plugins/bash/lang/psi/stubs/elements/BashStubFileElementType", "serialize" }));  dataStream.writeName(stub.getName().toString());
  }

  
  @NotNull
  public BashFileStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    if (dataStream == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "dataStream", "com/ansorgit/plugins/bash/lang/psi/stubs/elements/BashStubFileElementType", "deserialize" }));  StringRef name = dataStream.readName();
    
    if (new BashFileStubImpl(null, name) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/stubs/elements/BashStubFileElementType", "deserialize" }));  return (BashFileStub)new BashFileStubImpl(null, name);
  }
}
