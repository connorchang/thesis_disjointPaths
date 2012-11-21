public class DirectedEdge
{
    private final Vertex v; // edge source
    private final Vertex w; // edge target
    private double cost;    // edge cost
    private boolean tag;    // for taging shortest edge 
    
    public DirectedEdge(Vertex v, Vertex w, double cost)
    {
        this(v, w, cost, false);
    }
    
    public DirectedEdge(Vertex v, Vertex w, double cost, boolean tag)
    {
        this.v = v;
        this.w = w;
        this.cost = cost;
        this.tag = tag;
    }
    
    public double cost()
    {
        return cost;
    }
    
    public int from()
    {
        if (v.getGlobalID() != -1)
            return v.getGlobalID();
        else
            return v.getVertexID();
    }
    
    public int to()
    {
        if (w.getGlobalID() != -1)
            return w.getGlobalID();
        else
            return w.getVertexID();
    }
    
    public Vertex getFromVertex()
    {
        return v;
    }
    
    public Vertex getToVertex()
    {
        return w;
    }
    
    public String toString()
    {
        if (v.getGlobalID() == -1) 
            return String.format("(%d, %d)->(%d, %d) %.2f %b", v.getDomainID(), v.getVertexID(), w.getDomainID(), w.getVertexID(), cost, tag);
        else 
            return String.format("(%d, %d)->(%d, %d) %.2f", v.getDomainID(), v.getVertexID(), w.getDomainID(), w.getVertexID(), cost);
    }
    
    public void updateCost(double newCost)
    {
        this.cost = newCost;
    }
    
    public void tagEdge()
    {
        tag = true;
    }
    public void untagEdge()
    {
        tag = false;
    }
    public boolean getTag()
    {
        return tag;
    }
}
