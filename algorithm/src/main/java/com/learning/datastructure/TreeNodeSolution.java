package com.learning.datastructure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.LinkedTransferQueue;

/**
 * @author impassive
 */
public class TreeNodeSolution {


  public List<Integer> preorderTraversal2(TreeNode root) {
    ArrayList<Integer> result = new ArrayList<>();
    if (root == null) {
      return result;
    }
    Stack<TreeNode> stack = new Stack<>();
    stack.push(root);

    while (!stack.isEmpty()) {
      TreeNode pop = stack.pop();
      result.add(pop.val);
      if (pop.right != null) {
        stack.push(pop.right);
      }
      if (pop.left != null) {
        stack.push(pop.left);
      }
    }

    return result;
  }


  public List<Integer> preorderTraversal(TreeNode root) {
    if (root == null) {
      return null;
    }

    List<Integer> result = new ArrayList<>();
    preorder(root.right, result);
    return result;
  }

  private void preorder(TreeNode root, List<Integer> result) {
    if (root == null) {
      return;
    }
    result.add(root.val);

    preorder(root.left, result);
    preorder(root.right, result);
  }


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
