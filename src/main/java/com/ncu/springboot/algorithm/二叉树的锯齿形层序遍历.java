package com.ncu.springboot.algorithm;

import org.apache.commons.collections.ArrayStack;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 103. 二叉树的锯齿形层序遍历
 * <p>
 * 给你二叉树的根节点 root ，返回其节点值的 锯齿形层序遍历 。（即先从左往右，再从右往左进行下一层遍历，以此类推，层与层之间交替进行）。
 * <p>
 * 输入：root = [3,9,20,null,null,15,7]
 * 输出：[[3],[20,9],[15,7]]
 * 示例 2：
 * <p>
 * 输入：root = [1]
 * 输出：[[1]]
 * 示例 3：
 * <p>
 * 输入：root = []
 * 输出：[]
 * <p>
 * <p>
 * 提示：
 * <p>
 * 树中节点数目在范围 [0, 2000] 内
 * -100 <= Node.val <= 100
 */
class 二叉树的锯齿形层序遍历 {


    public static void main(String[] args) {
        // [3,9,20,null,null,15,7]
        TreeNode v3 = new TreeNode(3, null, null);
        TreeNode v9 = new TreeNode(9, null, null);
        TreeNode v20 = new TreeNode(20, null, null);
        TreeNode v15 = new TreeNode(15, null, null);
        TreeNode v7 = new TreeNode(7, null, null);
        v3.left = v9;
        v3.right = v20;
        v20.left = v15;
        v20.right = v7;


        List<List<Integer>> res = zigzagLevelOrder(v3);
        System.out.println(res);


        //[1,2,3,4,null,null,5]
//        TreeNode v1 = new TreeNode(1, null, null);
//        TreeNode v2 = new TreeNode(2, null, null);
//        TreeNode v3 = new TreeNode(3, null, null);
//        TreeNode v4 = new TreeNode(4, null, null);
//        TreeNode v5 = new TreeNode(5, null, null);
//        TreeNode v6 = new TreeNode(6, null, null);
//        TreeNode v7 = new TreeNode(7, null, null);
//        TreeNode v8 = new TreeNode(8, null, null);
//        TreeNode v9 = new TreeNode(9, null, null);
//        TreeNode v10 = new TreeNode(10, null, null);
//        TreeNode v11 = new TreeNode(11, null, null);
//
//        v1.left = v2;
//        v1.right = v3;
//        v2.left = v4;
//        v3.right = v5;
//        v4.left = v6;
//        v4.right = v7;
//        v5.left =v8;
//        v7.left=v9;
//        v8.right=v10;
//        v10.left =v11;
//
//        List<List<Integer>> res1 = zigzagLevelOrder(v1);
//
//        System.out.println(res1);


    }


    public static List<List<Integer>> zigzagLevelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) {
            return result;
        }


        levelOrder(result, Arrays.asList(root), true);
        return result;
    }


    public static void levelOrder(List<List<Integer>> result, List<TreeNode> roots, boolean left) {

        ArrayList<TreeNode> temp = new ArrayList<>();

        List<Integer> res = new ArrayList<>();
        if (roots != null && roots.size() >=0) {
           for (TreeNode treeNode : roots) {
               if (treeNode.left != null){temp.add(treeNode.left);}
               if (treeNode.right != null){temp.add(treeNode.right);}
           }
        }


        boolean returnFlag = false;

        if (left) {
            for (int i = 0; i < roots.size(); i++) {
                res.add(roots.get(i).val);
            }
        } else {
            for (int i = roots.size() -1; i >= 0; i--) {
                res.add(roots.get(i).val);
            }
        }
        if (temp == null || temp.size() == 0) {
            result.add(res);
        } else {
            result.add(res);
            levelOrder(result, temp, left ? false : true);
        }
    }


    private static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        public TreeNode() {
        }

        public TreeNode(int val) {
            this.val = val;
        }

        public TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

}