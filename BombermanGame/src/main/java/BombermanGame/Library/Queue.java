package BombermanGame.Library;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Queue<Item> implements Iterable<Item> {
    private Node first;
    private Node last;
    private int nQueue;

    public Queue() {
        this.first = null;
        this.last = null;
        this.nQueue = 0;
    }

    public boolean isEmpty() {
        return (nQueue == 0);
    }

    private void initNewDeque(Item item) {
        nQueue++;
        first = new Node(item);
        last = first;
    }

    public void add(Item item) {
        if (item == null) throw new IllegalArgumentException();
        if (this.isEmpty()) {
            initNewDeque(item);
            return;
        }
        nQueue++;
        Node temp = last;
        last = new Node(item);
        last.setPrevNode(temp);
        temp.setNextNode(last);
    }

    public Item remove() {
        if (this.isEmpty()) throw new NoSuchElementException();
        Item result = first.getItem();
        if (first.nextNode() == null) {
            first = null;
            last = null;
        } else {
            first = first.nextNode();
            first.setPrevNode(null);
        }
        nQueue--;
        return result;
    }

    public Iterator<Item> iterator() {
        return new QueueIterator();
    }

    private class Node {
        private final Item item;
        private Node next;
        private Node prev;

        public Node(Item item) {
            this.item = item;
            next = null;
            prev = null;
        }

        public Item getItem() {
            return item;
        }

        public void setNextNode(Node nextNode) {
            this.next = nextNode;
        }

        public void setPrevNode(Node preNode) {
            this.prev = preNode;
        }

        public Node nextNode() {
            return this.next;
        }
    }

    private class QueueIterator implements Iterator<Item> {
        private Node curNode;

        public QueueIterator() {
            this.curNode = first;
        }

        public boolean hasNext() {
            return curNode != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!this.hasNext()) throw new NoSuchElementException();
            Node result = curNode;
            curNode = curNode.nextNode();
            return result.getItem();
        }
    }
}
