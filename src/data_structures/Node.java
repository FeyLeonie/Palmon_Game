package data_structures;

/**
 * A node in a linked list that stores generic data.
 *
 * @param <T> the type of data stored in the node
 */
class Node<T>
{
    /**
     * The data stored in the node.
     */
    T data;

    /**
     * A reference to the next node in the linked list.
     */
    Node<T> next; //pointer

    /**
     * Constructs a node with the specified data.
     *
     * @param _data the data to be stored in the node
     */
    public Node(T _data)
    {

        this.data = _data;

        this.next = null;

    }
}

