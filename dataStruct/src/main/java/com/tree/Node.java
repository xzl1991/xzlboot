package com.tree;

/**
 * @auther xzl on 17:01 2018/6/11
 */
public class Node<T> {
    //节点
    //用来保存节点数据
    T data;
    T key;
    Node leftChild;
    Node rightChild;
    public Node(T key,T data){
        this.key = key;
        this.data =data;
    }
    public Node(T data, Node<T> left, Node<T> right) {
        this.data = data;
        this.leftChild = left;
        this.rightChild = right;
    }
}
