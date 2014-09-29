package jcifs.netbios;

import java.io.IOException;
import java.io.InputStream;

public abstract class SessionServicePacket
{
  static final int HEADER_LENGTH = 4;
  static final int MAX_MESSAGE_SIZE = 131071;
  public static final int NEGATIVE_SESSION_RESPONSE = 131;
  public static final int POSITIVE_SESSION_RESPONSE = 130;
  static final int SESSION_KEEP_ALIVE = 133;
  static final int SESSION_MESSAGE = 0;
  static final int SESSION_REQUEST = 129;
  static final int SESSION_RETARGET_RESPONSE = 132;
  int length;
  int type;

  static int readInt2(byte[] paramArrayOfByte, int paramInt)
  {
    return ((0xFF & paramArrayOfByte[paramInt]) << 8) + (0xFF & paramArrayOfByte[(paramInt + 1)]);
  }

  static int readInt4(byte[] paramArrayOfByte, int paramInt)
  {
    return ((0xFF & paramArrayOfByte[paramInt]) << 24) + ((0xFF & paramArrayOfByte[(paramInt + 1)]) << 16) + ((0xFF & paramArrayOfByte[(paramInt + 2)]) << 8) + (0xFF & paramArrayOfByte[(paramInt + 3)]);
  }

  static int readLength(byte[] paramArrayOfByte, int paramInt)
  {
    int i = paramInt + 1;
    int j = i + 1;
    int k = (0x1 & paramArrayOfByte[i]) << 16;
    int m = j + 1;
    int n = k + ((0xFF & paramArrayOfByte[j]) << 8);
    (m + 1);
    return n + (0xFF & paramArrayOfByte[m]);
  }

  static int readPacketType(InputStream paramInputStream, byte[] paramArrayOfByte, int paramInt)
    throws IOException
  {
    int i = readn(paramInputStream, paramArrayOfByte, paramInt, 4);
    if (i != 4)
    {
      if (i == -1)
        return -1;
      throw new IOException("unexpected EOF reading netbios session header");
    }
    return 0xFF & paramArrayOfByte[paramInt];
  }

  static int readn(InputStream paramInputStream, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    int i = 0;
    while (true)
    {
      int j;
      if (i < paramInt2)
      {
        j = paramInputStream.read(paramArrayOfByte, paramInt1 + i, paramInt2 - i);
        if (j > 0);
      }
      else
      {
        return i;
      }
      i += j;
    }
  }

  static void writeInt2(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
  {
    int i = paramInt2 + 1;
    paramArrayOfByte[paramInt2] = ((byte)(0xFF & paramInt1 >> 8));
    paramArrayOfByte[i] = ((byte)(paramInt1 & 0xFF));
  }

  static void writeInt4(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
  {
    int i = paramInt2 + 1;
    paramArrayOfByte[paramInt2] = ((byte)(0xFF & paramInt1 >> 24));
    int j = i + 1;
    paramArrayOfByte[i] = ((byte)(0xFF & paramInt1 >> 16));
    int k = j + 1;
    paramArrayOfByte[j] = ((byte)(0xFF & paramInt1 >> 8));
    paramArrayOfByte[k] = ((byte)(paramInt1 & 0xFF));
  }

  int readHeaderWireFormat(InputStream paramInputStream, byte[] paramArrayOfByte, int paramInt)
    throws IOException
  {
    int i = paramInt + 1;
    this.type = (0xFF & paramArrayOfByte[paramInt]);
    this.length = (((0x1 & paramArrayOfByte[i]) << 16) + readInt2(paramArrayOfByte, i + 1));
    return 4;
  }

  abstract int readTrailerWireFormat(InputStream paramInputStream, byte[] paramArrayOfByte, int paramInt)
    throws IOException;

  int readWireFormat(InputStream paramInputStream, byte[] paramArrayOfByte, int paramInt)
    throws IOException
  {
    readHeaderWireFormat(paramInputStream, paramArrayOfByte, paramInt);
    return 4 + readTrailerWireFormat(paramInputStream, paramArrayOfByte, paramInt);
  }

  int writeHeaderWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    int i = paramInt + 1;
    paramArrayOfByte[paramInt] = ((byte)this.type);
    if (this.length > 65535)
      paramArrayOfByte[i] = 1;
    int j = i + 1;
    writeInt2(this.length, paramArrayOfByte, j);
    return 4;
  }

  abstract int writeTrailerWireFormat(byte[] paramArrayOfByte, int paramInt);

  public int writeWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    this.length = writeTrailerWireFormat(paramArrayOfByte, paramInt + 4);
    writeHeaderWireFormat(paramArrayOfByte, paramInt);
    return 4 + this.length;
  }
}