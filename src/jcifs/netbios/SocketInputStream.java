package jcifs.netbios;

import java.io.IOException;
import java.io.InputStream;

class SocketInputStream extends InputStream
{
  private static final int TMP_BUFFER_SIZE = 256;
  private int bip;
  private byte[] header;
  private InputStream in;
  private int n;
  private SessionServicePacket ssp;
  private byte[] tmp;
  private int tot;

  SocketInputStream(InputStream paramInputStream)
  {
    this.in = paramInputStream;
    this.header = new byte[4];
    this.tmp = new byte[256];
  }

  public int available()
    throws IOException
  {
    if (this.bip > 0)
      return this.bip;
    return this.in.available();
  }

  public void close()
    throws IOException
  {
    this.in.close();
  }

  public int read()
    throws IOException
  {
    try
    {
      int i = read(this.tmp, 0, 1);
      if (i < 0);
      int j;
      for (int k = -1; ; k = j & 0xFF)
      {
        return k;
        j = this.tmp[0];
      }
    }
    finally
    {
    }
  }

  public int read(byte[] paramArrayOfByte)
    throws IOException
  {
    try
    {
      int i = read(paramArrayOfByte, 0, paramArrayOfByte.length);
      return i;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    int i = -1;
    if (paramInt2 == 0)
    {
      i = 0;
      return i;
    }
    while (true)
      try
      {
        this.tot = 0;
        if (this.bip > 0)
        {
          this.n = this.in.read(paramArrayOfByte, paramInt1, Math.min(paramInt2, this.bip));
          if (this.n == i)
          {
            if (this.tot <= 0)
              break;
            i = this.tot;
            break;
          }
          this.tot += this.n;
          paramInt1 += this.n;
          paramInt2 -= this.n;
          this.bip -= this.n;
          if (paramInt2 != 0)
            continue;
          i = this.tot;
          break;
        }
        switch (SessionServicePacket.readPacketType(this.in, this.header, 0))
        {
        case 133:
        case -1:
          if (this.tot <= 0)
            break;
          i = this.tot;
          break;
        case 0:
          this.bip = SessionServicePacket.readLength(this.header, 0);
        }
      }
      finally
      {
      }
  }

  public long skip(long paramLong)
    throws IOException
  {
    long l1 = 0L;
    if (paramLong <= l1);
    while (true)
    {
      return l1;
      long l2 = paramLong;
      if (l2 > l1);
      try
      {
        int i = read(this.tmp, 0, (int)Math.min(256L, l2));
        if (i < 0)
        {
          l1 = paramLong - l2;
          continue;
        }
        l2 -= i;
      }
      finally
      {
      }
    }
  }
}