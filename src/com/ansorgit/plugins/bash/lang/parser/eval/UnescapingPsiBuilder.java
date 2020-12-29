package com.ansorgit.plugins.bash.lang.parser.eval;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ITokenTypeRemapper;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.impl.DelegateMarker;
import com.intellij.lang.impl.PsiBuilderAdapter;
import com.intellij.lang.impl.PsiBuilderImpl;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;




























public class UnescapingPsiBuilder
  extends PsiBuilderAdapter
{
  private static final Logger LOG = Logger.getInstance(UnescapingPsiBuilder.class);
  
  private final PsiBuilderImpl myBuilderDelegate;
  
  private final Lexer myLexer;
  
  private final TextPreprocessor textProcessor;
  
  private final CharSequence processedText;
  
  private List<MyShiftedToken> myShrunkSequence;
  
  private int myShrunkSequenceSize;
  private CharSequence myShrunkCharSequence;
  private int myLexPosition;
  private IElementType currentRemapped;
  private ITokenTypeRemapper remapper;
  
  public UnescapingPsiBuilder(@NotNull Project project, @NotNull ParserDefinition parserDefinition, @NotNull Lexer lexer, @NotNull ASTNode chameleon, @NotNull CharSequence originalText, @NotNull CharSequence processedText, @NotNull TextPreprocessor textProcessor) {
    this(new PsiBuilderImpl(project, parserDefinition, lexer, chameleon, originalText), textProcessor, processedText);
  }
  
  private UnescapingPsiBuilder(PsiBuilderImpl builder, TextPreprocessor textProcessor, CharSequence processedText) {
    super((PsiBuilder)builder);
    this.textProcessor = textProcessor;
    this.processedText = processedText;
    
    LOG.assertTrue(this.myDelegate instanceof PsiBuilderImpl);
    this.myBuilderDelegate = (PsiBuilderImpl)this.myDelegate;
    
    this.myLexer = this.myBuilderDelegate.getLexer();
    
    initShrunkSequence();
  }

  
  public CharSequence getOriginalText() {
    return this.myShrunkCharSequence;
  }

  
  public void advanceLexer() {
    this.myLexPosition++;
    skipWhitespace();
    
    synchronizePositions(false);
  }




  
  private void synchronizePositions(boolean exact) {
    PsiBuilder delegate = getDelegate();
    
    if (this.myLexPosition >= this.myShrunkSequenceSize || delegate.eof()) {
      this.myLexPosition = this.myShrunkSequenceSize;
      while (!delegate.eof()) {
        delegate.advanceLexer();
      }
      
      return;
    } 
    if (delegate.getCurrentOffset() > ((MyShiftedToken)this.myShrunkSequence.get(this.myLexPosition)).realStart) {
      LOG.debug("delegate is ahead of my builder!");
      
      return;
    } 
    int keepUpPosition = getKeepUpPosition(exact);
    
    while (!delegate.eof()) {
      int delegatePosition = delegate.getCurrentOffset();
      
      if (delegatePosition < keepUpPosition) {
        delegate.advanceLexer();
      }
    } 
  }


  
  private int getKeepUpPosition(boolean exact) {
    if (exact) {
      return ((MyShiftedToken)this.myShrunkSequence.get(this.myLexPosition)).realStart;
    }
    
    int lexPosition = this.myLexPosition;
    while (lexPosition > 0 && (((MyShiftedToken)this.myShrunkSequence.get(lexPosition - 1)).shrunkStart == ((MyShiftedToken)this.myShrunkSequence.get(lexPosition)).shrunkStart || 
      isWhiteSpaceOnPos(lexPosition - 1))) {
      lexPosition--;
    }
    if (lexPosition == 0) {
      return ((MyShiftedToken)this.myShrunkSequence.get(lexPosition)).realStart;
    }
    return ((MyShiftedToken)this.myShrunkSequence.get(lexPosition - 1)).realStart + 1;
  }

  
  public IElementType lookAhead(int steps) {
    if (eof()) {
      return null;
    }
    int cur = this.myLexPosition;
    
    while (steps > 0) {
      cur++;
      while (cur < this.myShrunkSequenceSize && isWhiteSpaceOnPos(cur)) {
        cur++;
      }
      
      steps--;
    } 
    
    return (cur < this.myShrunkSequenceSize) ? ((MyShiftedToken)this.myShrunkSequence.get(cur)).elementType : null;
  }

  
  public IElementType rawLookup(int steps) {
    int cur = this.myLexPosition + steps;
    return (cur >= 0 && cur < this.myShrunkSequenceSize) ? ((MyShiftedToken)this.myShrunkSequence.get(cur)).elementType : null;
  }

  
  public int rawTokenTypeStart(int steps) {
    int cur = this.myLexPosition + steps;
    if (cur < 0) {
      return -1;
    }
    if (cur >= this.myShrunkSequenceSize) {
      return getOriginalText().length();
    }
    return ((MyShiftedToken)this.myShrunkSequence.get(cur)).shrunkStart;
  }

  
  public int rawTokenIndex() {
    return this.myLexPosition;
  }

  
  public int getCurrentOffset() {
    return (this.myLexPosition < this.myShrunkSequenceSize) ? ((MyShiftedToken)this.myShrunkSequence.get(this.myLexPosition)).shrunkStart : this.myShrunkCharSequence.length();
  }

  
  public void remapCurrentToken(IElementType type) {
    this.currentRemapped = type;
  }

  
  public void setTokenTypeRemapper(ITokenTypeRemapper remapper) {
    this.remapper = remapper;
    super.setTokenTypeRemapper(remapper);
  }

  
  @Nullable
  public IElementType getTokenType() {
    if (allIsEmpty()) {
      return TokenType.DUMMY_HOLDER;
    }
    
    skipWhitespace();
    
    if (this.currentRemapped != null) {
      IElementType iElementType = this.currentRemapped;
      this.currentRemapped = null;
      
      return iElementType;
    } 
    
    IElementType result = (this.myLexPosition < this.myShrunkSequenceSize) ? ((MyShiftedToken)this.myShrunkSequence.get(this.myLexPosition)).elementType : null;
    
    if (this.remapper != null && result != null) {
      String tokenText = getTokenText();
      int offset = getCurrentOffset();
      int end = offset + ((tokenText != null) ? tokenText.length() : 0);
      return this.remapper.filter(result, offset, end, tokenText);
    } 
    
    return result;
  }

  
  @Nullable
  public String getTokenText() {
    if (allIsEmpty()) {
      return getDelegate().getOriginalText().toString();
    }
    
    skipWhitespace();
    
    if (this.myLexPosition >= this.myShrunkSequenceSize) {
      return null;
    }
    
    MyShiftedToken token = this.myShrunkSequence.get(this.myLexPosition);
    return this.myShrunkCharSequence.subSequence(token.shrunkStart, token.shrunkEnd).toString();
  }

  
  public boolean eof() {
    boolean isEof = (this.myLexPosition >= this.myShrunkSequenceSize);
    if (!isEof) {
      return false;
    }
    
    synchronizePositions(true);
    return true;
  }


  
  @NotNull
  public PsiBuilder.Marker mark() {
    if (this.myLexPosition != 0) {
      synchronizePositions(true);
    }
    
    PsiBuilder.Marker mark = super.mark();
    if (new MyMarker(mark, this.myLexPosition) == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/parser/eval/UnescapingPsiBuilder", "mark" }));  return (PsiBuilder.Marker)new MyMarker(mark, this.myLexPosition);
  }
  
  private boolean allIsEmpty() {
    return (this.myShrunkSequenceSize == 0 && getDelegate().getOriginalText().length() != 0);
  }
  
  private void skipWhitespace() {
    while (this.myLexPosition < this.myShrunkSequenceSize && isWhiteSpaceOnPos(this.myLexPosition)) {
      this.myLexPosition++;
    }
  }
  
  private boolean isWhiteSpaceOnPos(int pos) {
    return this.myBuilderDelegate.whitespaceOrComment(((MyShiftedToken)this.myShrunkSequence.get(pos)).elementType);
  }
  
  protected void initShrunkSequence() {
    initTokenListAndCharSequence(this.myLexer);
    this.myLexPosition = 0;
  }
  
  private void initTokenListAndCharSequence(Lexer lexer) {
    lexer.start(this.processedText);
    
    this.myShrunkSequence = new ArrayList<>(512);
    StringBuilder charSequenceBuilder = new StringBuilder();
    
    int realPos = 0;
    int shrunkPos = 0;
    while (lexer.getTokenType() != null) {
      IElementType tokenType = lexer.getTokenType();
      String tokenText = lexer.getTokenText();
      
      int tokenStart = lexer.getTokenStart();
      int tokenEnd = lexer.getTokenEnd();
      int realLength = tokenEnd - tokenStart;
      
      int delta = this.textProcessor.getContentRange().getStartOffset();
      int originalStart = this.textProcessor.getOffsetInHost(tokenStart - delta);
      int originalEnd = this.textProcessor.getOffsetInHost(tokenEnd - delta);
      
      if (this.textProcessor.containsRange(tokenStart, tokenEnd) && originalStart != -1 && originalEnd != -1) {
        realLength = originalEnd - originalStart;
        int masqueLength = tokenEnd - tokenStart;
        
        this.myShrunkSequence.add(new MyShiftedToken(tokenType, realPos, realPos + realLength, shrunkPos, shrunkPos + masqueLength, tokenText));

        
        charSequenceBuilder.append(tokenText);
        
        shrunkPos += masqueLength;
      } else {
        this.myShrunkSequence.add(new MyShiftedToken(tokenType, realPos, realPos + realLength, shrunkPos, shrunkPos + realLength, tokenText));

        
        charSequenceBuilder.append(tokenText);
        
        shrunkPos += realLength;
      } 
      
      realPos += realLength;
      
      lexer.advance();
    } 
    
    this.myShrunkCharSequence = charSequenceBuilder.toString();
    this.myShrunkSequenceSize = this.myShrunkSequence.size();
  }

  
  private void logPos() {
    StringBuilder sb = new StringBuilder();
    sb.append("\nmyLexPosition=" + this.myLexPosition + "/" + this.myShrunkSequenceSize);
    if (this.myLexPosition < this.myShrunkSequenceSize) {
      MyShiftedToken token = this.myShrunkSequence.get(this.myLexPosition);
      sb.append("\nshrunk:" + token.shrunkStart + "," + token.shrunkEnd);
      sb.append("\nreal:" + token.realStart + "," + token.realEnd);
      sb.append("\nTT:" + getTokenText());
    } 
    sb.append("\ndelegate:");
    sb.append("eof=" + this.myDelegate.eof());
    if (!this.myDelegate.eof()) {
      
      sb.append("\nposition:" + this.myDelegate.getCurrentOffset() + "," + (this.myDelegate.getCurrentOffset() + this.myDelegate.getTokenText().length()));
      sb.append("\nTT:" + this.myDelegate.getTokenText());
    } 
    LOG.info(sb.toString());
  }

  
  private static class MyShiftedToken
  {
    public final IElementType elementType;
    
    public final int realStart;
    public final int realEnd;
    public final int shrunkStart;
    public final int shrunkEnd;
    private final String tokenText;
    
    public MyShiftedToken(IElementType elementType, int realStart, int realEnd, int shrunkStart, int shrunkEnd, String tokenText) {
      this.elementType = elementType;
      this.realStart = realStart;
      this.realEnd = realEnd;
      this.shrunkStart = shrunkStart;
      this.shrunkEnd = shrunkEnd;
      this.tokenText = tokenText;
    }

    
    public String toString() {
      return "MSTk: [" + this.realStart + ", " + this.realEnd + "] -> [" + this.shrunkStart + ", " + this.shrunkEnd + "]: " + this.elementType.toString() + " | " + this.tokenText;
    }
  }
  
  private class MyMarker extends DelegateMarker {
    private final int myBuilderPosition;
    
    public MyMarker(PsiBuilder.Marker delegate, int builderPosition) {
      super(delegate);
      
      this.myBuilderPosition = builderPosition;
    }

    
    public void rollbackTo() {
      super.rollbackTo();
      UnescapingPsiBuilder.this.myLexPosition = this.myBuilderPosition;
    }

    
    public void doneBefore(@NotNull IElementType type, @NotNull PsiBuilder.Marker before) {
      if (type == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "type", "com/ansorgit/plugins/bash/lang/parser/eval/UnescapingPsiBuilder$MyMarker", "doneBefore" }));  if (before == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "before", "com/ansorgit/plugins/bash/lang/parser/eval/UnescapingPsiBuilder$MyMarker", "doneBefore" }));  super.doneBefore(type, getDelegateOrThis(before));
    }

    
    public void doneBefore(@NotNull IElementType type, @NotNull PsiBuilder.Marker before, String errorMessage) {
      if (type == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "type", "com/ansorgit/plugins/bash/lang/parser/eval/UnescapingPsiBuilder$MyMarker", "doneBefore" }));  if (before == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "before", "com/ansorgit/plugins/bash/lang/parser/eval/UnescapingPsiBuilder$MyMarker", "doneBefore" }));  super.doneBefore(type, getDelegateOrThis(before), errorMessage);
    }

    
    public void done(@NotNull IElementType type) {
      if (type == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "type", "com/ansorgit/plugins/bash/lang/parser/eval/UnescapingPsiBuilder$MyMarker", "done" }));  super.done(type);
    }
    
    @NotNull
    private PsiBuilder.Marker getDelegateOrThis(@NotNull PsiBuilder.Marker marker) {
      if (marker == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "marker", "com/ansorgit/plugins/bash/lang/parser/eval/UnescapingPsiBuilder$MyMarker", "getDelegateOrThis" }));  if (marker instanceof DelegateMarker) {
        if (((DelegateMarker)marker).getDelegate() == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/parser/eval/UnescapingPsiBuilder$MyMarker", "getDelegateOrThis" }));  return ((DelegateMarker)marker).getDelegate();
      } 
      if (marker == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/parser/eval/UnescapingPsiBuilder$MyMarker", "getDelegateOrThis" }));  return marker;
    }
  }
}
