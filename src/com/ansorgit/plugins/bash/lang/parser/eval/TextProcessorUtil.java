package com.ansorgit.plugins.bash.lang.parser.eval;

import com.intellij.openapi.util.Ref;
import org.jetbrains.annotations.Nullable;















class TextProcessorUtil
{
  static boolean hasNext(CharSequence chars, int index, CharSequence expected) {
    if (index + expected.length() >= chars.length()) {
      return false;
    }
    
    return chars.subSequence(index, index + expected.length()).equals(expected);
  }
  
  static void resetOffsets(int[] sourceOffsets) {
    for (int i = 0; i < sourceOffsets.length; i++) {
      sourceOffsets[i] = -1;
    }
  }
  
  public static String patchOriginal(String originalText, int[] outSourceOffsets, @Nullable String replacement) {
    StringBuilder result = new StringBuilder(originalText.length());
    
    int added = 0;
    int decoded = 1, original = outSourceOffsets[decoded];
    for (; decoded < outSourceOffsets.length && original != -1 && original <= originalText.length(); 
      original = outSourceOffsets[decoded + 1], decoded++) {
      
      for (int i = decoded + added; i < original; i++) {
        if (replacement == null) {
          result.append(' ');
          added++;
        } else {
          result.append(replacement);
          added += replacement.length();
        } 
      } 
      
      result.append(originalText.charAt(original - 1));
      
      if (decoded + 1 == outSourceOffsets.length) {
        break;
      }
    } 
    
    return result.toString();
  }









  
  static boolean parseStringCharacters(String chars, StringBuilder outChars, Ref<int[]> sourceOffsetsRef) {
    int[] sourceOffsets = new int[chars.length() + 1];
    sourceOffsetsRef.set(sourceOffsets);

    
    if (chars.indexOf('\\') < 0) {
      outChars.append(chars);
      for (int i = 0; i < sourceOffsets.length; i++) {
        sourceOffsets[i] = i;
      }
      return true;
    } 

    
    resetOffsets(sourceOffsets);
    
    int index = 0;
    while (index < chars.length()) {
      char c = chars.charAt(index);
      index++;
      
      sourceOffsets[outChars.length()] = index - 1;
      sourceOffsets[outChars.length() + 1] = index;
      
      if (c != '\\') {
        
        outChars.append(c);
        
        continue;
      } 
      if (index == chars.length()) {
        
        outChars.append(c);
        return true;
      } 

      
      c = chars.charAt(index);
      
      if (c == '"' || c == '`' || c == '\\' || c == '!' || c == '\n' || (c == '$' && !hasNext(chars, index, "$$"))) {
        if (c != '\n')
        {
          outChars.append(c);
        }
      } else {
        
        outChars.append('\\');
        outChars.append(c);
      } 
      
      index++;
      sourceOffsets[outChars.length()] = index;
    } 
    return true;
  }









  
  static boolean enhancedParseStringCharacters(String chars, StringBuilder outChars, Ref<int[]> sourceOffsetsRef) {
    int[] sourceOffsets = new int[chars.length() + 1];
    sourceOffsetsRef.set(sourceOffsets);

    
    resetOffsets(sourceOffsets);
    
    if (chars.indexOf('\\') < 0) {
      outChars.append(chars);
      for (int i = 0; i < sourceOffsets.length; i++) {
        sourceOffsets[i] = i;
      }
      return true;
    } 
    
    int index = 0;
    while (index < chars.length()) {
      char c = chars.charAt(index++);
      
      sourceOffsets[outChars.length()] = index - 1;
      sourceOffsets[outChars.length() + 1] = index;
      
      if (c != '\\') {
        outChars.append(c);
        
        continue;
      } 
      if (index == chars.length()) {
        return false;
      }
      
      c = chars.charAt(index++);
      switch (c) {
        
        case 'n':
          outChars.append('\n');
          break;

        
        case 'r':
          outChars.append('\r');
          break;

        
        case 't':
          outChars.append('\t');
          break;

        
        case 'v':
          outChars.append(11);
          break;

        
        case 'b':
          outChars.append('\b');
          break;

        
        case 'a':
          outChars.append(7);
          break;


        
        case '"':
        case '\'':
        case '\\':
          outChars.append(c);
          break;


        
        case '0':
          if (index + 2 <= chars.length()) {
            try {
              int v = Integer.parseInt(chars.substring(index, index + 2), 8);
              outChars.append((char)v);
              index += 2;
            } catch (Exception e) {
              return false;
            }  break;
          } 
          return false;



        
        default:
          outChars.append('\\');
          outChars.append(c);
          break;
      } 
      
      sourceOffsets[outChars.length()] = index;
    } 
    return true;
  }
}
