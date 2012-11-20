
import java.util.*;


public class Domain
{
    private final int DomainID;
    //private Graph G;
    private LinkedList<DirectedEdge>[] adj;
    public int[] borderNodes; // public for testing
    private AggregatedTopology AR;
    private int V; // number of vertices
    private int E;
    
    public Domain(int domainID, int V)
    {
        int i;
        DomainID = domainID;
        this.V = V;
       
        adj = new LinkedList [V];
        
        
        for(i = 0; i < V; i++) {
            adj[i] = new LinkedList<DirectedEdge>();
        }
        
        
        // TODO need modify here, change 4 to variable
        borderNodes = new int[4];
        for (i = 0; i < 4; i++) 
            borderNodes[i] = i;
     }
    
    
    public Domain(In in) 
    {
        DomainID = in.readInt();
        V = in.readInt();
        E = in.readInt();
        int borderNum = in.readInt(); // borderNum
        adj = (LinkedList<DirectedEdge>[]) new LinkedList[V];
        
        for(int v = 0; v < V; v++) {
            adj[v] = new LinkedList<DirectedEdge>();
        }
        
        borderNodes = new int[borderNum];
        for (int i = 0; i < borderNum; i++) 
            borderNodes[i] = in.readInt();
        
        // System.out.println("E " + E);
        for (int i = 0; i < E; i++) {
            int v = in.readInt();
            Vertex vertexV = new Vertex(DomainID, 0, v);
            
            int w = in.readInt();
            Vertex vertexW = new Vertex(DomainID, 0, w);
            double cost = in.readDouble();
            DirectedEdge e = new DirectedEdge(vertexV, vertexW, cost);
            addEdge(e);
            
            //findAggregationTopology(2);
        }
        
        AR = new AggregatedTopology(this);
    }
    
    
    public int getDomainID()
    {
        return DomainID;
    }
    public int V() 
    {
        return V;
    }
    
    public void reverseNegate(DirectedEdge e)
    {
         DirectedEdge tmpEdge;
         removeEdge(e);
       
         for ( Iterator<DirectedEdge> EdgeIter = adj[e.to()].iterator(); EdgeIter.hasNext(); ) {
             tmpEdge = EdgeIter.next();
             if (tmpEdge.to() == e.from()) {
                 tmpEdge.updateCost(-e.cost());
                 //System.out.println( tmpEdge );
             }
         }
    }
    
    public DirectedEdge getEdge(int from, int to)
    {
        DirectedEdge tmpEdge;
        for ( Iterator<DirectedEdge> edgeIter = adj[from].iterator(); edgeIter.hasNext(); ) {
             tmpEdge = edgeIter.next();
             if (tmpEdge.to() == to) {
                //System.out.println("tmpEdge.to " + tmpEdge.to() + " to " + to);
                 return tmpEdge;
             }
         }
        return null;
    
    }
    
    public boolean oppositeEdges(DirectedEdge e)
    { 
        DirectedEdge tmpEdge;
        
        for ( Iterator<DirectedEdge> EdgeIter = adj[e.to()].iterator(); EdgeIter.hasNext(); ) {
            tmpEdge = EdgeIter.next();
            if (tmpEdge.to() == e.from()) {
                tmpEdge.untagEdge();
                return true;
                //System.out.println( tmpEdge );
            }
        }
        return false;
    }
    
    public void addEdge(DirectedEdge e)
    {
        //System.out.println("e.from()" + e.from());
        adj[e.from()].add(e);
    }
    
    public void removeEdge(DirectedEdge e)
    {
        adj[e.from()].remove(e);
    }
    
    public Iterable<DirectedEdge> adj(int v)
    {
        return adj[v];
    }
    
    public int outdegree(int v) 
    {
        return adj[v].size();
    }
   

    
    
    
    public Domain copyDomain() 
    {
        Domain copyG = new Domain(0, V());
        //System.out.println("copyDomain");
        for (DirectedEdge e : this.edges()) {
          DirectedEdge copyEdge = new DirectedEdge(new Vertex(0, 0, e.from()), new Vertex(0, 0, e.to()), e.cost(), e.getTag());
            //System.out.println("copy G edge = " + e);
            copyG.addEdge(copyEdge);
        }
        return copyG;
    }
    
    public void get2DisjointPaths(int s, int t)
    {  
        int n = s; 
        DirectedEdge tmpEdge;
       // path 1 
        while (n != t) {
            for ( Iterator<DirectedEdge> EdgeIter = adj[n].iterator(); EdgeIter.hasNext(); ) {
                tmpEdge = EdgeIter.next();
                
                if (tmpEdge.getTag()) {
                    n = tmpEdge.to();
                    System.out.println( tmpEdge );
                    tmpEdge.untagEdge();
                    break;
                }
            }
        
        }
       
        System.out.println("Path 2 ");
        n = s;
       
        
        
        while (n != t) {
            for ( Iterator<DirectedEdge> EdgeIter = adj[n].iterator(); EdgeIter.hasNext(); ) {
                tmpEdge = EdgeIter.next();
                System.out.println(tmpEdge + " " +  tmpEdge.getTag() );
                In in = new In();
                in.readInt();
                if (tmpEdge.getTag()) {
                    n = tmpEdge.to();
                    System.out.println( tmpEdge );
                    //tmpEdge.untagEdge();
                    break;
                }
            }
        
        }
        
        
        
    }
    
  
//    
//    public void findAggregationTopology()
//    {
//        for ( int i = 0; i < borderNodes.length-1; i++) {
//            for ( int j = i + 1; j < borderNodes.length; j++) {
//                //System.out.println("s = " + borderNodes[i] + " t = " + borderNodes[j]);
//                   try {
//                       //Domain GCopy = new Domain();
//                        Domain GCopy = (Domain) this.clone();
//                        DijkstraSP sp = new DijkstraSP(GCopy, borderNodes[i], borderNodes[j]);
//                
//                        System.out.println("distTo = " + sp.distTo(borderNodes[j]));
//                
//                       for (DirectedEdge e : sp.pathTo(borderNodes[j])) {
//                           StdOut.print(e + "   "); 
//                           e.tagEdge();
//                     
//                           GCopy.reverseNegate(e);
//                       }
//                       for (DirectedEdge e : GCopy.edges()) {
//                               //if (e.getTag())
//                                   StdOut.print(e + "   ");
//                       }
//                        
//                       
//                        
//                   } catch(Exception e) {
//                       System.out.println(e);
//                   }
//                
//                
//              //  DijkstraSP sp = new DijkstraSP(GCopy, borderNodes[i], borderNodes[j]);
//                
//                
//                
//                
//            }
//        
//        }
        
        
       
//       for (int i = 0; i < borderNum-1; i++) {
//            for (int j = i+1; j < borderNum; j++) {
//                borderPath[i][j] = new DijkstraSP(this, i,j);
//
//                GMod = reverseNegateGraph(); // reverse edge and negate cost
//                
//                // based on modifed graph Gmod
//                // for each border node, compute the shortest path to the rest border nodes
//                
//               AT[i][j] = (new AggregatedTopology(DomainID, Gmod, i, j, borderNum)).getAT();
//            }
//        }
//    }
//    
    
    
    // addedge WRONG for this function, it should use global ID
    public static void find2DisjointPath(In source, In dest, AggregatedTopology ars[], In interLink)
    {
        Domain globalG = new Domain(-1, 14);
        int globalIndex = 0, v, w, borderNum, interDomainS, interDomainT, interVertexS, interVertexT;
        double cost;
        Hashtable<Integer, Vertex> vertexDone = new Hashtable<Integer, Vertex>();
        Hashtable<Integer, Vertex> globalVertex = new Hashtable<Integer, Vertex>();
        
        Vertex vertexV, vertexW;
        
        
        for(int i = 0; i < ars.length; i++) {
        
            borderNum =  ars[i].getAR().length;
            
                  
//            System.out.println("domainID = " + ars[i].getDomainID());
            
            
            
            vertexDone.clear();
            for (int x = 0; x < borderNum-1; x++) {
                for (int y = x + 1; y < borderNum; y++) {
                    
                  
                    v = ars[i].getAR(x, y).getS();
                    
                    
                    if (!vertexDone.containsKey(v)) {
                        vertexV = new Vertex(ars[i].getDomainID(), 0, v, globalIndex);
                        globalVertex.put(globalIndex++, vertexV);
                        vertexDone.put(v, vertexV);
                    } else {
                        vertexV = (Vertex)vertexDone.get(v);
                    }
                    
                    
                    
                    
                   w = ars[i].getAR(x, y).getT();
                    
                     if (!vertexDone.containsKey(w)) {
                        vertexW = new Vertex(ars[i].getDomainID(), 0, w, globalIndex);
                        globalVertex.put(globalIndex++, vertexW);
                        vertexDone.put(w, vertexW);
                    } else {
                        vertexW = (Vertex)vertexDone.get(w);
                    }
                    
                    cost = ars[i].getAR(x, y).getCost();
                    DirectedEdge e = new DirectedEdge(vertexV, vertexW, cost);
                    
                    
                    //System.out.println("i = " + i + " v = " + v + " w = " + w + " cost = " + cost + " global " + globalIndex);
                    
                    
                    globalG.addEdge(e);
                    e = new DirectedEdge(vertexW, vertexV, cost);
                    globalG.addEdge(e);
                }
            
            }
            
            //System.out.println(globalVertex.size());
        }
        
        
        
        globalIndex = sourceDest(source, globalIndex, globalG, globalVertex);
        globalIndex = sourceDest(dest, globalIndex, globalG, globalVertex);
        
        
        int interLinkCount = interLink.readInt();
        
        //System.out.println(interLinkCount + "count ");
        
        for (int k = 0; k < interLinkCount; k++) {
            interDomainS = interLink.readInt();
            interVertexS = interLink.readInt();
            interDomainT = interLink.readInt();
            interVertexT = interLink.readInt();
            cost = interLink.readDouble();
            
            //System.out.println(interDomainS + " " + " " + interVertexS + " " + cost + " " + interDomainT + " " + interVertexT);
            vertexV = vertexW = null;
            
            for (Vertex value : globalVertex.values()) {
                if (value.getDomainID() == interDomainS && value.getVertexID() == interVertexS) {
                    vertexV = globalVertex.get(value.getGlobalID());
//                    System.out.println(value.getVertexID() +  " "+ value.getDomainID());
                } else if(value.getDomainID() == interDomainT && value.getVertexID() == interVertexT)  {
                    vertexW = globalVertex.get(value.getGlobalID());
                }
//            TODO break when vertexV and vertexW are found
            }
            if (vertexV != null && vertexW != null) {
                DirectedEdge e = new DirectedEdge(vertexV, vertexW, cost);
                globalG.addEdge(e);
            }
            
            
        }
        
        
//        for (DirectedEdge e : globalG.edges()) {
//            //if (e.getTag())
//            StdOut.print(e + "   ");
//        }
//        
//        
          DijkstraSP sp = new DijkstraSP(globalG, 8, 11);
          //System.out.println(sp.distTo(11));
        
        
          

          int fromDomain, toDomain;
        
        System.out.println("first path : ");
         for (DirectedEdge e : sp.pathTo(11)) {
             StdOut.print(e + "   ");
             
             fromDomain = e.getFromVertex().getDomainID();
             toDomain = e.getToVertex().getDomainID();
             if (fromDomain != toDomain || fromDomain == 0 || toDomain == 3) {
                 globalG.reverseNegate(e);
                 e.tagEdge();
             } 
         }
         System.out.println();
        
        // inter domain 1
        // ars[0].getAR(0, 3).getArray();
        DijkstraSPMod[][] modArray= ars[0].getAR(1, 2).getArray();
        modGlobalG(modArray, ars[0].getDomainID(), globalVertex, globalG);
      
        DijkstraSPMod secondPath = modArray[0][3];
        
        modArray = ars[1].getAR(0, 2).getArray();
        modGlobalG(modArray, ars[1].getDomainID(), globalVertex, globalG);
        
       
        
//        for (DirectedEdge e : ars[0].getAR(1, 2).getSP().pathTo(2)) {
//            System.out.println(e + " " );
//            e.tagEdge();
//            
//        }
//        
        
        
        DijkstraSPMod spmod = new DijkstraSPMod(globalG, 8, 11);
        
        System.out.println("mod Path = ");
          for (DirectedEdge e : spmod.pathTo(11)) {
             StdOut.print(e + "   ");
           
             if (!globalG.oppositeEdges(e)) {
                StdOut.print("remove  " + e + "   ");
                globalG.removeEdge(e);
                 //e.tagEdge();
             } else {
                 e.tagEdge();
             }
          }
          
          DijkstraSP firstPath = ars[0].getAR(1, 2).getSP();
          //DijkstraSPMod secondPath = modArray[0,3];
          
          interdomainPathCompute(firstPath, secondPath);
          
        System.out.println();
    
        for (DirectedEdge e : globalG.edges()) {
            //if (e.getTag())
                //StdOut.print(e + "   ");
            if (e.cost() < 0) {
                System.out.println("e.cost < 0 ");
                globalG.addEdge(new DirectedEdge(e.getToVertex(), e.getFromVertex(), -e.cost(), true));
            }
        }
         
        globalG.get2DisjointPaths(8, 11);
          
    }
//    
    
    private static void interdomainPathCompute(DijkstraSP first, DijkstraSPMod second)
    {
        Domain G = first.getDomain();
      
        for (DirectedEdge e : first.pathTo(2)) {
            G.reverseNegate(e);
            System.out.println(e + " " );
            e.tagEdge();
            
        }
        
        System.out.println("second ");
         for (DirectedEdge e : second.pathTo(3)) {
            System.out.println(e + " " );
//            e.tagEdge();
//            
             if (!G.oppositeEdges(e)) {
                StdOut.print("remove  " + e + "   ");
                G.removeEdge(e);
                 //e.tagEdge();
             } else {
                 e.tagEdge();
             }
            
            
        }
         
         
           for (DirectedEdge e : G.edges()) {
            if (e.cost() < 0) {
                G.addEdge(new DirectedEdge(e.getToVertex(), e.getFromVertex(), -e.cost(), true));
            }
           }
        
        System.out.println("final ");
           
           for (DirectedEdge e : G.edges()) {
            if (e.getTag()) {
                StdOut.println(e);
            }
           }
        
    
    }
    
    private static void modGlobalG(DijkstraSPMod[][] modArray, int domain, Hashtable<Integer, Vertex> globalVertex, Domain globalG) 
    {
        //System.out.println("modArray " + modArray.length);
        int borderNum = modArray.length, v, w;
        DirectedEdge tmpEdge;
        
        for (int i = 0; i < borderNum; i++) {
            for (int j = 0; j < borderNum; j++) {
                if (i != j){
                    //System.out.println(modArray[i][j].source() + " -> " + modArray[i][j].dest() + " distTo = " + modArray[i][j].distTo(modArray[i][j].dest()));
                    v = getGlobalVertex(domain, modArray[i][j].source(), globalVertex);
                    w = getGlobalVertex(domain, modArray[i][j].dest(), globalVertex);
                    tmpEdge = globalG.getEdge(v, w);
                    tmpEdge.updateCost(modArray[i][j].distTo(modArray[i][j].dest()));
                    //System.out.println("v " + v + " w " + w + " edge " + tmpEdge  );
                    
                }
            }
        }
    }
    
    
    private static int getGlobalVertex(int domain, int vertex, Hashtable<Integer, Vertex> globalVertex) 
    {
         for (Vertex value : globalVertex.values()) {
                if (value.getDomainID() == domain && value.getVertexID() == vertex) {
                    return value.getGlobalID();
                }
         }
         return -1;
    }
    
    private static int sourceDest(In in, int globalIndex, Domain globalG, Hashtable<Integer, Vertex> globalVertex)
    {
        int domain = in.readInt();
        int linkCount = in.readInt();
        int sourceDest = in.readInt();
        int v, w;
        double cost;
        Vertex vertexV, vertexW;
        Hashtable<Integer, Vertex> vertexDone = new Hashtable<Integer, Vertex>();
        
        //System.out.println(domain + " " + linkCount + " " + sourceDest);
        vertexDone.clear();
        for (int i = 0; i < linkCount; i++) {
            
            v = in.readInt();
           
            
            if (!vertexDone.containsKey(v)) {
                // if (v == sourceDest) 
                     //System.out.println("souceDest " + globalIndex);
                
                vertexV = new Vertex(domain, 0, v, globalIndex);
                globalVertex.put(globalIndex++, vertexV);
                vertexDone.put(v, vertexV);
            } else {
                vertexV = (Vertex)vertexDone.get(v);
            }
            
            
            
            
            w = in.readInt();
            
            if (!vertexDone.containsKey(w)) {
                vertexW = new Vertex(domain, 0, w, globalIndex);
                globalVertex.put(globalIndex++, vertexW);
                vertexDone.put(w, vertexW);
            } else {
                vertexW = (Vertex)vertexDone.get(w);
            }
            
            cost = in.readDouble();
            DirectedEdge e = new DirectedEdge(vertexV, vertexW, cost);
            
            
            //System.out.println("i = " + i + " v = " + v + " w = " + w + " cost = " + cost + " global " + globalIndex);
            
            
            globalG.addEdge(e);
            
            
            
        
        }
        return globalIndex;
    
    }
    
    
    /**
     * Return all edges in this digraph as an Iterable.
     * To iterate over the edges in the digraph, use foreach notation:
     * <tt>for (DirectedEdge e : G.edges())</tt>.
     */
    public Iterable<DirectedEdge> edges() {
        Bag<DirectedEdge> list = new Bag<DirectedEdge>();
        for (int v = 0; v < V; v++) {
            for (DirectedEdge e : adj(v)) {
                list.add(e);
            }
        }
        return list;
    } 
    
    
    public static void main(String[] args) 
    {
        In inSource = new In(args[0]);
        In inDest = new In(args[1]);
        In inInter1 = new In(args[2]);
        In inInter2 = new In(args[3]);
        In interLink = new In(args[4]);
        
        //Domain GSource = new Domain(inSource);
        //Domain GDest = new Domain(inDest);
        Domain GInter1 = new Domain(inInter1);
        Domain GInter2 = new Domain(inInter2);
        
        
        //GSource.AR.findAggregationTopology();
        AggregatedTopology interARs[] = {GInter1.AR, GInter2.AR};
        //AR.findAggregationTopology();
        for (int i = 0; i < interARs.length; i++) {
            interARs[i].findAggregationTopology();
        }
        
        find2DisjointPath(inSource, inDest, interARs, interLink); 
        
    }
    
    
    /**
     * Return a string representation of this graph.
     */
    // public String toString() {
    // String NEWLINE = System.getProperty("line.separator");
    // StringBuilder s = new StringBuilder();
    // s.append(V + " " + E + NEWLINE);
    // for (int v = 0; v < V; v++) {
    //    s.append(v + ": ");
    //    for (DirectedEdge e : adj[v]) {
    //   s.append(e + "  ");
    //    }
    //    s.append(NEWLINE);
    // }
    // return s.toString();
    // }
    
}
