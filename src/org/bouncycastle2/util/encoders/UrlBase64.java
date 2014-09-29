package org.bouncycastle2.util.encoders;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class UrlBase64
{
  private static final Encoder encoder = new UrlBase64Encoder();

  public static int decode(String paramString, OutputStream paramOutputStream)
    throws IOException
  {
    return encoder.decode(paramString, paramOutputStream);
  }

  public static int decode(byte[] paramArrayOfByte, OutputStream paramOutputStream)
    throws IOException
  {
    return encoder.decode(paramArrayOfByte, 0, paramArrayOfByte.length, paramOutputStream);
  }

  public static byte[] decode(String paramString)
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    try
    {
      encoder.decode(paramString, localByteArrayOutputStream);
      return localByteArrayOutputStream.toByteArray();
    }
    catch (IOException localIOException)
    {
      throw new RuntimeException("exception decoding URL safe base64 string: " + localIOException);
    }
  }

  public static byte[] decode(byte[] paramArrayOfByte)
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    try
    {
      encoder.decode(paramArrayOfByte, 0, paramArrayOfByte.length, localByteArrayOutputStream);
      return localByteArrayOutputStream.toByteArray();
    }
    catch (IOException localIOException)
    {
      throw new RuntimeException("exception decoding URL safe base64 string: " + localIOException);
    }
  }

  public static int encode(byte[] paramArrayOfByte, OutputStream paramOutputStream)
    throws IOException
  {
    return encoder.encode(paramArrayOfByte, 0, paramArrayOfByte.length, paramOutputStream);
  }

  public static byte[] encode(byte[] paramArrayOfByte)
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    try
    {
      encoder.encode(paramArrayOfByte, 0, paramArrayOfByte.length, localByteArrayOutputStream);
      return localByteArrayOutputStream.toByteArray();
    }
    catch (IOException localIOException)
    {
      throw new RuntimeException("exception encoding URL safe base64 string: " + localIOException);
    }
  }
}