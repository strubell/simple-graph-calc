package hw02;

/**
 *  Graph.java
 *  
 *  Undirected graph data type implemented using a HashMap to store Node
 *  vertices: keys are Node String-valued ids, values are Nodes present in
 *  this graph.
 *
 *  @author Emma Strubell
 *  COS397 - Chawathe
 *  February 17, 2011
 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;

public class Graph<T extends Comparable<T>> implements Iterable<Node<T>>
{
	// a table mapping id values to Nodes in this graph
	protected HashMap<String,Node<T>> nodes;

	// number of edges in the Graph
	private int size;

	/**
	 * Constructor: Creates an empty graph.
	 */
	public Graph(){
		this.nodes = new HashMap<String,Node<T>>();
		this.size = 0;
	}
	
	/**
	 * Constructor: makes a new Graph with the same Nodes and relationships between
	 * those nodes as the given Graph. 
	 * @param g the pre-existing graph to copy
	 */
	public Graph(Graph<T> g){
		this.nodes = new HashMap<String,Node<T>>(g.nodes);
		this.size = g.size;
	}

	/**
	 * @return number of vertices in the graph
	 */
	public int order(){
		return nodes.size();
	}

	/**
	 * @return number of edges in the graph
	 */
	public int size(){
		return size;
	}
	
	/**
	 * Adds an edge between Node with id v and Node with id w,
	 * adding new Nodes with those ids if they don't already exist.
	 * 
	 * @param v value of Node to connect to Node with value w
	 * @param w value of Node to connect to Node with value v
	 */
	public void addEdge(String v, String w){
		if(v.equals(w))
			return;
		if (!hasEdge(v, w)){
		    if (!hasVertex(v))
		    	addVertex(v);
		    if (!hasVertex(w))
		    	addVertex(w);
		    nodes.get(v).addEdge(nodes.get(w));
		    nodes.get(w).addEdge(nodes.get(v));
		    size++;
		}
	}
	
	/**
	 * Adds an edge between Node v and Node w.
	 * 
	 * @param v Node to connect to Node w
	 * @param w Node to connect to Node v
	 */
	public void addEdge(Node<T> v, Node<T> w){
			addEdge(v.id, w.id);
	}
	
	/**
     * Add a new Node with id s, if it does not yet exist
     * 
     * @param n id of new vertex
     */
	public void addVertex(String s){
	    if (!hasVertex(s)){
	    	Node<T> node = new Node<T>(s);
	    	nodes.put(s, node);
	    }
	}
	
	/**
     * Add the given Node as a vertex in this Graph
     * 
     * @param n Node<T> to add
     */
	public void addVertex(Node<T> n){
		if(!hasVertex(n))
			nodes.put(n.id, new Node<T>(n));
	}
	
	/**
	 * Adds a new Node to this graph that combines the two given Nodes and
	 * gives the result an id consisting of the two given Nodes' ids separated by
	 * concatStr, as is necessary in computing graph products.
	 * 
	 * @param u Node to combine
	 * @param v other Node to combine
	 * @param concatStr id of combined Node
	 */
	public void addVertex(Node<T> u, Node<T> v, String concatStr){
		LinkedList<T> elems = new LinkedList<T>(u.elements);
		elems.addAll(v.elements);
		nodes.put(u.id + concatStr + v.id, new Node<T>(elems, u.id + concatStr + v.id));
	}

	/**
	 * Whether or not there exists a Node with id v in this Graph
	 * 
	 * @param v id to check for in the graph
	 * @return boolean true if a Node with id v is in the graph, false if not
	 */
	public boolean hasVertex(String v){
		return nodes.containsKey(v);
	}
	
	/**
	 * Whether or not there exists a Node with the same id as v in this Graph
	 * 
	 * @param v Node with id to check for in the graph
	 * @return boolean true if a Node with same id as v is in the graph, false if not
	 */
	public boolean hasVertex(Node<T> v){
		return nodes.containsKey(v.id);
	}

	/**
	 * Whether or not there exists an edge between Nodes with ids v
	 * and w in this Graph
	 * 
	 * @param v id contained by one Node on edge
	 * @param w id contained by other Node on edge
	 * @return true if edge exists between Nodes with ids v and w,
	 * false otherwise (and false if either Node does not exist).
	 */
	public boolean hasEdge(String v, String w){
		if (!hasVertex(v) || !hasVertex(w))
			return false;
		return nodes.get(v).hasEdge(w);
	}
	
	/**
	 * Whether or not there exists an edge between Nodes v and w,
	 * using their ids.
	 * 
	 * @param v one Node on edge
	 * @param w other Node on edge
	 * @return true if edge exists between Nodes v and w, false otherwise
	 * (and false if either Node does not exist in this Graph).
	 */
	public boolean hasEdge(Node<T> v, Node<T> w){
		return hasEdge(v.id, w.id);
	}

	/**
	 * WARNING: really inefficient; String building of really long strings (graphs of
	 * order around 1000, size around 100,000) may fail due to inherent inefficiency
	 * of Java (and all) String concatenation.
	 * 
	 * @return the normalized text representation of this graph, as per the homework.
	 */
	public String toString(){
		String s = "";
		TreeSet<String> elems = new TreeSet<String>();
		for(Node<T> u: nodes.values()){
			if(u.degree() == 0)
				elems.add(u.id);
			else
				for(Node<T> v : u)
					elems.add(v.compareTo(u) < 0? v + "--" + u : u + "--" + v);
		}
		for(String e : elems)
			s.concat(e + " ");
		return s;
	}
	
	/**
	 * Print the normalized text representation of this graph, as per the homework;
	 * No excess whitespace, edges and vertices are listed in lexicographic order,
	 * and vertices in edges are in lexicographic order.
	 */
	public void print(){
		TreeSet<String> elems = new TreeSet<String>();
		for(Node<T> u: nodes.values()){
			if(u.degree() == 0)
				elems.add(u.id);
			else
				for(Node<T> v : u)
					elems.add(v.compareTo(u) < 0? v + "--" + u : u + "--" + v);
		}
		for(String e : elems)
			System.out.print(e + " ");
		System.out.println("\n");
	}
	
	/**
	 * Creates GraphViz .dot and LaTeX/TikZ .tex files that depict this graph using the
	 * circo circular layout. Tested to work for drawing graphs up to order 50,and should
	 * resize figures appropriately (so that they fit on a page) up to that size. Use nice,
	 * succinct vertex labels if you want these graphs to look nice.
	 * 
	 * @param gname the name to give this graph
	 */
	public void texPrint(String gname){
		try {
			BufferedWriter file = new BufferedWriter(new FileWriter(gname + ".dot"));
			HashSet<String> edges = new HashSet<String>();
			
			// command to execute to generate TikZ .tex file
			String[] command = new String[]{"dot2tex", gname + ".dot", "-o", gname + ".tex"};
			
			// scale graphs so they will be as small as possible yet attractive/demonstrative
			double scale = .25;
			int sep = 0;
			if(order() > 10){
				sep = 1;
				scale = .2;
			}
			if(order() > 25){
				scale = .125;
				sep = 1;
			}

			file.write("graph " + gname + "{\n");
			file.write("d2toptions =\"-ftikz -tmath --figonly --tikzedgelabels --styleonly --prog=circo\";\n");
			file.write("d2tnodeoptions=\"shape=circle, inner sep="+sep+"pt, minimum width=1pt, scale="+scale+"\";\n");
			file.write("d2tedgeoptions=\"color=gray,scale=" + scale + "\";\n");
			for(Node<T> u: nodes.values()){
				if(u.degree() == 0)
						file.write(u.id + ";\n");
				else
					for(Node<T> v : u)
						edges.add(v.compareTo(u) < 0? v + "--" + u + ";\n": u + "--" + v+ ";\n");
			}
			for(String s : edges)
				file.write(s);
			file.write("}\n");
			file.close();
			Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			System.err.println("Unable to create TikZ output of " + gname + "!");
			e.printStackTrace();
		}
	}
	
	/**
	 * Merge the given Graph with this one. Specifically, combine vertices and edges
	 * of both graphs, and if the same vertex (same id) exists in the two graphs, do
	 * not rename the second, but rather combine them into one vertex with all edges
	 * of the two duplicate vertices.
	 * 
	 * @param g the Graph to combine with this one
	 */
	public void merge(Graph<T> g){
		for(Node<T> u : g){
			if(hasVertex(u)){
				for(Node<T> v : u)
					addEdge(u.id, v.id);
			}
			else
				addVertex(u);
		}
	}
	
	/**
	 * @return iterator over all vertices in graph
	 */
	public Iterator<Node<T>> iterator() {
		return nodes.values().iterator();
	}
}
