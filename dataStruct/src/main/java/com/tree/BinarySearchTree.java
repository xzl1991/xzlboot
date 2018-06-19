package com.tree;

import java.util.Stack;

/**
 *
  * @author xzl
  * @Description  <p> 仅供学习 ：
 *     搜索二叉树的 创建 添加 ，删除， 和前中后遍历
  * </p>
  * @Param
  * @Date  2018-06-12 16:52:07
 */
public class BinarySearchTree<T extends Comparable> {
    private Node<T> root;
    public BinarySearchTree(){
        this.root = null;
    }
    //清空
    public void  clean(){
        root = null;
    }
    //判空
    public boolean isEmpty(){
        return root==null;
    }
    //是否包含元素
    public boolean contains(T value){
        return contains(value,root);
    }
    private boolean contains(T value,Node root){
        if ( root == null){
            return false;
        }
        int comp = value.compareTo(root.data);
        if (comp<0){
            return contains(value,root.leftChild);
        }else if (comp>0){
            return contains(value,root.rightChild);
        }else {
            return true;
        }
    }
    //region     插入元素
    public int insert(T value){
        root = insert(value,root);
        return 0;
    }
    private Node<T> insert(T value,Node root){
        if (root == null){
            return new Node<>(value,null,null);
        }
        int comp = value.compareTo(root.data);
        if (comp<0){
            root.leftChild = insert(value,root.leftChild);
        }else if (comp>0){
            root.rightChild = insert(value,root.rightChild);
        }else{
            throw new RuntimeException("已存在相同值");
        }
        return root;
    }
    //endregion  插入元素
    //region     删除元素
    public void delete(T value){
        root = delete(value,root);
    }
    private Node<T> delete(T value,Node<T> root){
        if (root == null){
            return null;
        }
        int comp = value.compareTo(root.data);
        if (comp<0){

        }else if (comp>0){

        }else if (root.rightChild !=null && root.leftChild !=null){
            //左右节点都存在--查找中继后续节点
            //1.当前节点值被其右子树的最小值代替
            root.data = (T) findMin(root.rightChild).data;
            //2.删除右子树最小值
            root.rightChild = delete(root.data,root.rightChild);
        }else {
            //如果被删除节点是一个叶子 或只有一个儿子
            root = root.leftChild==null?root.rightChild:root.leftChild;
        }
        return root;
    }
    private Node<T> findMin(Node<T> node){
        if (node == null){
            return null;
        }
        while (node.leftChild!=null){
            node = node.leftChild;
        }
        return node;
    }
    //endregion

    //region 前序遍历树
    public void preOredr1(Node node){
        Stack<Node> stack = new Stack<>();
        System.out.println();
        System.out.println("前序非递归 1： ");
        if (node != null){
            stack.push(node);
            while (!stack.empty()){
                node = stack.pop();
                System.out.print(node.data +" ");
                if (node.rightChild!=null)
                    stack.push(node.rightChild);
                if (node.leftChild!=null)
                    stack.push(node.leftChild);
            }
        }
    }
    public void preOredr2(Node node){
        System.out.println();
        System.out.println("前序非递归 2： ");
        //非递归
        Stack<Node> stack = new Stack<>();
        while (node !=null || !stack.empty()){
            while (node != null){
                //输出  和中序的区别是输出的位置
                System.out.print(node.data + " ");
                stack.push(node);
                node = node.leftChild;
            }
            if (!stack.empty()){
                node = stack.pop();
                //从栈顶取出一个节点，指向它的右孩子，继续前面while循环
                node = node.rightChild;
            }
        }
        if(node !=null){
            System.out.print(node.data +" ");
            preOredrRevert(node.leftChild);
            preOredrRevert(node.rightChild);
        }
        System.out.println();
    }
    public void preOredrRevert(Node node){
        //递归
        if(node !=null){
            System.out.print(node.data +" ");
            preOredrRevert(node.leftChild);
            preOredrRevert(node.rightChild);
        }
    }
    //endregion 前序遍历树
    // region 中序遍历树
    public void printMidOrder(Node root){
        //非递归
        Stack<Node> stack = new Stack<>();
        System.out.println();
        System.out.println("中序非递归： ");
        while (root != null || !stack.empty()){
            while (root!=null){
                stack.push(root);
                //一路指向左孩子
                root = root.leftChild;
            }
            if (!stack.empty()){
                //取栈顶元素并输出，并指向右孩子
                root = stack.pop();
                System.out.print(root.data + " ");
                root = root.rightChild;
            }
        }
        System.out.println();
    }
    public void printMidRevert(){
        //递归
        printtMid(root);
    }
    private void printtMid(Node<T> node){
        if (node!=null){
            printtMid(node.leftChild);
            System.out.print(node.data+ "  ") ;
            printtMid(node.rightChild);
        }
    }
    //endregion 中序遍历树
    //region 后续遍历
    /***
     *  双栈：
     *      对于跟节点t，先入栈，然后沿着其左子树往下收索，直到没有左子树的节点，此时该节点入栈
     *  但此时不能进行出栈访问，要检查右孩子，所以接下来一相同的规则进行处理，当访问其右孩子时，
     *  该节点又出现在栈顶，此时就可以出栈访问，这样就保证了正确的访问顺序，这个过程中，
     *  每个节点都两次出现在栈顶，只有在第二次出现在栈顶的时候才能访问它， 因此有必要设置两个栈
     */
    public void  postOrderStacks(Node<T> node){
        //非递归
        System.out.println();
        System.out.println("后续遍历非递归-- 双栈：");
        Stack<Node<T>> nodeStack = new Stack();
        //第二个栈有0 1 元素，1表示第二次访问，0,表示第一次访问
        Stack<Integer> count = new Stack();
        while (node != null || !nodeStack.empty()){
            //1.一路压入左孩子
            while (node != null){
                nodeStack.push(node);
                count.push(0);
                node = node.leftChild;
            }
            //2.处理右子树
            //peek查看堆栈顶部的对象，但不从堆栈中移除它。
            while (!nodeStack.empty() && count.peek()==1){
                //有两次访问的记录，就出栈且输出，两个栈是同步pop的
                count.pop();
                System.out.print(nodeStack.pop().data + " ");
            }
            //3.第二次访问了，栈2的元素置为1，这回要指向右孩子
            if (!nodeStack.empty()){
                //只要找到栈里的 左节点---> 遍历右节点内容 --- 先输出(上一步，左树出栈了)--->父节点 置1-->遍历右节点(第一步入栈)
                count.pop();
                count.push(1);
                node = nodeStack.peek();
                node = node.rightChild;
            }
        }
    }
    public void  postOrderStack_1(Node<T> cur){
        //非递归
        System.out.println();
        System.out.println("后续遍历非递归-- 单栈1：");
        Stack<Node<T>> nodeStack = new Stack();
        Node<T> pre = cur,node = cur;
        while (node != null || nodeStack.size()>0){
            while (node != null){
                //压入左孩子
                nodeStack.push(node);
                node = node.leftChild;
            }
            if (!nodeStack.empty()){
                //记录当前节点的右孩子
                Node<T> temp = nodeStack.peek().rightChild;
                if (temp == null || temp==pre){
                    //满足俩条件就出栈输出
                    node = nodeStack.pop();
                    System.out.print(node.data + " ");
                    pre = node;                  //标记已经被访问
                    node = null;               //把这个没用的节点置为null.，防止执行上面的while循环
                }else {
                    node = temp;
                }
            }
        }

    }
    public void  postOrderStack_2(Node<T> node){
        //非递归
        System.out.println();
        System.out.println("后续遍历非递归-- 单栈2：");
        Stack<Node<T>> nodeStack = new Stack();
        /**
         * <p>先扫描跟节点的所有左节点并入栈，接着将栈顶元素出栈，
         * 再扫描该节点的右孩子节点并入栈，扫描右孩子的所有左节点并入栈，当一个节点的左右孩子均被访问后在访问该节点，
         * 这里用一个初始值为null的节点表示右子树刚刚被访问的
         * 一直到栈为空，这里的难点是如何判断右孩子已经被访问过了
         * </p>
         * */
        Node tempNode = null;
        while (node != null){
            while (node.leftChild != null){
                //先左子树入栈,注意:这里最左节点（叶子）没有入栈，后面才入栈(方便后面输出)
                nodeStack.push(node);
                node = node.leftChild;
            }
            while (node.rightChild==null || node.rightChild ==tempNode){
                //当前节点无右孩子或者右孩子已经输出
                System.out.print(node.data +  " ");
                tempNode = node;//用来记录上一个节点
                if (nodeStack.empty()) return;
                node = nodeStack.pop();//节点出栈
            }
            nodeStack.push(node);
            node = node.rightChild;
        }
    }
    public void  postOrderRevert(Node<T> node){
        //递归
        if (node != null){
            postOrderRevert(node.leftChild);
            postOrderRevert(node.rightChild);
            System.out.print(node.data + " ");
        }
    }
    //endregion 后续遍历

    public static void main(String[] args) {
        /**
         *          12
         *     10       14
         *  9    11    13   18
         * */
        BinarySearchTree searchTree = new BinarySearchTree();
        searchTree.insert(12);
        searchTree.insert(10);
        searchTree.insert(14);
        searchTree.insert(13);
        searchTree.insert(9);
        searchTree.insert(11);
        searchTree.insert(18);
//        for (int i=0;i<10;i++){
//            searchTree.insert(i);
////            searchTree.printMid();
//        }
        System.out.println("包含122 > :"+ searchTree.contains(122));
        System.out.println("包含13 > :"+ searchTree.contains(13));
        searchTree.printMidRevert();
        searchTree.printMidOrder(searchTree.root);
        System.out.println("前续遍历-== 递归：");
        searchTree.preOredrRevert(searchTree.root);
        searchTree.preOredr1(searchTree.root);
        searchTree.preOredr2(searchTree.root);
        System.out.println("后续遍历-== 递归：");
        searchTree.postOrderRevert(searchTree.root);
        searchTree.postOrderStacks(searchTree.root);
        searchTree.postOrderStack_1(searchTree.root);
        searchTree.postOrderStack_2(searchTree.root);
//        searchTree.preOredr2(searchTree.root);
//        searchTree.preOredr1(searchTree.root);
//        searchTree.preOredr2(searchTree.root);
    }
}







































