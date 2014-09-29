package jcifs.smb;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import jcifs.util.LogStream;

class TransactNamedPipeInputStream extends SmbFileInputStream
{
  private static final int INIT_PIPE_SIZE = 4096;
  private int beg_idx;
  private boolean dcePipe;
  Object lock;
  private int nxt_idx;
  private byte[] pipe_buf = new byte[4096];
  private int used;

  TransactNamedPipeInputStream(SmbNamedPipe paramSmbNamedPipe)
    throws SmbException, MalformedURLException, UnknownHostException
  {
    super(paramSmbNamedPipe, 0x20 | 0xFFFF00FF & paramSmbNamedPipe.pipeType);
    if ((0x600 & paramSmbNamedPipe.pipeType) != 1536);
    for (boolean bool = true; ; bool = false)
    {
      this.dcePipe = bool;
      this.lock = new Object();
      return;
    }
  }

  public int available()
    throws IOException
  {
    if (LogStream.level >= 3)
      SmbFile.log.println("Named Pipe available() does not apply to TRANSACT Named Pipes");
    return 0;
  }

  public int dce_read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    return super.read(paramArrayOfByte, paramInt1, paramInt2);
  }

  public int read()
    throws IOException
  {
    synchronized (this.lock)
    {
      try
      {
        while (this.used == 0)
          this.lock.wait();
      }
      catch (InterruptedException localInterruptedException)
      {
        throw new IOException(localInterruptedException.getMessage());
      }
    }
    int i = 0xFF & this.pipe_buf[this.beg_idx];
    this.beg_idx = ((1 + this.beg_idx) % this.pipe_buf.length);
    return i;
  }

  public int read(byte[] paramArrayOfByte)
    throws IOException
  {
    return read(paramArrayOfByte, 0, paramArrayOfByte.length);
  }

  public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    if (paramInt2 <= 0)
      return 0;
    synchronized (this.lock)
    {
      try
      {
        while (this.used == 0)
          this.lock.wait();
      }
      catch (InterruptedException localInterruptedException)
      {
        throw new IOException(localInterruptedException.getMessage());
      }
    }
    int i = this.pipe_buf.length - this.beg_idx;
    if (paramInt2 > this.used);
    for (int j = this.used; ; j = paramInt2)
    {
      if ((this.used > i) && (j > i))
      {
        System.arraycopy(this.pipe_buf, this.beg_idx, paramArrayOfByte, paramInt1, i);
        int k = paramInt1 + i;
        System.arraycopy(this.pipe_buf, 0, paramArrayOfByte, k, j - i);
      }
      while (true)
      {
        this.used -= j;
        this.beg_idx = ((j + this.beg_idx) % this.pipe_buf.length);
        return j;
        System.arraycopy(this.pipe_buf, this.beg_idx, paramArrayOfByte, paramInt1, j);
      }
    }
  }

  int receive(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    byte[] arrayOfByte;
    if (paramInt2 > this.pipe_buf.length - this.used)
    {
      int j = 2 * this.pipe_buf.length;
      if (paramInt2 > j - this.used)
        j = paramInt2 + this.used;
      arrayOfByte = this.pipe_buf;
      this.pipe_buf = new byte[j];
      int k = arrayOfByte.length - this.beg_idx;
      if (this.used > k)
      {
        System.arraycopy(arrayOfByte, this.beg_idx, this.pipe_buf, 0, k);
        System.arraycopy(arrayOfByte, 0, this.pipe_buf, k, this.used - k);
        this.beg_idx = 0;
        this.nxt_idx = this.used;
      }
    }
    else
    {
      int i = this.pipe_buf.length - this.nxt_idx;
      if (paramInt2 <= i)
        break label222;
      System.arraycopy(paramArrayOfByte, paramInt1, this.pipe_buf, this.nxt_idx, i);
      System.arraycopy(paramArrayOfByte, paramInt1 + i, this.pipe_buf, 0, paramInt2 - i);
    }
    while (true)
    {
      this.nxt_idx = ((paramInt2 + this.nxt_idx) % this.pipe_buf.length);
      this.used = (paramInt2 + this.used);
      return paramInt2;
      System.arraycopy(arrayOfByte, this.beg_idx, this.pipe_buf, 0, this.used);
      break;
      label222: System.arraycopy(paramArrayOfByte, paramInt1, this.pipe_buf, this.nxt_idx, paramInt2);
    }
  }
}