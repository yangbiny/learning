package com.learning.datastructure.search;

import com.learning.datastructure.TreeNode;

/**
 * @author impassive
 */
public class TreeSolution {

  public static void main(String[] args) {
    TreeSolution treeSolution = new TreeSolution();
    treeSolution.islandPerimeter();
    treeSolution.numIslands();
    treeSolution.solve();
    treeSolution.hasPathSum();
  }


  private void hasPathSum() {
    Integer[] arr = {5, 4, 8, 11, null, 13, 4, 7, 2, null, null, null, 1};
    TreeNode treeNode = TreeNodeUtils.createTreeNode(arr);
    boolean b = hasPathSum(treeNode, 22);
    System.out.println(b);
  }

  private void solve() {
    char[][] board = {
        {'X', 'X', 'X', 'X'},
        {'X', 'O', 'O', 'X'},
        {'X', 'X', 'O', 'X'},
        {'X', 'O', 'X', 'X'}
    };
    solve(board);
    System.out.println(board);
  }

  public void numIslands() {
    char[][] grid = {
        {'1', '1', '1', '1', '0'},
        {'1', '1', '0', '1', '0'},
        {'1', '1', '0', '0', '0'},
        {'0', '0', '0', '0', '0'}
    };
    int i = numIslands(grid);
    System.out.println(i);
  }

  private void islandPerimeter() {
    int[][] grid = {
        {0, 1, 0, 0},
        {1, 1, 1, 0},
        {0, 1, 0, 0},
        {1, 1, 0, 0}
    };
    int i = islandPerimeter(grid);
    System.out.println(i);
  }

  public int islandPerimeter(int[][] grid) {
    int result = 0;
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[i].length; j++) {
        if (grid[i][j] == 1) {
          result = dfsIslandPerimeter(grid, i, j);
        }
      }
    }
    return result;
  }


  private int dfsIslandPerimeter(int[][] grid, int i, int j) {
    if (i < 0 || j < 0 || i >= grid.length || j >= grid[i].length) {
      return 1;
    }
    if (grid[i][j] == 0) {
      return 1;
    }
    if (grid[i][j] != 1) {
      return 0;
    }
    grid[i][j] = 2;
    return dfsIslandPerimeter(grid, i - 1, j) +
        dfsIslandPerimeter(grid, i + 1, j) +
        dfsIslandPerimeter(grid, i, j - 1) +
        dfsIslandPerimeter(grid, i, j + 1);
  }


  /**
   * leetcode 695
   */
  public int maxAreaOfIsland(int[][] grid) {
    int result = 0;
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[i].length; j++) {
        if (grid[i][j] == '1') {
          int i1 = dfsMaxAreaOfIsland(grid, i, j);
          if (i1 > result) {
            result = i1;
          }
        }
      }
    }
    return result;
  }

  private int dfsMaxAreaOfIsland(int[][] grid, int i, int j) {
    if (i < 0 || j < 0 || i >= grid.length || j >= grid[i].length) {
      return 0;
    }
    if (grid[i][j] != 1) {
      return 0;
    }
    grid[i][j] = 2;
    return 1 +
        dfsMaxAreaOfIsland(grid, i - 1, j) +
        dfsMaxAreaOfIsland(grid, i + 1, j) +
        dfsMaxAreaOfIsland(grid, i, j - 1) +
        dfsMaxAreaOfIsland(grid, i, j + 1);
  }

  /**
   * leetcode 200
   */
  public int numIslands(char[][] grid) {
    int result = 0;
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[i].length; j++) {
        if (grid[i][j] == '1') {
          dfsIslands(grid, i, j);
          result += 1;
        }
      }
    }
    return result;
  }


  private int dfsIslands(char[][] grid, int i, int j) {
    if (i < 0 || j < 0 || i >= grid.length || j >= grid[i].length) {
      return 0;
    }
    if (grid[i][j] != '1') {
      return 0;
    }
    grid[i][j] = '2';
    return 1 +
        dfsIslands(grid, i - 1, j) +
        dfsIslands(grid, i + 1, j) +
        dfsIslands(grid, i, j - 1) +
        dfsIslands(grid, i, j + 1);
  }


  public void solve(char[][] board) {
    if (board == null || board.length == 0) {
      return;
    }

    boolean[][] visited = new boolean[board.length][board[0].length];

    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        if (board[i][j] == 'X') {
          visited[i][j] = true;
          continue;
        }
      }
    }

  }

  private void dfs(char[][] board, int i, int j) {

    if (i < 0 || j < 0 || i >= board.length || j >= board[i].length || board[i][j] == 'X') {
      return;
    }

    dfs(board, i - 1, j);
    dfs(board, i + 1, j);
    dfs(board, i, j - 1);
    dfs(board, i, j + 1);
  }


  public boolean hasPathSum(TreeNode root, int targetSum) {
    if (root == null) {
      return false;
    }
    if (root.val == targetSum && root.left == null && root.right == null) {
      return true;
    }

    int target = targetSum - root.val;
    boolean left = hasPathSum(root.left, target);
    boolean right = hasPathSum(root.right, target);
    return left || right;
  }


}



