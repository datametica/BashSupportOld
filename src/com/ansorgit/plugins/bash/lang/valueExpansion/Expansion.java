package com.ansorgit.plugins.bash.lang.valueExpansion;

interface Expansion {
  String findNext(boolean paramBoolean);
  
  boolean isFlipped();
  
  boolean hasNext();
}
