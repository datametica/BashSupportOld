package com.ansorgit.plugins.bash.lang.lexer;

import com.intellij.lexer.Lexer;
import com.intellij.lexer.MergeFunction;
import com.intellij.lexer.MergingLexerAdapterBase;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;





























class MergingLexer
  extends MergingLexerAdapterBase
{
  private final LexerMergeFunction mergeFunction;
  
  public MergingLexer(Lexer original, MergeTuple... mergeTuples) {
    super(original);
    this.mergeFunction = new LexerMergeFunction(mergeTuples);
  }

  
  public MergeFunction getMergeFunction() {
    return this.mergeFunction;
  }
  
  private static class LexerMergeFunction implements MergeFunction {
    private final MergeTuple[] mergeTuples;
    
    public LexerMergeFunction(MergeTuple[] mergeTuples) {
      this.mergeTuples = mergeTuples;
    }

    
    public IElementType merge(IElementType type, Lexer lexer) {
      for (MergeTuple currentTuple : this.mergeTuples) {
        TokenSet tokensToMerge = currentTuple.getTokensToMerge();
        
        if (tokensToMerge.contains(type)) {
          IElementType current = lexer.getTokenType();
          
          while (tokensToMerge.contains(current)) {
            lexer.advance();
            
            current = lexer.getTokenType();
          } 
          
          return currentTuple.getTargetType();
        } 
      } 
      
      return type;
    }
  }
}
