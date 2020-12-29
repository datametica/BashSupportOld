package com.ansorgit.plugins.bash.lang.parser.util;

import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.ansorgit.plugins.bash.lang.parser.BashPsiBuilder;
import com.ansorgit.plugins.bash.util.BashStrings;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import java.util.function.Predicate;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;





















public class ParserUtil
{
  @NonNls
  private static final String BUNDLE = "com.ansorgit.plugins.bash.bash";
  
  public static void errorToken(PsiBuilder builder, @PropertyKey(resourceBundle = "com.ansorgit.plugins.bash.bash") String message) {
    PsiBuilder.Marker marker = builder.mark();
    builder.advanceLexer();
    marker.error(BashStrings.message(message, new Object[0]));
  }
  
  public static void error(PsiBuilder builder, @PropertyKey(resourceBundle = "com.ansorgit.plugins.bash.bash") String message) {
    builder.error(BashStrings.message(message, new Object[0]));
  }
  
  public static void error(PsiBuilder.Marker marker, @PropertyKey(resourceBundle = "com.ansorgit.plugins.bash.bash") String message) {
    marker.error(BashStrings.message(message, new Object[0]));
  }








  
  public static IElementType getTokenAndAdvance(BashPsiBuilder builder, boolean showWhitespace) {
    IElementType tokenType = builder.getTokenType(showWhitespace);
    
    if (!builder.eof()) {
      builder.advanceLexer();
    }
    
    return tokenType;
  }







  
  public static IElementType getTokenAndAdvance(PsiBuilder builder) {
    IElementType tokenType = builder.getTokenType();

    
    if (tokenType != null) {
      builder.advanceLexer();
    }
    
    return tokenType;
  }
  
  public static boolean smartRemapAndAdvance(PsiBuilder builder, String expectedTokenText, IElementType expectedTokenType, IElementType newTokenType) {
    IElementType current = builder.getTokenType();
    if (current == newTokenType) {
      
      builder.advanceLexer();
    } else if (expectedTokenText.equals(builder.getTokenText()) && current == expectedTokenType) {
      builder.remapCurrentToken(newTokenType);
      builder.advanceLexer();
    } else {
      builder.error("unexpected token");
      return false;
    } 
    
    return true;
  }
  
  public static void remapMarkAdvance(PsiBuilder builder, IElementType newTokenType, IElementType markAs) {
    builder.remapCurrentToken(newTokenType);
    
    markTokenAndAdvance(builder, markAs);
  }






  
  public static void markTokenAndAdvance(PsiBuilder builder, IElementType markAs) {
    PsiBuilder.Marker marker = builder.mark();
    
    builder.advanceLexer();
    
    marker.done(markAs);
  }








  
  public static boolean checkAndRollback(BashPsiBuilder builder, Predicate<BashPsiBuilder> function) {
    PsiBuilder.Marker start = builder.mark();
    builder.enterNewErrorLevel(false);
    
    boolean result = function.test(builder);
    
    builder.leaveLastErrorLevel();
    start.rollbackTo();
    
    return result;
  }







  
  public static boolean conditionalRead(PsiBuilder builder, IElementType token) {
    if (builder.getTokenType() == token) {
      builder.advanceLexer();
      return true;
    } 
    
    return false;
  }







  
  public static boolean conditionalRead(PsiBuilder builder, TokenSet tokens) {
    if (tokens.contains(builder.getTokenType())) {
      builder.advanceLexer();
      return true;
    } 
    
    return false;
  }
  
  public static boolean checkNextOrError(BashPsiBuilder builder, PsiBuilder.Marker marker, IElementType expected, @PropertyKey(resourceBundle = "com.ansorgit.plugins.bash.bash") String message) {
    IElementType next = getTokenAndAdvance((PsiBuilder)builder);
    if (next != expected) {
      marker.drop();
      error((PsiBuilder)builder, message);
      return false;
    } 
    
    return true;
  }






  
  public static boolean isWordToken(IElementType token) {
    return BashTokenTypes.stringLiterals.contains(token);
  }






  
  public static boolean isIdentifier(IElementType tokenType) {
    return (tokenType == BashTokenTypes.WORD || BashTokenTypes.identifierKeywords.contains(tokenType));
  }
  
  public static boolean hasNextTokens(PsiBuilder builder, boolean enableWhitespace, IElementType... tokens) {
    for (int i = 0, tokensLength = tokens.length; i < tokensLength; i++) {
      IElementType lookAheadToken = enableWhitespace ? builder.rawLookup(i) : builder.lookAhead(i);
      if (lookAheadToken != tokens[i]) {
        return false;
      }
    } 
    
    return true;
  }






  
  public static boolean isWhitespaceOrLineFeed(IElementType token) {
    return (token == BashTokenTypes.WHITESPACE || token == BashTokenTypes.LINE_FEED);
  }
  
  public static boolean containsTokenInLookahead(PsiBuilder builder, IElementType token, int maxLookahead, boolean allowWhitespace) {
    int i = 0;
    
    while (i < maxLookahead) {
      IElementType current = allowWhitespace ? builder.lookAhead(i) : builder.rawLookup(i);
      if (current == null) {
        return false;
      }
      
      if (current == token) {
        return true;
      }
      
      i++;
    } 
    
    return false;
  }


























  
  public static boolean isEmptyListFollowedBy(BashPsiBuilder builder, IElementType token) {
    return isEmptyListFollowedBy(builder, TokenSet.create(new IElementType[] { token }));
  }







  
  public static boolean isEmptyListFollowedBy(BashPsiBuilder builder, TokenSet tokens) {
    if (tokens.contains(builder.getTokenType())) {
      return true;
    }
    
    int steps = 0;
    
    while (builder.lookAhead(steps) == BashTokenTypes.LINE_FEED) {
      steps++;
    }
    
    if (builder.lookAhead(steps) == BashTokenTypes.SEMI) {
      steps++;
    }
    
    while (builder.lookAhead(steps) == BashTokenTypes.LINE_FEED) {
      steps++;
    }
    
    return tokens.contains(builder.lookAhead(steps));
  }







  
  public static boolean readEmptyListFollowedBy(BashPsiBuilder builder, IElementType token) {
    return readEmptyListFollowedBy(builder, TokenSet.create(new IElementType[] { token }));
  }







  
  public static boolean readEmptyListFollowedBy(BashPsiBuilder builder, TokenSet tokens) {
    if (tokens.contains(builder.getTokenType())) {
      return true;
    }
    
    int steps = 0;
    
    while (builder.lookAhead(steps) == BashTokenTypes.LINE_FEED) {
      steps++;
    }
    
    if (builder.lookAhead(steps) == BashTokenTypes.SEMI) {
      steps++;
    }
    
    while (builder.lookAhead(steps) == BashTokenTypes.LINE_FEED) {
      steps++;
    }
    
    if (tokens.contains(builder.lookAhead(steps))) {
      for (int i = 0; i < steps; i++) {
        builder.advanceLexer();
        builder.getTokenType();
      } 
      
      return true;
    } 
    
    return false;
  }





  
  public static boolean isWord(@NotNull BashPsiBuilder builder, @NotNull String text) {
    if (builder == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "builder", "com/ansorgit/plugins/bash/lang/parser/util/ParserUtil", "isWord" }));  if (text == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "text", "com/ansorgit/plugins/bash/lang/parser/util/ParserUtil", "isWord" }));  return (builder.getTokenType() == BashTokenTypes.WORD && text.equals(builder.getTokenText()));
  }
}
