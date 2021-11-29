package interview_questions;

import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

public class LRUCache<K, V> {

    @Getter
    private Node<K, V> lru;
    @Getter
    private Node<K, V> mru;

    private Map<K, Node<K, V>> container;

    private int capacity;
    private int currentSize;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.currentSize = 0;
        lru = new Node<>(null, null, null, null);
        mru = lru;
        container = new HashMap<>();
    }

    public V get(K key) {
        Node<K, V> currentNode = container.get(key);
        if (currentNode == null) {
            return null;
        } else if (currentNode.key == mru.key) {
            return mru.value;
        }

        makeMostRecent(currentNode);

        return currentNode.value;
    }

    public void put(K key, V value) {
        if (container.containsKey(key)) {
            Node<K, V> existingNode = container.get(key);
            existingNode.value = value;
            if (key == mru.key) {
                return;
            } else {
                makeMostRecent(existingNode);
            }
        }

        Node<K, V> myNode = new Node<K, V>(mru, null, key, value);
        mru.next = myNode;
        container.put(key, myNode);
        mru = myNode;

        if (currentSize == capacity) {
            container.remove(lru.key);
            lru = lru.next;
            lru.prev = null;
        }

        if (currentSize < capacity) {
            if (currentSize == 0) {
                lru = myNode;
            }
            currentSize++;
        }

    }

    private void makeMostRecent(Node<K,V> node) {
        Node<K, V> nextNode = node.next;
        Node<K, V> previousNode = node.prev;

        if (node.key == lru.key) {
            nextNode.prev = null;
            lru = nextNode;
        } else {
            previousNode.next = nextNode;
            nextNode.prev = previousNode;
        }

        node.prev = mru;
        mru.next = node;
        mru = node;
        mru.next = null;
    }

    @ToString
    static class Node<T, U> {
        T key;
        U value;

        Node<T, U> prev;
        Node<T, U> next;

        public Node(Node<T, U> prev, Node<T, U> next, T key, U value) {
            this.prev = prev;
            this.next = next;
            this.key = key;
            this.value = value;
        }

    }
}
