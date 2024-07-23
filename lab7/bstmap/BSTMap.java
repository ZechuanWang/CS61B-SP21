package bstmap;

import java.util.Iterator;
import java.util.Set;

import afu.org.checkerframework.checker.oigj.qual.O;

/** A data structure that uses a binary search tree to store pairs of keys and values.
 *  Any key must appear at most once in the dictionary, but values may appear multiple
 *  times. Key operations are get(key), put(key, value), and contains(key) methods. The value
 *  associated to a key is the value in the last call to put with that key.
 *  @Author Zechuan Wang*/
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private Node root;

    private class Node {
        private K key;
        private V val;
        private Node left, right;
        private int size; // numbers of nodes in its subtree

        public Node (K key, V val, int size) {
            this.key = key;
            this.val = val;
            this.size = size;
        }
    }

    /**Constructor*/
    public BSTMap() {

    }


    @Override
    public void clear() {
        root.left = root.right = null;
        root = null;
    }

    @Override
    public boolean containsKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException("calls get() with a null key");
        }
        return getK(root, key) != null;
    }

    private K getK(Node x, K key){
        if (x == null) {
            return null;
        }
        int cmp = x.key.compareTo(key);
        if (cmp > 0) {
            return getK(x.left, key);
        } else if (cmp < 0) {
            return getK(x.right, key);
        } else {
            return x.key;
        }
    }

    private V get(Node x, K key) {
        if (key == null) {
            throw new IllegalArgumentException("calls get() with a null key");
        }
        if (x == null) {
            return null;
        }
        int cmp = x.key.compareTo(key);
        if (cmp > 0) {
            return get(x.left, key);
        } else if (cmp < 0) {
            return get(x.right, key);
        } else {
            return x.val;
        }
    }

    @Override
    public V get(K key) {
        return get(root, key);
    }

    private int size(Node root) {
        if (root == null) {
            return 0;
        }
        return root.size;
    }

    @Override
    public int size(){
        return size(root);
    }

    private Node put(Node x, K key, V val){
        if (x == null) {
            return new Node(key, val, 1);
        }
        int cmp = x.key.compareTo(key);
        if (cmp > 0) {
            x.left = put(x.left, key, val);
        } else if (cmp < 0) {
            x.right = put(x.right, key, val);
        } else {
            x.val = val;
        }
        x.size = size(x.left) + size(x.right) + 1;
        return x;
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("calls put() with a null key");
        }
        root = put(root, key, value);
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException("Unsupport KeySet operation");
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException("Unsupport remove operation");
    }

    @Override
    public  V remove(K key, V value) {
        throw new UnsupportedOperationException("Unsupport remove operation");
    }

    public Iterator<K> iterator() {
        throw new UnsupportedOperationException("Unsupport iterator operation");
    }

    public static void main(String[] args){
        BSTMap<Integer, Integer> b = new BSTMap<>();
        b.put(2, 1);
        b.put(3, 1);
        b.put(1, 1);
        System.out.println(b.size());
        b.clear();
        System.out.println(b.size());
    }
}
