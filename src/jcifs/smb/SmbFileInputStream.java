package jcifs.smb;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import jcifs.util.LogStream;
import jcifs.util.transport.TransportException;

public class SmbFileInputStream extends InputStream
{
  private int access;
  SmbFile file;
  private long fp;
  private int openFlags;
  private int readSize;
  private byte[] tmp = new byte[1];

  public SmbFileInputStream(String paramString)
    throws SmbException, MalformedURLException, UnknownHostException
  {
    this(new SmbFile(paramString));
  }

  public SmbFileInputStream(SmbFile paramSmbFile)
    throws SmbException, MalformedURLException, UnknownHostException
  {
    this(paramSmbFile, 1);
  }

  SmbFileInputStream(SmbFile paramSmbFile, int paramInt)
    throws SmbException, MalformedURLException, UnknownHostException
  {
    this.file = paramSmbFile;
    this.openFlags = (paramInt & 0xFFFF);
    this.access = (0xFFFF & paramInt >>> 16);
    if (paramSmbFile.type != 16)
    {
      paramSmbFile.open(paramInt, this.access, 128, 0);
      this.openFlags = (0xFFFFFFAF & this.openFlags);
    }
    while (true)
    {
      this.readSize = Math.min(-70 + paramSmbFile.tree.session.transport.rcv_buf_size, -70 + paramSmbFile.tree.session.transport.server.maxBufferSize);
      return;
      paramSmbFile.connect0();
    }
  }

  public int available()
    throws IOException
  {
    if (this.file.type != 16)
      return 0;
    TransPeekNamedPipeResponse localTransPeekNamedPipeResponse;
    try
    {
      SmbNamedPipe localSmbNamedPipe = (SmbNamedPipe)this.file;
      this.file.open(32, 0xFF0000 & localSmbNamedPipe.pipeType, 128, 0);
      TransPeekNamedPipe localTransPeekNamedPipe = new TransPeekNamedPipe(this.file.unc, this.file.fid);
      localTransPeekNamedPipeResponse = new TransPeekNamedPipeResponse(localSmbNamedPipe);
      localSmbNamedPipe.send(localTransPeekNamedPipe, localTransPeekNamedPipeResponse);
      if ((localTransPeekNamedPipeResponse.status == 1) || (localTransPeekNamedPipeResponse.status == 4))
      {
        this.file.opened = false;
        return 0;
      }
    }
    catch (SmbException localSmbException)
    {
      throw seToIoe(localSmbException);
    }
    int i = localTransPeekNamedPipeResponse.available;
    return i;
  }

  public void close()
    throws IOException
  {
    try
    {
      this.file.close();
      this.tmp = null;
      return;
    }
    catch (SmbException localSmbException)
    {
      throw seToIoe(localSmbException);
    }
  }

  public int read()
    throws IOException
  {
    if (read(this.tmp, 0, 1) == -1)
      return -1;
    return 0xFF & this.tmp[0];
  }

  public int read(byte[] paramArrayOfByte)
    throws IOException
  {
    return read(paramArrayOfByte, 0, paramArrayOfByte.length);
  }

  public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    return readDirect(paramArrayOfByte, paramInt1, paramInt2);
  }

  public int readDirect(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    if (paramInt2 <= 0)
      return 0;
    long l1 = this.fp;
    if (this.tmp == null)
      throw new IOException("Bad file descriptor");
    this.file.open(this.openFlags, this.access, 128, 0);
    if (LogStream.level >= 4)
      SmbFile.log.println("read: fid=" + this.file.fid + ",off=" + paramInt1 + ",len=" + paramInt2);
    SmbComReadAndXResponse localSmbComReadAndXResponse = new SmbComReadAndXResponse(paramArrayOfByte, paramInt1);
    if (this.file.type == 16)
      localSmbComReadAndXResponse.responseTimeout = 0L;
    int j;
    int k;
    label387: 
    do
    {
      int i = this.readSize;
      if (paramInt2 > i)
      {
        j = this.readSize;
        if (LogStream.level >= 4)
          SmbFile.log.println("read: len=" + paramInt2 + ",r=" + j + ",fp=" + this.fp);
      }
      while (true)
      {
        try
        {
          SmbComReadAndX localSmbComReadAndX = new SmbComReadAndX(this.file.fid, this.fp, j, null);
          if (this.file.type == 16)
          {
            localSmbComReadAndX.remaining = 1024;
            localSmbComReadAndX.maxCount = 1024;
            localSmbComReadAndX.minCount = 1024;
          }
          this.file.send(localSmbComReadAndX, localSmbComReadAndXResponse);
          k = localSmbComReadAndXResponse.dataLength;
          if (k > 0)
            break label387;
          if (this.fp - l1 > 0L)
          {
            l2 = this.fp - l1;
            return (int)l2;
            j = paramInt2;
          }
        }
        catch (SmbException localSmbException)
        {
          if ((this.file.type == 16) && (localSmbException.getNtStatus() == -1073741493))
            return -1;
          throw seToIoe(localSmbException);
        }
        long l2 = -1L;
      }
      this.fp += k;
      paramInt2 -= k;
      localSmbComReadAndXResponse.off = (k + localSmbComReadAndXResponse.off);
    }
    while ((paramInt2 > 0) && (k == j));
    return (int)(this.fp - l1);
  }

  protected IOException seToIoe(SmbException paramSmbException)
  {
    Object localObject = paramSmbException;
    Throwable localThrowable = paramSmbException.getRootCause();
    if ((localThrowable instanceof TransportException))
    {
      localObject = (TransportException)localThrowable;
      localThrowable = ((TransportException)localObject).getRootCause();
    }
    if ((localThrowable instanceof InterruptedException))
    {
      localObject = new InterruptedIOException(localThrowable.getMessage());
      ((IOException)localObject).initCause(localThrowable);
    }
    return localObject;
  }

  public long skip(long paramLong)
    throws IOException
  {
    if (paramLong > 0L)
    {
      this.fp = (paramLong + this.fp);
      return paramLong;
    }
    return 0L;
  }
}