package com.learning.datastructure;

import java.util.List;

/**
 * @author impassive
 */
public class LowestCommonAncestor {

  public static void main(String[] args) {
    List<Integer> data = List.of(3, 5, 1, 6, 2, 0, 8, -1, -1, 7, 4);
    TreeNode treeNode = TreeNodeBuilder.buildTree(data);
    LowestCommonAncestor lowestCommonAncestor = new LowestCommonAncestor();
    TreeNode result = lowestCommonAncestor.lowestCommonAncestor(
        treeNode,
        new TreeNode(6),
        new TreeNode(2)
    );
    System.out.println(result.val);
  }

  public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
    if (root == null || root.val == p.val || root.val == q.val) {
      return root;
    }

    TreeNode left = lowestCommonAncestor(root.left, p, q);
    TreeNode right = lowestCommonAncestor(root.right, p, q);
    if (left == null) {
      return right;
    }
    if (right == null) {
      return left;
    }
    return root;
  }
}
