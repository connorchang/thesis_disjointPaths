public class AggregatedTopology
{
    
    private Domain G;
    private AR ar[][];
    private int borderNum;
     
    
    public AggregatedTopology(Domain G) {
        this.G = G;
        ar = new AR[G.borderNodes.length][G.borderNodes.length];
        borderNum = G.borderNodes.length;
    }
    
    public int getDomainID() 
    {
        return G.getDomainID();
    }
    
    public void findAggregationTopology()
    {
        System.out.println("length " + G.borderNodes.length);
        for ( int i = 0; i < G.borderNodes.length-1; i++) {
            for ( int j = i + 1; j < G.borderNodes.length; j++) {
                System.out.println("\ns = " + G.borderNodes[i] + " t = " + G.borderNodes[j]);
                try {
                    Domain GCopy = G.copyDomain();
                    
                    DijkstraSP sp = new DijkstraSP(GCopy, GCopy.borderNodes[i], GCopy.borderNodes[j]);
    
                    for (DirectedEdge e : sp.pathTo(GCopy.borderNodes[j])) {
                        //StdOut.print(e + "   "); 
                        //e.tagEdge();
                        GCopy.reverseNegate(e);
                    }
                    
                    DijkstraSPMod[][] spmodArray = new DijkstraSPMod[GCopy.borderNodes.length][GCopy.borderNodes.length];
                    for (int x = 0; x < GCopy.borderNodes.length; x++) {
                        for (int y = 0; y < GCopy.borderNodes.length; y++) {
                            if (x == y) { continue; }
                            spmodArray[x][y] = new DijkstraSPMod(GCopy, GCopy.borderNodes[x], GCopy.borderNodes[y]);
                        }
                    }
                        
                    
                    if (i != j)
                        ar[i][j] = new AR(G.borderNodes[i], G.borderNodes[j], sp, spmodArray);
                    
                    //if (ar[i][j].s == 0 && ar[i][j].t == 3) 
//                    {
//                        System.out.println("S = " + ar[i][j].s + " T = " + ar[i][j].t);
//                        System.out.println("cost = " + ar[i][j].sp.distTo(ar[i][j].t));
//
//                        for (DirectedEdge e : ar[i][j].sp.pathTo(ar[i][j].t)) {
//                            StdOut.print(e + "   ");
//                        }
//                        
//                        for (int x = 0; x < GCopy.borderNodes.length; x++) {
//                            for (int y = 0; y < GCopy.borderNodes.length; y++) {
//                                if (x != y)
//                                    System.out.println("distTo = " + ar[i][j].spmodArray[x][y].distTo(ar[i][j].spmodArray[x][y].dest())); 
//                            }
//                        }
                       
//                        
//                        In in = new In();
//                        in.readInt();
//                    }   
                    
                } catch(Exception e) {
                    System.out.println(e);
                }
            }
        }
    }
    

    public int getBorderNum()
    {
        return borderNum;
    }
   
    public void find2DisjointPath()
    {
          
      }
      
      public AR getAR(int i, int j)
      {
          return ar[i][j];
      
      }
      
      public AR[][] getAR()
      {
          return ar;
      }
    
      public class AR
    {
        private int s;
        private int t;
        private DijkstraSP sp;
        private DijkstraSPMod spmodArray[][];
    
        private AR(int s, int t, DijkstraSP sp, DijkstraSPMod[][] spmodArray) 
        {
            this.s = s;
            this.t = t;
            this.sp = sp;
            this.spmodArray = spmodArray;
        }
        
        public DijkstraSP getSP()
        {
            return sp;
        }
        
        public double getCost()
        {
            return sp.distTo(t);
        }
        
        public int getS()
        {
            return s;
        }
        
        public int getT()
        {
            return t;
        }
        
        public DijkstraSPMod[][] getArray()
        {
            return spmodArray;
        }
        
    }
}