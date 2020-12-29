package com.ansorgit.plugins.bash.lang.psi.stubs.elements;

import com.ansorgit.plugins.bash.lang.parser.BashElementTypes;
import com.ansorgit.plugins.bash.lang.psi.BashStubElementType;
import com.ansorgit.plugins.bash.lang.psi.api.BashFileReference;
import com.ansorgit.plugins.bash.lang.psi.api.command.BashIncludeCommand;
import com.ansorgit.plugins.bash.lang.psi.impl.command.BashIncludeCommandImpl;
import com.ansorgit.plugins.bash.lang.psi.stubs.api.BashIncludeCommandStub;
import com.ansorgit.plugins.bash.lang.psi.stubs.impl.BashIncludeCommandStubImpl;
import com.ansorgit.plugins.bash.lang.psi.stubs.index.BashIncludeCommandIndex;
import com.ansorgit.plugins.bash.lang.psi.stubs.index.BashIncludedFilenamesIndex;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.Stub;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.util.indexing.IndexingDataKeys;
import com.intellij.util.io.StringRef;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;















public class BashIncludeCommandElementType
  extends BashStubElementType<BashIncludeCommandStub, BashIncludeCommand>
{
  public BashIncludeCommandElementType() {
    super("include-command");
  }

  
  @NotNull
  public String getExternalId() {
    if ("bash.includeCommand" == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/stubs/elements/BashIncludeCommandElementType", "getExternalId" }));  return "bash.includeCommand";
  }
  
  public void serialize(@NotNull BashIncludeCommandStub stub, @NotNull StubOutputStream dataStream) throws IOException {
    if (stub == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "stub", "com/ansorgit/plugins/bash/lang/psi/stubs/elements/BashIncludeCommandElementType", "serialize" }));  if (dataStream == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "dataStream", "com/ansorgit/plugins/bash/lang/psi/stubs/elements/BashIncludeCommandElementType", "serialize" }));  dataStream.writeName(stub.getIncludedFilename());
    dataStream.writeName(stub.getIncluderFilePath());
  }
  
  @NotNull
  public BashIncludeCommandStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    if (dataStream == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "dataStream", "com/ansorgit/plugins/bash/lang/psi/stubs/elements/BashIncludeCommandElementType", "deserialize" }));  StringRef filename = dataStream.readName();
    StringRef includer = dataStream.readName();
    if (new BashIncludeCommandStubImpl(parentStub, filename, includer, (IStubElementType)this) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/stubs/elements/BashIncludeCommandElementType", "deserialize" }));  return (BashIncludeCommandStub)new BashIncludeCommandStubImpl(parentStub, filename, includer, (IStubElementType)this);
  }
  
  public BashIncludeCommand createPsi(@NotNull BashIncludeCommandStub stub) {
    if (stub == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "stub", "com/ansorgit/plugins/bash/lang/psi/stubs/elements/BashIncludeCommandElementType", "createPsi" }));  return (BashIncludeCommand)new BashIncludeCommandImpl(stub, BashElementTypes.INCLUDE_COMMAND_ELEMENT);
  }
  
  public BashIncludeCommandStub createStub(@NotNull BashIncludeCommand psi, StubElement parentStub) {
    if (psi == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "psi", "com/ansorgit/plugins/bash/lang/psi/stubs/elements/BashIncludeCommandElementType", "createStub" }));  BashFileReference fileReference = psi.getFileReference();
    
    String filename = null;
    String includer = null;
    
    if (fileReference != null && fileReference.isStatic()) {
      filename = fileReference.getFilename();
      if (filename.contains("/") && !filename.endsWith("/")) {
        int index = filename.lastIndexOf("/");
        filename = filename.substring(index + 1);
      } 
      
      VirtualFile virtualFile = (VirtualFile)psi.getContainingFile().getUserData(IndexingDataKeys.VIRTUAL_FILE);
      if (virtualFile == null) {
        virtualFile = psi.getContainingFile().getViewProvider().getVirtualFile();
      }
      
      includer = virtualFile.getPath();
    } 
    
    return (BashIncludeCommandStub)new BashIncludeCommandStubImpl(parentStub, StringRef.fromString(filename), StringRef.fromString(includer), BashElementTypes.INCLUDE_COMMAND_ELEMENT);
  }

  
  public void indexStub(@NotNull BashIncludeCommandStub stub, @NotNull IndexSink sink) {
    if (stub == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "stub", "com/ansorgit/plugins/bash/lang/psi/stubs/elements/BashIncludeCommandElementType", "indexStub" }));  if (sink == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "sink", "com/ansorgit/plugins/bash/lang/psi/stubs/elements/BashIncludeCommandElementType", "indexStub" }));  String filenamef = stub.getIncludedFilename();
    if (filenamef != null) {
      sink.occurrence(BashIncludedFilenamesIndex.KEY, filenamef);
    }
    
    String includerFilePath = stub.getIncluderFilePath();
    if (includerFilePath != null)
      sink.occurrence(BashIncludeCommandIndex.KEY, includerFilePath); 
  }
}
