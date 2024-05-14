package data_structures;

public class Stack<T>
{
    Node<T> root;
    Node<T> top;

    int stackSize;

    public void push(Stack<T> _stack, T _data)
    {

        Node<T> newNode = new Node<T>(_data);

        if (_stack.root == null) {
            _stack.root = newNode;
            _stack.top = newNode;
        } else {
            newNode.next = _stack.top;
            _stack.top = newNode;
        }
        stackSize ++;
    }

    public T pull(Stack<T> _stack)
    {
        T _data = _stack.top.data;
        _stack.top = _stack.top.next;
        stackSize--;
        return _data;
    }

    public boolean isEmpty()
    {
        return stackSize == 0;
    }
}