package data_structures;

public class Node<T> implements Cloneable
{
    T data;
    Node<T> next; // pointer
    Node<T> previous;
    int index;

    Node (T _data ) {
        this.data = _data;
    }

    Node (Node<T> _toCopy ) {
        this.data = _toCopy.data;
        this.next = _toCopy.next;
        this.previous = _toCopy.previous;
    }


}

