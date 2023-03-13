package com.learning.datastructure;

import java.util.List;

/**
 * @author impassive
 */
public class DeleteLinkListLastNNode {

  public static void main(String[] args) {
    DeleteLinkListLastNNode deleteLinkListLastNNode = new DeleteLinkListLastNNode();

    ListNode listNode = new ListNode(1);
    listNode.next = new ListNode(2);
    listNode.next.next = new ListNode(3);
    listNode.next.next.next = new ListNode(4);

    ListNode listNode1 = deleteLinkListLastNNode.removeNthFromEnd(listNode, 4);
    System.out.println(listNode1);
  }


  public ListNode removeNthFromEnd(ListNode head, int n) {
    int cnt = 1;
    ListNode tmpHead = head;
    while (tmpHead.next != null) {
      cnt++;
      tmpHead = tmpHead.next;
    }

    int index = cnt - n;
    if (index == 0) {
      return head.next;
    }

    tmpHead = head;
    while (index > 1 && tmpHead != null) {
      tmpHead = tmpHead.next;
      index--;
    }
    if (tmpHead != null && tmpHead.next != null) {
      tmpHead.next = tmpHead.next.next;
    }
    return head;
  }
}
