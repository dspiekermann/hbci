package org.kapott.hbci.comm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class LoggingInputStream
    extends InputStream
{
    private InputStream  targetInputStream;
    private OutputStream logger;
    
    public LoggingInputStream(InputStream target, OutputStream logger)
    {
        this.targetInputStream=target;
        this.logger=logger;
    }
    
    public int read() 
        throws IOException
    {
        int c=targetInputStream.read();
        logger.write(c);
        return c;
    }

    public void close()
        throws IOException
    {
        logger.flush();
        targetInputStream.close();
    }

    public int available()
        throws IOException
    {
        return targetInputStream.available();
    }

    public int read(byte[] b, int off, int len)
        throws IOException
    {
        int result=targetInputStream.read(b, off, len);
        logger.write(b, off, result);
        return result;
    }

    public int read(byte[] b)
        throws IOException
    {
        int result=targetInputStream.read(b);
        logger.write(b, 0, result);
        return result;
    }
}
