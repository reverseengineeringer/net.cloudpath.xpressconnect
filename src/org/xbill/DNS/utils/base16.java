package org.xbill.DNS.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class base16
{
  private static final String Base16 = "0123456789ABCDEF";

  public static byte[] fromString(String paramString)
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    byte[] arrayOfByte1 = paramString.getBytes();
    for (int i = 0; i < arrayOfByte1.length; i++)
      if (!Character.isWhitespace((char)arrayOfByte1[i]))
        localByteArrayOutputStream.write(arrayOfByte1[i]);
    byte[] arrayOfByte2 = localByteArrayOutputStream.toByteArray();
    if (arrayOfByte2.length % 2 != 0)
      return null;
    localByteArrayOutputStream.reset();
    DataOutputStream localDataOutputStream = new DataOutputStream(localByteArrayOutputStream);
    int j = 0;
    while (true)
    {
      int m;
      if (j < arrayOfByte2.length)
      {
        int k = (byte)"0123456789ABCDEF".indexOf(Character.toUpperCase((char)arrayOfByte2[j]));
        m = (byte)"0123456789ABCDEF".indexOf(Character.toUpperCase((char)arrayOfByte2[(j + 1)])) + (k << 4);
      }
      try
      {
        localDataOutputStream.writeByte(m);
        label133: j += 2;
        continue;
        return localByteArrayOutputStream.toByteArray();
      }
      catch (IOException localIOException)
      {
        break label133;
      }
    }
  }

  public static String toString(byte[] paramArrayOfByte)
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    for (int i = 0; i < paramArrayOfByte.length; i++)
    {
      int j = (short)(0xFF & paramArrayOfByte[i]);
      int k = (byte)(j >> 4);
      int m = (byte)(j & 0xF);
      localByteArrayOutputStream.write("0123456789ABCDEF".charAt(k));
      localByteArrayOutputStream.write("0123456789ABCDEF".charAt(m));
    }
    return new String(localByteArrayOutputStream.toByteArray());
  }
}