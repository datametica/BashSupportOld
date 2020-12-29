package com.ansorgit.plugins.bash.lang.psi.impl.command;

import com.ansorgit.plugins.bash.lang.psi.stubs.api.BashCommandStub;
import com.intellij.lang.ASTNode;
import com.intellij.psi.StubBasedPsiElement;
import com.intellij.psi.stubs.IStubElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

















public class BashSimpleCommandImpl
  extends AbstractBashCommand<BashCommandStub>
  implements StubBasedPsiElement<BashCommandStub>
{
  public BashSimpleCommandImpl(ASTNode astNode) {
    super(astNode, "Simple command");
  }
  
  public BashSimpleCommandImpl(@NotNull BashCommandStub stub, @NotNull IStubElementType nodeType, @Nullable String name) {
    super(stub, nodeType, name);
  }
}
