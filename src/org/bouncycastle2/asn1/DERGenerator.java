package org.bouncycastle2.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.bouncycastle2.util.io.Streams;

public abstract class DERGenerator extends ASN1Generator
{
  private boolean _isExplicit;
  private int _tagNo;
  private boolean _tagged = false;

  protected DERGenerator(OutputStream paramOutputStream)
  {
    super(paramOutputStream);
  }

  public DERGenerator(OutputStream paramOutputStream, int paramInt, boolean paramBoolean)
  {
    super(paramOutputStream);
    this._tagged = true;
    this._isExplicit = paramBoolean;
    this._tagNo = paramInt;
  }

  private void writeLength(OutputStream paramOutputStream, int paramInt)
    throws IOException
  {
    if (paramInt > 127)
    {
      int i = 1;
      int j = paramInt;
      j >>>= 8;
      if (j == 0)
        paramOutputStream.write((byte)(i | 0x80));
      for (int k = 8 * (i - 1); ; k -= 8)
      {
        if (k < 0)
        {
          return;
          i++;
          break;
        }
        paramOutputStream.write((byte)(paramInt >> k));
      }
    }
    paramOutputStream.write((byte)paramInt);
  }

  void writeDEREncoded(int paramInt, byte[] paramArrayOfByte)
    throws IOException
  {
    if (this._tagged)
    {
      int i = 0x80 | this._tagNo;
      if (this._isExplicit)
      {
        int j = 0x80 | (0x20 | this._tagNo);
        ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
        writeDEREncoded(localByteArrayOutputStream, paramInt, paramArrayOfByte);
        writeDEREncoded(this._out, j, localByteArrayOutputStream.toByteArray());
        return;
      }
      if ((paramInt & 0x20) != 0)
      {
        writeDEREncoded(this._out, i | 0x20, paramArrayOfByte);
        return;
      }
      writeDEREncoded(this._out, i, paramArrayOfByte);
      return;
    }
    writeDEREncoded(this._out, paramInt, paramArrayOfByte);
  }

  void writeDEREncoded(OutputStream paramOutputStream, int paramInt, InputStream paramInputStream)
    throws IOException
  {
    writeDEREncoded(paramOutputStream, paramInt, Streams.readAll(paramInputStream));
  }

  void writeDEREncoded(OutputStream paramOutputStream, int paramInt, byte[] paramArrayOfByte)
    throws IOException
  {
    paramOutputStream.write(paramInt);
    writeLength(paramOutputStream, paramArrayOfByte.length);
    paramOutputStream.write(paramArrayOfByte);
  }
}