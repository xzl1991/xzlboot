package com.tree;

/**
 * @author xzl
 * @Description  根据前序 -- 中序重建二叉树
 * @Param
 * @Date  2018-06-13 15:20:47
 */
public class TreeRebuild<T> {
    public static void main(String[] args) {
        int[] pre = {1,2,4,7,3,5,6,8};
        int[] end = {4,7,2,1,5,3,8,6};
        TreeRebuild rebuild = new TreeRebuild();
        BinarySearchTree searchTree = new BinarySearchTree();
        Node  node = rebuild.rebuild(pre,end) ;
        //后续结果：
        searchTree.postOrderStack_1(node);
    }
    public Node<T>  rebuild(int[] pre,int[] mid){
        Node<T> node = builde(pre,0,pre.length-1,mid,0,mid.length-1);
        return node;
    }
    private Node builde(int[] pre,int start,int end,int[] mid,int midStrt,int midEnd){
        if(start>end||midStrt>midEnd){//判定是否序列是否便利完。
            return null;
        }
        Node root =new Node(pre[start],null,null);//存入节点
        //从中序遍历开始，寻找和根节点相同的元素。
        for(int i=midStrt;i<=midEnd;i++){
            if (pre[start] == mid[i]){
                //找到了之后分为左右子树，递归进行查找。
                root.leftChild =  builde(pre,start+1,start+i-midStrt,mid,midStrt,i-1);
                root.rightChild = builde(pre,start+i-midStrt+1,end,mid,i+1,midEnd);
            }
        }
        return root;
    }
}
