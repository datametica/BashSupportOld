package com.ansorgit.plugins.bash.lang.psi.stubs.elements;

import com.ansorgit.plugins.bash.lang.parser.BashElementTypes;
import com.ansorgit.plugins.bash.lang.psi.BashStubElementType;
import com.ansorgit.plugins.bash.lang.psi.api.command.BashCommand;
import com.ansorgit.plugins.bash.lang.psi.impl.command.BashSimpleCommandImpl;
import com.ansorgit.plugins.bash.lang.psi.stubs.api.BashCommandStub;
import com.ansorgit.plugins.bash.lang.psi.stubs.impl.BashCommandStubImpl;
import com.ansorgit.plugins.bash.lang.psi.stubs.index.BashCommandNameIndex;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.Stub;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.util.PathUtilRt;
import com.intellij.util.io.StringRef;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;















public class BashSimpleCommandElementType
  extends BashStubElementType<BashCommandStub, BashCommand>
{
  public BashSimpleCommandElementType() {
    super("simple-command");
  }

  
  @NotNull
  public String getExternalId() {
    if ("bash.simpleCommand" == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/stubs/elements/BashSimpleCommandElementType", "getExternalId" }));  return "bash.simpleCommand";
  }
  
  public void serialize(@NotNull BashCommandStub stub, @NotNull StubOutputStream dataStream) throws IOException {
    if (stub == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "stub", "com/ansorgit/plugins/bash/lang/psi/stubs/elements/BashSimpleCommandElementType", "serialize" }));  if (dataStream == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "dataStream", "com/ansorgit/plugins/bash/lang/psi/stubs/elements/BashSimpleCommandElementType", "serialize" }));  dataStream.writeName(stub.getBashCommandName());
    dataStream.writeBoolean(stub.isInternalCommand(false));
    dataStream.writeBoolean(stub.isInternalCommand(true));
    dataStream.writeBoolean(stub.isGenericCommand());
  }
  
  @NotNull
  public BashCommandStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    if (dataStream == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "dataStream", "com/ansorgit/plugins/bash/lang/psi/stubs/elements/BashSimpleCommandElementType", "deserialize" }));  StringRef bashCommandFilename = dataStream.readName();
    boolean internalCommandBash3 = dataStream.readBoolean();
    boolean internalCommandBash4 = dataStream.readBoolean();
    boolean genericCommand = dataStream.readBoolean();
    
    if (new BashCommandStubImpl(parentStub, StringRef.toString(bashCommandFilename), (IStubElementType)this, internalCommandBash3, internalCommandBash4, genericCommand) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/stubs/elements/BashSimpleCommandElementType", "deserialize" }));  return (BashCommandStub)new BashCommandStubImpl(parentStub, StringRef.toString(bashCommandFilename), (IStubElementType)this, internalCommandBash3, internalCommandBash4, genericCommand);
  }
  
  public BashCommand createPsi(@NotNull BashCommandStub stub) {
    if (stub == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "stub", "com/ansorgit/plugins/bash/lang/psi/stubs/elements/BashSimpleCommandElementType", "createPsi" }));  return (BashCommand)new BashSimpleCommandImpl(stub, BashElementTypes.SIMPLE_COMMAND_ELEMENT, "simple command");
  }
  
  public BashCommandStub createStub(@NotNull BashCommand psi, StubElement parentStub) {
    if (psi == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "psi", "com/ansorgit/plugins/bash/lang/psi/stubs/elements/BashSimpleCommandElementType", "createStub" }));  String filename = null;
    
    String commandName = psi.getReferencedCommandName();
    if (commandName != null) {
      filename = PathUtilRt.getFileName(commandName);
    }
    
    return (BashCommandStub)new BashCommandStubImpl(parentStub, filename, BashElementTypes.SIMPLE_COMMAND_ELEMENT, psi.isInternalCommand(false), psi.isInternalCommand(true), psi.isGenericCommand());
  }

  
  public void indexStub(@NotNull BashCommandStub stub, @NotNull IndexSink sink) {
    if (stub == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "stub", "com/ansorgit/plugins/bash/lang/psi/stubs/elements/BashSimpleCommandElementType", "indexStub" }));  if (sink == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "sink", "com/ansorgit/plugins/bash/lang/psi/stubs/elements/BashSimpleCommandElementType", "indexStub" }));  String filename = stub.getBashCommandName();
    if (filename != null)
      sink.occurrence(BashCommandNameIndex.KEY, filename); 
  }
}
