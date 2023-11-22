package com.learning.datastructure;

import java.util.List;
import java.util.Stack;

/**
 * @author impassive
 */
public class RevertLink {

  public static void main(String[] args) {
    RevertLink link = new RevertLink();

    ListNode next = new ListNode(1);
    ListNode next1 = new ListNode(2);
    next.next = next1;
    next1.next = next;
    ListNode detectCycle = new ListNode(3, next);

    link.detectCycle(next);

    ListNode head1 = new ListNode(1, new ListNode(2, new ListNode(3, new ListNode(4))));
    link.reorderList(head1);

    ListNode node = new ListNode(1,
        new ListNode(2, new ListNode(3, new ListNode(4, new ListNode(5)))));

    ListNode node1 = link.reverseBetween(node, 2, 4);
    System.out.println(node1);

    ListNode head = new ListNode(1,
        new ListNode(1, new ListNode(1, new ListNode(1, new ListNode(1)))));
    ListNode node2 = link.deleteDuplicates(head);
    System.out.println(node2);
  }

  public ListNode detectCycle(ListNode head) {
    if (head == null || head.next == null) {
      return null;
    }

    ListNode slow = head;
    ListNode fast = head.next.next;

    while (slow != null && fast != null) {
      if (slow == fast) {
        return slow;
      }
      slow = slow.next;
      fast = fast.next;
    }

    return null;
  }

  public void reorderList(ListNode head) {
    int n = 0;
    ListNode tmp = head;
    while (tmp != null) {
      n += 1;
      tmp = tmp.next;
    }
    tmp = head;
    Stack<ListNode> nodes = new Stack<>();
    int index = 1;
    int max = n % 2 == 0 ? n / 2 : n / 2 + 1;
    while (tmp != null) {
      ListNode x = tmp.next;
      index++;
      if (index > max) {
        tmp.next = null;
        if (index - 1 > max) {
          nodes.push(tmp);
        }
      }
      tmp = x;
    }

    tmp = head;
    while (!nodes.isEmpty()) {
      ListNode pop = nodes.pop();
      pop.next = tmp.next;
      tmp.next = pop;
      tmp = tmp.next.next;
    }

  }

  public boolean hasCycle(ListNode head) {
    if (head == null || head.next == null) {
      return false;
    }
    ListNode fast = head.next.next;
    ListNode slow = head;

    while (slow != fast) {
      if (slow == null || fast == null || fast.next == null) {
        return false;
      }
      slow = slow.next;
      fast = fast.next.next;
    }
    return true;
  }


  public ListNode deleteDuplicates(ListNode head) {
    if (head.next == null) {
      return head;
    }
    ListNode result = new ListNode(-1);

    ListNode tmp = result;
    int prev = head.val;
    while (head != null) {

      if (head.next == null) {
        tmp.next = head;
        break;
      }
      if (prev == head.val || head.val == head.next.val) {
        prev = head.val;
        head = head.next.next;
        continue;
      }

      ListNode next = head.next;
      head.next = null;
      tmp.next = head;
      head = next;
    }
    return result.next;
  }

  public ListNode reverseBetween(ListNode head, int left, int right) {
    ListNode listNode = new ListNode(-1);
    listNode.next = head;

    ListNode leftNode = listNode;
    ListNode rightNode = listNode;

    for (int i = 0; i < left - 1; i++) {
      leftNode = leftNode.next;
    }

    for (int i = 0; i < right; i++) {
      rightNode = rightNode.next;
    }

    leftNode.next = reverse(leftNode.next, rightNode);
    return listNode.next;
  }

  private ListNode reverse(ListNode nextNode, ListNode rightNode) {

    ListNode after = rightNode.next;
    rightNode.next = null;

    ListNode result = new ListNode(-1);
    result.next = after;

    while (true) {
      boolean needBreak = nextNode == rightNode;
      ListNode next = nextNode.next;
      nextNode.next = result.next;
      result.next = nextNode;
      nextNode = next;
      if (needBreak) {
        break;
      }
    }
    return result.next;
  }
}
