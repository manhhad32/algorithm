package vn.codezx.algorithm;


import java.util.ArrayList;
import java.util.List;

public class PPCNode {

  String itemID;
  int count;
  int preOrder;
  int postOrder;
  List<PPCNode> children;

  public PPCNode(String itemID) {
    this.itemID = itemID;
    if ("NULL".equals(itemID)) {
      this.count = 0;
    } else{
      this.count = 1; // Mặc định là 1 khi nút được tạo
    }

    this.preOrder = -1; // Chưa được gán mã PreOrder
    this.postOrder = -1; // Chưa được gán mã PostOrder
    this.children = new ArrayList<>();
  }

  public PPCNode(String itemID, int preOrder, int postOrder, int count) {
    this.itemID = itemID;
    this.preOrder = preOrder;
    this.postOrder = postOrder;
    this.count = count;
    this.children = new ArrayList<>();
  }

  // Tìm nút con với itemID đã cho
  public PPCNode findChild(String itemID) {
    for (PPCNode child : children) {
      if (child.itemID.equals(itemID)) {
        return child;
      }
    }
    return null;
  }

  // Thêm nút con mới
  public PPCNode addChild(String itemID) {
    PPCNode childNode = new PPCNode(itemID);
    children.add(childNode);
    return childNode;
  }

}
