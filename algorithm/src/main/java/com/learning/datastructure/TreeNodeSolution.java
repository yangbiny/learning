package com.learning.datastructure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedTransferQueue;

/**
 * @author impassive
 */
public class TreeNodeSolution {


  public List<List<Integer>> levelOrder(TreeNode root) {
    if (root == null) {
      return Collections.emptyList();
    }
    Queue<TreeNode> queue = new LinkedTransferQueue<>();
    queue.offer(root);

    List<List<Integer>> result = new ArrayList<>();
    while (!queue.isEmpty()) {
      int size = queue.size();
      List<Integer> tmp = new ArrayList<>();
      while (size > 0) {
        TreeNode poll = queue.poll();
        if (poll == null) {
          break;
        }
        tmp.add(poll.val);
        if (poll.left != null) {
          queue.add(poll.left);
        }
        if (poll.right != null) {
          queue.add(poll.right);
        }
        size--;
      }
      result.add(tmp);
    }
    return result;
  }

}
