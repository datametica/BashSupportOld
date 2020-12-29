package com.ansorgit.plugins.bash.jetbrains;

import com.intellij.execution.filters.Filter;
import com.intellij.execution.filters.HyperlinkInfo;
import com.intellij.execution.filters.InvalidExpressionException;
import com.intellij.execution.filters.OpenFileHyperlinkInfo;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import java.io.File;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;



















public class ExtendedRegexFilter
  implements Filter
{
  @NonNls
  public static final String FILE_PATH_MACROS = "$FILE_PATH$";
  @NonNls
  public static final String LINE_MACROS = "$LINE$";
  @NonNls
  public static final String COLUMN_MACROS = "$COLUMN$";
  @NonNls
  private static final String FILE_PATH_REGEXP = "((?:\\p{Alpha}\\:)?[0-9 a-z_A-Z\\-\\\\./+#_$]+)";
  @NonNls
  private static final String NUMBER_REGEXP = "([0-9]+)";
  @NonNls
  private static final String FILE_STR = "file";
  @NonNls
  private static final String LINE_STR = "line";
  @NonNls
  private static final String COLUMN_STR = "column";
  private final int myFileRegister;
  private final int myLineRegister;
  private final int myColumnRegister;
  private final Pattern myPattern;
  private final Project myProject;
  
  public ExtendedRegexFilter(Project project, @NonNls String expression) {
    this.myProject = project;
    validate(expression);
    
    if (expression == null || expression.trim().isEmpty()) {
      throw new InvalidExpressionException("expression == null or empty");
    }
    
    int filePathIndex = expression.indexOf("$FILE_PATH$");
    int lineIndex = expression.indexOf("$LINE$");
    int columnIndex = expression.indexOf("$COLUMN$");
    
    if (filePathIndex == -1) {
      throw new InvalidExpressionException("Expression must contain $FILE_PATH$ macros.");
    }
    
    TreeMap<Integer, String> map = new TreeMap<>();
    
    map.put(new Integer(filePathIndex), "file");
    
    expression = StringUtil.replace(expression, "$FILE_PATH$", "((?:\\p{Alpha}\\:)?[0-9 a-z_A-Z\\-\\\\./+#_$]+)");
    
    if (lineIndex != -1) {
      expression = StringUtil.replace(expression, "$LINE$", "([0-9]+)");
      map.put(new Integer(lineIndex), "line");
    } 
    
    if (columnIndex != -1) {
      expression = StringUtil.replace(expression, "$COLUMN$", "([0-9]+)");
      map.put(new Integer(columnIndex), "column");
    } 

    
    int count = 0;
    for (Integer integer : map.keySet()) {
      count++;
      String s = map.get(integer);
      
      if ("file".equals(s)) {
        filePathIndex = count; continue;
      } 
      if ("line".equals(s)) {
        lineIndex = count; continue;
      } 
      if ("column".equals(s)) {
        columnIndex = count;
      }
    } 
    
    this.myFileRegister = filePathIndex;
    this.myLineRegister = lineIndex;
    this.myColumnRegister = columnIndex;
    this.myPattern = Pattern.compile(expression, 8);
  }

  
  public static void validate(String expression) {
    if (expression == null || expression.trim().isEmpty()) {
      throw new InvalidExpressionException("expression == null or empty");
    }
    
    expression = substituteMacrosWithRegexps(expression);
    Pattern.compile(expression, 8);
  }
  
  private static String substituteMacrosWithRegexps(String expression) {
    int filePathIndex = expression.indexOf("$FILE_PATH$");
    int lineIndex = expression.indexOf("$LINE$");
    int columnIndex = expression.indexOf("$COLUMN$");
    
    if (filePathIndex == -1) {
      throw new InvalidExpressionException("Expression must contain $FILE_PATH$ macros.");
    }
    
    expression = StringUtil.replace(expression, "$FILE_PATH$", "((?:\\p{Alpha}\\:)?[0-9 a-z_A-Z\\-\\\\./+#_$]+)");
    
    if (lineIndex != -1) {
      expression = StringUtil.replace(expression, "$LINE$", "([0-9]+)");
    }
    
    if (columnIndex != -1) {
      expression = StringUtil.replace(expression, "$COLUMN$", "([0-9]+)");
    }
    return expression;
  }

  
  public Filter.Result applyFilter(String line, int entireLength) {
    Matcher matcher = this.myPattern.matcher(line);
    if (!matcher.find()) {
      return null;
    }
    
    String filePath = matcher.group(this.myFileRegister);
    if (filePath == null) {
      return null;
    }
    
    String lineNumber = "0";
    if (this.myLineRegister != -1) {
      lineNumber = matcher.group(this.myLineRegister);
    }
    
    String columnNumber = "0";
    if (this.myColumnRegister != -1) {
      columnNumber = matcher.group(this.myColumnRegister);
    }
    
    int line1 = 0;
    int column = 0;
    try {
      line1 = Integer.parseInt(lineNumber);
      column = Integer.parseInt(columnNumber);
    } catch (NumberFormatException numberFormatException) {}


    
    if (line1 > 0) line1--; 
    if (column > 0) column--;
    
    int highlightStartOffset = entireLength - line.length() + matcher.start(this.myFileRegister);
    int highlightEndOffset = highlightStartOffset + filePath.length();
    HyperlinkInfo info = createOpenFileHyperlink(filePath, line1, column);
    return new Filter.Result(highlightStartOffset, highlightEndOffset, info);
  }
  
  @Nullable
  protected HyperlinkInfo createOpenFileHyperlink(String fileName, int line, int column) {
    fileName = fileName.replace(File.separatorChar, '/');
    VirtualFile file = LocalFileSystem.getInstance().findFileByPath(fileName);
    return (file != null) ? (HyperlinkInfo)new OpenFileHyperlinkInfo(this.myProject, file, line, column) : null;
  }
  
  public static String[] getMacrosName() {
    return new String[] { "$FILE_PATH$", "$LINE$", "$COLUMN$" };
  }
}
