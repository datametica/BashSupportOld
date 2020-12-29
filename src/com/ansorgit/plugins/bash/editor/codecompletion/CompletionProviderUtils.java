package com.ansorgit.plugins.bash.editor.codecompletion;

import com.ansorgit.plugins.bash.util.OSUtil;
import com.intellij.codeInsight.completion.PrioritizedLookupElement;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.util.SystemInfoRt;
import com.intellij.psi.PsiNamedElement;
import com.intellij.util.PlatformIcons;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.Icon;
import org.jetbrains.annotations.Nullable;



















final class CompletionProviderUtils
{
  private static final Icon fileIcon = IconLoader.getIcon("/fileTypes/text.png");
  private static final Icon dirIcon = PlatformIcons.DIRECTORY_CLOSED_ICON;



  
  static Collection<LookupElement> createFromPsiItems(Collection<? extends PsiNamedElement> elements, @Nullable Icon icon, @Nullable Integer groupId) {
    return (Collection<LookupElement>)elements.stream().map(psi -> {
          LookupElementBuilder element = LookupElementBuilder.create(psi).withCaseSensitivity(true);

          
          if (icon != null) {
            element = element.withIcon(icon);
          }
          
          return (LookupElement)((groupId != null) ? PrioritizedLookupElement.withGrouping((LookupElement)element, groupId.intValue()) : element);
        }).collect(Collectors.toList());
  }
  
  static Collection<LookupElement> createItems(Collection<String> lookupStrings, Icon icon, boolean trimLookupString, Integer groupId) {
    return (Collection<LookupElement>)lookupStrings
      .stream()
      .map(item -> {
          LookupElementBuilder elementBuilder = LookupElementBuilder.create(item).withCaseSensitivity(true);

          
          if (icon != null) {
            elementBuilder = elementBuilder.withIcon(icon);
          }

          
          if (trimLookupString) {
            elementBuilder = elementBuilder.withLookupString(item.replace("_", ""));
          }

          
          return (LookupElement)((groupId != null) ? PrioritizedLookupElement.withGrouping((LookupElement)elementBuilder, groupId.intValue()) : elementBuilder);
        }).collect(Collectors.toList());
  }
  
  static Collection<LookupElement> createPathItems(List<String> osPathes) {
    return (Collection<LookupElement>)osPathes.stream()
      .map(path -> SystemInfoRt.isWindows ? OSUtil.toBashCompatible(path) : path)


      
      .map(path -> {
          int groupId = path.startsWith("/") ? CompletionGrouping.AbsoluteFilePath.ordinal() : CompletionGrouping.RelativeFilePath.ordinal();
          return PrioritizedLookupElement.withGrouping(createPathLookupElement(path, !path.endsWith("/")), groupId);
        }).collect(Collectors.toList());
  }
  
  static LookupElement createPathLookupElement(String path, boolean isFile) {
    return (LookupElement)LookupElementBuilder.create(path).withIcon(isFile ? fileIcon : dirIcon);
  }
}
