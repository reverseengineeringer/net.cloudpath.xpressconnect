package org.xbill.DNS.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class base64
{
  private static final String Base64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";

  public static String formatString(byte[] paramArrayOfByte, int paramInt, String paramString, boolean paramBoolean)
  {
    String str = toString(paramArrayOfByte);
    StringBuffer localStringBuffer = new StringBuffer();
    int i = 0;
    if (i < str.length())
    {
      localStringBuffer.append(paramString);
      if (i + paramInt >= str.length())
      {
        localStringBuffer.append(str.substring(i));
        if (paramBoolean)
          localStringBuffer.append(" )");
      }
      while (true)
      {
        i += paramInt;
        break;
        localStringBuffer.append(str.substring(i, i + paramInt));
        localStringBuffer.append("\n");
      }
    }
    return localStringBuffer.toString();
  }

  public static byte[] fromString(String paramString)
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    byte[] arrayOfByte1 = paramString.getBytes();
    for (int i = 0; i < arrayOfByte1.length; i++)
      if (!Character.isWhitespace((char)arrayOfByte1[i]))
        localByteArrayOutputStream.write(arrayOfByte1[i]);
    byte[] arrayOfByte2 = localByteArrayOutputStream.toByteArray();
    if (arrayOfByte2.length % 4 != 0)
      return null;
    localByteArrayOutputStream.reset();
    DataOutputStream localDataOutputStream = new DataOutputStream(localByteArrayOutputStream);
    for (int j = 0; j < (3 + arrayOfByte2.length) / 4; j++)
    {
      short[] arrayOfShort1 = new short[4];
      short[] arrayOfShort2 = new short[3];
      for (int k = 0; k < 4; k++)
        arrayOfShort1[k] = ((short)"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=".indexOf(arrayOfByte2[(k + j * 4)]));
      arrayOfShort2[0] = ((short)((arrayOfShort1[0] << 2) + (arrayOfShort1[1] >> 4)));
      if (arrayOfShort1[2] == 64)
      {
        arrayOfShort2[2] = -1;
        arrayOfShort2[1] = -1;
        if ((0xF & arrayOfShort1[1]) != 0)
          return null;
      }
      else if (arrayOfShort1[3] == 64)
      {
        arrayOfShort2[1] = ((short)(0xFF & (arrayOfShort1[1] << 4) + (arrayOfShort1[2] >> 2)));
        arrayOfShort2[2] = -1;
        if ((0x3 & arrayOfShort1[2]) != 0)
          return null;
      }
      else
      {
        arrayOfShort2[1] = ((short)(0xFF & (arrayOfShort1[1] << 4) + (arrayOfShort1[2] >> 2)));
        arrayOfShort2[2] = ((short)(0xFF & (arrayOfShort1[2] << 6) + arrayOfShort1[3]));
      }
      int m = 0;
      while (true)
        if (m < 3)
          try
          {
            if (arrayOfShort2[m] >= 0)
              localDataOutputStream.writeByte(arrayOfShort2[m]);
            m++;
          }
          catch (IOException localIOException)
          {
          }
    }
    return localByteArrayOutputStream.toByteArray();
  }

  public static String toString(byte[] paramArrayOfByte)
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    for (int i = 0; i < (2 + paramArrayOfByte.length) / 3; i++)
    {
      short[] arrayOfShort1 = new short[3];
      short[] arrayOfShort2 = new short[4];
      int j = 0;
      if (j < 3)
      {
        if (j + i * 3 < paramArrayOfByte.length)
          arrayOfShort1[j] = ((short)(0xFF & paramArrayOfByte[(j + i * 3)]));
        while (true)
        {
          j++;
          break;
          arrayOfShort1[j] = -1;
        }
      }
      arrayOfShort2[0] = ((short)(arrayOfShort1[0] >> 2));
      if (arrayOfShort1[1] == -1)
      {
        arrayOfShort2[1] = ((short)((0x3 & arrayOfShort1[0]) << 4));
        if (arrayOfShort1[1] != -1)
          break label178;
        arrayOfShort2[3] = 64;
        arrayOfShort2[2] = 64;
      }
      while (true)
      {
        for (int k = 0; k < 4; k++)
          localByteArrayOutputStream.write("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=".charAt(arrayOfShort2[k]));
        arrayOfShort2[1] = ((short)(((0x3 & arrayOfShort1[0]) << 4) + (arrayOfShort1[1] >> 4)));
        break;
        label178: if (arrayOfShort1[2] == -1)
        {
          arrayOfShort2[2] = ((short)((0xF & arrayOfShort1[1]) << 2));
          arrayOfShort2[3] = 64;
        }
        else
        {
          arrayOfShort2[2] = ((short)(((0xF & arrayOfShort1[1]) << 2) + (arrayOfShort1[2] >> 6)));
          arrayOfShort2[3] = ((short)(0x3F & arrayOfShort1[2]));
        }
      }
    }
    return new String(localByteArrayOutputStream.toByteArray());
  }
}