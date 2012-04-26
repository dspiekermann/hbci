package org.kapott.hbci.comm;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.kapott.hbci.manager.HBCIUtils;

public class HBCI4JavaLogOutputStream
    extends OutputStream
{
    private ByteArrayOutputStream logdata;
    
    public HBCI4JavaLogOutputStream()
    {
        this.logdata=new ByteArrayOutputStream();
    }
    
    public void write(int b)
        throws IOException
    {
        this.logdata.write(b);
    }

    public void write(byte[] b, int off, int len)
        throws IOException
    {
        this.logdata.write(b, off, len);
    }

    public void write(byte[] b)
        throws IOException
    {
        this.logdata.write(b);
    }

    public void close()
        throws IOException
    {
        this.logdata.flush();
        this.logdata.close();
    }

    public void flush()
        throws IOException
    {
        if (this.logdata.size()!=0) {
            HBCIUtils.log("socket log: "+this.logdata.toString("ISO-8859-1"), HBCIUtils.LOG_DEBUG2);
        }
        this.logdata.reset();
    }
}
