package data_structures;

import java.util.ArrayList;

public class Queue<T>
{

    Node<T> head;

    Node<T> tail;

    int queueSize;

    //Complexity O(1)

    public void enqueue(T _data)
    {

        Node<T> newNode = new Node<T>(_data);

        //Queue ist leer

        if (queueSize == 0) {

            head = newNode;
            tail = newNode;
            queueSize = 1;
        }

        //Queue hat Werte

        else
        {
            tail.next = newNode;
            tail = newNode;
            queueSize++; 					//bedeutete: queueSize = queueSize + 1;
        }

    }

    //Complexity O(1)
    public T dequeue() {

        if (queueSize == 0) 				//Fall: Queue ist leer
        {

            System.out.println("Queue is empty");

            return null;					//wir geben hier null aus, weil z.B. -1 könnte ja ein inhaltlicher Wert sein

        }

        T data = head.data;				//Zwischenspeichern, weil wir gleich head überschreiben

        head = head.next;					//neuer Head, alte Node wird ignoriert

        queueSize--;						//Zählvariable anpassen

        return data;						//Wert von altem Head zurückgeben
    }

    public int getQueueSize() // for returning the size of the Queue at any time
    {
        return queueSize;
    }

    public T showTop() // for showing the top Object
    {
        if (queueSize == 0)
        {
            System.out.println("Queue is empty");
            return null;
        }
        return head.data;
    }

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
