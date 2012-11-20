/*************************************************************************
 *  Compilation:  javac DijkstraSP.java
 *  Execution:    java DijkstraSP input.txt s
 *  Dependencies: EdgeWeightedDigraph.java IndexMinPQ.java Stack.java DirectedEdge.java
 *  Data files:   http://algs4.cs.princeton.edu/44sp/tinyEWD.txt
 *                http://algs4.cs.princeton.edu/44sp/mediumEWD.txt
 *                http://algs4.cs.princeton.edu/44sp/largeEWD.txt
 *
 *  Dijkstra's algorithm. Computes the shortest path tree.
 *  Assumes all costs are nonnegative.
 *
 *  % java DijkstraSP tinyEWD.txt 0
 *  0 to 0 (0.00)  
 *  0 to 1 (1.05)  0->4  0.38   4->5  0.35   5->1  0.32   
 *  0 to 2 (0.26)  0->2  0.26   
 *  0 to 3 (0.99)  0->2  0.26   2->7  0.34   7->3  0.39   
 *  0 to 4 (0.38)  0->4  0.38   
 *  0 to 5 (0.73)  0->4  0.38   4->5  0.35   
 *  0 to 6 (1.51)  0->2  0.26   2->7  0.34   7->3  0.39   3->6  0.52   
 *  0 to 7 (0.60)  0->2  0.26   2->7  0.34   
 *
 *  % java DijkstraSP mediumEWD.txt 0
 *  0 to 0 (0.00)  
 *  0 to 1 (0.71)  0->44  0.06   44->93  0.07   ...  107->1  0.07   
 *  0 to 2 (0.65)  0->44  0.06   44->231  0.10  ...  42->2  0.11   
 *  0 to 3 (0.46)  0->97  0.08   97->248  0.09  ...  45->3  0.12   
 *  0 to 4 (0.42)  0->44  0.06   44->93  0.07   ...  77->4  0.11   
 *  ...
 *
 *************************************************************************/
import java.util.*;

public class DijkstraSP {
    private double[] distTo;          // distTo[v] = distance  of shortest s->v path
    private DirectedEdge[] edgeTo;    // edgeTo[v] = last edge on shortest s->v path
    private IndexMinPQ<Double> pq;    // priority queue of vertices
    private Domain G;

    public DijkstraSP(Domain G) {
        distTo = new double[G.V()];
        edgeTo = new DirectedEdge[G.V()];
        for (int v = 0; v < G.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;
       
        
        // relax vertices in order of distance from s
        pq = new IndexMinPQ<Double>(G.V());
        
    }
   
   
    
    
    
    public DijkstraSP(Domain G, int s) {
      this.G = G;
        distTo = new double[G.V()];
        edgeTo = new DirectedEdge[G.V()];
        for (int v = 0; v < G.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;
        distTo[s] = 0.0;

        // relax vertices in order of distance from s
        pq = new IndexMinPQ<Double>(G.V());
        pq.insert(s, distTo[s]);
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            for (DirectedEdge e : G.adj(v))
                relax(e);
        }

        // check optimality conditions
        assert check(G, s);
    }

public DijkstraSP(Domain G, int s, int t) {
    this.G = G;
    distTo = new double[G.V()];
        edgeTo = new DirectedEdge[G.V()];
        for (int v = 0; v < G.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;
        distTo[s] = 0.0;
        

        // relax vertices in order of distance from s
        pq = new IndexMinPQ<Double>(G.V());
        pq.insert(s, distTo[s]);
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            if (v == t) {
                break;
            }
            for (DirectedEdge e : G.adj(v))
                relax(e);
        }

        // check optimality conditions
     //   assert check(G, s);
    }
    


public DijkstraSP(Domain G, Vertex source, Vertex dest) {
   this(G, source.getGlobalID(), dest.getGlobalID());
}

public Domain getDomain()
{
    return G;
}

// relax edge e and update pq if changed
    private void relax(DirectedEdge e) {
        int v = e.from(), w = e.to();
        if (distTo[w] > distTo[v] + e.cost()) {
            distTo[w] = distTo[v] + e.cost();
            edgeTo[w] = e;
            if (pq.contains(w)) pq.decreaseKey(w, distTo[w]);
            else                pq.insert(w, distTo[w]);
        }
    }
    
    public void computeSP(Domain G, int s, int t)
    {
        distTo[s] = 0.0;
        pq.insert(s, distTo[s]);
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            if (v == t) {
                break;
            }
            for (DirectedEdge e : G.adj(v))
                relax(e);
        }    
    }

    // length of shortest path from s to v
    public double distTo(int v) {
        return distTo[v];
    }

    // is there a path from s to v?
    public boolean hasPathTo(int v) {
        return distTo[v] < Double.POSITIVE_INFINITY;
    }

    // shortest path from s to v as an Iterable, null if no such path
    public Iterable<DirectedEdge> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        Stack<DirectedEdge> path = new Stack<DirectedEdge>();
        for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()]) {
            path.push(e);
        }
        return path;
    }


    // check optimality conditions:
    // (i) for all edges e:            distTo[e.to()] <= distTo[e.from()] + e.cost()
    // (ii) for all edge e on the SPT: distTo[e.to()] == distTo[e.from()] + e.cost()
    private boolean check(Domain G, int s) {

        // check that edge costs are nonnegative
        for (DirectedEdge e : G.edges()) {
            if (e.cost() < 0) {
                System.err.println("negative edge cost detected");
                return false;
            }
        }

        // check that distTo[v] and edgeTo[v] are consistent
        if (distTo[s] != 0.0 || edgeTo[s] != null) {
            System.err.println("distTo[s] and edgeTo[s] inconsistent");
            return false;
        }
        for (int v = 0; v < G.V(); v++) {
            if (v == s) continue;
            if (edgeTo[v] == null && distTo[v] != Double.POSITIVE_INFINITY) {
                System.err.println("distTo[] and edgeTo[] inconsistent");
                return false;
            }
        }

        // check that all edges e = v->w satisfy distTo[w] <= distTo[v] + e.cost()
        for (int v = 0; v < G.V(); v++) {
            for (DirectedEdge e : G.adj(v)) {
                int w = e.to();
                if (distTo[v] + e.cost() < distTo[w]) {
                    System.err.println("edge " + e + " not relaxed");
                    return false;
                }
            }
        }

        // check that all edges e = v->w on SPT satisfy distTo[w] == distTo[v] + e.cost()
        for (int w = 0; w < G.V(); w++) {
            if (edgeTo[w] == null) continue;
            DirectedEdge e = edgeTo[w];
            int v = e.from();
            if (w != e.to()) return false;
            if (distTo[v] + e.cost() != distTo[w]) {
                System.err.println("edge " + e + " on shortest path not tight");
                return false;
            }
        }
        return true;
    }


    public static void main(String[] args) {
        In in = new In(args[0]);
        Domain G = new Domain(in);
        int s = Integer.parseInt(args[1]);
        int t = Integer.parseInt(args[2]);
        // compute shortest paths
        DijkstraSP sp = new DijkstraSP(G, s, t);

        
        

         for (DirectedEdge e : sp.pathTo(t)) {
             StdOut.print(e + "   ");
             e.tagEdge();
             
             //adj[e.to()]
             // no need to addEdge only 
             //G.addEdge(new DirectedEdge(new Vertex(0, 0, e.to()), new Vertex(0, 0, e.from()), -e.cost()));
             //G.removeEdge(e);
             G.reverseNegate(e);
                 
             //System.out.println(G.adj(e.from()));
               
             
         
         }
         
         System.out.println();
         
           DijkstraSPMod spmod = new DijkstraSPMod(G, s, t);

        //System.out.println("cost " + sp.distTo(5));

         for (DirectedEdge e : spmod.pathTo(t)) {
             StdOut.print(e + "   ");
           
             if (!G.oppositeEdges(e)) {
                StdOut.print("mod tag " + e + "   ");
                G.removeEdge(e);
                 //e.tagEdge();
             } else {
                 e.tagEdge();
             }
             
         }
                 
                 
         
         
         System.out.println("******");
         
        for (DirectedEdge e : G.edges()) {
            //if (e.getTag())
             //   StdOut.print(e + "   ");
                if (e.cost() < 0) {
                    G.addEdge(new DirectedEdge(new Vertex(0, 0, e.to()), new Vertex(0, 0, e.from()), -e.cost(), true));
                
                }
        
        }
//         
//        for (DirectedEdge e : G.edges()) {
//            if (e.getTag())
//                StdOut.print(e + "   ");
//          
//        }
        
        
        
        
        G.get2DisjointPaths(s, t);
        
        // print shortest path
//        for (int t = 0; t < G.V(); t++) {
//            if (sp.hasPathTo(t)) {
//                StdOut.printf("%d to %d (%.2f)  ", s, t, sp.distTo(t));
//                if (sp.hasPathTo(t)) {
//                    for (DirectedEdge e : sp.pathTo(t)) {
//                        StdOut.print(e + "   ");
//                    }
//                }
//                StdOut.println();
//            }
//            else {
//                StdOut.printf("%d to %d         no path\n", s, t);
//            }
//        }
    }

}
