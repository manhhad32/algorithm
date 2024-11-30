package vn.codezx.algorithm;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

  public List<List<PPCNode>> genrateNList(List<List<String>> itemFrequency) {
    Map<String, List<PPCNode>> nLists = new HashMap<>();
    populateNLists(root, nLists);
    List<List<PPCNode>> lnList = new ArrayList<>();
    Set<String> uniqueItemSet = new HashSet<>();

    for (Map.Entry<String, List<PPCNode>> entry : nLists.entrySet()) {
      List<String> item = new ArrayList<>();
      List<PPCNode> nodes = new ArrayList<>();
      for (PPCNode node : entry.getValue()) {
        nodes.add(node);
        if(uniqueItemSet.add(node.itemID)) {
          item.add(node.itemID);
          itemFrequency.add(item);
        }
      }
      lnList.add(nodes);
    }

    return lnList;
  }

  //Generate N-lists for Fn item base on Fn-1
  public List<List<PPCNode>> generateNewPPCCode( List<List<PPCNode>> nListF1, List<List<String>> itemFrequency) {
    List<List<PPCNode>> nListFn = new ArrayList<>();
    Set<String> uniqueNode = new HashSet<>();
    Set<String> uniqueItemFrequency = new HashSet<>();
    int n = nListF1.size();
    for (int i = 0; i < n; i++) {
      List<PPCNode> parentNodes = nListF1.get(i);
      for(int j = i+1; j < n; j++) {
        List<PPCNode> chilNodes = nListF1.get(j);
        List<PPCNode> newNodes = new ArrayList<>();
        int support = 0;
        for (PPCNode parentNode : parentNodes) {
          for (PPCNode chilNode : chilNodes) {
            PPCNode newNode = createNewNode(parentNode, chilNode);
            if (newNode != null) {
              String uniqueValue = Integer.toString(newNode.preOrder)
                  .concat(Integer.toString(newNode.postOrder)).concat(Integer.toString(newNode.count));
              if(uniqueNode.add(uniqueValue)) {
                newNodes.add(newNode);
                support += newNode.count;
              }
            }
          }

        }
        List<String> item = new ArrayList<>();
        if (support >= this.minSupport) {
          nListFn.add(newNodes);
          for(PPCNode node : newNodes) {
            if(uniqueItemFrequency.add(node.itemID)) {
              item.add(node.itemID);
            }
          }
          itemFrequency.add(item);
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
      String newName = normalizationNameNode(parrent.itemID.concat(",").concat(child.itemID));
      newNode = new PPCNode(newName, parrent.preOrder, parrent.postOrder, child.count);
    }
    return newNode;
  }
  private String normalizationNameNode(String s) {
    String newName = "";
    String[] items = s.split(",");
    List<String> uniqueItems = new ArrayList<>();
    Set<String> uniqueItemSet= new HashSet<>();
    for(String item : items) {
      if(uniqueItemSet.add(item)) {
        uniqueItems.add(item);
      }
    }
    newName = String.join(",", uniqueItems);
    return newName ;
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
