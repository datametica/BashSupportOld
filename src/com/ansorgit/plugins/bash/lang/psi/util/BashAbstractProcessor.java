package com.ansorgit.plugins.bash.lang.psi.util;

import com.ansorgit.plugins.bash.lang.psi.BashScopeProcessor;
import com.ansorgit.plugins.bash.lang.psi.api.ResolveProcessor;
import com.google.common.collect.Iterators;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.scope.PsiScopeProcessor;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import org.jetbrains.annotations.NotNull;



















public abstract class BashAbstractProcessor
  implements BashScopeProcessor, PsiScopeProcessor, ResolveProcessor
{
  private final boolean preferNeigbourhood;
  private Multimap<Integer, PsiElement> results;
  private Multimap<PsiElement, PsiElement> includeCommands;
  
  protected BashAbstractProcessor(boolean preferNeighbourhood) {
    this.preferNeigbourhood = preferNeighbourhood;
  }
  
  public void handleEvent(@NotNull PsiScopeProcessor.Event event, Object o) {
    if (event == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "event", "com/ansorgit/plugins/bash/lang/psi/util/BashAbstractProcessor", "handleEvent" })); 
  }
  public final PsiElement getBestResult(boolean firstResult, PsiElement referenceElement) {
    return findBestResult(this.results, firstResult, referenceElement);
  }
  
  public Collection<PsiElement> getResults() {
    if (this.results == null) {
      return Collections.emptyList();
    }
    
    return this.results.values();
  }
  
  public boolean hasResults() {
    return (this.results != null && !this.results.isEmpty());
  }
  
  protected final void removeResult(PsiElement element) {
    if (this.results != null && this.results.containsValue(element)) {
      this.results.values().remove(element);
    }
  }
  
  protected final void storeResult(PsiElement element, Integer rating, PsiElement includeCommand) {
    if (this.results == null) {
      this.results = (Multimap<Integer, PsiElement>)LinkedListMultimap.create();
    }
    this.results.put(rating, element);
    
    if (includeCommand != null) {
      if (this.includeCommands == null) {
        this.includeCommands = (Multimap<PsiElement, PsiElement>)LinkedListMultimap.create();
      }
      this.includeCommands.put(element, includeCommand);
    } 
  }









  
  private PsiElement findBestResult(Multimap<Integer, PsiElement> results, boolean firstResult, PsiElement referenceElement) {
    if (!hasResults()) {
      return null;
    }
    
    if (firstResult) {
      return (PsiElement)Iterators.get(results.values().iterator(), 0);
    }

    
    int referenceLevel = (this.preferNeigbourhood && referenceElement != null) ? BashPsiUtils.blockNestingLevel(referenceElement) : 0;


    
    int bestRating = Integer.MAX_VALUE;
    int bestDelta = Integer.MAX_VALUE;
    for (Iterator<Integer> iterator = results.keySet().iterator(); iterator.hasNext(); ) { int rating = ((Integer)iterator.next()).intValue();
      int delta = Math.abs(referenceLevel - rating);
      if (delta < bestDelta) {
        bestDelta = delta;
        bestRating = rating;
      }  }



    
    if (this.preferNeigbourhood) {
      return (PsiElement)Iterators.getLast(results.get(Integer.valueOf(bestRating)).iterator());
    }
    
    long smallestOffset = 2147483647L;
    PsiElement bestElement = null;
    
    for (PsiElement e : results.get(Integer.valueOf(bestRating))) {
      
      int textOffset = e.getTextOffset();
      if (BashPsiUtils.isInjectedElement(e)) {
        
        PsiLanguageInjectionHost injectionHost = InjectedLanguageManager.getInstance(e.getProject()).getInjectionHost(e);
        if (injectionHost != null) {
          textOffset += injectionHost.getTextOffset();
        }
      } 


      
      Collection<PsiElement> includeCommands = (this.includeCommands != null) ? this.includeCommands.get(e) : Collections.<PsiElement>emptyList();
      if (!includeCommands.isEmpty()) {
        for (PsiElement includeCommand : includeCommands) {
          int includeOffset = includeCommand.getTextOffset();
          if (includeOffset < smallestOffset) {
            smallestOffset = includeOffset;
            bestElement = e;
          } 
        }  continue;
      }  if (textOffset < smallestOffset) {
        smallestOffset = textOffset;
        bestElement = e;
      } 
    } 
    
    return bestElement;
  }

  
  public void reset() {
    if (this.results != null)
      this.results.clear(); 
  }
  
  public void prepareResults() {}
}
