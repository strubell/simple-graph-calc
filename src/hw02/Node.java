package hw02;

/**
 *  Node.java
 * 
 * 	A Graph Node ADT that contains a LinkedList of elements of any Comparable
 *  type T, and HashMap mapping "id" strings to adjacent nodes. The adjacency
 *  list is implemented with a HashMap to give constant runtime O(1) for
 *  adding and retrieving elements.
 *  
 *  Nodes store LinkedLists of elements and not single elements so that this 
 *  Node supports combining of Nodes and thus their labels and elements, such
 *  as in graph products.
 * 
 *  @param <T> the Node's contained element type.
 *  
 *  @author Emma Strubell
 *  COS397 - Chawathe
 *  February 17, 2011
 */

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

class Node<T extends Comparable<T>> implements Comparable<Node<T>>, Iterable<Node<T>>
{	
    // the data in the node
    protected LinkedList<T> elements;
    
    // a String value associated with this node. this is basically to
    // deal with default graph naming schemes in the graph calculator
    protected String id;
    
    // mapping of ids -> Nodes adjacent to this one
    protected HashMap<String,Node<T>> adjacents;
    
    /**
     * Constructor.
     * @param elem the element.
     */
    public Node(T elem){
    	elements = new LinkedList<T>();
    	elements.add(elem);
    	id = elem.toString();
    	adjacents = new HashMap<String,Node<T>>();
    }
    
    /**
     * Constructor.
     * @param id id to assign to new Node.
     */
    public Node(String id){
    	elements = new LinkedList<T>();
    	this.id = id;
    	adjacents = new HashMap<String, Node<T>>();
    }
    
    /**
     * Constructor that takes a pre-existing Node and creates a new node
     * that is a replica: same id, same elements, same adjacent nodes
     * 
     * @param n the Node to copy
     */
    public Node(Node<T> n){
    	elements = new LinkedList<T>(n.elements);
    	this.id = n.id;
    	adjacents = new HashMap<String, Node<T>>(n.adjacents);
    }
    
    /**
     * Constructor that takes a pre-existing Node and creates a new node
     * with the same elements as that node, but a new id and empty list
     * of adjacent Nodes
     * 
     * @param n the Node to copy
     * @param id id of this new Node
     */
    public Node(Node<T> n, String id){
    	elements = new LinkedList<T>(n.elements);
    	this.id = id;
    	adjacents = new HashMap<String, Node<T>>();
    }
    
    /**
     * Constructor.
     * @param elems the list of elements to give this new Node
     * @param id the new id to give this node
     */
    public Node(LinkedList<T> elems, String id){
    	elements = elems;
    	this.id = id;
    	adjacents = new HashMap<String, Node<T>>();
    }

    /**
     * @param n the Node with which to compare
     * @return boolean whether the id of this node is equal to that of n
     */
    public boolean equals(Node<T> n){
    	return this.id.equals(n.id);
    }

    /**
     * @return String representation of this Node: its id
     */
    public String toString(){
    	return this.id;
    }
    
    /**
     * Adds an edge from this Node to the given Node v. 
     * 
     * @param v the Node that will be adjacent to this one
     */
    public void addEdge(Node<T> v){
    	adjacents.put(v.id, v);
    }
    
    /**
     * @param v possible adjacent Node
     * @return boolean whether this node is adjacent to v
     */
    public boolean hasEdge(Node<T> v){
    	return adjacents.containsValue(v);
    }
    
    /**
     * @param v id of possible adjacent Node
     * @return boolean whether this node is adjacent to the Node with id v
     */
    public boolean hasEdge(String v){
    	return adjacents.containsKey(v);
    }

    /**
     * @param n the Node to compare this one to
     * @return whether this Node's id is lexicographically greater, less than,
     * or equal to that of n
     */
    public int compareTo(Node<T> n){
    	return this.id.compareTo(n.id);
    }
    
    /**
     * @return the number of edges incident on this node
     */
    public int degree(){
    	return adjacents.size();
    }

    /**
     * @return an Iterator over the Nodes adjacent to this one
     */
	public Iterator<Node<T>> iterator() {
		return adjacents.values().iterator();
	}
}