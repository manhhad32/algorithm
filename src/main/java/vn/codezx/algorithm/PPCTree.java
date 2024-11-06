package vn.codezx.algorithm;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PPCTree {

  PPCNode root;
  private int preOrderCounter = 0;
  private int postOrderCounter = 0;

  public PPCTree() {
    root = new PPCNode("NULL"); // Initialize root with itemID as NULL
  }

  // Add transaction to the tree
  public void addTransaction(List<String> transaction) {
    PPCNode currentNode = root;
    for (String item : transaction) {
      PPCNode child = currentNode.findChild(item);
      if (child == null) {
        currentNode = currentNode.addChild(item);
      } else {
        child.count++;
        currentNode = child;
      }
    }
  }

  // Assign PreOrder and PostOrder numbers
  public void assignPrePostOrder(PPCNode node) {
   if (node == null) {
    return;
   }

    node.preOrder = preOrderCounter++;
    for (PPCNode child : node.children) {
      assignPrePostOrder(child);
    }
    node.postOrder = postOrderCounter++;
  }

  // Display tree with PreOrder and PostOrder numbers
  public void displayTree(PPCNode node, String indent) {
   if (node == null) {
    return;
   }

    System.out.println(indent + "Item: " + node.itemID + ", Count: " + node.count +
        ", PreOrder: " + node.preOrder + ", PostOrder: " + node.postOrder);

    for (PPCNode child : node.children) {
      displayTree(child, indent + "  ");
    }
  }

  // Step 6: Generate N-lists for each item
  public Map<String, List<PPCNode>> generateNLists() {
    Map<String, List<PPCNode>> nLists = new HashMap<>();
    populateNLists(root, nLists);
    return nLists;
  }

  // Recursive function to populate N-lists
  private void populateNLists(PPCNode node, Map<String, List<PPCNode>> nLists) {
   if (node == null) {
    return;
   }

    if (!node.itemID.equals("NULL")) { // Ignore root with itemID "NULL"
      nLists.computeIfAbsent(node.itemID, k -> new ArrayList<>()).add(node);
    }

    for (PPCNode child : node.children) {
      populateNLists(child, nLists);
    }
  }
}
