package org.bouncycastle2.asn1;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import org.bouncycastle2.util.io.Streams;

class DefiniteLengthInputStream extends LimitedInputStream
{
  private static final byte[] EMPTY_BYTES = new byte[0];
  private final int _originalLength;
  private int _remaining;

  DefiniteLengthInputStream(InputStream paramInputStream, int paramInt)
  {
    super(paramInputStream, paramInt);
    if (paramInt < 0)
      throw new IllegalArgumentException("negative lengths not allowed");
    this._originalLength = paramInt;
    this._remaining = paramInt;
    if (paramInt == 0)
      setParentEofDetect(true);
  }

  int getRemaining()
  {
    return this._remaining;
  }

  public int read()
    throws IOException
  {
    int i;
    if (this._remaining == 0)
      i = -1;
    int j;
    do
    {
      return i;
      i = this._in.read();
      if (i < 0)
        throw new EOFException("DEF length " + this._originalLength + " object truncated by " + this._remaining);
      j = -1 + this._remaining;
      this._remaining = j;
    }
    while (j != 0);
    setParentEofDetect(true);
    return i;
  }

  public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    int j;
    if (this._remaining == 0)
      j = -1;
    int k;
    do
    {
      return j;
      int i = Math.min(paramInt2, this._remaining);
      j = this._in.read(paramArrayOfByte, paramInt1, i);
      if (j < 0)
        throw new EOFException("DEF length " + this._originalLength + " object truncated by " + this._remaining);
      k = this._remaining - j;
      this._remaining = k;
    }
    while (k != 0);
    setParentEofDetect(true);
    return j;
  }

  byte[] toByteArray()
    throws IOException
  {
    if (this._remaining == 0)
      return EMPTY_BYTES;
    byte[] arrayOfByte = new byte[this._remaining];
    int i = this._remaining - Streams.readFully(this._in, arrayOfByte);
    this._remaining = i;
    if (i != 0)
      throw new EOFException("DEF length " + this._originalLength + " object truncated by " + this._remaining);
    setParentEofDetect(true);
    return arrayOfByte;
  }
}