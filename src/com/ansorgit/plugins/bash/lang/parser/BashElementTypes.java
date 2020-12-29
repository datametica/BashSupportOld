package com.ansorgit.plugins.bash.lang.parser;

import com.ansorgit.plugins.bash.lang.lexer.BashElementType;
import com.ansorgit.plugins.bash.lang.parser.eval.BashEvalElementType;
import com.ansorgit.plugins.bash.lang.psi.api.command.BashCommand;
import com.ansorgit.plugins.bash.lang.psi.api.command.BashIncludeCommand;
import com.ansorgit.plugins.bash.lang.psi.api.function.BashFunctionDef;
import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVar;
import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVarDef;
import com.ansorgit.plugins.bash.lang.psi.impl.BashBackquoteImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.BashGroupImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.BashLogicalBlockImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.expression.BashSubshellCommandImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.loops.BashForImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.loops.BashSelectImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.loops.BashUntilImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.loops.BashWhileImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.shell.BashCaseImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.shell.BashConditionalCommandImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.shell.BashExtendedConditionalCommandImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.shell.BashIfImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.shell.BashLetCommandImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.shell.BashTimeCommandImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.shell.BashTrapCommandImpl;
import com.ansorgit.plugins.bash.lang.psi.stubs.api.BashCommandStub;
import com.ansorgit.plugins.bash.lang.psi.stubs.api.BashFunctionDefStub;
import com.ansorgit.plugins.bash.lang.psi.stubs.api.BashIncludeCommandStub;
import com.ansorgit.plugins.bash.lang.psi.stubs.api.BashVarDefStub;
import com.ansorgit.plugins.bash.lang.psi.stubs.api.BashVarStub;
import com.ansorgit.plugins.bash.lang.psi.stubs.elements.BashFunctionDefElementType;
import com.ansorgit.plugins.bash.lang.psi.stubs.elements.BashIncludeCommandElementType;
import com.ansorgit.plugins.bash.lang.psi.stubs.elements.BashSimpleCommandElementType;
import com.ansorgit.plugins.bash.lang.psi.stubs.elements.BashStubFileElementType;
import com.ansorgit.plugins.bash.lang.psi.stubs.elements.BashVarDefElementType;
import com.ansorgit.plugins.bash.lang.psi.stubs.elements.BashVarElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.tree.ICompositeElementType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IStubFileElementType;
import com.intellij.util.ReflectionUtil;
import java.lang.reflect.Constructor;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;






public interface BashElementTypes
{
  public static final IStubFileElementType FILE = (IStubFileElementType)new BashStubFileElementType();
  public static final IElementType FILE_REFERENCE = (IElementType)new BashElementType("File reference");
  public static final IElementType SHEBANG_ELEMENT = (IElementType)new BashElementType("shebang element");
  public static final IElementType GROUP_ELEMENT = new BashCompositeElementType("group element", BashGroupImpl.class);
  
  public static final IElementType LOGICAL_BLOCK_ELEMENT = new BashCompositeElementType("logical block", BashLogicalBlockImpl.class);

  
  public static final IStubElementType<BashVarStub, BashVar> VAR_ELEMENT = (IStubElementType<BashVarStub, BashVar>)new BashVarElementType();
  
  public static final IElementType VAR_COMPOSED_VAR_ELEMENT = (IElementType)new BashElementType("composed variable, like subshell");
  public static final IElementType PARSED_WORD_ELEMENT = (IElementType)new BashElementType("combined word");
  public static final IElementType PARAM_EXPANSION_ELEMENT = (IElementType)new BashElementType("var substitution");
  public static final IElementType FUNCTION_DEF_NAME_ELEMENT = (IElementType)new BashElementType("named symbol");
  
  public static final IElementType REDIRECT_LIST_ELEMENT = (IElementType)new BashElementType("redirect list");
  public static final IElementType REDIRECT_ELEMENT = (IElementType)new BashElementType("redirect element");
  public static final IElementType PROCESS_SUBSTITUTION_ELEMENT = (IElementType)new BashElementType("process substitution element");
  
  public static final IStubElementType<BashCommandStub, BashCommand> SIMPLE_COMMAND_ELEMENT = (IStubElementType<BashCommandStub, BashCommand>)new BashSimpleCommandElementType();
  public static final IStubElementType<BashVarDefStub, BashVarDef> VAR_DEF_ELEMENT = (IStubElementType<BashVarDefStub, BashVarDef>)new BashVarDefElementType();
  public static final IElementType GENERIC_COMMAND_ELEMENT = (IElementType)new BashElementType("generic bash command");
  public static final IStubElementType<BashIncludeCommandStub, BashIncludeCommand> INCLUDE_COMMAND_ELEMENT = (IStubElementType<BashIncludeCommandStub, BashIncludeCommand>)new BashIncludeCommandElementType();
  
  public static final IElementType PIPELINE_COMMAND = (IElementType)new BashElementType("pipeline command");
  
  public static final IElementType COMPOSED_COMMAND = (IElementType)new BashElementType("composed command");
  
  public static final IElementType WHILE_COMMAND = new BashCompositeElementType("while loop", BashWhileImpl.class);
  public static final IElementType UNTIL_COMMAND = new BashCompositeElementType("until loop", BashUntilImpl.class);
  public static final IElementType FOR_COMMAND = new BashCompositeElementType("for shellcommand", BashForImpl.class);
  public static final IElementType SELECT_COMMAND = new BashCompositeElementType("select command", BashSelectImpl.class);
  public static final IElementType IF_COMMAND = new BashCompositeElementType("if shellcommand", BashIfImpl.class);
  public static final IElementType CONDITIONAL_COMMAND = new BashCompositeElementType("conditional shellcommand", BashConditionalCommandImpl.class);
  public static final IElementType EXTENDED_CONDITIONAL_COMMAND = new BashCompositeElementType("extended conditional shellcommand", BashExtendedConditionalCommandImpl.class);
  public static final IElementType SUBSHELL_COMMAND = new BashCompositeElementType("subshell shellcommand", BashSubshellCommandImpl.class);
  public static final IElementType BACKQUOTE_COMMAND = new BashCompositeElementType("backquote shellcommand", BashBackquoteImpl.class);
  public static final IElementType TRAP_COMMAND = new BashCompositeElementType("trap command", BashTrapCommandImpl.class);
  public static final IElementType LET_COMMAND = new BashCompositeElementType("let command", BashLetCommandImpl.class);
  public static final IStubElementType<BashFunctionDefStub, BashFunctionDef> FUNCTION_DEF_COMMAND = (IStubElementType<BashFunctionDefStub, BashFunctionDef>)new BashFunctionDefElementType();
  public static final IElementType GROUP_COMMAND = new BashCompositeElementType("group command", BashGroupImpl.class);
  
  public static final IElementType ARITHMETIC_COMMAND = (IElementType)new BashElementType("arithmetic command");
  public static final IElementType ARITH_ASSIGNMENT_CHAIN_ELEMENT = (IElementType)new BashElementType("arithmetic assignment chain");
  public static final IElementType ARITH_ASSIGNMENT_ELEMENT = (IElementType)new BashElementType("arithmetic assignment");
  public static final IElementType ARITH_VARIABLE_OPERATOR_ELEMENT = (IElementType)new BashElementType("arithmetic with variable operator");
  public static final IElementType ARITH_SUM_ELEMENT = (IElementType)new BashElementType("arithmetic sum");
  public static final IElementType ARITH_BIT_OR_ELEMENT = (IElementType)new BashElementType("arithmetic bitwise or");
  public static final IElementType ARITH_BIT_XOR_ELEMENT = (IElementType)new BashElementType("arithmetic bitwise xor");
  public static final IElementType ARITH_BIT_AND_ELEMENT = (IElementType)new BashElementType("arithmetic bitwise and");
  public static final IElementType ARITH_COMPUND_COMPARISION_ELEMENT = (IElementType)new BashElementType("arith compund comparision");
  public static final IElementType ARITH_EQUALITY_ELEMENT = (IElementType)new BashElementType("arithmetic equality");
  public static final IElementType ARITH_EXPONENT_ELEMENT = (IElementType)new BashElementType("arithmetic exponent");
  public static final IElementType ARITH_LOGIC_AND_ELEMENT = (IElementType)new BashElementType("arithmetic logic and");
  public static final IElementType ARITH_LOGIC_OR_ELEMENT = (IElementType)new BashElementType("arithmetic logic or");
  public static final IElementType ARITH_MULTIPLICACTION_ELEMENT = (IElementType)new BashElementType("arithmetic multiplication");
  public static final IElementType ARITH_NEGATION_ELEMENT = (IElementType)new BashElementType("arithmetic negation");
  public static final IElementType ARITH_POST_INCR_ELEMENT = (IElementType)new BashElementType("arithmetic post incr");
  public static final IElementType ARITH_PRE_INC_ELEMENT = (IElementType)new BashElementType("arithmetic pre incr");
  public static final IElementType ARITH_SHIFT_ELEMENT = (IElementType)new BashElementType("arithmetic shift");
  public static final IElementType ARITH_SIMPLE_ELEMENT = (IElementType)new BashElementType("arithmetic simple");
  public static final IElementType ARITH_TERNERAY_ELEMENT = (IElementType)new BashElementType("arithmetic ternary operator");
  public static final IElementType ARITH_PARENS_ELEMENT = (IElementType)new BashElementType("arithmetic parenthesis expr");
  public static final IElementType CASE_COMMAND = new BashCompositeElementType("case pattern", BashCaseImpl.class);
  public static final IElementType CASE_PATTERN_LIST_ELEMENT = (IElementType)new BashElementType("case pattern list");
  public static final IElementType CASE_PATTERN_ELEMENT = (IElementType)new BashElementType("case pattern");
  public static final IElementType TIME_COMMAND = new BashCompositeElementType("time with optional -p", BashTimeCommandImpl.class);
  
  public static final IElementType EXPANSION_ELEMENT = (IElementType)new BashElementType("single bash expansion");
  public static final IElementType VAR_ASSIGNMENT_LIST = (IElementType)new BashElementType("array assignment list");
  public static final IElementType STRING_ELEMENT = (IElementType)new BashElementType("string");
  public static final IElementType LET_LAZY_EXPRESSION = (IElementType)new BashElementType("lazy LET expression");
  public static final IElementType HEREDOC_START_ELEMENT = (IElementType)new BashElementType("heredoc start element");
  public static final IElementType HEREDOC_CONTENT_ELEMENT = (IElementType)new BashElementType("heredoc content element");
  public static final IElementType HEREDOC_END_ELEMENT = (IElementType)new BashElementType("heredoc end element");
  public static final IElementType HEREDOC_END_IGNORING_TABS_ELEMENT = (IElementType)new BashElementType("heredoc end element (ignoring tabs)");
  
  public static final IElementType EVAL_BLOCK = (IElementType)new BashEvalElementType();
  
  public static final IElementType BINARY_DATA = (IElementType)new BashElementType("binary data");
  
  public static class BashCompositeElementType extends IBashElementType implements ICompositeElementType {
    private final Constructor<? extends ASTNode> myConstructor;
    
    private BashCompositeElementType(@NonNls String debugName, Class<? extends ASTNode> nodeClass) {
      this(debugName, nodeClass, false);
    }
    
    private BashCompositeElementType(@NonNls String debugName, Class<? extends ASTNode> nodeClass, boolean leftBound) {
      super(debugName);
      this.myConstructor = ReflectionUtil.getDefaultConstructor(nodeClass);
    }

    
    @NotNull
    public ASTNode createCompositeNode() {
      if ((ASTNode)ReflectionUtil.createInstance(this.myConstructor, new Object[0]) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/parser/BashElementTypes$BashCompositeElementType", "createCompositeNode" }));  return (ASTNode)ReflectionUtil.createInstance(this.myConstructor, new Object[0]);
    }
  }
}
