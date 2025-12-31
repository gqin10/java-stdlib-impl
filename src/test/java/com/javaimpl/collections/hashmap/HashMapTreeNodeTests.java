package com.javaimpl.collections.hashmap;


import com.javaimpl.common.object.ListNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class HashMapTreeNodeTests {

    @Test
    public void testPut() {
        int[] list = {3, 5, 2, 1, 8, 4, 10};
        HashMapTreeNode<String, String> root = null, tail = null;
        for (Integer i : list) {
            HashMapTreeNode<String, String> newNode = new HashMapTreeNode<>(String.valueOf(i), String.valueOf(i));
            if (root == null) {
                root = tail = newNode;
            }
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        HashMapTreeNode<String, String> rootNode = root.treeify();
        rootNode.printTree();
        Assertions.assertEquals(list.length, rootNode.size());
    }

}
