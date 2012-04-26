package org.kapott.hbci.comm;

import java.io.IOException;
import java.io.OutputStream;

public class LoggingOutputStream
    extends OutputStream
{
    private OutputStream targetOutputStream;
    private OutputStream logger;
    
    public LoggingOutputStream(OutputStream target, OutputStream logger)
    {
        this.targetOutputStream=target;
        this.logger=logger;
    }

    public void write(byte[] b) 
        throws IOException
    {
        logger.write(b);
        targetOutputStream.write(b);
    }

    public void write(byte[] b, int off, int len) 
        throws IOException
    {
        logger.write(b, off, len);
        targetOutputStream.write(b, off, len);
    }

    public void write(int b) 
        throws IOException
    {
        logger.write(b);
        targetOutputStream.write(b);
    }

    public void close()
        throws IOException
    {
        logger.flush();
        targetOutputStream.close();
    }

    public void flush()
        throws IOException
    {
        logger.flush();
        targetOutputStream.flush();
    }
}
