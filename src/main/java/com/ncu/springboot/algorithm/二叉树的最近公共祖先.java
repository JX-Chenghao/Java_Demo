package com.ncu.springboot.algorithm;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 给定一个二叉树, 找到该树中两个指定节点的最近公共祖先。
 * <p>
 * 百度百科中最近公共祖先的定义为：“对于有根树 T 的两个节点 p、q，最近公共祖先表示为一个节点 x，满足 x 是 p、q 的祖先且 x 的深度尽可能大（一个节点也可以是它自己的祖先）。”
 * <p>
 * <p>
 * <p>
 * 示例 1：
 * <p>
 * <p>
 * 输入：root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 1
 * 输出：3
 * 解释：节点 5 和节点 1 的最近公共祖先是节点 3 。
 * 示例 2：
 * <p>
 * <p>
 * 输入：root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 4
 * 输出：5
 * 解释：节点 5 和节点 4 的最近公共祖先是节点 5 。因为根据定义最近公共祖先节点可以为节点本身。
 * 示例 3：
 * <p>
 * 输入：root = [1,2], p = 1, q = 2
 * 输出：1
 * <p>
 * <p>
 * 提示：
 * <p>
 * 树中节点数目在范围 [2, 105] 内。
 * -109 <= Node.val <= 109
 * 所有 Node.val 互不相同 。
 * p != q
 * p 和 q 均存在于给定的二叉树中。
 */
public class 二叉树的最近公共祖先 {
    public static void main(String[] args) {
        TreeNode v3 = new TreeNode(3, null, null);
        TreeNode v5 = new TreeNode(5, null, null);
        TreeNode v6 = new TreeNode(6, null, null);
        TreeNode v2 = new TreeNode(2, null, null);
        TreeNode v7 = new TreeNode(7, null, null);
        TreeNode v4 = new TreeNode(4, null, null);
        TreeNode v1 = new TreeNode(1, null, null);
        TreeNode v8 = new TreeNode(8, null, null);
        v3.left = v5;
        v3.right =v1;
        v5.left = v6;
        v5.right = v2;
        v2.left =v7;
        v2.right = v4;
        v1.right = v8;

        lowestCommonAncestor(v3, v5, v4);
    }

    public static TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        List<List<TreeNode>> res = new ArrayList<>(2);
        dfs(root, res, new ArrayList(), p, q);
        if (res.size() == 0) {
            return root;
        }
        for (List<TreeNode> data : res) {
            //2条路径
            System.out.println(data);
        }

        List<TreeNode> treeNodes1 = res.get(0);
        List<TreeNode> treeNodes2 = res.get(1);
        TreeNode temp = root;
        for (int i=0 ;i<treeNodes1.size();i++) {
            boolean b = treeNodes1.get(i) == treeNodes2.get(i);
            if (!b) {
                return temp;
            } else {
                temp = treeNodes1.get(i);
            }
        }
        return temp;
    }


    public static void dfs(TreeNode root, List<List<TreeNode>> res, ArrayList list, TreeNode p, TreeNode q) {
        if (root == null || res.size() >=2) {
            return;
        }
        list.add(root);
        if (root == p || root == q) {
            List copy = copy(list);
            res.add(copy);
        }

        if (root.left != null) {
            dfs(root.left, res, list, p, q);

        }

        if (root.right != null) {
            dfs(root.right, res, list, p, q);
        }
        list.remove(root);
    }

    private static List<TreeNode> copy(ArrayList<TreeNode> list) {
        LinkedList<TreeNode>  clone = new LinkedList<>();
        for (TreeNode treeNode : list) {
            clone.add(treeNode);
        }
        return clone;
    }


    private static class TreeNode {
        int val;
        二叉树的最近公共祖先.TreeNode left;
        二叉树的最近公共祖先.TreeNode right;

        public TreeNode() {
        }

        public TreeNode(int val) {
            this.val = val;
        }

        public TreeNode(int val, 二叉树的最近公共祖先.TreeNode left, 二叉树的最近公共祖先.TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }

        @Override
        public String toString() {
            return val+"";
        }
    }
}
