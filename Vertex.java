public class Vertex
{
    private int DomainID;
    private final int routerType; // 0:border 1:edge 2:status
    private final int vertexID;   // vertexID within domain
    private final int globalID;   // for qosBroker compute the global disjoint path
    
    public Vertex(int DomainID, int routerType, int vertexID)
    {
        this(DomainID, routerType, vertexID, -1);
    }
    
    public Vertex(int DomainID, int routerType, int vertexID, int globalID)
    {
        this.DomainID = DomainID;
        this.routerType = routerType;
        this.vertexID = vertexID;
        this.globalID = globalID;
    }
    
    public int getVertexID ()
    {
        return vertexID;
    }
    
    public int getGlobalID()
    {
        return globalID;
    }
    
    public int getDomainID()
    {
        return DomainID;
    }
    
    public Vertex getVertex(int DomainID, int vertexID)
    {
        if (this.DomainID == DomainID && this.vertexID == vertexID)
            return this;
        else
            return null;
    }
}
