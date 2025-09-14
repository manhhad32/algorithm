package vn.codezx.algorithm.advance;


import java.util.ArrayList;
import java.util.List;

public class PPCNode {
  public String itemID;
  int count;
  int preOrder;
  int postOrder;
  List<PPCNode> children = new ArrayList<>();

  public PPCNode(String itemID) {
    this.itemID = itemID;
    this.count = 0;
  }

  public PPCNode addChild(String itemID) {
    PPCNode child = new PPCNode(itemID);
    this.children.add(child);
    return child;
  }

}
