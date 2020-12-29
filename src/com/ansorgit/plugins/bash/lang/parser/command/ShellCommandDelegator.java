package com.ansorgit.plugins.bash.lang.parser.command;

import com.ansorgit.plugins.bash.lang.parser.BashPsiBuilder;
import com.ansorgit.plugins.bash.lang.parser.Parsing;
import com.ansorgit.plugins.bash.lang.parser.ParsingFunction;
import com.ansorgit.plugins.bash.lang.parser.misc.RedirectionParsing;




















public class ShellCommandDelegator
  implements ParsingFunction
{
  public boolean isValid(BashPsiBuilder builder) {
    return Parsing.shellCommand.isValid(builder);
  }
  
  public boolean parse(BashPsiBuilder builder) {
    boolean ok = Parsing.shellCommand.parse(builder);

    
    if (!ok) {
      return false;
    }
    RedirectionParsing.RedirectParseResult result = Parsing.redirection.parseListIfValid(builder, true);
    return (result == RedirectionParsing.RedirectParseResult.NO_REDIRECT || result == RedirectionParsing.RedirectParseResult.OK);
  }
}
