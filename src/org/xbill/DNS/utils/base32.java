package org.xbill.DNS.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class base32
{
  private String alphabet;
  private boolean lowercase;
  private boolean padding;

  public base32(String paramString, boolean paramBoolean1, boolean paramBoolean2)
  {
    this.alphabet = paramString;
    this.padding = paramBoolean1;
    this.lowercase = paramBoolean2;
  }

  private static int blockLenToPadding(int paramInt)
  {
    switch (paramInt)
    {
    default:
      return -1;
    case 1:
      return 6;
    case 2:
      return 4;
    case 3:
      return 3;
    case 4:
      return 1;
    case 5:
    }
    return 0;
  }

  private static int paddingToBlockLen(int paramInt)
  {
    switch (paramInt)
    {
    case 2:
    case 5:
    default:
      return -1;
    case 6:
      return 1;
    case 4:
      return 2;
    case 3:
      return 3;
    case 1:
      return 4;
    case 0:
    }
    return 5;
  }

  public byte[] fromString(String paramString)
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    byte[] arrayOfByte1 = paramString.getBytes();
    for (int i = 0; i < arrayOfByte1.length; i++)
    {
      char c = (char)arrayOfByte1[i];
      if (!Character.isWhitespace(c))
        localByteArrayOutputStream.write((byte)Character.toUpperCase(c));
    }
    if (this.padding)
    {
      if (localByteArrayOutputStream.size() % 8 != 0)
        return null;
    }
    else
      while (localByteArrayOutputStream.size() % 8 != 0)
        localByteArrayOutputStream.write(61);
    byte[] arrayOfByte2 = localByteArrayOutputStream.toByteArray();
    localByteArrayOutputStream.reset();
    DataOutputStream localDataOutputStream = new DataOutputStream(localByteArrayOutputStream);
    for (int j = 0; j < arrayOfByte2.length / 8; j++)
    {
      short[] arrayOfShort = new short[8];
      int[] arrayOfInt = new int[5];
      int k = 8;
      int n;
      for (int m = 0; ; m++)
      {
        if ((m >= 8) || ((char)arrayOfByte2[(m + j * 8)] == '='))
        {
          n = paddingToBlockLen(k);
          if (n >= 0)
            break;
          return null;
        }
        arrayOfShort[m] = ((short)this.alphabet.indexOf(arrayOfByte2[(m + j * 8)]));
        if (arrayOfShort[m] < 0)
          return null;
        k--;
      }
      arrayOfInt[0] = (arrayOfShort[0] << 3 | arrayOfShort[1] >> 2);
      arrayOfInt[1] = ((0x3 & arrayOfShort[1]) << 6 | arrayOfShort[2] << 1 | arrayOfShort[3] >> 4);
      arrayOfInt[2] = ((0xF & arrayOfShort[3]) << 4 | 0xF & arrayOfShort[4] >> 1);
      arrayOfInt[3] = (arrayOfShort[4] << 7 | arrayOfShort[5] << 2 | arrayOfShort[6] >> 3);
      arrayOfInt[4] = ((0x7 & arrayOfShort[6]) << 5 | arrayOfShort[7]);
      int i1 = 0;
      while (true)
        if (i1 < n)
          try
          {
            localDataOutputStream.writeByte((byte)(0xFF & arrayOfInt[i1]));
            i1++;
          }
          catch (IOException localIOException)
          {
          }
    }
    return localByteArrayOutputStream.toByteArray();
  }

  public String toString(byte[] paramArrayOfByte)
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    for (int i = 0; i < (4 + paramArrayOfByte.length) / 5; i++)
    {
      short[] arrayOfShort = new short[5];
      int[] arrayOfInt = new int[8];
      int j = 5;
      int k = 0;
      if (k < 5)
      {
        if (k + i * 5 < paramArrayOfByte.length)
          arrayOfShort[k] = ((short)(0xFF & paramArrayOfByte[(k + i * 5)]));
        while (true)
        {
          k++;
          break;
          arrayOfShort[k] = 0;
          j--;
        }
      }
      int m = blockLenToPadding(j);
      arrayOfInt[0] = ((byte)(0x1F & arrayOfShort[0] >> 3));
      arrayOfInt[1] = ((byte)((0x7 & arrayOfShort[0]) << 2 | 0x3 & arrayOfShort[1] >> 6));
      arrayOfInt[2] = ((byte)(0x1F & arrayOfShort[1] >> 1));
      arrayOfInt[3] = ((byte)((0x1 & arrayOfShort[1]) << 4 | 0xF & arrayOfShort[2] >> 4));
      arrayOfInt[4] = ((byte)((0xF & arrayOfShort[2]) << 1 | 0x1 & arrayOfShort[3] >> 7));
      arrayOfInt[5] = ((byte)(0x1F & arrayOfShort[3] >> 2));
      arrayOfInt[6] = ((byte)((0x3 & arrayOfShort[3]) << 3 | 0x7 & arrayOfShort[4] >> 5));
      arrayOfInt[7] = ((byte)(0x1F & arrayOfShort[4]));
      for (int n = 0; n < arrayOfInt.length - m; n++)
      {
        char c = this.alphabet.charAt(arrayOfInt[n]);
        if (this.lowercase)
          c = Character.toLowerCase(c);
        localByteArrayOutputStream.write(c);
      }
      if (this.padding)
        for (int i1 = arrayOfInt.length - m; i1 < arrayOfInt.length; i1++)
          localByteArrayOutputStream.write(61);
    }
    return new String(localByteArrayOutputStream.toByteArray());
  }

  public static class Alphabet
  {
    public static final String BASE32 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567=";
    public static final String BASE32HEX = "0123456789ABCDEFGHIJKLMNOPQRSTUV=";
  }
}