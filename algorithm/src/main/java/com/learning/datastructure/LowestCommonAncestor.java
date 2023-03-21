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
    // 如果左子树 为空，说明都在 右子树，返回右子数的结果
    if (left == null) {
      return right;
    }
    // 与上面相反
    if (right == null) {
      return left;
    }
    // 都不为空，说明在两边，返回根节点
    return root;
  }
}
