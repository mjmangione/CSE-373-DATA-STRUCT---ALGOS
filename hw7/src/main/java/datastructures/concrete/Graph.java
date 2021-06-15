package datastructures.concrete;

import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.concrete.dictionaries.KVPair;
import datastructures.interfaces.IEdge;
import datastructures.interfaces.IList;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IDisjointSet;
import datastructures.interfaces.ISet;
import datastructures.interfaces.IPriorityQueue;
import misc.Sorter;
import misc.exceptions.NoPathExistsException;

/**
 * Represents an undirected, weighted graph, possibly containing self-loops, parallel edges,
 * and unconnected components.
 *
 * Note: This class is not meant to be a full-featured way of representing a graph.
 * We stick with supporting just a few, core set of operations needed for the
 * remainder of the project.
 */
public class Graph<V, E extends IEdge<V> & Comparable<E>> {
    // NOTE 1:
    //
    // Feel free to add as many fields, private helper methods, and private
    // inner classes as you want.
    //
    // And of course, as always, you may also use any of the data structures
    // and algorithms we've implemented so far.
    //
    // Note: If you plan on adding a new class, please be sure to make it a private
    // static inner class contained within this file. Our testing infrastructure
    // works by copying specific files from your project to ours, and if you
    // add new files, they won't be copied and your code will not compile.
    //
    //
    // NOTE 2:
    //
    // You may notice that the generic types of Graph are a little bit more
    // complicated than usual.
    //
    // This class uses two generic parameters: V and E.
    //
    // - 'V' is the type of the vertices in the graph. The vertices can be
    //   any type the client wants -- there are no restrictions.
    //
    // - 'E' is the type of the edges in the graph. We've constrained Graph
    //   so that E *must* always be an instance of IEdge<V> AND Comparable<E>.
    //
    //   What this means is that if you have an object of type E, you can use
    //   any of the methods from both the IEdge interface and from the Comparable
    //   interface
    //
    // If you have any additional questions about generics, or run into issues while
    // working with them, please ask ASAP either on Piazza or during office hours.
    //
    // Working with generics is really not the focus of this class, so if you
    // get stuck, let us know we'll try and help you get unstuck as best as we can.
    private IDictionary<V, ISet<E>> graph;
    private int numEdges = 0;
    private IList<E> edges;

    /**
     * Constructs a new graph based on the given vertices and edges.
     *
     * Note that each edge in 'edges' represents a unique edge. For example, if 'edges'
     * contains an entry for '(A,B)' and for '(B,A)', that means there are two parallel
     * edges between vertex 'A' and vertex 'B'.
     *
     * @throws IllegalArgumentException if any edges have a negative weight
     * @throws IllegalArgumentException if any edges connect to a vertex not present in 'vertices'
     * @throws IllegalArgumentException if 'vertices' or 'edges' are null or contain null
     * @throws IllegalArgumentException if 'vertices' contains duplicates
     */
    public Graph(IList<V> vertices, IList<E> edges) {
        this.edges = edges;
        graph = new ChainedHashDictionary<>();
        for (V vertex : vertices){
            if (vertex == null || graph.containsKey(vertex)){
                throw new IllegalArgumentException();
            }
            graph.put(vertex, new ChainedHashSet<>());
        }
        for (E edge : edges){
            if (edge == null || edge.getVertex1() == null || edge.getVertex2() == null){
                throw new IllegalArgumentException();
            }
            if (edge.getWeight() < 0 || !graph.containsKey(edge.getVertex1()) || !graph.containsKey(edge.getVertex2())){
                throw new IllegalArgumentException();
            }
            numEdges++;
            graph.get(edge.getVertex1()).add(edge);
            graph.get(edge.getVertex2()).add(edge);
        }
    }

    /**
     * Sometimes, we store vertices and edges as sets instead of lists, so we
     * provide this extra constructor to make converting between the two more
     * convenient.
     *
     * @throws IllegalArgumentException if any of the edges have a negative weight
     * @throws IllegalArgumentException if one of the edges connects to a vertex not
     *                                  present in the 'vertices' list
     * @throws IllegalArgumentException if vertices or edges are null or contain null
     */
    public Graph(ISet<V> vertices, ISet<E> edges) {
        // You do not need to modify this method.
        this(setToList(vertices), setToList(edges));
    }

    // You shouldn't need to call this helper method -- it only needs to be used
    // in the constructor above.
    private static <T> IList<T> setToList(ISet<T> set) {
        if (set == null) {
            throw new IllegalArgumentException();
        }
        IList<T> output = new DoubleLinkedList<>();
        for (T item : set) {
            output.add(item);
        }
        return output;
    }

    /**
     * Returns the number of vertices contained within this graph.
     */
    public int numVertices() {
        return graph.size();
    }

    /**
     * Returns the number of edges contained within this graph.
     */
    public int numEdges() {
        return numEdges;
    }

    /**
     * Returns the set of all edges that make up the minimum spanning tree of
     * this graph.
     *
     * If there exists multiple valid MSTs, return any one of them.
     *
     * Precondition: the graph does not contain any unconnected components.
     */
    public ISet<E> findMinimumSpanningTree() {
        ISet<E> mst = new ChainedHashSet<>();
        IDisjointSet<V> included = new ArrayDisjointSet<>();
        for (KVPair<V, ISet<E>> kv : graph) {
            included.makeSet(kv.getKey());
        }
        Sorter sort = new Sorter();
        edges = sort.topKSort(numEdges, edges);
        for (E edge : edges) {
            if (included.findSet(edge.getVertex1()) != included.findSet(edge.getVertex2())) {
                mst.add(edge);
                included.union(edge.getVertex1(), edge.getVertex2());
            }
        }
        return mst;
    }

    /**
     * Returns the edges that make up the shortest path from the start
     * to the end.
     *
     * The first edge in the output list should be the edge leading out
     * of the starting node; the last edge in the output list should be
     * the edge connecting to the end node.
     *
     * Return an empty list if the start and end vertices are the same.
     *
     * @throws NoPathExistsException  if there does not exist a path from the start to the end
     * @throws IllegalArgumentException if start or end is null or not in the graph
     */
    public IList<E> findShortestPathBetween(V start, V end) {
        if (start == null || end == null || !graph.containsKey(start) || !graph.containsKey(end)){
            throw new IllegalArgumentException();
        }
        IList<E> output = new DoubleLinkedList<>();
        if (start.equals(end)) {
            return output;
        }
        IDictionary<V, ComparableVertex> vertices = new ChainedHashDictionary<>();
        IPriorityQueue<ComparableVertex<V, E>> queue = new ArrayHeap<>();
        for (KVPair<V, ISet<E>> kv : graph) {
            vertices.put(kv.getKey(), new ComparableVertex<>(kv.getKey()));
        }
        vertices.get(start).distance = 0.00;


        for (E edge : graph.get(start)){
                ComparableVertex vertex = vertices.get(edge.getOtherVertex(start));
                vertex.distance = edge.getWeight();
                vertex.previous = start;
                vertex.previousEdge = edge;
                if (!queue.contains(vertex)) {
                    queue.add(vertex);
                }
        }

        vertices.get(start).isProcessed = true;
        while (!queue.isEmpty()) {
            ComparableVertex cVertex = queue.removeMin();
            V vertex = (V) cVertex.vertex;
            for (E edge : graph.get(vertex)) {
                    ComparableVertex edgeVertex = vertices.get(edge.getOtherVertex(vertex));
                    if (!edgeVertex.isProcessed && !edgeVertex.vertex.equals(vertex)) {
                        ComparableVertex newVertex = new ComparableVertex(edgeVertex);
                        if (cVertex.distance + edge.getWeight() < edgeVertex.distance) {
                            newVertex.distance = cVertex.distance + edge.getWeight();
                            newVertex.previous = vertex;
                            newVertex.previousEdge = edge;
                        }


                        if (edgeVertex.distance == Double.POSITIVE_INFINITY) {
                            queue.add(newVertex);
                            vertices.put((V) edgeVertex.vertex, newVertex);
                        } else if (edgeVertex.distance > newVertex.distance) {
                            vertices.put((V) edgeVertex.vertex, newVertex);
                            queue.replace(edgeVertex, newVertex);
                        }
                    }
            }
            vertices.get(vertex).isProcessed = true;
        }
        ComparableVertex current = vertices.get(end);
        if (current.previous == null) {
            throw new NoPathExistsException();
        }
        while (current.previous != null) {
            output.insert(0, (E) current.previousEdge);
            current = vertices.get((V) current.previous);
        }

        return output;

    }


     private static final class ComparableVertex<V, E> implements Comparable<ComparableVertex<V, E>> {
        // Add any fields you think will be useful
        private Double distance;
        private boolean isProcessed;
        private V previous;
        private E previousEdge;
        private final V vertex;

        private ComparableVertex(V vertex){
            this.distance = Double.POSITIVE_INFINITY;
            this.isProcessed = false;
            this.previous = null;
            this.previousEdge = null;
            this.vertex = vertex;
        }

        private ComparableVertex(ComparableVertex other){
             this.distance = other.distance;
             this.isProcessed = other.isProcessed;
             this.previous = (V) other.previous;
             this.previousEdge = (E) other.previousEdge;
             this.vertex = (V) other.vertex;
         }

        public int compareTo(ComparableVertex other) {
            return (int) ((distance - other.distance) * 1000);
        }
    }
}
