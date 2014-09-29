package org.bouncycastle2.util;

import java.io.ByteArrayOutputStream;
import java.util.Vector;

public final class Strings
{
  public static String fromUTF8ByteArray(byte[] paramArrayOfByte)
  {
    int i = 0;
    int j = 0;
    char[] arrayOfChar;
    int k;
    int m;
    while (true)
    {
      if (i >= paramArrayOfByte.length)
      {
        arrayOfChar = new char[j];
        k = 0;
        m = 0;
        if (k < paramArrayOfByte.length)
          break;
        return new String(arrayOfChar);
      }
      j++;
      if ((0xF0 & paramArrayOfByte[i]) == 240)
      {
        j++;
        i += 4;
      }
      else if ((0xE0 & paramArrayOfByte[i]) == 224)
      {
        i += 3;
      }
      else if ((0xC0 & paramArrayOfByte[i]) == 192)
      {
        i += 2;
      }
      else
      {
        i++;
      }
    }
    int n;
    if ((0xF0 & paramArrayOfByte[k]) == 240)
    {
      int i2 = ((0x3 & paramArrayOfByte[k]) << 18 | (0x3F & paramArrayOfByte[(k + 1)]) << 12 | (0x3F & paramArrayOfByte[(k + 2)]) << 6 | 0x3F & paramArrayOfByte[(k + 3)]) - 65536;
      int i3 = (char)(0xD800 | i2 >> 10);
      int i4 = (char)(0xDC00 | i2 & 0x3FF);
      int i5 = m + 1;
      arrayOfChar[m] = i3;
      n = i4;
      k += 4;
      m = i5;
    }
    while (true)
    {
      int i1 = m + 1;
      arrayOfChar[m] = n;
      m = i1;
      break;
      if ((0xE0 & paramArrayOfByte[k]) == 224)
      {
        n = (char)((0xF & paramArrayOfByte[k]) << 12 | (0x3F & paramArrayOfByte[(k + 1)]) << 6 | 0x3F & paramArrayOfByte[(k + 2)]);
        k += 3;
      }
      else if ((0xD0 & paramArrayOfByte[k]) == 208)
      {
        n = (char)((0x1F & paramArrayOfByte[k]) << 6 | 0x3F & paramArrayOfByte[(k + 1)]);
        k += 2;
      }
      else if ((0xC0 & paramArrayOfByte[k]) == 192)
      {
        n = (char)((0x1F & paramArrayOfByte[k]) << 6 | 0x3F & paramArrayOfByte[(k + 1)]);
        k += 2;
      }
      else
      {
        n = (char)(0xFF & paramArrayOfByte[k]);
        k++;
      }
    }
  }

  public static String[] split(String paramString, char paramChar)
  {
    Vector localVector = new Vector();
    int i = 1;
    String[] arrayOfString;
    if (i == 0)
      arrayOfString = new String[localVector.size()];
    for (int k = 0; ; k++)
    {
      if (k == arrayOfString.length)
      {
        return arrayOfString;
        int j = paramString.indexOf(paramChar);
        if (j > 0)
        {
          localVector.addElement(paramString.substring(0, j));
          paramString = paramString.substring(j + 1);
          break;
        }
        localVector.addElement(paramString);
        i = 0;
        break;
      }
      arrayOfString[k] = ((String)localVector.elementAt(k));
    }
  }

  public static byte[] toByteArray(String paramString)
  {
    byte[] arrayOfByte = new byte[paramString.length()];
    for (int i = 0; ; i++)
    {
      if (i == arrayOfByte.length)
        return arrayOfByte;
      arrayOfByte[i] = ((byte)paramString.charAt(i));
    }
  }

  public static byte[] toByteArray(char[] paramArrayOfChar)
  {
    byte[] arrayOfByte = new byte[paramArrayOfChar.length];
    for (int i = 0; ; i++)
    {
      if (i == arrayOfByte.length)
        return arrayOfByte;
      arrayOfByte[i] = ((byte)paramArrayOfChar[i]);
    }
  }

  public static String toLowerCase(String paramString)
  {
    int i = 0;
    char[] arrayOfChar = paramString.toCharArray();
    for (int j = 0; ; j++)
    {
      if (j == arrayOfChar.length)
      {
        if (i != 0)
          paramString = new String(arrayOfChar);
        return paramString;
      }
      int k = arrayOfChar[j];
      if ((65 <= k) && (90 >= k))
      {
        i = 1;
        arrayOfChar[j] = ((char)(97 + (k - 65)));
      }
    }
  }

  public static byte[] toUTF8ByteArray(String paramString)
  {
    return toUTF8ByteArray(paramString.toCharArray());
  }

  public static byte[] toUTF8ByteArray(char[] paramArrayOfChar)
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    int i = 0;
    if (i >= paramArrayOfChar.length)
      return localByteArrayOutputStream.toByteArray();
    int j = paramArrayOfChar[i];
    if (j < 128)
      localByteArrayOutputStream.write(j);
    while (true)
    {
      i++;
      break;
      if (j < 2048)
      {
        localByteArrayOutputStream.write(0xC0 | j >> 6);
        localByteArrayOutputStream.write(0x80 | j & 0x3F);
      }
      else if ((j >= 55296) && (j <= 57343))
      {
        if (i + 1 >= paramArrayOfChar.length)
          throw new IllegalStateException("invalid UTF-16 codepoint");
        i++;
        int k = paramArrayOfChar[i];
        if (j > 56319)
          throw new IllegalStateException("invalid UTF-16 codepoint");
        int m = 65536 + ((j & 0x3FF) << 10 | k & 0x3FF);
        localByteArrayOutputStream.write(0xF0 | m >> 18);
        localByteArrayOutputStream.write(0x80 | 0x3F & m >> 12);
        localByteArrayOutputStream.write(0x80 | 0x3F & m >> 6);
        localByteArrayOutputStream.write(0x80 | m & 0x3F);
      }
      else
      {
        localByteArrayOutputStream.write(0xE0 | j >> 12);
        localByteArrayOutputStream.write(0x80 | 0x3F & j >> 6);
        localByteArrayOutputStream.write(0x80 | j & 0x3F);
      }
    }
  }

  public static String toUpperCase(String paramString)
  {
    int i = 0;
    char[] arrayOfChar = paramString.toCharArray();
    for (int j = 0; ; j++)
    {
      if (j == arrayOfChar.length)
      {
        if (i != 0)
          paramString = new String(arrayOfChar);
        return paramString;
      }
      int k = arrayOfChar[j];
      if ((97 <= k) && (122 >= k))
      {
        i = 1;
        arrayOfChar[j] = ((char)(65 + (k - 97)));
      }
    }
  }
}