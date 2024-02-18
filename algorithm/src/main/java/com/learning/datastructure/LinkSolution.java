package com.learning.datastructure;

import java.util.Stack;

/**
 * @author impassive
 */
public class LinkSolution {

  public static void main(String[] args) {
    LinkSolution link = new LinkSolution();

    partitionLink(link);

    addTwoNumbers(link);

    reverseKGroup(link);

    removeNthFormEnd(link);

    detectCycle(link);

    recordList(link);

    reverseBetween(link);

    deleteDuplicates(link);

  }

  private static void partitionLink(LinkSolution link) {
    ListNode listNode = new ListNode(1,
        new ListNode(4, new ListNode(3, new ListNode(2, new ListNode(5, new ListNode(2))))));

//    ListNode listNode = new ListNode(2, new ListNode(1));

    ListNode partition = link.partition(listNode, 3);
    System.out.println(partition);
  }

  public ListNode partition(ListNode head, int x) {
    ListNode first = new ListNode(-1);
    ListNode second = new ListNode(-1);

    ListNode ftmp = first;
    ListNode stmp = second;

    ListNode h = head;
    while (h != null) {
      if (h.val < x) {
        first.next = h;
        first = first.next;
      } else {
        second.next = h;
        second = second.next;
      }
      ListNode next = h.next;
      h.next = null;
      h = next;
    }
    first.next = stmp.next;

    return ftmp.next;
  }


  private static void addTwoNumbers(LinkSolution link) {
    ListNode l1 = new ListNode(2, new ListNode(4, new ListNode(3, new ListNode(9))));
    ListNode l2 = new ListNode(5, new ListNode(6, new ListNode(4)));

    ListNode listNode = link.addTwoNumbers(l1, l2);
    System.out.println(listNode);
  }


  private static void reverseKGroup(LinkSolution link) {
    ListNode head = new ListNode(
        1,
        new ListNode(2,
            new ListNode(3,
                new ListNode(4,
                    new ListNode(5))))
    );

    ListNode listNode = link.reverseKGroup(head, 3);
    System.out.println(listNode);
  }

  private static void removeNthFormEnd(LinkSolution link) {
    ListNode head = new ListNode(
        10,
        new ListNode(9,
            new ListNode(8,
                new ListNode(7,
                    new ListNode(6,
                        new ListNode(5,
                            new ListNode(4,
                                new ListNode(3,
                                    new ListNode(2,
                                        new ListNode(1)))))))))
    );

    ListNode listNode = link.removeNthFromEnd(head, 2);
    System.out.println(listNode);
  }

  private static void deleteDuplicates(LinkSolution link) {
    ListNode head = new ListNode(1,
        new ListNode(1, new ListNode(1, new ListNode(1, new ListNode(1)))));
    ListNode node2 = link.deleteDuplicates(head);
    System.out.println(node2);
  }

  private static void reverseBetween(LinkSolution link) {
    ListNode node = new ListNode(1,
        new ListNode(2, new ListNode(3, new ListNode(4, new ListNode(5)))));

    ListNode node1 = link.reverseBetween(node, 2, 4);
    System.out.println(node1);
  }

  private static void recordList(LinkSolution link) {
    ListNode head1 = new ListNode(1, new ListNode(2, new ListNode(3, new ListNode(4))));
    link.reorderList(head1);
  }

  private static void detectCycle(LinkSolution link) {
    ListNode next3 = new ListNode(3);
    ListNode next2 = new ListNode(2);
    ListNode next0 = new ListNode(0);
    ListNode next_4 = new ListNode(-4);

    next3.next = next2;
    next2.next = next0;
    next0.next = next_4;
    next_4.next = next2;

    ListNode listNode = link.detectCycle(next3);
    System.out.println(listNode);
  }

  public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
    ListNode result = new ListNode(-1);
    ListNode tmp = result;

    int value = 0;
    while (l1 != null || l2 != null) {
      int val = l1 != null ? l1.val : 0;
      int val2 = l2 != null ? l2.val : 0;

      int tp = val + val2 + value;
      if (tp >= 10) {
        tp = tp % 10;
        value = 1;
      } else {
        value = 0;
      }
      tmp.next = new ListNode(tp);
      tmp = tmp.next;
      l1 = l1 != null ? l1.next : null;
      l2 = l2 != null ? l2.next : null;
    }
    if (value > 0) {
      tmp.next = new ListNode(value);
    }
    return result.next;
  }

  public ListNode mergeKLists(ListNode[] lists) {
    if (lists.length == 0) {
      return null;
    }
    if (lists.length == 1) {
      return lists[0];
    }

    ListNode result = merge(lists[0], lists[1]);

    for (int i = 2; i < lists.length; i++) {
      result = merge(result, lists[i]);
    }
    return result;
  }

  private ListNode merge(ListNode first, ListNode second) {
    ListNode result = new ListNode(-1);

    ListNode tmp = result;
    while (first != null && second != null) {

      if (first.val < second.val) {
        ListNode next = first.next;
        first.next = null;
        tmp.next = first;
        first = next;
      } else {
        ListNode next = second.next;
        second.next = null;
        tmp.next = second;
        second = next;
      }
      tmp = tmp.next;
    }

    if (first != null) {
      tmp.next = first;
    }
    if (second != null) {
      tmp.next = second;
    }
    return result.next;
  }

  public ListNode reverseKGroup(ListNode head, int k) {
    ListNode result = new ListNode(-1);

    Stack<ListNode> stack = new Stack<>();
    ListNode tmp = result;
    while (head != null) {
      int cnt = 0;

      ListNode ptr = head;
      while (ptr != null && cnt < k) {
        ptr = ptr.next;
        cnt++;
      }
      if (cnt == k) {
        while (cnt > 0 && head != null) {
          ListNode next = head.next;
          head.next = null;
          stack.push(head);
          head = next;
          cnt--;
        }
        while (!stack.isEmpty()) {
          tmp.next = stack.pop();
          tmp = tmp.next;
        }
      } else {
        tmp.next = head;
        break;
      }
    }

    return result.next;
  }


  public ListNode removeNthFromEnd(ListNode head, int n) {
    if (head == null) {
      return null;
    }
    ListNode tmp = head;
    int cnt = 0;
    while (tmp != null) {
      cnt++;
      tmp = tmp.next;
    }
    int index = cnt - n;
    if (index == 0) {
      return head.next;
    }

    tmp = head;
    while (index > 0) {
      ListNode next = tmp.next;
      if (index == 1) {
        tmp.next = next.next;
        break;
      }
      tmp = next;
      index--;
    }

    return head;
  }


  public ListNode detectCycle(ListNode head) {
    if (head == null) {
      return null;
    }
    ListNode slow = head;
    ListNode fast = head;

    while (slow != null && fast != null) {
      slow = slow.next;
      if (fast.next == null) {
        return null;
      }
      fast = fast.next.next;
      if (slow == fast) {
        ListNode ptr = head;
        while (ptr != slow) {
          ptr = ptr.next;
          slow = slow.next;
        }
        return slow;
      }
    }

    return null;
  }


  public ListNode detectCycle1(ListNode head) {
    if (head == null || head.next == null) {
      return null;
    }
    ListNode slow = head;
    ListNode fast = head.next.next;

    while (slow != null && fast != null) {
      if (slow == fast) {
        ListNode ptr = head;
        while (ptr != slow) {
          ptr = ptr.next;
          slow = slow.next;
        }
        return slow;
      }
      slow = slow.next;
      if (fast.next == null) {
        return null;
      }
      fast = fast.next.next;
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
