package com.learning.datastructure.search;

import com.learning.datastructure.TreeNode;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author impassive
 */
public class TreeNodeUtils {

  public static void main(String[] args) {
    Integer[] array = {5, 4, 8, 11, null, 13, 4, 7, 2, null, null, null, 1};
    TreeNode treeNode = createTreeNode(array);
    System.out.println(treeNode);
  }

  public static TreeNode createTreeNode(Integer[] array) {
    if (array == null || array.length == 0) {
      return null;
    }
    // 5,4,8,11,null,13,4,7,2,null,null,null,1
    TreeNode root = new TreeNode(array[0]);

    Queue<TreeNode> queue = new LinkedList<>();
    queue.add(root);
    for (int i = 1; i < array.length; ) {
      int size = queue.size();
      while (size > 0) {
        TreeNode node = queue.poll();
        if (node == null) {
          break;
        }

        if (i < array.length && array[i] != null) {
          node.left = new TreeNode(array[i]);
          queue.add(node.left);
        } else {
          queue.add(null);
        }
        i++;

        if (i < array.length && array[i] != null) {
          node.right = new TreeNode(array[i]);
          queue.add(node.right);
        } else {
          queue.add(null);
        }
        i++;

        size--;
      }

    }

    return root;
  }


}
