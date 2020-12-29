package com.ansorgit.plugins.bash.lang.parser.misc;

import com.ansorgit.plugins.bash.lang.parser.ParsingChain;
import com.ansorgit.plugins.bash.lang.parser.ParsingFunction;
import com.ansorgit.plugins.bash.lang.parser.ParsingTool;
import com.ansorgit.plugins.bash.lang.parser.shellCommand.ArithmeticParser;
import com.ansorgit.plugins.bash.lang.parser.shellCommand.BacktickParsingFunction;
import com.ansorgit.plugins.bash.lang.parser.shellCommand.CaseParsingFunction;
import com.ansorgit.plugins.bash.lang.parser.shellCommand.ConditionalCommandParsingFunction;
import com.ansorgit.plugins.bash.lang.parser.shellCommand.ConditionalExpressionParsingFunction;
import com.ansorgit.plugins.bash.lang.parser.shellCommand.ForLoopParsingFunction;
import com.ansorgit.plugins.bash.lang.parser.shellCommand.GroupCommandParsingFunction;
import com.ansorgit.plugins.bash.lang.parser.shellCommand.HistoryExpansionParsingFunction;
import com.ansorgit.plugins.bash.lang.parser.shellCommand.IfParsingFunction;
import com.ansorgit.plugins.bash.lang.parser.shellCommand.SelectParsingFunction;
import com.ansorgit.plugins.bash.lang.parser.shellCommand.SubshellParsingFunction;
import com.ansorgit.plugins.bash.lang.parser.shellCommand.TrapCommandParsingFunction;
import com.ansorgit.plugins.bash.lang.parser.shellCommand.UntilLoopParserFunction;
import com.ansorgit.plugins.bash.lang.parser.shellCommand.WhileLoopParserFunction;


























public class ShellCommandParsing
  extends ParsingChain
  implements ParsingTool
{
  public final CaseParsingFunction caseParser;
  public final ParsingFunction whileParser;
  public final ParsingFunction untilParser;
  public final ParsingFunction selectParser;
  
  public ShellCommandParsing() {
    this.caseParser = new CaseParsingFunction();
    
    this.whileParser = (ParsingFunction)new WhileLoopParserFunction();
    
    this.untilParser = (ParsingFunction)new UntilLoopParserFunction();
    
    this.selectParser = (ParsingFunction)new SelectParsingFunction();


    
    this.conditionalExpressionParser = (ParsingFunction)new ConditionalExpressionParsingFunction();
    
    this.conditionalCommandParser = (ParsingFunction)new ConditionalCommandParsingFunction();
    
    this.ifParser = (ParsingFunction)new IfParsingFunction();
    
    this.forLoopParser = (ParsingFunction)new ForLoopParsingFunction();
    
    this.subshellParser = (ParsingFunction)new SubshellParsingFunction();
    
    this.groupCommandParser = (ParsingFunction)new GroupCommandParsingFunction();
    
    this.backtickParser = (ParsingFunction)new BacktickParsingFunction();
    
    this.historyExpansionParser = (ParsingFunction)new HistoryExpansionParsingFunction();
    
    this.trapCommandParser = (ParsingFunction)new TrapCommandParsingFunction();
    addParsingFunction(this.forLoopParser);
    addParsingFunction((ParsingFunction)this.caseParser);
    addParsingFunction(this.whileParser);
    addParsingFunction(this.untilParser);
    addParsingFunction(this.selectParser);
    addParsingFunction(this.ifParser);
    addParsingFunction(this.subshellParser);
    addParsingFunction(this.groupCommandParser);
    addParsingFunction(this.trapCommandParser);
    addParsingFunction((ParsingFunction)arithmeticParser);
    addParsingFunction(this.conditionalCommandParser);
    addParsingFunction(this.conditionalExpressionParser);
  }
  
  public static final ArithmeticParser arithmeticParser = new ArithmeticParser();
  public final ParsingFunction conditionalExpressionParser;
  public final ParsingFunction conditionalCommandParser;
  public final ParsingFunction ifParser;
  public final ParsingFunction forLoopParser;
  public final ParsingFunction subshellParser;
  public final ParsingFunction groupCommandParser;
  public final ParsingFunction backtickParser;
  public final ParsingFunction historyExpansionParser;
  public final ParsingFunction trapCommandParser;
}
