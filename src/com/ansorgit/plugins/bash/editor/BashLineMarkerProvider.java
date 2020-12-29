package com.ansorgit.plugins.bash.editor;

import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.psi.PsiElement;
import com.intellij.util.PlatformIcons;
import java.util.Collection;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;























public class BashLineMarkerProvider
  implements LineMarkerProvider
{
  @Nullable
  public LineMarkerInfo getLineMarkerInfo(@NotNull PsiElement element) {
    if (element == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "element", "com/ansorgit/plugins/bash/editor/BashLineMarkerProvider", "getLineMarkerInfo" }));  if (element.getNode().getElementType() == BashTokenTypes.WORD && element.getParent() instanceof com.ansorgit.plugins.bash.lang.psi.api.BashFunctionDefName && element.getParent().getParent() instanceof com.ansorgit.plugins.bash.lang.psi.api.function.BashFunctionDef) {
      return new LineMarkerInfo(element, element.getTextRange(), PlatformIcons.METHOD_ICON, 4, null, null, GutterIconRenderer.Alignment.LEFT);
    }
    
    return null;
  }

  
  public void collectSlowLineMarkers(@NotNull List<PsiElement> elements, @NotNull Collection<LineMarkerInfo> result) {
    if (elements == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "elements", "com/ansorgit/plugins/bash/editor/BashLineMarkerProvider", "collectSlowLineMarkers" }));  if (result == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "result", "com/ansorgit/plugins/bash/editor/BashLineMarkerProvider", "collectSlowLineMarkers" })); 
  }
}
