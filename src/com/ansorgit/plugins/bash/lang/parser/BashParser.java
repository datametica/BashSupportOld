package com.ansorgit.plugins.bash.lang.parser;

import com.ansorgit.plugins.bash.lang.BashVersion;
import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;


























public class BashParser
  implements PsiParser
{
  private static final Logger log = Logger.getInstance("BashParser");
  private static final String debugKey = "bashsupport.debug";
  private static final boolean debugMode = ("true".equals(System.getProperty("bashsupport.debug")) || "true".equals(System.getenv("bashsupport.debug")));
  private final Project project;
  private final BashVersion version;
  
  public BashParser(Project project, BashVersion version) {
    this.project = project;
    this.version = version;
  }
  
  @NotNull
  public ASTNode parse(@NotNull IElementType root, @NotNull PsiBuilder psiBuilder) {
    if (root == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "root", "com/ansorgit/plugins/bash/lang/parser/BashParser", "parse" }));  if (psiBuilder == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "psiBuilder", "com/ansorgit/plugins/bash/lang/parser/BashParser", "parse" }));  BashPsiBuilder builder = new BashPsiBuilder(this.project, psiBuilder, this.version);
    builder.putUserData(BashPsiBuilder.IN_EVAL_MODE, Boolean.valueOf(root instanceof com.ansorgit.plugins.bash.lang.parser.eval.BashEvalElementType));
    
    if (debugMode) {
      log.info("Enabling parser's debug mode...");
    }
    
    builder.setDebugMode(debugMode);
    
    PsiBuilder.Marker rootMarker = builder.mark();
    Parsing.file.parseFile(builder);
    rootMarker.done(root);
    
    if (builder.getTreeBuilt() == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/parser/BashParser", "parse" }));  return builder.getTreeBuilt();
  }
}
