package com.ansorgit.plugins.bash.lang.psi.stubs.api;

import com.ansorgit.plugins.bash.lang.psi.api.command.BashIncludeCommand;

public interface BashIncludeCommandStub extends BashCommandStubBase<BashIncludeCommand> {
  String getIncludedFilename();
  
  String getIncluderFilePath();
}
