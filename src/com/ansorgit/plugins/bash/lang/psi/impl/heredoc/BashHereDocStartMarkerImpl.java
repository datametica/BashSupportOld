package com.ansorgit.plugins.bash.lang.psi.impl.heredoc;

import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.command.BashComposedCommand;
import com.ansorgit.plugins.bash.lang.psi.api.heredoc.BashHereDocEndMarker;
import com.ansorgit.plugins.bash.lang.psi.api.heredoc.BashHereDocMarker;
import com.ansorgit.plugins.bash.lang.psi.api.heredoc.BashHereDocStartMarker;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiElementFactory;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import com.ansorgit.plugins.bash.lang.util.HeredocSharedImpl;
import com.google.common.collect.Lists;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiReference;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.util.IncorrectOperationException;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;










public class BashHereDocStartMarkerImpl
  extends AbstractHeredocMarker
  implements BashHereDocStartMarker
{
  public BashHereDocStartMarkerImpl(ASTNode astNode) {
    super(astNode, "Bash heredoc start marker");
  }

  
  public boolean isIgnoringTabs() {
    return false;
  }

  
  public HeredocMarkerReference createReference() {
    return new HeredocStartMarkerReference(this);
  }

  
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "visitor", "com/ansorgit/plugins/bash/lang/psi/impl/heredoc/BashHereDocStartMarkerImpl", "accept" }));  if (visitor instanceof BashVisitor) {
      ((BashVisitor)visitor).visitHereDocStartMarker(this);
    } else {
      visitor.visitElement((PsiElement)this);
    } 
  }
  
  public boolean isEvaluatingVariables() {
    return HeredocSharedImpl.isEvaluatingMarker(getText().trim());
  }
  
  private static class HeredocStartMarkerReference
    extends HeredocMarkerReference {
    HeredocStartMarkerReference(BashHereDocStartMarker marker) {
      super((BashHereDocMarker)marker);
    }

    
    @Nullable
    public PsiElement resolveInner() {
      String markerName = this.marker.getMarkerText();
      if (markerName == null || markerName.isEmpty()) {
        return null;
      }


      
      BashComposedCommand parent = (BashComposedCommand)BashPsiUtils.findParent((PsiElement)this.marker, BashComposedCommand.class);
      if (parent == null) {
        return null;
      }
      
      final List<BashHereDocEndMarker> endMarkers = Lists.newLinkedList();
      BashPsiUtils.visitRecursively((PsiElement)parent, new BashVisitor()
          {
            public void visitHereDocEndMarker(BashHereDocEndMarker marker) {
              endMarkers.add(marker);
            }
          });

      
      int markerPos = 0;
      for (PsiElement sibling = this.marker.getPrevSibling(); sibling != null; sibling = sibling.getPrevSibling()) {
        if (sibling instanceof BashHereDocMarker) {
          markerPos++;
        }
      } 
      
      return (endMarkers.size() > markerPos) ? (PsiElement)endMarkers.get(markerPos) : null;
    }


    
    protected PsiElement createMarkerElement(String name) {
      String newName = HeredocSharedImpl.wrapMarker(name, this.marker.getText());
      return BashPsiElementFactory.createHeredocStartMarker(this.marker.getProject(), newName);
    }
  }
}
