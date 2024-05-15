package data_structures;

class Node<T>
{
        T data;

        Node<T> next; //pointer

        public Node(T _data) {

            this.data = _data;

            this.next = null;

        }
}

