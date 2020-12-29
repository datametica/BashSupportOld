package com.ansorgit.plugins.bash.lang.psi.impl.heredoc;

import com.ansorgit.plugins.bash.lang.parser.BashElementTypes;
import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.command.BashComposedCommand;
import com.ansorgit.plugins.bash.lang.psi.api.heredoc.BashHereDocEndMarker;
import com.ansorgit.plugins.bash.lang.psi.api.heredoc.BashHereDocMarker;
import com.ansorgit.plugins.bash.lang.psi.api.heredoc.BashHereDocStartMarker;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiElementFactory;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
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














public class BashHereDocEndMarkerImpl
  extends AbstractHeredocMarker
  implements BashHereDocEndMarker
{
  public BashHereDocEndMarkerImpl(ASTNode astNode) {
    super(astNode, "Bash heredoc end marker");
  }

  
  public HeredocMarkerReference createReference() {
    return new HeredocEndMarkerReference(this);
  }

  
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "visitor", "com/ansorgit/plugins/bash/lang/psi/impl/heredoc/BashHereDocEndMarkerImpl", "accept" }));  if (visitor instanceof BashVisitor) {
      ((BashVisitor)visitor).visitHereDocEndMarker(this);
    } else {
      visitor.visitElement((PsiElement)this);
    } 
  }

  
  public boolean isIgnoringTabs() {
    return (getNode().getElementType() == BashElementTypes.HEREDOC_END_IGNORING_TABS_ELEMENT);
  }
  
  private static class HeredocEndMarkerReference extends HeredocMarkerReference {
    HeredocEndMarkerReference(BashHereDocEndMarker marker) {
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
      
      final List<BashHereDocStartMarker> startMarkers = Lists.newLinkedList();
      BashPsiUtils.visitRecursively((PsiElement)parent, new BashVisitor()
          {
            public void visitHereDocStartMarker(BashHereDocStartMarker marker) {
              startMarkers.add(marker);
            }
          });

      
      int markerPos = 0;
      for (PsiElement current = this.marker.getPrevSibling(); current != null; current = current.getPrevSibling()) {
        if (current instanceof BashHereDocEndMarker) {
          markerPos++;
        }
      } 
      
      return (startMarkers.size() > markerPos) ? (PsiElement)startMarkers.get(markerPos) : null;
    }

    
    protected PsiElement createMarkerElement(String name) {
      String markerText = getElement().getText();
      
      int leadingTabs = 0;
      for (int i = 0; i < markerText.length() && markerText.charAt(i) == '\t'; i++) {
        leadingTabs++;
      }
      
      return BashPsiElementFactory.createHeredocEndMarker(this.marker.getProject(), name, leadingTabs);
    }
  }
}
