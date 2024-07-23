package bstmap;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


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
    public void printInOrder(){
        printInOrder(root);
    }

    private void printInOrder(Node root){
        if (root == null) {
            return;
        }
        printInOrder(root.left);
        System.out.println(root.key.toString() + " -> " + root.val.toString());
        printInOrder(root.right);
    }

    private void keySet(Node root, Set<K> set){
        if (root == null) {
            return;
        }
        set.add(root.key);
        keySet(root.left, set);
        keySet(root.right, set);
    }
    @Override
    public Set<K> keySet() {
        Set<K> set = new HashSet<>();
        keySet(root, set);
        return set;
    }

    @Override
    public V remove(K key) {
        if (!containsKey(key)) {
            return null;
        }
        V val = get(key);
        root = remove(root, key);
        return val;
    }
    private Node remove(Node root, K key){
        if (root == null) {
            return null;
        }
        int cmp = key.compareTo(root.key);
        if (cmp < 0) {
            root.left = remove(root.left, key);
        } else if (cmp > 0) {
            root.right = remove(root.right, key);
        } else {
            if (root.right == null) {
                return root.left;
            }
            if (root.left == null) {
                return root.right;
            }
            Node tmp = root;
            root = findMinInRight(root.right);
            root.right = deleteMin(tmp.right);
            root.left = tmp.left;
        }
        root.size = size(root.left) + size(root.right) + 1;
        return root;
    }

    private Node findMinInRight(Node root){
        if (root.left == null) {
            return root;
        }
        return findMinInRight(root.left);
    }
    private Node deleteMin(Node x) {
        if (x.left == null) return x.right;
        x.left = deleteMin(x.left);
        x.size = size(x.left) + size(x.right) + 1;
        return x;
    }

    @Override
    public  V remove(K key, V value) {
        if (!containsKey(key)) {
            return null;
        }
        V val = get(key);
        if (val != value) {
            return val;
        }
        root = remove(root, key);
        return val;
    }

    public Iterator<K> iterator() {
        return keySet().iterator();
    }


}
