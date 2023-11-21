package com.learning.datastructure;

/**
 * @author impassive
 */
public class RevertLink {

  public static void main(String[] args) {
    RevertLink link = new RevertLink();

    ListNode node = new ListNode(1,
        new ListNode(2, new ListNode(3, new ListNode(4, new ListNode(5)))));

    ListNode node1 = link.reverseBetween(node, 2, 4);
    System.out.println(node1);

    ListNode head = new ListNode(1,
        new ListNode(1, new ListNode(1, new ListNode(1, new ListNode(1)))));
    ListNode node2 = link.deleteDuplicates(head);
    System.out.println(node2);
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
