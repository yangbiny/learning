package com.learning.datastructure;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

/**
 * @author impassive
 */
public class TreeNodeBuilder {


  public static TreeNode buildTree(List<Integer> datas) {
    TreeNode root = new TreeNode(datas.get(0));

    Queue<TreeNode> queue = new ArrayDeque<>();
    queue.add(root);
    int index = 1;

    while (!queue.isEmpty()) {
      TreeNode poll = queue.poll();

      if (index <= datas.size() - 1) {
        Integer x = datas.get(index++);
        if (x >= 0) {
          TreeNode left = new TreeNode(x);
          poll.left = left;
          queue.add(left);
        }
      }
      if (index + 1 <= datas.size()) {
        Integer x = datas.get(index++);
        if (x >= 0) {
          TreeNode right = new TreeNode(x);
          poll.right = right;
          queue.add(right);
        }
      }

    }

    return root;
  }


}
