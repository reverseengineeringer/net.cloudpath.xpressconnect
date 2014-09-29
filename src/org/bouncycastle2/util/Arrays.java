package org.bouncycastle2.util;

public final class Arrays
{
  public static boolean areEqual(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    if (paramArrayOfByte1 == paramArrayOfByte2);
    while (true)
    {
      return true;
      if ((paramArrayOfByte1 == null) || (paramArrayOfByte2 == null))
        return false;
      if (paramArrayOfByte1.length != paramArrayOfByte2.length)
        return false;
      for (int i = 0; i != paramArrayOfByte1.length; i++)
        if (paramArrayOfByte1[i] != paramArrayOfByte2[i])
          return false;
    }
  }

  public static boolean areEqual(char[] paramArrayOfChar1, char[] paramArrayOfChar2)
  {
    if (paramArrayOfChar1 == paramArrayOfChar2);
    while (true)
    {
      return true;
      if ((paramArrayOfChar1 == null) || (paramArrayOfChar2 == null))
        return false;
      if (paramArrayOfChar1.length != paramArrayOfChar2.length)
        return false;
      for (int i = 0; i != paramArrayOfChar1.length; i++)
        if (paramArrayOfChar1[i] != paramArrayOfChar2[i])
          return false;
    }
  }

  public static boolean areEqual(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
  {
    if (paramArrayOfInt1 == paramArrayOfInt2);
    while (true)
    {
      return true;
      if ((paramArrayOfInt1 == null) || (paramArrayOfInt2 == null))
        return false;
      if (paramArrayOfInt1.length != paramArrayOfInt2.length)
        return false;
      for (int i = 0; i != paramArrayOfInt1.length; i++)
        if (paramArrayOfInt1[i] != paramArrayOfInt2[i])
          return false;
    }
  }

  public static boolean areEqual(boolean[] paramArrayOfBoolean1, boolean[] paramArrayOfBoolean2)
  {
    if (paramArrayOfBoolean1 == paramArrayOfBoolean2);
    while (true)
    {
      return true;
      if ((paramArrayOfBoolean1 == null) || (paramArrayOfBoolean2 == null))
        return false;
      if (paramArrayOfBoolean1.length != paramArrayOfBoolean2.length)
        return false;
      for (int i = 0; i != paramArrayOfBoolean1.length; i++)
        if (paramArrayOfBoolean1[i] != paramArrayOfBoolean2[i])
          return false;
    }
  }

  public static byte[] clone(byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte == null)
      return null;
    byte[] arrayOfByte = new byte[paramArrayOfByte.length];
    System.arraycopy(paramArrayOfByte, 0, arrayOfByte, 0, paramArrayOfByte.length);
    return arrayOfByte;
  }

  public static int[] clone(int[] paramArrayOfInt)
  {
    if (paramArrayOfInt == null)
      return null;
    int[] arrayOfInt = new int[paramArrayOfInt.length];
    System.arraycopy(paramArrayOfInt, 0, arrayOfInt, 0, paramArrayOfInt.length);
    return arrayOfInt;
  }

  public static boolean constantTimeAreEqual(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    if (paramArrayOfByte1 == paramArrayOfByte2)
      return true;
    if ((paramArrayOfByte1 == null) || (paramArrayOfByte2 == null))
      return false;
    if (paramArrayOfByte1.length != paramArrayOfByte2.length)
      return false;
    int i = 0;
    for (int j = 0; ; j++)
    {
      if (j == paramArrayOfByte1.length)
      {
        if (i == 0)
          break;
        return false;
      }
      i |= paramArrayOfByte1[j] ^ paramArrayOfByte2[j];
    }
  }

  public static void fill(byte[] paramArrayOfByte, byte paramByte)
  {
    for (int i = 0; ; i++)
    {
      if (i >= paramArrayOfByte.length)
        return;
      paramArrayOfByte[i] = paramByte;
    }
  }

  public static void fill(long[] paramArrayOfLong, long paramLong)
  {
    for (int i = 0; ; i++)
    {
      if (i >= paramArrayOfLong.length)
        return;
      paramArrayOfLong[i] = paramLong;
    }
  }

  public static void fill(short[] paramArrayOfShort, short paramShort)
  {
    for (int i = 0; ; i++)
    {
      if (i >= paramArrayOfShort.length)
        return;
      paramArrayOfShort[i] = paramShort;
    }
  }

  public static int hashCode(byte[] paramArrayOfByte)
  {
    int j;
    if (paramArrayOfByte == null)
      j = 0;
    while (true)
    {
      return j;
      int i = paramArrayOfByte.length;
      for (j = i + 1; ; j = j * 257 ^ paramArrayOfByte[i])
      {
        i--;
        if (i < 0)
          break;
      }
    }
  }
}