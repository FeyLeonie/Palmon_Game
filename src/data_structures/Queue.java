package data_structures;

import java.util.ArrayList;

/**
 * queue implementation using a linked list.
 *
 * @param <T> the type of elements stored in the queue
 */
public class Queue<T>
{

    /**
     * The first node in the queue.
     */
    Node<T> head;

    /**
     * The last node in the queue.
     */
    Node<T> tail;

    /**
     * The size of the queue.
     */
    int queueSize;

    /**
     * Adds an element to the end of the queue.
     *
     * @param _data the element to be added to the queue
     *
     * software runtime complexity is O(1)
     */
    public void enqueue(T _data)
    {

        Node<T> newNode = new Node<T>(_data);

        //Queue is empty

        if (queueSize == 0) {

            head = newNode;
            tail = newNode;
            queueSize = 1;
        }
        else
        {
            tail.next = newNode;
            tail = newNode;
            queueSize++;
        }

    }

    /**
     * Removes and returns the element at the front of the queue.
     *
     * @return the element removed from the queue
     *
     * software complexity runtime is O(1)
     */
    public T dequeue() {

        // Case: Queue is empty
        if (queueSize == 0)
        {
            System.out.println("Queue is empty");
            return null;
        }

        T data = head.data; // Temporary storage because head will be overwritten

        head = head.next; // New head; the old node is discarded

        queueSize--; // Adjusting the size counter

        return data; // Returning the value of the old head
    }

    /**
     * Returns the size of the queue.
     *
     * @return the size of the queue
     */
    public int getQueueSize() // for returning the size of the Queue at any time
    {
        return queueSize;
    }

    /**
     * Converts the queue into an ArrayList.
     *
     * @return an ArrayList containing the elements of the queue
     */
    public ArrayList<T> toArrayList() // Converting the queue into an ArrayList
    {
        ArrayList<T> arrayList = new ArrayList<>(); // initializing the ArrayList

        Node<T> current = head; // current Node to the front
        while (current != null)  // iterating through every Object
        {
            arrayList.add(current.data); // adding the current object to the ArrayList (until null)
            current = current.next; // moving on with the next Object
        }
        return arrayList; // returning the filled ArrayList
    }
}
