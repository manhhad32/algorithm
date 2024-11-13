package vn.codezx.algorithm;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    Map<String, List<PPCNode>> result = generateNewPPCCode(nLists);
    return result;
  }

  private Map<String, List<PPCNode>> generateNewPPCCode(Map<String, List<PPCNode>> nListF1) {
    List<String> itemSets = nListF1.keySet().stream().collect(Collectors.toList());
    int sizeItemSets = itemSets.size();
    int step = 1;
    for(int i = 0; i <= sizeItemSets - step; i++) {
      List<PPCNode> nodeF1i =  nListF1.get(itemSets.get(i));
      if(nodeF1i == null) {
        continue;
      }
      int idxF1i = 0;
      for(int j = i + 1; j < sizeItemSets;  j++ ) {
        if(idxF1i < nodeF1i.size()) {
          PPCNode nodei = nodeF1i.get(idxF1i);
          List<PPCNode> nodeF1j = nListF1.get(itemSets.get(j));
          if ((nodeF1j == null)) {
            continue;
          }
          int numM = 1;
          String newId = nodei.itemID;
          List<PPCNode> nPPCNodes = new ArrayList<>();
          for (int k = 0; k < nodeF1j.size(); k++) {
            if (numM > step) {
              continue;
            }
            PPCNode ppcNodej = nodeF1j.get(k);
            if ((ppcNodej.preOrder < nodei.preOrder) || (ppcNodej.postOrder > nodei.postOrder)) {
              continue;
            }
            newId = newId.concat(",").concat(ppcNodej.itemID);
            int preOrder = nodei.preOrder;
            int postOrder = nodei.postOrder;
            int count;
            if ((ppcNodej.preOrder == nodei.preOrder) && (ppcNodej.postOrder == nodei.postOrder)) {
              count = ppcNodej.count + nodei.count;
            } else {
              count = ppcNodej.count;
            }
            PPCNode nPPCNode = new PPCNode(newId, preOrder, postOrder, count);
            nPPCNodes.add(nPPCNode);
            numM++;
          }
          if (!nPPCNodes.isEmpty()) {
            nListF1.put(newId, nPPCNodes);
          }
          step++;
        }
        idxF1i++;
      }


    }
    return nListF1;
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
