
import java.util.*;
import java.util.Vector;


public class Domain
{
    private final int DomainID;
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
        
        adj = new LinkedList[V];
        
        
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
        int v, w, i;
        double cost;
        DirectedEdge e;
        Vertex VertexV, VertexW;
        Hashtable <Integer, Vertex> vertexDone = new Hashtable<Integer, Vertex>();
        DomainID = in.readInt();
        
        V = in.readInt();
        E = in.readInt();
        int borderNum = in.readInt(); // borderNum
        adj = (LinkedList<DirectedEdge>[]) new LinkedList[V];
        
        for(i = 0; i < V; i++) {
            adj[i] = new LinkedList<DirectedEdge>();
        }
        
        borderNodes = new int[borderNum];
        for (i = 0; i < borderNum; i++) 
            borderNodes[i] = in.readInt();
        
        for (i = 0; i < E; i++) {
            v = in.readInt();
            Vertex vertexV = createVertex(vertexDone, v, DomainID, -1); 
            
            w = in.readInt();
            Vertex vertexW = createVertex(vertexDone, w, DomainID, -1); 
            cost = in.readDouble();
            e = new DirectedEdge(vertexV, vertexW, cost);
            addEdge(e);
        }
        vertexDone.clear();
        AR = new AggregatedTopology(this);
    }
    
    private Vertex createVertex(Hashtable<Integer, Vertex> vertexDone, int v, int domainID, int globalIndex)
    {
        Vertex vertex;
        if (!vertexDone.containsKey(v)) {
            vertex = new Vertex(domainID, 0, v, globalIndex);
            vertexDone.put(v, vertex);
        } else {
            vertex = (Vertex)vertexDone.get(v);
        }
        
        return vertex;
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
        Domain copyG = new Domain(getDomainID(), V());
        //System.out.println("copyDomain");
        for (DirectedEdge e : this.edges()) {
            DirectedEdge copyEdge = new DirectedEdge(new Vertex(0, 0, e.from()), new Vertex(0, 0, e.to()), e.cost(), e.getTag());
            //System.out.println("copy G edge = " + e);
            copyG.addEdge(copyEdge);
        }
        return copyG;
    }
    
    public void get2DisjointPaths(Vertex s, Vertex t, Vector<DirectedEdge> saveEdges)
    {  
       //int fromDomain, toDomain;
        Vertex nextVertex = s;
        DirectedEdge tmpEdge;
        // path 1 
        
        System.out.println("disjoint path");
        for (DirectedEdge e : saveEdges) {
           if (nextVertex.getDomainID() == t.getDomainID() && nextVertex.getVertexID() == t.getVertexID()) break;
           
           
           
            for (DirectedEdge e1 : saveEdges) { 
                if (e1.getFromVertex().getDomainID() == nextVertex.getDomainID() && e.getFromVertex().getVertexID() == nextVertex.getVertexID()) {
                    System.out.println(e1 + " ");
                    e1.untagEdge();
                    nextVertex = e1.getToVertex();
                    System.out.println(nextVertex.getDomainID() + " " + nextVertex.getVertexID());
                }
            }
        }
        
        
//        
//        
//        while (n != t) {
//            for ( Iterator<DirectedEdge> EdgeIter = adj[n].iterator(); EdgeIter.hasNext(); ) {
//                tmpEdge = EdgeIter.next();
//        
//                if (tmpEdge.getTag()) {
//                    fromDomain = tmpEdge.getFromVertex().getDomainID();
//                    toDomain = tmpEdge.getToVertex().getDomainID();
//                    if (fromDomain != toDomain || fromDomain == 0 || toDomain == 3) {
//                        
//                        n = tmpEdge.getToVertex().getGlobalID();
//                        System.out.println( " edge " + tmpEdge );
//                        tmpEdge.untagEdge();
//                        break;
//                    }
//                    nextVertex = tmpEdge.getToVertex();
//                } else {
//                    for (DirectedEdge e : saveEdges) {
//                        if (nextVertex.getDomainID() == e.getFromVertex().getDomainID() && nextVertex.getVertexID() == e.getFromVertex().getVertexID()) {
//                            System.out.println(e);
//                            e.untagEdge();
//                            nextVertex = e.getToVertex();
//                        }
//                    }
//                }
//               
//            }
//        }
//        
//        System.out.println("Path 2 ");
//        n = s;
//        
        
        
//        while (n != t) {
//            for ( Iterator<DirectedEdge> EdgeIter = adj[n].iterator(); EdgeIter.hasNext(); ) {
//                tmpEdge = EdgeIter.next();
//                System.out.println(tmpEdge + " " +  tmpEdge.getTag() );
//                In in = new In();
//                in.readInt();
//                if (tmpEdge.getTag()) {
//                    n = tmpEdge.to();
//                    System.out.println( tmpEdge );
//                    //tmpEdge.untagEdge();
//                    break;
//                }
//            }
//            
//        }
        
        
        
    }
    
    // addedge WRONG for this function, it should use global ID
    public static void find2DisjointPath(In source, In dest, AggregatedTopology ars[], In interLink)
    {
        Domain globalG = new Domain(-1, 14);
        int globalIndex = 0, v, w, borderNum, interDomainS, interDomainT, interVertexS, interVertexT;
        double cost;
        Hashtable<Integer, Vertex> vertexDone = new Hashtable<Integer, Vertex>();
        Hashtable<Integer, Vertex> globalVertex = new Hashtable<Integer, Vertex>();
        Hashtable<Integer, AggregatedTopology> AT = new Hashtable<Integer, AggregatedTopology>();
        
        for (int i = 0; i < ars.length; i++){
            //System.out.println("ars.domainid " + ars[i].getDomainID());
            AT.put(ars[i].getDomainID(), ars[i]);
        }
        
        
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
        
        DijkstraSP sp = new DijkstraSP(globalG, 8, 11);
        System.out.println("dist " + sp.distTo(11));
        
        for (DirectedEdge e : sp.pathTo(11)){
            System.out.println(e);
        
        }
        if  (sp == null) {
            System.out.println("null");
        }
        
        int fromDomain, toDomain;
        
        //DijkstraSPMod[][] modArray= ars[0].getAR(1, 2).getArray();
        DijkstraSPMod[][] modArray;
        
        
        DijkstraSP[] firstPath = new DijkstraSP[ars.length];
        DijkstraSPMod[] secondPath = new DijkstraSPMod[ars.length];
        System.out.println("first path : ");
        int i = 0;
        for (DirectedEdge e : sp.pathTo(11)) {
            //StdOut.print(e + "   ");
            if ( e == null) 
                System.out.println("null");
            fromDomain = e.getFromVertex().getDomainID();
            toDomain = e.getToVertex().getDomainID();
            if (fromDomain != toDomain || fromDomain == 0 || toDomain == 3) {
                globalG.reverseNegate(e);
                e.tagEdge();
            } else {
                // domain is inter Domain 
                // System.out.printf("domain %d, from %d, to %d", fromDomain, e.getFromVertex().getVertexID(), e.getToVertex().getVertexID());
                modArray = AT.get(fromDomain).getAR(e.getFromVertex().getVertexID(), e.getToVertex().getVertexID()).getArray();
                firstPath[i++] = AT.get(fromDomain).getAR(e.getFromVertex().getVertexID(), e.getToVertex().getVertexID()).getSP();
                modGlobalG(modArray, fromDomain, globalVertex, globalG);
            } 
        }
    
        
        // modifing graph complete
        System.out.println();
        
        DijkstraSPMod spmod = new DijkstraSPMod(globalG, 8, 11);

        i = 0;
        System.out.println("mod Path = ");
        for (DirectedEdge e : spmod.pathTo(11)) {
            StdOut.print(e + "   ");
            
            
            fromDomain = e.getFromVertex().getDomainID();
            toDomain = e.getToVertex().getDomainID();
            if (fromDomain != toDomain || fromDomain == 0 || toDomain == 3) {
                if (!globalG.oppositeEdges(e)) {
                    //StdOut.print("remove  " + e + "   ");
                    globalG.removeEdge(e);
                } else {
                    e.tagEdge();
                }
            } else {
                secondPath[i] = AT.get(fromDomain).getAR(firstPath[i].getS(), firstPath[i].getT()).getArray()[e.getFromVertex().getVertexID()][e.getToVertex().getVertexID()];
                //System.out.printf("second domain %d, from %d, to %d", fromDomain, e.getFromVertex().getVertexID(), e.getToVertex().getVertexID());
                i++;
            } 
        }
        
        Vector<DirectedEdge> saveEdges = new Vector<DirectedEdge>();
        for (i = 0; i < ars.length; i++) {
            interdomainPathCompute(firstPath[i], secondPath[i], saveEdges);
        }
        
        System.out.println("save edges ");
        for (DirectedEdge e: saveEdges) {
            System.out.println(e);
        }
        
        System.out.println();
        
        for (DirectedEdge e : globalG.edges()) {
            fromDomain = e.getFromVertex().getDomainID();
            toDomain = e.getToVertex().getDomainID();
            if (fromDomain != toDomain || fromDomain == 0 || toDomain == 3) {
                if (e.cost() < 0) {
                    //System.out.println("e.cost < 0 ");
                    globalG.addEdge(new DirectedEdge(e.getToVertex(), e.getFromVertex(), -e.cost(), true));
                }
            }
        }
        
        System.out.println("\nSource domain, destination domain and inter domain edges :");
        for (DirectedEdge e: globalG.edges()) {
            if (e.getTag()) {
                
                fromDomain = e.getFromVertex().getDomainID();
                toDomain = e.getToVertex().getDomainID();
                if (fromDomain != toDomain || fromDomain == 0 || toDomain == 3) {
                    System.out.print(e + " ");
                    saveEdges.add(e);
                }
            }
        }
        
        System.out.println("*****************************");
        
        for (DirectedEdge e: saveEdges) {
            System.out.println(e);
        }
       globalG.get2DisjointPaths(globalVertex.get(8), globalVertex.get(11), saveEdges);
        
    }

    private static void interdomainPathCompute(DijkstraSP first, DijkstraSPMod second, Vector<DirectedEdge> save)
    {
        //Vector<DirectedEdge> v = new Vector<DirectedEdge>();
        Domain G = first.getDomain();
        
        for(DirectedEdge e: G.edges()) {
            System.out.println(e.getFromVertex().getDomainID() + "&&&&&&&&&&&&&&&" );
        }
        
//        System.out.println("\nfirst");
        for (DirectedEdge e : first.pathTo(first.getT())) {
            G.reverseNegate(e);
//            System.out.print(e + " " );
            e.tagEdge();
            
        }
        
//        System.out.println("\nsecond ");
        for (DirectedEdge e : second.pathTo(second.getT())) {
//            System.out.print(e + " " );
//            e.tagEdge();
//            
            if (!G.oppositeEdges(e)) {
                //StdOut.print("remove  " + e + "   ");
                G.removeEdge(e);
                //e.tagEdge();
            } else {
                e.tagEdge();
            }
        }
        
        
        for (DirectedEdge e : G.edges()) {
            if (e.cost() < 0) {
                System.out.println(e.getFromVertex().getDomainID() + " &&&&&&&&&&&&&&&&&&&&&&&&&&&& ");
                
                G.addEdge(new DirectedEdge(e.getToVertex(), e.getFromVertex(), -e.cost(), true));
            }
        }
        
        System.out.printf("domain %d edges \n ", G.getDomainID());
        
        for (DirectedEdge e : G.edges()) {
            if (e.getTag()) {
                save.add(e);
                System.out.println(e + " ");
                
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
                    v = getGlobalID(domain, modArray[i][j].getS(), globalVertex);
                    w = getGlobalID(domain, modArray[i][j].getT(), globalVertex);
                    tmpEdge = globalG.getEdge(v, w);
                    tmpEdge.updateCost(modArray[i][j].distTo(modArray[i][j].getT()));
                    //System.out.println("v " + v + " w " + w + " edge " + tmpEdge  );
                    
                }
            }
        }
    }
    
    
    private static int getGlobalID(int domain, int vertex, Hashtable<Integer, Vertex> globalVertex) 
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
