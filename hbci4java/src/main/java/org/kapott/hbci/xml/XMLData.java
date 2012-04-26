package org.kapott.hbci.xml;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/* contains data information for an xml tree */
public class XMLData
{
    private Document   rootdoc;
    private Map        nodes;   // path -> node
    private Properties values;
    private Map        restrictions;
    private Map        errors;
    
    private boolean createOptionalElements;
    
    public XMLData()
    {
        this.nodes=new Hashtable();
        this.values=new Properties();
        this.restrictions=new Hashtable();
        this.errors=new Hashtable();
    }
    

    public Document getRootDoc()
    {
        return rootdoc;
    }

    public void setRootDoc(Document rootdoc)
    {
        this.rootdoc = rootdoc;
    }

    public void storeNode(String path, Node node)
    {
        this.nodes.put(path, node);
    }
    
    public Node getNodeByPath(String path)
    {
        return (Node)nodes.get(path);
    }

    public void setValue(String key, String value)
    {
        if (value!=null) {
            this.values.setProperty(key, value);
        }
    }
    
    public String getValue(String key)
    {
        return this.values.getProperty(key);
    }
    
    public Enumeration getValueNames()
    {
        return this.values.propertyNames();
    }
    
    public Map getErrors()
    {
        return this.errors;
    }
    
    public Iterator getRestrictionPaths()
    {
        return this.restrictions.keySet().iterator();
    }
    
    public Map getRestrictions(String path)
    {
        return (Map)this.restrictions.get(path);
    }
    
    public void setRestrictions(String path, Map restrictions)
    {
        this.restrictions.put(path, restrictions);
    }
    
    public void setCreateOptionalElements(boolean x)
    {
        this.createOptionalElements=x;
    }
    
    public boolean getCreateOptionalElements()
    {
        return this.createOptionalElements;
    }
}
