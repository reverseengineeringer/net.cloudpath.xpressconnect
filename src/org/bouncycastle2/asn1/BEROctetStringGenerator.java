package org.bouncycastle2.asn1;

import java.io.IOException;
import java.io.OutputStream;

public class BEROctetStringGenerator extends BERGenerator
{
  public BEROctetStringGenerator(OutputStream paramOutputStream)
    throws IOException
  {
    super(paramOutputStream);
    writeBERHeader(36);
  }

  public BEROctetStringGenerator(OutputStream paramOutputStream, int paramInt, boolean paramBoolean)
    throws IOException
  {
    super(paramOutputStream, paramInt, paramBoolean);
    writeBERHeader(36);
  }

  public OutputStream getOctetOutputStream()
  {
    return getOctetOutputStream(new byte[1000]);
  }

  public OutputStream getOctetOutputStream(byte[] paramArrayOfByte)
  {
    return new BufferedBEROctetStream(paramArrayOfByte);
  }

  private class BufferedBEROctetStream extends OutputStream
  {
    private byte[] _buf;
    private DEROutputStream _derOut;
    private int _off;

    BufferedBEROctetStream(byte[] arg2)
    {
      Object localObject;
      this._buf = localObject;
      this._off = 0;
      this._derOut = new DEROutputStream(BEROctetStringGenerator.this._out);
    }

    public void close()
      throws IOException
    {
      if (this._off != 0)
      {
        byte[] arrayOfByte = new byte[this._off];
        System.arraycopy(this._buf, 0, arrayOfByte, 0, this._off);
        DEROctetString.encode(this._derOut, arrayOfByte);
      }
      BEROctetStringGenerator.this.writeBEREnd();
    }

    public void write(int paramInt)
      throws IOException
    {
      byte[] arrayOfByte = this._buf;
      int i = this._off;
      this._off = (i + 1);
      arrayOfByte[i] = ((byte)paramInt);
      if (this._off == this._buf.length)
      {
        DEROctetString.encode(this._derOut, this._buf);
        this._off = 0;
      }
    }

    public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
      throws IOException
    {
      while (true)
      {
        if (paramInt2 <= 0);
        int i;
        do
        {
          return;
          i = Math.min(paramInt2, this._buf.length - this._off);
          System.arraycopy(paramArrayOfByte, paramInt1, this._buf, this._off, i);
          this._off = (i + this._off);
        }
        while (this._off < this._buf.length);
        DEROctetString.encode(this._derOut, this._buf);
        this._off = 0;
        paramInt1 += i;
        paramInt2 -= i;
      }
    }
  }
}