package com.ansorgit.plugins.bash.call;

import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.google.common.collect.Lists;
import com.intellij.lang.ASTNode;
import com.intellij.lang.ITokenTypeRemapper;
import com.intellij.lang.LighterASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.WhitespaceSkippedCallback;
import com.intellij.lang.WhitespacesAndCommentsBinder;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.util.containers.Stack;
import com.intellij.util.diff.FlyweightCapableTreeStructure;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MockPsiBuilder
  implements PsiBuilder
{
  private static final Logger log = Logger.getInstance("#bash.MockPsiBuilder");
  private final List<IElementType> elements;
  private final List<String> textTokens;
  private final List<String> errors = new ArrayList<>();
  private final Stack<MockMarker> markers = new Stack();
  private final Map<Key<?>, Object> userData = new HashMap<>();
  
  private final List<Pair<MockMarker, IElementType>> doneMarkers = Lists.newLinkedList();
  
  private static final TokenSet ignoredTokens = TokenSet.orSet(new TokenSet[] { BashTokenTypes.whitespaceTokens });
  private TokenSet enforcedCommentTokens = BashTokenTypes.commentTokens;
  
  int elementPosition = 0;
  private ITokenTypeRemapper tokenRemapper = null;
  
  public MockPsiBuilder(IElementType... data) {
    this.elements = new ArrayList<>();
    this.elements.addAll(Arrays.asList(data));
    
    this.textTokens = Lists.newLinkedList();
  }
  
  public MockPsiBuilder(List<String> textTokens, IElementType... data) {
    this.elements = new ArrayList<>();
    this.elements.addAll(Arrays.asList(data));
    
    this.textTokens = Lists.newLinkedList(textTokens);
  }
  
  public boolean hasErrors() {
    return !this.errors.isEmpty();
  }
  
  public List<String> getErrors() {
    return this.errors;
  }
  
  public int processedElements() {
    return this.elementPosition;
  }
  
  public Project getProject() {
    return null;
  }
  
  public CharSequence getOriginalText() {
    return "unkown (mocked PsiBuilder)";
  }
  
  public void advanceLexer() {
    this.elementPosition++;
  }
  
  public IElementType getTokenType() {
    if (this.elementPosition < this.elements.size()) {
      IElementType type = this.elements.get(this.elementPosition);
      while (this.enforcedCommentTokens.contains(type) || ignoredTokens.contains(type)) {
        if (this.elementPosition + 1 >= this.elements.size()) {
          return null;
        }
        
        advanceLexer();
        type = this.elements.get(this.elementPosition);
      } 
      
      return (this.tokenRemapper != null) ? this.tokenRemapper
        .filter(type, this.elementPosition, this.elementPosition + 1, null) : type;
    } 

    
    return null;
  }
  
  public void setTokenTypeRemapper(ITokenTypeRemapper iTokenTypeRemapper) {
    this.tokenRemapper = iTokenTypeRemapper;
  }
  
  public String getTokenText() {
    getTokenType();
    return (this.textTokens.size() > this.elementPosition) ? this.textTokens.get(this.elementPosition) : "unknown";
  }


  
  public void remapCurrentToken(IElementType newTokenType) {
    getTokenType();
    this.elements.set(this.elementPosition, newTokenType);
  }

  
  public void setWhitespaceSkippedCallback(WhitespaceSkippedCallback whitespaceSkippedCallback) {
    throw new UnsupportedOperationException();
  }
  
  public IElementType lookAhead(int lookAhead) {
    int indexPos = this.elementPosition;
    int elementCounter = 0;
    int length = this.elements.size();
    
    while (indexPos < length && elementCounter < lookAhead) {
      indexPos++;
      
      while (indexPos < length && this.elements.get(indexPos) == BashTokenTypes.WHITESPACE) {
        indexPos++;
      }
      
      elementCounter++;
    } 
    
    return (indexPos < length) ? this.elements.get(indexPos) : null;
  }
  
  public IElementType rawLookup(int lookAhead) {
    int pos = this.elementPosition + lookAhead;
    return (pos >= 0 && pos < this.elements.size()) ? this.elements.get(pos) : null;
  }
  
  public int rawTokenTypeStart(int lookAhead) {
    int requestedPos = this.elementPosition + lookAhead;
    if (requestedPos >= this.textTokens.size()) {
      log.warn("Invalid request for rawTokenTypeStart!");
      return -1;
    } 
    
    if (requestedPos == 0) {
      return -1;
    }
    
    int offset = 0;
    for (int i = 0; i <= requestedPos; i++) {
      offset += ((String)this.textTokens.get(i)).length();
    }
    
    return offset;
  }


  
  public int rawTokenIndex() {
    return getCurrentOffset();
  }
  
  public int getCurrentOffset() {
    return this.elementPosition;
  }

  
  @NotNull
  public PsiBuilder.Marker mark() {
    StringBuilder details = new StringBuilder("Marker opened at:\n");
    try {
      throw new IllegalStateException();
    } catch (IllegalStateException e) {
      StackTraceElement[] stack = e.getStackTrace();
      int length = stack.length;
      for (int i = 0; i < length && i < 10; i++) {
        details.append(stack[i].toString()).append("\n");
      }

      
      MockMarker mockMarker = new MockMarker(this.elementPosition, details.toString());
      this.markers.push(mockMarker);
      if (mockMarker == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/call/MockPsiBuilder", "mark" }));  return mockMarker;
    } 
  }
  public void error(String s) {
    this.errors.add("[" + this.elementPosition + "]: " + s);
  }
  
  public boolean eof() {
    return (this.elementPosition >= this.elements.size());
  }


  
  @NotNull
  public ASTNode getTreeBuilt() {
    if (null == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/call/MockPsiBuilder", "getTreeBuilt" }));  return null;
  }


  
  @NotNull
  public FlyweightCapableTreeStructure<LighterASTNode> getLightTree() {
    if (null == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/call/MockPsiBuilder", "getLightTree" }));  return null;
  }

  
  public void setDebugMode(boolean b) {}
  
  public void enforceCommentTokens(@NotNull TokenSet tokenSet) {
    if (tokenSet == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "tokenSet", "com/ansorgit/plugins/bash/call/MockPsiBuilder", "enforceCommentTokens" }));  this.enforcedCommentTokens = tokenSet;
  }
  
  public LighterASTNode getLatestDoneMarker() {
    return null;
  }
  
  public <T> T getUserData(@NotNull Key<T> tKey) {
    if (tKey == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "tKey", "com/ansorgit/plugins/bash/call/MockPsiBuilder", "getUserData" }));  return (T)this.userData.get(tKey);
  }
  
  public <T> void putUserData(@NotNull Key<T> tKey, T t) {
    if (tKey == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "tKey", "com/ansorgit/plugins/bash/call/MockPsiBuilder", "putUserData" }));  this.userData.put(tKey, t);
  }
  
  public List<Pair<MockMarker, IElementType>> getDoneMarkers() {
    return this.doneMarkers;
  }
  
  public <T> T getUserDataUnprotected(@NotNull Key<T> tKey) {
    if (tKey == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "tKey", "com/ansorgit/plugins/bash/call/MockPsiBuilder", "getUserDataUnprotected" }));  return null;
  }

  
  public <T> void putUserDataUnprotected(@NotNull Key<T> tKey, @Nullable T t) {
    if (tKey == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "tKey", "com/ansorgit/plugins/bash/call/MockPsiBuilder", "putUserDataUnprotected" })); 
  }
  
  public final class MockMarker implements PsiBuilder.Marker { private final int position;
    private final String details;
    boolean closed = false;
    
    private MockMarker(int originalPosition, String details) {
      this.position = originalPosition;
      this.details = details;
    }
    
    @NotNull
    public PsiBuilder.Marker precede() {
      MockMarker preceedingMarker = new MockMarker(this.position, "preceded marker " + this);
      
      int pos = MockPsiBuilder.this.markers.indexOf(this);
      if (pos != -1) {
        MockPsiBuilder.this.markers.add(pos, preceedingMarker);
      }
      
      if (preceedingMarker == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/call/MockPsiBuilder$MockMarker", "precede" }));  return preceedingMarker;
    }
    
    private void finishMarker() {
      if (this.closed) {
        throw new IllegalStateException("This marker is already closed (either dropped or done)!");
      }
      
      this.closed = true;
      
      if (MockPsiBuilder.this.markers.isEmpty() || MockPsiBuilder.this.markers.peek() != this) {
        StringBuilder details = new StringBuilder();
        Stack<MockMarker> markers = MockPsiBuilder.this.markers;
        
        for (int i = markers.size() - 1; i >= 0; i--) {
          MockMarker m = (MockMarker)markers.get(i);
          if (m == this) {
            details.append("## current marker follows ##");
          }
          details.append(m.details).append("\n\n");
        } 
        
        boolean hasCurrent = MockPsiBuilder.this.markers.contains(this);
        String detailMessage = "This marker isn't closed in order. Current markers (newest is later):\n" + details.toString();
        if (!hasCurrent) {
          detailMessage = "++ The current marker is already closed!\n" + detailMessage;
        }
        
        throw new IllegalStateException(detailMessage);
      } 
      
      MockPsiBuilder.this.markers.pop();
    }
    
    public void drop() {
      finishMarker();
    }
    
    public void rollbackTo() {
      MockPsiBuilder.this.elementPosition = this.position;
      finishMarker();
    }
    
    public void done(@NotNull IElementType elementType) {
      if (elementType == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "elementType", "com/ansorgit/plugins/bash/call/MockPsiBuilder$MockMarker", "done" }));  finishMarker();
      
      MockPsiBuilder.this.doneMarkers.add(Pair.create(this, elementType));
    }
    
    public void collapse(@NotNull IElementType elementType) {
      if (elementType == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "elementType", "com/ansorgit/plugins/bash/call/MockPsiBuilder$MockMarker", "collapse" }));  done(elementType);
    }
    
    public void doneBefore(@NotNull IElementType elementType, @NotNull PsiBuilder.Marker marker) {
      if (elementType == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "elementType", "com/ansorgit/plugins/bash/call/MockPsiBuilder$MockMarker", "doneBefore" }));  if (marker == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "marker", "com/ansorgit/plugins/bash/call/MockPsiBuilder$MockMarker", "doneBefore" }));  doneBefore(elementType, marker, "");
    }
    
    public void doneBefore(@NotNull IElementType elementType, @NotNull PsiBuilder.Marker marker, String message) {
      if (elementType == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "elementType", "com/ansorgit/plugins/bash/call/MockPsiBuilder$MockMarker", "doneBefore" }));  if (marker == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "marker", "com/ansorgit/plugins/bash/call/MockPsiBuilder$MockMarker", "doneBefore" }));  if (this.closed) {
        throw new IllegalStateException("This marker is already closed (either dropped or done)");
      }
      
      if (!(marker instanceof MockMarker)) {
        throw new IllegalStateException("The stopMarker must be a MockMarker");
      }
      
      this.closed = true;
      
      int index = MockPsiBuilder.this.markers.indexOf(this);
      int indexStopMarker = MockPsiBuilder.this.markers.indexOf(marker);
      
      if (indexStopMarker == -1) {
        throw new IllegalStateException("stopMarker wasn't found");
      }
      if (index == -1) {
        throw new IllegalStateException("marker wasn't found");
      }
      if (index > indexStopMarker) {
        throw new IllegalStateException(String.format("The stopMarker must have been created after the current marker: index %d, stop marker index %s", new Object[] { Integer.valueOf(index), Integer.valueOf(indexStopMarker) }));
      }
      
      MockPsiBuilder.this.markers.remove(index);
      
      MockPsiBuilder.this.doneMarkers.add(Pair.create(this, elementType));
    }
    
    public void error(String s) {
      MockPsiBuilder.this.error("Marker@" + this.position + ": " + s);
      finishMarker();
    }

    
    public void errorBefore(String s, @NotNull PsiBuilder.Marker marker) {
      if (marker == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "marker", "com/ansorgit/plugins/bash/call/MockPsiBuilder$MockMarker", "errorBefore" }));  error(s);
    }
    
    public void setCustomEdgeTokenBinders(@Nullable WhitespacesAndCommentsBinder whitespacesAndCommentsBinder, @Nullable WhitespacesAndCommentsBinder whitespacesAndCommentsBinder1) {} }

}
