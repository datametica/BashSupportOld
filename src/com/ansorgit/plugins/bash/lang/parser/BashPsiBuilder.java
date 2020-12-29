package com.ansorgit.plugins.bash.lang.parser;

import com.ansorgit.plugins.bash.lang.BashVersion;
import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.WhitespacesAndCommentsBinder;
import com.intellij.lang.impl.PsiBuilderAdapter;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.UserDataHolder;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.containers.Stack;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;




















public final class BashPsiBuilder
  extends PsiBuilderAdapter
  implements PsiBuilder
{
  public static final Key<Boolean> IN_EVAL_MODE = Key.create("BASH_EVAL_PARSING");
  
  private static final Logger log = Logger.getInstance("#bash.BashPsiBuilder");
  
  private final Stack<Boolean> errorsStatusStack = new Stack();
  private final BashTokenRemapper tokenRemapper;
  private final BashVersion bashVersion;
  private final BackquoteData backquoteData = new BackquoteData();
  private final ParsingStateData parsingStateData = new ParsingStateData();
  private final Project project;
  
  public BashPsiBuilder(Project project, PsiBuilder wrappedBuilder, BashVersion bashVersion) {
    super(wrappedBuilder);
    
    this.project = project;
    this.bashVersion = bashVersion;
    this.tokenRemapper = new BashTokenRemapper(this);
    setTokenTypeRemapper(this.tokenRemapper);
  }
  
  public boolean isEvalMode() {
    return ((Boolean)IN_EVAL_MODE.get((UserDataHolder)this, Boolean.FALSE)).booleanValue();
  }




  
  @Nullable
  public String getTokenText(boolean enableWhitespace) {
    if (enableWhitespace && rawLookup(0) == BashTokenTypes.WHITESPACE) {
      int startOffset = rawTokenTypeStart(0);
      if (startOffset == -1)
      {
        startOffset = 0;
      }
      
      int length = rawTokenTypeStart(1) - startOffset;
      if (length == 1) {
        return "";
      }
      
      return StringUtils.repeat(" ", length);
    } 
    
    return getTokenText();
  }
  
  @Nullable
  public final IElementType getTokenType(boolean withWhitespace) {
    return withWhitespace ? rawLookup(0) : getTokenType();
  }
  
  @Nullable
  public IElementType getRemappingTokenType() {
    try {
      this.parsingStateData.enterSimpleCommand();
      
      return getTokenType();
    } finally {
      this.parsingStateData.leaveSimpleCommand();
    } 
  }





  
  public boolean readOptionalNewlines() {
    return readOptionalNewlines(-1);
  }








  
  public boolean readOptionalNewlines(int maxNewlines) {
    return readOptionalNewlines(maxNewlines, false);
  }







  
  public boolean readOptionalNewlines(int maxNewlines, boolean allowSurroundingWhitespace) {
    if (maxNewlines < 0) {
      maxNewlines = Integer.MAX_VALUE;
    }
    
    int readNewlines = 0;
    while (getTokenType(allowSurroundingWhitespace) == BashTokenTypes.LINE_FEED && readNewlines < maxNewlines) {
      advanceLexer();
      readNewlines++;
    } 
    
    return (readNewlines > 0);
  }
  
  public boolean isBash4() {
    return BashVersion.Bash_v4.equals(this.bashVersion);
  }
  
  public void remapShebangToComment() {
    this.tokenRemapper.enableShebangToCommentMapping();
  }





  
  public BackquoteData getBackquoteData() {
    return this.backquoteData;
  }
  
  public final ParsingStateData getParsingState() {
    return this.parsingStateData;
  }










  
  public void enterNewErrorLevel(boolean enableErrors) {
    this.errorsStatusStack.push(Boolean.valueOf(enableErrors));
  }



  
  public void leaveLastErrorLevel() {
    if (!this.errorsStatusStack.isEmpty()) {
      this.errorsStatusStack.pop();
    }
  }





  
  public void error(String message) {
    if (isErrorReportingEnabled()) {
      this.myDelegate.error(message);
    } else if (log.isDebugEnabled()) {
      log.debug("Supressed psi error: " + message);
    } 
  }
  
  public Project getProject() {
    return this.project;
  }









  
  @NotNull
  public PsiBuilder.Marker mark() {
    if (new BashPsiMarker(this, this.myDelegate.mark()) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/parser/BashPsiBuilder", "mark" }));  return new BashPsiMarker(this, this.myDelegate.mark());
  }





  
  boolean isErrorReportingEnabled() {
    return (this.errorsStatusStack.isEmpty() || ((Boolean)this.errorsStatusStack.peek()).booleanValue());
  }

  
  private static final class BashPsiMarker
    implements PsiBuilder.Marker
  {
    BashPsiBuilder psiBuilder;
    
    PsiBuilder.Marker original;

    
    public BashPsiMarker(BashPsiBuilder psiBuilder, PsiBuilder.Marker original) {
      this.psiBuilder = psiBuilder;
      this.original = original;
    }



    
    public void doneBefore(@NotNull IElementType type, @NotNull PsiBuilder.Marker beforeCandidate) {
      if (type == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "type", "com/ansorgit/plugins/bash/lang/parser/BashPsiBuilder$BashPsiMarker", "doneBefore" }));  if (beforeCandidate == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "beforeCandidate", "com/ansorgit/plugins/bash/lang/parser/BashPsiBuilder$BashPsiMarker", "doneBefore" }));  PsiBuilder.Marker before = (beforeCandidate instanceof BashPsiMarker) ? ((BashPsiMarker)beforeCandidate).original : beforeCandidate;
      
      this.original.doneBefore(type, before);
    }

    
    public void error(String errorMessage) {
      if (this.psiBuilder.isErrorReportingEnabled()) {
        this.original.error(errorMessage);
      } else {
        drop();
        
        if (BashPsiBuilder.log.isDebugEnabled()) {
          BashPsiBuilder.log.debug("Marker: suppressed error " + errorMessage);
        }
      } 
    }

    
    public void drop() {
      this.original.drop();
    }


    
    public void done(@NotNull IElementType type) {
      if (type == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "type", "com/ansorgit/plugins/bash/lang/parser/BashPsiBuilder$BashPsiMarker", "done" }));  this.original.done(type);
    }

    
    public void doneBefore(@NotNull IElementType type, @NotNull PsiBuilder.Marker before, String errorMessage) {
      if (type == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "type", "com/ansorgit/plugins/bash/lang/parser/BashPsiBuilder$BashPsiMarker", "doneBefore" }));  if (before == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "before", "com/ansorgit/plugins/bash/lang/parser/BashPsiBuilder$BashPsiMarker", "doneBefore" }));  this.original.doneBefore(type, before, errorMessage);
    }

    
    public void errorBefore(String message, @NotNull PsiBuilder.Marker marker) {
      if (marker == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "marker", "com/ansorgit/plugins/bash/lang/parser/BashPsiBuilder$BashPsiMarker", "errorBefore" }));  this.original.errorBefore(message, marker);
    }

    
    public void rollbackTo() {
      this.original.rollbackTo();
    }
    
    public void clean() {
      this.original = null;
      this.psiBuilder = null;
    }
    
    public void collapse(@NotNull IElementType iElementType) {
      if (iElementType == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "iElementType", "com/ansorgit/plugins/bash/lang/parser/BashPsiBuilder$BashPsiMarker", "collapse" }));  this.original.collapse(iElementType);
    }
    
    public PsiBuilder.Marker precede() {
      return this.original.precede();
    }
    
    public void setCustomEdgeTokenBinders(@Nullable WhitespacesAndCommentsBinder whitespacesAndCommentsBinder, @Nullable WhitespacesAndCommentsBinder whitespacesAndCommentsBinder1) {
      this.original.setCustomEdgeTokenBinders(whitespacesAndCommentsBinder, whitespacesAndCommentsBinder1);
    }
  }
}
