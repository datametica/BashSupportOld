package com.ansorgit.plugins.bash.editor.codefolding;

import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVar;
import com.ansorgit.plugins.bash.lang.psi.impl.vars.BashVarDefImpl;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import com.ansorgit.plugins.bash.lang.psi.util.BashResolveUtil;
import com.ansorgit.plugins.bash.settings.BashProjectSettings;
import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilderEx;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.DumbService;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.util.containers.ContainerUtil;
import java.util.ArrayList;
import java.util.Collection;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(mv = {1, 1, 1}, bv = {1, 0, 0}, k = 1, d1 = {"\000B\n\002\030\002\n\002\030\002\n\002\030\002\n\002\b\002\n\002\020\021\n\002\030\002\n\000\n\002\030\002\n\000\n\002\030\002\n\000\n\002\020\013\n\002\b\002\n\002\020\016\n\000\n\002\030\002\n\000\n\002\020\b\n\002\b\007\030\000 \0312\0020\0012\0020\002:\001\031B\005¢\006\002\020\003J+\020\004\032\b\022\004\022\0020\0060\0052\006\020\007\032\0020\b2\006\020\t\032\0020\n2\006\020\013\032\0020\fH\026¢\006\002\020\rJ\030\020\016\032\0020\0172\006\020\020\032\0020\0212\006\020\022\032\0020\023H\002J\020\020\024\032\0020\0172\006\020\020\032\0020\021H\026J\020\020\025\032\0020\f2\006\020\020\032\0020\021H\026J\030\020\026\032\0020\0172\006\020\027\032\0020\0212\006\020\030\032\0020\023H\002¨\006\032"}, d2 = {"Lcom/ansorgit/plugins/bash/editor/codefolding/BashVariableFoldingBuilder;", "Lcom/intellij/lang/folding/FoldingBuilderEx;", "Lcom/intellij/openapi/project/DumbAware;", "()V", "buildFoldRegions", "", "Lcom/intellij/lang/folding/FoldingDescriptor;", "root", "Lcom/intellij/psi/PsiElement;", "document", "Lcom/intellij/openapi/editor/Document;", "quick", "", "(Lcom/intellij/psi/PsiElement;Lcom/intellij/openapi/editor/Document;Z)[Lcom/intellij/lang/folding/FoldingDescriptor;", "computePlaceholderText", "", "node", "Lcom/intellij/lang/ASTNode;", "depth", "", "getPlaceholderText", "isCollapsedByDefault", "nodePlaceholderText", "valueNode", "currentDepth", "Companion", "BashSupport1_main"})
public final class BashVariableFoldingBuilder
  extends FoldingBuilderEx
  implements DumbAware
{
  private static final TokenSet skippedTextTokens;
  private static final int DEFAULT_DEPTH_OF_FOLDING = 1;
  public static final Companion Companion = new Companion(null); static { skippedTextTokens = TokenSet.create(new IElementType[] { BashTokenTypes.STRING_BEGIN, BashTokenTypes.STRING_END });
    DEFAULT_DEPTH_OF_FOLDING = 1; } @Metadata(mv = {1, 1, 1}, bv = {1, 0, 0}, k = 1, d1 = {"\000 \n\002\030\002\n\002\020\000\n\002\b\002\n\002\020\b\n\002\b\003\n\002\030\002\n\002\030\002\n\002\b\003\b\003\030\0002\0020\001B\007\b\002¢\006\002\020\002R\024\020\003\032\0020\004XD¢\006\b\n\000\032\004\b\005\020\006R\031\020\007\032\0070\b¢\006\002\b\tX\004¢\006\b\n\000\032\004\b\n\020\013¨\006\f"}, d2 = {"Lcom/ansorgit/plugins/bash/editor/codefolding/BashVariableFoldingBuilder$Companion;", "", "()V", "DEFAULT_DEPTH_OF_FOLDING", "", "getDEFAULT_DEPTH_OF_FOLDING", "()I", "skippedTextTokens", "Lcom/intellij/psi/tree/TokenSet;", "Lorg/jetbrains/annotations/NotNull;", "getSkippedTextTokens", "()Lcom/intellij/psi/tree/TokenSet;", "BashSupport1_main"}) public static final class Companion { private Companion() {} private final int getDEFAULT_DEPTH_OF_FOLDING() { return BashVariableFoldingBuilder.DEFAULT_DEPTH_OF_FOLDING; } private final TokenSet getSkippedTextTokens() { return BashVariableFoldingBuilder.skippedTextTokens; } }
   @NotNull
  public String getPlaceholderText(@NotNull ASTNode node) {
    Intrinsics.checkParameterIsNotNull(node, "node"); return computePlaceholderText(node, Companion.getDEFAULT_DEPTH_OF_FOLDING());
  }
  private final String computePlaceholderText(ASTNode node, int depth) {
    if (!(node.getPsi() instanceof BashVar)) node.getPsi();  BashVar bashVar = (BashVar)null;
    
    String replacement = node.getText();
    if (depth > 0 && bashVar != null && BashResolveUtil.hasStaticVarDefPath(bashVar)) {
      bashVar.getNeighborhoodReference(); if (!((bashVar.getNeighborhoodReference() != null) ? bashVar.getNeighborhoodReference().resolve() : null instanceof BashVarDefImpl)) (bashVar.getNeighborhoodReference() != null) ? bashVar.getNeighborhoodReference().resolve() : null;  (BashVarDefImpl)null; PsiElement varDefValue = ((BashVarDefImpl)null != null) ? ((BashVarDefImpl)null).findAssignmentValue() : null;
      if (varDefValue != null) {
        Intrinsics.checkExpressionValueIsNotNull(varDefValue.getNode(), "varDefValue.node"); replacement = nodePlaceholderText(varDefValue.getNode(), depth);
      } 
    } 
    
    Intrinsics.checkExpressionValueIsNotNull(replacement, "replacement"); return replacement;
  }
  
  private final String nodePlaceholderText(ASTNode valueNode, int currentDepth) {
    Object[] arrayOfObject1 = (Object[])valueNode.getChildren((TokenSet)null);





























    
    Object[] arrayOfObject2 = arrayOfObject1; ArrayList<Object> arrayList3 = new ArrayList();
    for (byte b = 0; b < arrayOfObject2.length; ) { Object element$iv$iv = arrayOfObject2[b]; ASTNode it = (ASTNode)element$iv$iv; if (!Companion.getSkippedTextTokens().contains(it.getElementType()))
        arrayList3.add(element$iv$iv);  }  ArrayList<Object> arrayList1 = arrayList3;
    ArrayList<Object> arrayList2 = arrayList1; Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault(arrayList1, 10));
    for (Object item$iv$iv : arrayList2) {
      ASTNode aSTNode = (ASTNode)item$iv$iv; Collection collection = destination$iv$iv; Intrinsics.checkExpressionValueIsNotNull(aSTNode, "it"); Intrinsics.checkExpressionValueIsNotNull(aSTNode.getText(), "it.text");
    }  return CollectionsKt.joinToString$default(destination$iv$iv, "", null, null, 0, null, null, 62, null); } @NotNull public FoldingDescriptor[] buildFoldRegions(@NotNull PsiElement root, @NotNull Document document, boolean quick) { Intrinsics.checkParameterIsNotNull(root, "root"); Intrinsics.checkParameterIsNotNull(document, "document");
    if (DumbService.isDumb(root.getProject()) || !BashProjectSettings.storedSettings(root.getProject()).isVariableFolding())
      return new FoldingDescriptor[0];  ArrayList descriptors = ContainerUtil.newArrayList(); BashPsiUtils.visitRecursively(root, new BashVariableFoldingBuilder$buildFoldRegions$1(descriptors)); ArrayList arrayList1 = descriptors;
    if (arrayList1 == null) throw new TypeCastException("null cannot be cast to non-null type java.util.Collection<T>");  Collection thisCollection$iv = arrayList1;
    if (thisCollection$iv.toArray((Object[])new FoldingDescriptor[thisCollection$iv.size()]) == null) throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");  return (FoldingDescriptor[])thisCollection$iv.toArray((Object[])new FoldingDescriptor[thisCollection$iv.size()]); }

  
  @Metadata(mv = {1, 1, 1}, bv = {1, 0, 0}, k = 1, d1 = {"\000\031\n\000\n\002\030\002\n\002\b\002\n\002\020\002\n\000\n\002\030\002\n\000*\001\000\b\n\030\0002\0020\001B\005¢\006\002\020\002J\020\020\003\032\0020\0042\006\020\005\032\0020\006H\026¨\006\007"}, d2 = {"com/ansorgit/plugins/bash/editor/codefolding/BashVariableFoldingBuilder$buildFoldRegions$1", "Lcom/ansorgit/plugins/bash/lang/psi/BashVisitor;", "(Ljava/util/ArrayList;)V", "visitVarUse", "", "bashVar", "Lcom/ansorgit/plugins/bash/lang/psi/api/vars/BashVar;", "BashSupport1_main"})
  public static final class BashVariableFoldingBuilder$buildFoldRegions$1 extends BashVisitor {
    BashVariableFoldingBuilder$buildFoldRegions$1(ArrayList $captured_local_variable$0) {}
    
    public void visitVarUse(@NotNull BashVar bashVar) {
      Intrinsics.checkParameterIsNotNull(bashVar, "bashVar");
      if (BashResolveUtil.hasStaticVarDefPath(bashVar))
        this.$descriptors.add(new FoldingDescriptor((PsiElement)bashVar, bashVar.getTextRange())); 
    }
  }
  
  public boolean isCollapsedByDefault(@NotNull ASTNode node) {
    Intrinsics.checkParameterIsNotNull(node, "node");
    return false;
  }
}
