package com.ansorgit.plugins.bash.lang.psi.impl;

import com.google.common.collect.Multimap;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import java.util.Set;



















public interface Keys
{
  public static final Key<Multimap<VirtualFile, PsiElement>> visitedIncludeFiles = new Key("visitedIncludeFiles");
  
  public static final Key<Set<PsiElement>> VISITED_SCOPES_KEY = Key.create("BASH_SCOPES_VISITED");



  
  public static final Key<Boolean> FILE_WALK_GO_DEEP = Key.create("BASH_FILE_WALK_DEEP");

  
  public static final Key<PsiElement> resolvingIncludeCommand = Key.create("BASH_RESOLVING_INCLUDE");
}
