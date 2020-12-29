package com.ansorgit.plugins.bash.util;

import java.util.EmptyStackException;



















public class IntStack
{
  private int[] data;
  private int size;
  
  public IntStack(int initialCapacity) {
    this.data = new int[initialCapacity];
    this.size = 0;
  }
  
  public IntStack() {
    this(5);
  }
  
  public void push(int t) {
    if (this.size >= this.data.length) {
      int[] newdata = new int[(int)Math.ceil(this.data.length * 1.5D)];
      System.arraycopy(this.data, 0, newdata, 0, this.size);
      this.data = newdata;
    } 
    this.data[this.size++] = t;
  }
  
  public int peek() {
    if (this.size == 0) {
      throw new EmptyStackException();
    }
    return this.data[this.size - 1];
  }
  
  public int pop() {
    if (this.size == 0) {
      throw new EmptyStackException();
    }
    return this.data[--this.size];
  }
  
  public boolean contains(int value) {
    for (int i = 0; i < this.size; i++) {
      if (this.data[i] == value) {
        return true;
      }
    } 
    
    return false;
  }
  
  public boolean empty() {
    return (this.size == 0);
  }

  
  public boolean equals(Object o) {
    if (o instanceof IntStack) {
      IntStack otherStack = (IntStack)o;
      if (this.size != otherStack.size) {
        return false;
      }
      for (int i = 0; i < otherStack.size; i++) {
        if (this.data[i] != otherStack.data[i]) {
          return false;
        }
      } 
      return true;
    } 
    
    return false;
  }
  
  public void clear() {
    this.size = 0;
  }
}
