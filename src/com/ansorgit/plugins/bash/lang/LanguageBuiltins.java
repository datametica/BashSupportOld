package com.ansorgit.plugins.bash.lang;

import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;




















public final class LanguageBuiltins
{
  public static final Collection<String> bourneShellVars = Sets.newHashSet((Object[])new String[] { "CDPATH", "HOME", "IFS", "MAIL", "MAILPATH", "OPTARG", "OPTIND", "PATH", "PS1", "PS2" });




  
  public static final Collection<String> bashShellParamReferences = Sets.newHashSet((Object[])new String[] { "*", "@", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19" });





  
  public static final Collection<String> bashShellVars = Sets.newHashSet((Object[])new String[] { "$", "#", "*", "@", "-", "!", "_", "?", "BASH", "BASHOPTS", "BASHPID", "BASH_ALIASES", "BASH_ARGC", "BASH_ARGV", "BASH_CMDS", "BASH_COMMAND", "BASH_ENV", "BASH_EXECUTION_STRING", "BASH_LINENO", "BASH_REMATCH", "BASH_SOURCE", "BASH_SUBSHELL", "BASH_VERSINFO", "BASH_VERSION", "COLUMNS", "COMP_CWORD", "COMP_LINE", "COMP_POINT", "COMP_TYPE", "COMP_KEY", "COMP_WORDBREAKS", "COMP_WORDS", "COMPREPLY", "DIRSTACK", "EMACS", "EUID", "FCEDIT", "FIGNORE", "FUNCNAME", "GLOBIGNORE", "GROUPS", "histchars", "HISTCMD", "HISTCONTROL", "HISTFILE", "HISTFILESIZE", "HISTIGNORE", "HISTIGNORE", "HISTSIZE", "HISTTIMEFORMAT", "HOSTFILE", "HOSTNAME", "HOSTTYPE", "IGNOREEOF", "INPUTRC", "LANG", "LC_ALL", "LC_COLLATE", "LC_CTYPE", "LC_MESSAGES", "LC_NUMERIC", "LINENO", "LINES", "MACHTYPE", "MAILCHECK", "OLDPWD", "OPTERR", "OSTYPE", "PIPESTATUS", "POSIXLY_CORRECT", "PPID", "PROMPT_COMMAND", "PROMPT_DIRTRIM", "PS3", "PS4", "PWD", "RANDOM", "REPLY", "SECONDS", "SHELL", "SHELLOPTS", "SHLVL", "TIMEFORMAT", "TMOUT", "TMPDIR", "UID" });



















  
  public static final Collection<String> bashShellVars_v4 = Sets.newHashSet((Object[])new String[] { "BASHPID", "PROMPT_DIRTRIM" });

  
  public static final Collection<String> readonlyShellVars = Sets.newHashSet((Object[])new String[] { "BASH", "BASHOPTS", "BASHPID", "BASH_SUBSHELL", "BASH_VERSINFO", "BASH_VERSION", "EUID", "HOSTNAME", "HOSTTYPE", "OLDPWD", "PPID", "PWD", "UID" });


  
  public static final Collection<String> commands = Sets.newHashSet((Object[])new String[] { ".", ":", "alias", "bg", "bind", "break", "builtin", "cd", "caller", "command", "compgen", "complete", "continue", "declare", "typeset", "dirs", "disown", "echo", "enable", "eval", "exec", "exit", "export", "fc", "fg", "getopts", "hash", "help", "history", "jobs", "kill", "let", "local", "logout", "popd", "printf", "pushd", "pwd", "read", "readonly", "return", "set", "shift", "shopt", "unset", "source", "suspend", "test", "times", "trap", "type", "ulimit", "umask", "unalias", "wait" });







  
  public static final Collection<String> commands_v4 = Sets.newHashSet((Object[])new String[] { "coproc", "mapfile", "readarray" });

  
  public static final Collection<String> varDefCommands = Sets.newHashSet((Object[])new String[] { "export", "read", "declare", "readonly", "typeset", "getopts", "mapfile", "readarray", "printf" });

  
  public static final Collection<String> localVarDefCommands = Collections.singleton("local");

  
  public static final Collection<String> readonlyVarDefCommands = Collections.singleton("readonly");

  
  public static final Collection<String> completionKeywords = Sets.newLinkedHashSet(Arrays.asList(new String[] { "if", "then", "elif", "else", "fi", "while", "do", "done", "until", "case", "in", "esac", "true", "false" }));



  
  static {
    bashShellVars.addAll(bashShellParamReferences);

    
    readonlyShellVars.addAll(bashShellParamReferences);
  }



  
  public static boolean isInternalCommand(String commandName, boolean bash4) {
    return ((bash4 && commands_v4.contains(commandName)) || commands.contains(commandName));
  }
}
