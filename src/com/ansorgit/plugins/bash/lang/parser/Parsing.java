package com.ansorgit.plugins.bash.lang.parser;

import com.ansorgit.plugins.bash.lang.parser.command.CommandParsing;
import com.ansorgit.plugins.bash.lang.parser.command.PipelineParsing;
import com.ansorgit.plugins.bash.lang.parser.misc.BraceExpansionParsing;
import com.ansorgit.plugins.bash.lang.parser.misc.ListParsing;
import com.ansorgit.plugins.bash.lang.parser.misc.ProcessSubstitutionParsing;
import com.ansorgit.plugins.bash.lang.parser.misc.RedirectionParsing;
import com.ansorgit.plugins.bash.lang.parser.misc.ShellCommandParsing;
import com.ansorgit.plugins.bash.lang.parser.misc.WordParsing;
import com.ansorgit.plugins.bash.lang.parser.paramExpansion.ParameterExpansionParsing;
import com.ansorgit.plugins.bash.lang.parser.variable.VarParsing;

















public final class Parsing
{
  public static final FileParsing file = new FileParsing();
  public static final RedirectionParsing redirection = new RedirectionParsing();
  public static final CommandParsing command = new CommandParsing();
  public static final ShellCommandParsing shellCommand = new ShellCommandParsing();
  public static final ListParsing list = new ListParsing();
  public static final PipelineParsing pipeline = new PipelineParsing();
  public static final WordParsing word = new WordParsing();
  public static final VarParsing var = new VarParsing();
  public static final BraceExpansionParsing braceExpansionParsing = new BraceExpansionParsing();
  public static final ParameterExpansionParsing parameterExpansionParsing = new ParameterExpansionParsing();
  public static final ProcessSubstitutionParsing processSubstitutionParsing = new ProcessSubstitutionParsing();
}
