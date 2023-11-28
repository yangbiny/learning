package com.learning.datastructure;

import java.util.Stack;

class MyQueue {

  private final Stack<Integer> first;
  private final Stack<Integer> second;

  public MyQueue() {
    first = new Stack<>();
    second = new Stack<>();
  }

  public void push(int x) {
    first.push(x);
  }

  public int pop() {
    if (second.isEmpty()) {
      revert();
    }
    return second.pop();
  }

  private void revert() {
    if (first.isEmpty()) {
      return;
    }
    while (!first.isEmpty()) {
      second.push(first.pop());
    }
  }

  public int peek() {
    if (second.isEmpty()) {
      revert();
    }
    return second.peek();
  }

  public boolean empty() {
    return first.isEmpty() && second.isEmpty();
  }
}