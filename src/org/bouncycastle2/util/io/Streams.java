package org.bouncycastle2.util.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class Streams
{
  private static int BUFFER_SIZE = 512;

  public static void drain(InputStream paramInputStream)
    throws IOException
  {
    byte[] arrayOfByte = new byte[BUFFER_SIZE];
    while (paramInputStream.read(arrayOfByte, 0, arrayOfByte.length) >= 0);
  }

  public static void pipeAll(InputStream paramInputStream, OutputStream paramOutputStream)
    throws IOException
  {
    byte[] arrayOfByte = new byte[BUFFER_SIZE];
    while (true)
    {
      int i = paramInputStream.read(arrayOfByte, 0, arrayOfByte.length);
      if (i < 0)
        return;
      paramOutputStream.write(arrayOfByte, 0, i);
    }
  }

  public static long pipeAllLimited(InputStream paramInputStream, long paramLong, OutputStream paramOutputStream)
    throws IOException
  {
    long l = 0L;
    byte[] arrayOfByte = new byte[BUFFER_SIZE];
    while (true)
    {
      int i = paramInputStream.read(arrayOfByte, 0, arrayOfByte.length);
      if (i < 0)
        return l;
      l += i;
      if (l > paramLong)
        throw new StreamOverflowException("Data Overflow");
      paramOutputStream.write(arrayOfByte, 0, i);
    }
  }

  public static byte[] readAll(InputStream paramInputStream)
    throws IOException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    pipeAll(paramInputStream, localByteArrayOutputStream);
    return localByteArrayOutputStream.toByteArray();
  }

  public static byte[] readAllLimited(InputStream paramInputStream, int paramInt)
    throws IOException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    pipeAllLimited(paramInputStream, paramInt, localByteArrayOutputStream);
    return localByteArrayOutputStream.toByteArray();
  }

  public static int readFully(InputStream paramInputStream, byte[] paramArrayOfByte)
    throws IOException
  {
    return readFully(paramInputStream, paramArrayOfByte, 0, paramArrayOfByte.length);
  }

  public static int readFully(InputStream paramInputStream, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    int i = 0;
    while (true)
    {
      if (i >= paramInt2);
      int j;
      do
      {
        return i;
        j = paramInputStream.read(paramArrayOfByte, paramInt1 + i, paramInt2 - i);
      }
      while (j < 0);
      i += j;
    }
  }
}