package hw02;

/**
 * 	GraphCalculator.java
 * 	
 * 	This class implements a Graph calculator. Adheres to specifications of Homework #2.
 * 
 *  @author Emma Strubell
 *  COS397 - Chawathe
 *  February 17, 2011
 */

import java.util.HashMap;

public class GraphCalculator<T extends Comparable<T>>
{
	// maps identifiers to stored graphs
	protected HashMap<String, Graph<T>> graphs;
	
	// the string to be used for concatenating vertex ids
	// default: "_" (underscore)
	private String concatStr;
	
	/**
	 * Constructor: uses default concatenation String
	 */
	public GraphCalculator(){
		graphs = new HashMap<String, Graph<T>>();
		concatStr = "_";
	}
	
	/**
	 * Constructor
	 * @param concatStr String to use for concatenating vertex ids
	 */
	public GraphCalculator(String concatStr){
		graphs = new HashMap<String, Graph<T>>();
		this.concatStr = concatStr;
	}
	
	/**
	 * @param g the Graph to take the complement of
	 * @return a new Graph that is the complement of the given Graph
	 */
	public Graph<T> complement(Graph<T> g){
		Graph<T> h = new Graph<T>();
		for(Node<T> u : g){
			h.addVertex(u.id);
			for(Node<T> v : g)
				if(!g.hasEdge(u, v))
					h.addEdge(u.id, v.id);
		}
		return h;
	}
	
	/**
	 * @param g1 one Graph to take direct sum with
	 * @param g2 other Graph to take direct sum with
	 * @return new Graph that is the direct sum of g1 and g2
	 */
	public Graph<T> directSum(Graph<T> g1, Graph<T> g2){
		Graph<T> h = new Graph<T>(g1);
		Graph<T> j = renameDuplicates(g1, g2);
		for(Node<T> u : j){
			h.addVertex(u);
			for(Node<T> e : u)
				h.addEdge(u, e);
		}
		return h;
	}
	
	/**
	 * @param g1 one Graph to join
	 * @param g2 other Graph to join
	 * @return new Graph that is the join of g1 and g2
	 */
	public Graph<T> join(Graph<T> g1, Graph<T> g2){
		Graph<T> h = new Graph<T>(g1);
		Graph<T> j = renameDuplicates(g1, g2);
		for(Node<T> u : j){
			h.addVertex(u);
			for(Node<T> e : u)
				h.addEdge(u, e);
		}
		for(Node<T> u : g1)
			for(Node<T> v : j)
				h.addEdge(u, v);
		return h;
	}
	
	/**
	 * @param g1 one Graph to take direct product with
	 * @param g2 other Graph to take direct product with
	 * @return new Graph that is the direct product of g1 and g2
	 */
	public Graph<T> directProduct(Graph<T> g1, Graph<T> g2){
		Graph<T> h = combineVertices(g1, g2);
		for(Node<T> u : g1)
			for(Node<T> v : g2)
				for(Node<T> e : u)
					for(Node<T> f : v)
						h.addEdge(u + concatStr + v, e + concatStr + f);
		return h;
	}
	
	/**
	 * @param g1 one Graph to take cartesian product with
	 * @param g2 other Graph to take cartesian product with
	 * @return new Graph that is the cartesian product of g1 and g2
	 */
	public Graph<T> cartesianProduct(Graph<T> g1, Graph<T> g2){
		Graph<T> h = combineVertices(g1, g2);
		for(Node<T> u : g1)
			for(Node<T> v : g2){
				for(Node<T> e : u)
					h.addEdge(u + concatStr + v, e + concatStr + v);
				for(Node<T> f : v)
					h.addEdge(u + concatStr + v, u + concatStr + f);
			}
		return h;
	}
	
	/**
	 * @param g1 one Graph to take strong product with
	 * @param g2 other Graph to take strong product with
	 * @return new Graph that is the strong product of g1 and g2
	 */
	public Graph<T> strongProduct(Graph<T> g1, Graph<T> g2){
		Graph<T> h = combineVertices(g1, g2);
		for(Node<T> u : g1)
			for(Node<T> v : g2)
				for(Node<T> e : u){
					for(Node<T> f : v){
						h.addEdge(u + concatStr + v, e + concatStr + f);
						h.addEdge(u + concatStr + v, u + concatStr + f);
					}
				}
		return h;
	}
	
	/**
	 * @param g1 one Graph to take odd product with
	 * @param g2 other Graph to take odd product with
	 * @return new Graph that is the odd product of g1 and g2
	 */
	public Graph<T> oddProduct(Graph<T> g1, Graph<T> g2){
		Graph<T> h = combineVertices(g1, g2);
		for(Node<T> u : g1)
			for(Node<T> v1 : g2)
				for(Node<T> e : u)
					for(Node<T> v2 : g2)
						if(!v1.hasEdge(v2))
							h.addEdge(u + concatStr + v1, e + concatStr + v2);	
		for(Node<T> v : g2)
			for(Node<T> u1 : g1)
				for(Node<T> f : v)
					for(Node<T> u2 : g1)
						if(!u1.hasEdge(u2))
							h.addEdge(u1 + concatStr + v, u2 + concatStr + f);	
		return h;
	}
	
	/**
	 * @param g1 one Graph to take lexicographic product with
	 * @param g2 other Graph to take lexicographic product with
	 * @return new Graph that is the lexicographic product of g1 and g2
	 */
	public Graph<T> lexicographicProduct(Graph<T> g1, Graph<T> g2){
		Graph<T> h = combineVertices(g1, g2);
		for(Node<T> u : g1)
			for(Node<T> v1 : g2)
				for(Node<T> e : u){
					for(Node<T> v2 : g2)
						h.addEdge(u + concatStr + v1, e + concatStr + v2);
					for(Node<T> f : v1)						
						h.addEdge(u + concatStr + v1, u + concatStr + f);
				}		
		return h;
	}
	
	/**
	 * For join and direct sum operators, duplicate vertices in the second graph
	 * are renamed with a "_1" appended to the end of vertex ids. This method
	 * combines two graphs into one, renaming any vertices with duplicate labels
	 * @param g1 first Graph to combine
	 * @param g2 second Graph to combine
	 * @return a new Graph containing all vertices of g1 and g2, renamed if necessary
	 */
	private Graph<T> renameDuplicates(Graph<T> g1, Graph<T> g2){
		Graph<T> h = new Graph<T>();
		
		// populate new graph with (possibly) renamed vertices
		for(Node<T> u : g2){
			if(g1.hasVertex(u)){
				h.addVertex(new Node<T>(u, u.id + "_1"));
			}
			else{
				h.addVertex(new Node<T>(u, u.id));
			}
		}
		for(Node<T> u : g2){
			for(Node<T> e : u){
				if(g1.hasVertex(u)){
					if(g1.hasVertex(e))
						h.addEdge(h.nodes.get(u + "_1"), h.nodes.get(e + "_1"));
					else
						h.addEdge(h.nodes.get(u + "_1"), e);
				}
				else{
					if(g1.hasVertex(e))
						h.addEdge(u, h.nodes.get(e + "_1"));
					else
						h.addEdge(u, e);
				}
			}
		}
		return h;
	}
	
	/**
	 * Adds the given Graph with the given identifier (name) to the
	 * Graph table, so that this graph can be used via later references
	 * to its name.
	 * 
	 * @param name identifier associated with the Graph
	 * @param g the Graph to be added with above name
	 */
	public void addGraph(String name, Graph<T> g){
		graphs.put(name, g);
	}
	
	/**
	 * Generates a complete graph of the given order. If the default concatenation
	 * String is being used, then vertices are given ids corresponding to the specifications
	 * on the newsgroup. Otherwise, less ugly ids are assigned: "k_" followed by an integer
	 * (this gives much prettier math-friendly LaTeX output, for example.)
	 * 
	 * @param order the order Graph to generate
	 * @return a complete graph of given order
	 */
	public Graph<T> completeGraph(int order){
		Graph<T> g = new Graph<T>();
		String lbl;
		if(concatStr.equals("_"))
			lbl = "_K_" + order + "_";
		else
			lbl = "k_";
		
		// create all vertices
		for(int i = 0; i < order; i++){
			g.addVertex(lbl+i);
		}
		
		// connect each Node i to all other Nodes j in the graph.
		for(int i = 0; i < order; i++)
			for(int j = i+1; j < order; j++)
				g.addEdge(lbl+i, lbl+j);
		return g;
	}
	
	/**
	 * Generates a complete bipartite graph of the given orders. If the default concatenation
	 * String is being used, then vertices are given ids corresponding to the specifications
	 * on the newsgroup. Otherwise, less ugly ids are assigned: "b_" followed by an integer
	 * (this gives much prettier math-friendly LaTeX output, for example.)
	 * 
	 * @param order1 the order of one side of the graph
	 * @param order2 the order of the other side of the graph
	 * @return a complete bipartite graph of given orders
	 */
	public Graph<T> completeBipartiteGraph(int order1, int order2){
		Graph<T> g = new Graph<T>();
		String lbl;
		if(concatStr.equals("_"))
			lbl = "_K_" + order1 + "_" + order2 + "_";
		else
			lbl = "b_";
		
		
		// create right vertices
		for(int i = 0; i < order1; i++)
			g.addVertex(lbl+i);
		
		// create left vertices and connect each to all right vertices
		for(int i = order1; i < order1+order2; i++){
			g.addVertex(lbl+i);
			for(int j = 0; j < order1; j++)
				g.addEdge(lbl+i, lbl+j);
		}

		return g;
	}
	
	/**
	 * Generates a path graph of the given order. If the default concatenation
	 * String is being used, then vertices are given ids corresponding to the specifications
	 * on the newsgroup. Otherwise, less ugly ids are assigned: "p_" followed by an integer
	 * (this gives much prettier math-friendly LaTeX output, for example.)
	 * 
	 * @param order the order Graph to generate
	 * @return a path graph of given order
	 */
	public Graph<T> pathGraph(int order){
		Graph<T> g = new Graph<T>();
		String lbl;
		if(concatStr.equals("_"))
			lbl = "_P_" + order + "_";
		else
			lbl = "p_";
		
		// assuming order is at least 1
		g.addVertex(lbl+0);
		for(int i = 1; i < order; i++){
			g.addVertex(lbl+i);
			g.addEdge(lbl+i, lbl+(i-1));
		}
		return g;
	}
	
	/**
	 * Generates a cycle graph of the given order. If the default concatenation
	 * String is being used, then vertices are given ids corresponding to the specifications
	 * on the newsgroup. Otherwise, less ugly ids are assigned: "c_" followed by an integer
	 * (this gives much prettier math-friendly LaTeX output, for example.)
	 * 
	 * @param order the order Graph to generate
	 * @return a cycle graph of given order
	 */
	public Graph<T> cycleGraph(int order){
		Graph<T> g = new Graph<T>();
		String lbl;
		if(concatStr.equals("_"))
			lbl = "_C_" + order + "_";
		else
			lbl = "c_";
		
		// assuming order is at least 1
		int i = 0;
		g.addVertex(lbl+i);
		for(i = 1; i < order; i++){
			g.addVertex(lbl+i);
			g.addEdge(lbl+i, lbl+(i-1));
		}
		g.addEdge(lbl+(i-1), lbl+0);
		return g;
	}
	
	/**
	 * Generates a wheel graph of the given order. If the default concatenation
	 * String is being used, then vertices are given ids corresponding to the specifications
	 * on the newsgroup. Otherwise, less ugly ids are assigned: "w_" followed by an integer
	 * (this gives much prettier math-friendly LaTeX output, for example.)
	 * 
	 * @param order the order Graph to generate
	 * @return a wheel graph of given order
	 */
	public Graph<T> wheelGraph(int order){
		Graph<T> g = new Graph<T>();
		String lbl;
		if(concatStr.equals("_"))
			lbl = "_W_" + order + "_";
		else
			lbl = "w_";
		
		int i = 0;
		g.addVertex(lbl+i);
		for(i = 1; i < order-1; i++){
			g.addVertex(lbl+i);
			g.addEdge(lbl+i, lbl+(i-1));
		}
		g.addEdge(lbl+(i-1), lbl+0);
		
		// center vertex
		g.addVertex(lbl+(order-1));
		for(i = 0; i < order-1; i++)
			g.addEdge(lbl+(order-1), lbl+i);
			
		return g;
	}
	
	/**
	 * Generates a star graph of the given order. If the default concatenation
	 * String is being used, then vertices are given ids corresponding to the specifications
	 * on the newsgroup. Otherwise, less ugly ids are assigned: "s_" followed by an integer
	 * (this gives much prettier math-friendly LaTeX output, for example.)
	 * 
	 * @param order the order Graph to generate
	 * @return a star graph of given order
	 */
	public Graph<T> starGraph(int order){
		Graph<T> g = new Graph<T>();
		String lbl;
		if(concatStr.equals("_"))
			lbl = "_S_" + order + "_";
		else
			lbl = "s_";
		
		g.addVertex(lbl+0);
		for(int i = 1; i < order; i++){
			g.addVertex(lbl+i);
			g.addEdge(lbl+i, lbl+0);
		}
		
		return g;
	}
	
	/**
	 * Combines the vertices of the two given graphs (in the way that vertices are combined
	 * when computing graph products.)
	 * 
	 * @param g1 first graph to combine
	 * @param g2 second graph to combine
	 * @return a new Graph whose V x V vertices consist of all the combinations of vertex ids
	 * from g1 and g2 (in that order)
	 */
	private Graph<T> combineVertices(Graph<T> g1, Graph<T> g2){
		Graph<T> h = new Graph<T>();
		for(Node<T> u : g1)
			for(Node<T> v : g2)
				h.addVertex(u, v, concatStr);
		return h;
	}
	
	/**
	 * Prints the normalized output of the graph associated with the given identifier (name)
	 * to standard output.
	 * 
	 * @param name name of the stored Graph to print
	 */
	public void print(String name){
		if(graphs.containsKey(name))
			graphs.get(name).print();
		else
			System.out.println("No graph with name " + name + " exists!");
	}
	
	/**
	 * Creates GraphViz .dot and LaTeX/TikZ .tex output for the graph with the given name
	 * 
	 * @param name name of the stored Graph to print
	 */
	public void texPrint(String name){
		if(graphs.containsKey(name))
			graphs.get(name).texPrint(name);
		else
			System.out.println("No graph with name " + name + " exists!");
	}
}
