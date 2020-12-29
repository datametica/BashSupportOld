package com.ansorgit.plugins.bash.lang.parser.eval;

import com.intellij.lexer.DelegateLexer;
import com.intellij.lexer.Lexer;
import com.intellij.lexer.LexerPosition;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;















public class PrefixSuffixAddingLexer
  extends DelegateLexer
{
  private final String prefix;
  private final IElementType prefixType;
  private final String suffix;
  private final IElementType suffixType;
  boolean afterPrefix = false;
  boolean delegateEOF = false;
  boolean afterEOF = false;
  
  public PrefixSuffixAddingLexer(@NotNull Lexer delegate, String prefix, IElementType prefixType, String suffix, IElementType suffixType) {
    super(delegate);
    this.prefix = prefix;
    this.prefixType = prefixType;
    this.suffix = suffix;
    this.suffixType = suffixType;
  }

  
  @Nullable
  public IElementType getTokenType() {
    if (this.afterEOF)
      return null; 
    if (!this.afterPrefix)
      return this.prefixType; 
    if (this.delegateEOF) {
      return this.suffixType;
    }
    
    return this.myDelegate.getTokenType();
  }

  
  public int getTokenStart() {
    int tokenStart = this.myDelegate.getTokenStart();
    if (!this.afterPrefix && tokenStart == 0) {
      return 0;
    }
    
    if (this.afterEOF) {
      return this.prefix.length() + this.suffix.length() + tokenStart;
    }
    
    return this.prefix.length() + tokenStart;
  }

  
  public int getTokenEnd() {
    if (!this.afterPrefix) {
      return this.prefix.length();
    }
    
    int tokenEnd = this.myDelegate.getTokenEnd();
    if (this.delegateEOF) {
      return this.prefix.length() + this.suffix.length() + tokenEnd;
    }
    
    return this.prefix.length() + tokenEnd;
  }


  
  public int getBufferEnd() {
    return this.myDelegate.getBufferEnd() + this.prefix.length() + this.suffix.length();
  }

  
  @NotNull
  public LexerPosition getCurrentPosition() {
    final int start = getTokenStart();
    final int state = getState();
    
    if (new LexerPosition()
      {
        public int getOffset() {
          return start;
        }
        
        public int getState()
        {
          return state; } } == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/parser/eval/PrefixSuffixAddingLexer", "getCurrentPosition" }));  return new LexerPosition() { public int getState() { return state; }
         public int getOffset() {
          return start;
        } }
      ;
  }
  public void advance() {
    if (this.afterPrefix) {
      this.myDelegate.advance();
    }
    
    this.afterPrefix = true;
    this.afterEOF |= this.delegateEOF;
    this.delegateEOF |= (this.myDelegate.getTokenType() == null) ? 1 : 0;
  }

  
  @NotNull
  public String getTokenText() {
    if (getTokenSequence().toString() == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/parser/eval/PrefixSuffixAddingLexer", "getTokenText" }));  return getTokenSequence().toString();
  }

  
  @NotNull
  public CharSequence getTokenSequence() {
    if (this.afterEOF) {
      if ("" == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/parser/eval/PrefixSuffixAddingLexer", "getTokenSequence" }));  return "";
    } 
    
    if (!this.afterPrefix) {
      if (this.prefix == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/parser/eval/PrefixSuffixAddingLexer", "getTokenSequence" }));  return this.prefix;
    } 
    
    if (this.delegateEOF) {
      if (this.suffix == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/parser/eval/PrefixSuffixAddingLexer", "getTokenSequence" }));  return this.suffix;
    } 
    
    if (this.myDelegate.getTokenSequence() == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/parser/eval/PrefixSuffixAddingLexer", "getTokenSequence" }));  return this.myDelegate.getTokenSequence();
  }

  
  public void restore(@NotNull final LexerPosition position) {
    if (position == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "position", "com/ansorgit/plugins/bash/lang/parser/eval/PrefixSuffixAddingLexer", "restore" }));  int prefixLength = this.prefix.length();
    
    final int newOffset = position.getOffset() - prefixLength;
    if (newOffset == 0) {
      this.afterPrefix = false;
    }
    
    if (newOffset < this.myDelegate.getBufferEnd()) {
      this.delegateEOF = false;
      this.afterEOF = false;
    } 
    
    this.myDelegate.restore(new LexerPosition()
        {
          public int getOffset() {
            return newOffset;
          }

          
          public int getState() {
            return position.getState();
          }
        });
  }

  
  public void start(@NotNull CharSequence buffer, int startOffset, int endOffset, int initialState) {
    if (buffer == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "buffer", "com/ansorgit/plugins/bash/lang/parser/eval/PrefixSuffixAddingLexer", "start" })); 
    int newStartOffset = (startOffset == 0) ? this.prefix.length() : startOffset;


    
    int newEndOffset = (endOffset == buffer.length()) ? (endOffset - this.suffix.length()) : endOffset;

    
    CharSequence newBuffer = buffer.subSequence(newStartOffset, newEndOffset);
    this.myDelegate.start(newBuffer, 0, newBuffer.length(), initialState);
    
    this.afterPrefix = false;
    this.delegateEOF = false;
    this.afterEOF = false;
  }
}
