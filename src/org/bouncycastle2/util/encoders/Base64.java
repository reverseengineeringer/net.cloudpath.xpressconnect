package org.bouncycastle2.util.encoders;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Base64
{
  private static final Encoder encoder = new Base64Encoder();

  public static int decode(String paramString, OutputStream paramOutputStream)
    throws IOException
  {
    return encoder.decode(paramString, paramOutputStream);
  }

  public static byte[] decode(String paramString)
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(3 * (paramString.length() / 4));
    try
    {
      encoder.decode(paramString, localByteArrayOutputStream);
      return localByteArrayOutputStream.toByteArray();
    }
    catch (IOException localIOException)
    {
      throw new RuntimeException("exception decoding base64 string: " + localIOException);
    }
  }

  public static byte[] decode(byte[] paramArrayOfByte)
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(3 * (paramArrayOfByte.length / 4));
    try
    {
      encoder.decode(paramArrayOfByte, 0, paramArrayOfByte.length, localByteArrayOutputStream);
      return localByteArrayOutputStream.toByteArray();
    }
    catch (IOException localIOException)
    {
      throw new RuntimeException("exception decoding base64 string: " + localIOException);
    }
  }

  public static int encode(byte[] paramArrayOfByte, int paramInt1, int paramInt2, OutputStream paramOutputStream)
    throws IOException
  {
    return encoder.encode(paramArrayOfByte, paramInt1, paramInt2, paramOutputStream);
  }

  public static int encode(byte[] paramArrayOfByte, OutputStream paramOutputStream)
    throws IOException
  {
    return encoder.encode(paramArrayOfByte, 0, paramArrayOfByte.length, paramOutputStream);
  }

  public static byte[] encode(byte[] paramArrayOfByte)
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(4 * ((2 + paramArrayOfByte.length) / 3));
    try
    {
      encoder.encode(paramArrayOfByte, 0, paramArrayOfByte.length, localByteArrayOutputStream);
      return localByteArrayOutputStream.toByteArray();
    }
    catch (IOException localIOException)
    {
      throw new RuntimeException("exception encoding base64 string: " + localIOException);
    }
  }
}