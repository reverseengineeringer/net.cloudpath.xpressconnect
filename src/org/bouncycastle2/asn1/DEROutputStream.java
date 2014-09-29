package org.bouncycastle2.asn1;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class DEROutputStream extends FilterOutputStream
  implements DERTags
{
  public DEROutputStream(OutputStream paramOutputStream)
  {
    super(paramOutputStream);
  }

  private void writeLength(int paramInt)
    throws IOException
  {
    if (paramInt > 127)
    {
      int i = 1;
      int j = paramInt;
      j >>>= 8;
      if (j == 0)
        write((byte)(i | 0x80));
      for (int k = 8 * (i - 1); ; k -= 8)
      {
        if (k < 0)
        {
          return;
          i++;
          break;
        }
        write((byte)(paramInt >> k));
      }
    }
    write((byte)paramInt);
  }

  public void write(byte[] paramArrayOfByte)
    throws IOException
  {
    this.out.write(paramArrayOfByte, 0, paramArrayOfByte.length);
  }

  public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    this.out.write(paramArrayOfByte, paramInt1, paramInt2);
  }

  void writeEncoded(int paramInt1, int paramInt2, byte[] paramArrayOfByte)
    throws IOException
  {
    writeTag(paramInt1, paramInt2);
    writeLength(paramArrayOfByte.length);
    write(paramArrayOfByte);
  }

  void writeEncoded(int paramInt, byte[] paramArrayOfByte)
    throws IOException
  {
    write(paramInt);
    writeLength(paramArrayOfByte.length);
    write(paramArrayOfByte);
  }

  protected void writeNull()
    throws IOException
  {
    write(5);
    write(0);
  }

  public void writeObject(Object paramObject)
    throws IOException
  {
    if (paramObject == null)
    {
      writeNull();
      return;
    }
    if ((paramObject instanceof DERObject))
    {
      ((DERObject)paramObject).encode(this);
      return;
    }
    if ((paramObject instanceof DEREncodable))
    {
      ((DEREncodable)paramObject).getDERObject().encode(this);
      return;
    }
    throw new IOException("object not DEREncodable");
  }

  void writeTag(int paramInt1, int paramInt2)
    throws IOException
  {
    if (paramInt2 < 31)
    {
      write(paramInt1 | paramInt2);
      return;
    }
    write(paramInt1 | 0x1F);
    if (paramInt2 < 128)
    {
      write(paramInt2);
      return;
    }
    byte[] arrayOfByte = new byte[5];
    int i = -1 + arrayOfByte.length;
    arrayOfByte[i] = ((byte)(paramInt2 & 0x7F));
    do
    {
      paramInt2 >>= 7;
      i--;
      arrayOfByte[i] = ((byte)(0x80 | paramInt2 & 0x7F));
    }
    while (paramInt2 > 127);
    write(arrayOfByte, i, arrayOfByte.length - i);
  }
}