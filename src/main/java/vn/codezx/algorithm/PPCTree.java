package vn.codezx.algorithm;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PPCTree {

  PPCNode root;
  private int preOrderCounter = 0;
  private int postOrderCounter = 0;
  private int minSupport;

  public PPCTree() {
    root = new PPCNode("NULL"); // Initialize root with itemID as NULL
  }
  public void setMinSupport(int minSupport) {
    this.minSupport =minSupport;
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


    System.out.println(indent + "{(" + node.preOrder + "," + node.postOrder +"), "
        + node.itemID  + ":" + node.count + "}");


    for (PPCNode child : node.children) {
      displayTree(child, indent + "  ");
    }
  }

  // Step 6: Generate N-lists for each item
  public Map<String,List<PPCNode>> generateNLists() {
    Map<String, List<PPCNode>> nLists = new HashMap<>();
    populateNLists(root, nLists);
    return nLists;
  }

  //Generate N-lists for Fn item base on Fn-1
  public Map<String, List<PPCNode>> generateNewPPCCode(Map<String, List<PPCNode>> nListF1) {
    Map<String, List<PPCNode>> nListFn = new HashMap<>();
    List<String> itemSets = nListF1.keySet().stream().collect(Collectors.toList());
    int n = itemSets.size();
    for (int i = 0; i < n; i++) {
      List<PPCNode> parentNodes = nListF1.get(itemSets.get(i));

      for(int j = i+1; j < n; j++ ) {
        List<PPCNode> chilNodes = nListF1.get(itemSets.get(j));
        List<PPCNode> newNodes = new ArrayList<>();
        int m = parentNodes.size();
        int support = 0;
        for(int idp = 0; idp < m; idp++) {
          for(int idc = 0; idc < chilNodes.size(); idc++) {
            PPCNode newNode = createNewNode(parentNodes.get(idp), chilNodes.get(idc));
            if(newNode != null) {
              newNodes.add(newNode);
              support += newNode.count;
            }
          }
        }
        if(support >= this.minSupport) {
          nListFn.put(parentNodes.get(0).itemID.concat(chilNodes.get(0).itemID), newNodes);
        }
      }
    }
    return nListFn;
  }

  private boolean checkMerge(PPCNode parrent, PPCNode child) {
    return (parrent.preOrder < child.preOrder) && (parrent.postOrder > child.postOrder);
  }
  private PPCNode createNewNode(PPCNode parrent, PPCNode child) {
    PPCNode newNode = null;
    if(checkMerge(parrent, child)){
      String newName = parrent.itemID.concat(child.itemID);
      newNode = new PPCNode(newName, parrent.preOrder, parrent.postOrder, child.count);
    }
    return newNode;
  }

  private int supportPPCNode(List<PPCNode> listNode) {
    int support = 0;
    for(PPCNode ppcNode : listNode) {
      support += ppcNode.count;
    }
    return support;
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
