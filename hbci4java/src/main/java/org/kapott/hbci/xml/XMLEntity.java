package org.kapott.hbci.xml;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/* An XMLEntity is either an element or an attribute with a "path" */
public class XMLEntity
{
    private Node   node;
    private String path;
    
    public XMLEntity(Node node, String path)
    {
        this.node=node;
        this.path=path;
    }
    
    public Node getNode()
    {
        return node;
    }
    public void setNode(Node node)
    {
        this.node = node;
    }
    public String getPath()
    {
        return path;
    }
    public void setPath(String path)
    {
        this.path = path;
    }
    
    public Element getElement()
    {
        return (Element)this.getNode();
    }
    
    public String toString()
    {
        return "<XMLEntity "+this.path+">";
    }
}
