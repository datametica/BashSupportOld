package com.ansorgit.plugins.bash.editor.highlighting;

import com.ansorgit.plugins.bash.util.BashIcons;
import com.google.common.collect.Maps;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import com.intellij.openapi.util.io.StreamUtil;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import javax.swing.Icon;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;















public class BashColorsAndFontsPage
  implements ColorSettingsPage
{
  @NotNull
  public String getDisplayName() {
    if ("Bash" == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/highlighting/BashColorsAndFontsPage", "getDisplayName" }));  return "Bash";
  }
  
  @Nullable
  public Icon getIcon() {
    return BashIcons.BASH_FILE_ICON;
  }
  
  private static final AttributesDescriptor[] ATTRS = new AttributesDescriptor[] { new AttributesDescriptor("Binary data", BashSyntaxHighlighter.BINARY_DATA), new AttributesDescriptor("Line comment", BashSyntaxHighlighter.LINE_COMMENT), new AttributesDescriptor("Shebang (#!) comment", BashSyntaxHighlighter.SHEBANG_COMMENT), new AttributesDescriptor("Keyword", BashSyntaxHighlighter.KEYWORD), new AttributesDescriptor("Parenthesis", BashSyntaxHighlighter.PAREN), new AttributesDescriptor("Braces", BashSyntaxHighlighter.BRACE), new AttributesDescriptor("Brackets", BashSyntaxHighlighter.BRACKET), new AttributesDescriptor("String \"...\"", BashSyntaxHighlighter.STRING), new AttributesDescriptor("String '...'", BashSyntaxHighlighter.STRING2), new AttributesDescriptor("Number", BashSyntaxHighlighter.NUMBER), new AttributesDescriptor("Backquotes `...`", BashSyntaxHighlighter.BACKQUOTE), new AttributesDescriptor("Command redirection", BashSyntaxHighlighter.REDIRECTION), new AttributesDescriptor("Conditional operator", BashSyntaxHighlighter.CONDITIONAL), new AttributesDescriptor("Function definition", BashSyntaxHighlighter.FUNCTION_DEF_NAME), new AttributesDescriptor("Function call", BashSyntaxHighlighter.FUNCTION_CALL), new AttributesDescriptor("Bash internal command", BashSyntaxHighlighter.INTERNAL_COMMAND), new AttributesDescriptor("External command", BashSyntaxHighlighter.EXTERNAL_COMMAND), new AttributesDescriptor("Variable declaration, e.g. a=1", BashSyntaxHighlighter.VAR_DEF), new AttributesDescriptor("Simple variable use", BashSyntaxHighlighter.VAR_USE), new AttributesDescriptor("Use of built-in variable", BashSyntaxHighlighter.VAR_USE_BUILTIN), new AttributesDescriptor("Use of composed variable", BashSyntaxHighlighter.VAR_USE_COMPOSED), new AttributesDescriptor("Here-document", BashSyntaxHighlighter.HERE_DOC), new AttributesDescriptor("Here-document start marker", BashSyntaxHighlighter.HERE_DOC_START), new AttributesDescriptor("Here-document end marker", BashSyntaxHighlighter.HERE_DOC_END) };






































  
  @NotNull
  public AttributesDescriptor[] getAttributeDescriptors() {
    if (ATTRS == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/highlighting/BashColorsAndFontsPage", "getAttributeDescriptors" }));  return ATTRS;
  }
  
  @NotNull
  public ColorDescriptor[] getColorDescriptors() {
    if (ColorDescriptor.EMPTY_ARRAY == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/highlighting/BashColorsAndFontsPage", "getColorDescriptors" }));  return ColorDescriptor.EMPTY_ARRAY;
  }
  
  @NotNull
  public SyntaxHighlighter getHighlighter() {
    if (new BashSyntaxHighlighter() == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/highlighting/BashColorsAndFontsPage", "getHighlighter" }));  return (SyntaxHighlighter)new BashSyntaxHighlighter();
  }
  @NonNls
  @NotNull
  public String getDemoText() {
    String demoText;
    InputStream resource = getClass().getClassLoader().getResourceAsStream("/highlighterDemoText.sh");
    
    try {
      demoText = StreamUtil.readText(resource, "UTF-8");
    } catch (IOException e) {
      throw new RuntimeException("BashSupport could not load the syntax highlighter demo text.", e);
    } 
    
    if (demoText == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/editor/highlighting/BashColorsAndFontsPage", "getDemoText" }));  return demoText;
  }
  
  private static final Map<String, TextAttributesKey> tags = Maps.newHashMap();
  
  static {
    tags.put("keyword", BashSyntaxHighlighter.KEYWORD);
    
    tags.put("binary", BashSyntaxHighlighter.BINARY_DATA);
    
    tags.put("shebang", BashSyntaxHighlighter.SHEBANG_COMMENT);
    tags.put("lineComment", BashSyntaxHighlighter.LINE_COMMENT);
    tags.put("number", BashSyntaxHighlighter.NUMBER);
    
    tags.put("redirect", BashSyntaxHighlighter.REDIRECTION);
    
    tags.put("string", BashSyntaxHighlighter.STRING);
    tags.put("simpleString", BashSyntaxHighlighter.STRING2);
    
    tags.put("heredoc", BashSyntaxHighlighter.HERE_DOC);
    tags.put("heredocStart", BashSyntaxHighlighter.HERE_DOC_START);
    tags.put("heredocEnd", BashSyntaxHighlighter.HERE_DOC_END);
    
    tags.put("backquote", BashSyntaxHighlighter.BACKQUOTE);
    
    tags.put("internalCmd", BashSyntaxHighlighter.INTERNAL_COMMAND);
    tags.put("externalCmd", BashSyntaxHighlighter.EXTERNAL_COMMAND);
    
    tags.put("functionDef", BashSyntaxHighlighter.FUNCTION_DEF_NAME);
    tags.put("functionCall", BashSyntaxHighlighter.FUNCTION_CALL);
    
    tags.put("varDef", BashSyntaxHighlighter.VAR_DEF);
    tags.put("varUse", BashSyntaxHighlighter.VAR_USE);
    tags.put("internalVar", BashSyntaxHighlighter.VAR_USE_BUILTIN);
    tags.put("composedVar", BashSyntaxHighlighter.VAR_USE_COMPOSED);


    
    tags.put("dummy", TextAttributesKey.find("dummy"));
  }
  
  @Nullable
  public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
    return tags;
  }
}
