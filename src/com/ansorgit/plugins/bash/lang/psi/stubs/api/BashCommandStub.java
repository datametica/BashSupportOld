package com.ansorgit.plugins.bash.lang.psi.stubs.api;

import com.ansorgit.plugins.bash.lang.psi.api.command.BashCommand;

public interface BashCommandStub extends BashCommandStubBase<BashCommand> {
  String getBashCommandName();
}
