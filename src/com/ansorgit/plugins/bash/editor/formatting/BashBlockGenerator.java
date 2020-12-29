package com.ansorgit.plugins.bash.editor.formatting;

import com.ansorgit.plugins.bash.editor.formatting.processor.BashIndentProcessor;
import com.ansorgit.plugins.bash.lang.parser.BashElementTypes;
import com.intellij.formatting.Alignment;
import com.intellij.formatting.Block;
import com.intellij.formatting.Indent;
import com.intellij.formatting.Wrap;
import com.intellij.lang.ASTNode;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import java.util.ArrayList;
import java.util.List;



























public class BashBlockGenerator
  implements BashElementTypes
{
  private static Alignment myAlignment;
  private static Wrap myWrap;
  private static CodeStyleSettings mySettings;
  
  public static List<Block> generateSubBlocks(ASTNode node, Alignment _myAlignment, Wrap _myWrap, CodeStyleSettings _mySettings, BashBlock block) {
    myWrap = _myWrap;
    mySettings = _mySettings;
    myAlignment = _myAlignment;

    
    List<Block> subBlocks = new ArrayList<>();
    ASTNode[] children = getBashChildren(node);
    ASTNode prevChildNode = null;
    for (ASTNode childNode : children) {
      if (canBeCorrectBlock(childNode)) {
        Indent indent = BashIndentProcessor.getChildIndent(block, prevChildNode, childNode);
        subBlocks.add(new BashBlock(childNode, myAlignment, indent, myWrap, mySettings));
        prevChildNode = childNode;
      } 
    } 
    return subBlocks;
  }





  
  private static boolean canBeCorrectBlock(ASTNode node) {
    return (node.getText().trim().length() > 0);
  }
  
  private static ASTNode[] getBashChildren(ASTNode node) {
    return node.getChildren(null);
  }
}
