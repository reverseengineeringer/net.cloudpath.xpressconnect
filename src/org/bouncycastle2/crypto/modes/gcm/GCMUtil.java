package org.bouncycastle2.crypto.modes.gcm;

import org.bouncycastle2.crypto.util.Pack;
import org.bouncycastle2.util.Arrays;

abstract class GCMUtil
{
  static int[] asInts(byte[] paramArrayOfByte)
  {
    int[] arrayOfInt = new int[4];
    arrayOfInt[0] = Pack.bigEndianToInt(paramArrayOfByte, 0);
    arrayOfInt[1] = Pack.bigEndianToInt(paramArrayOfByte, 4);
    arrayOfInt[2] = Pack.bigEndianToInt(paramArrayOfByte, 8);
    arrayOfInt[3] = Pack.bigEndianToInt(paramArrayOfByte, 12);
    return arrayOfInt;
  }

  static void multiply(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    byte[] arrayOfByte1 = Arrays.clone(paramArrayOfByte1);
    byte[] arrayOfByte2 = new byte[16];
    int j;
    int k;
    for (int i = 0; ; i++)
    {
      if (i >= 16)
      {
        System.arraycopy(arrayOfByte2, 0, paramArrayOfByte1, 0, 16);
        return;
      }
      j = paramArrayOfByte2[i];
      k = 7;
      if (k >= 0)
        break;
    }
    if ((j & 1 << k) != 0)
      xor(arrayOfByte2, arrayOfByte1);
    if ((0x1 & arrayOfByte1[15]) != 0);
    for (int m = 1; ; m = 0)
    {
      shiftRight(arrayOfByte1);
      if (m != 0)
        arrayOfByte1[0] = ((byte)(0xFFFFFFE1 ^ arrayOfByte1[0]));
      k--;
      break;
    }
  }

  static void multiplyP(int[] paramArrayOfInt)
  {
    if ((0x1 & paramArrayOfInt[3]) != 0);
    for (int i = 1; ; i = 0)
    {
      shiftRight(paramArrayOfInt);
      if (i != 0)
        paramArrayOfInt[0] = (0xE1000000 ^ paramArrayOfInt[0]);
      return;
    }
  }

  static void multiplyP8(int[] paramArrayOfInt)
  {
    for (int i = 8; ; i--)
    {
      if (i == 0)
        return;
      multiplyP(paramArrayOfInt);
    }
  }

  static byte[] oneAsBytes()
  {
    byte[] arrayOfByte = new byte[16];
    arrayOfByte[0] = -128;
    return arrayOfByte;
  }

  static int[] oneAsInts()
  {
    int[] arrayOfInt = new int[4];
    arrayOfInt[0] = -2147483648;
    return arrayOfInt;
  }

  static void shiftRight(byte[] paramArrayOfByte)
  {
    int i = 0;
    int k;
    for (int j = 0; ; j = (k & 0x1) << 7)
    {
      k = 0xFF & paramArrayOfByte[i];
      paramArrayOfByte[i] = ((byte)(j | k >>> 1));
      i++;
      if (i == 16)
        return;
    }
  }

  static void shiftRight(int[] paramArrayOfInt)
  {
    int i = 0;
    int k;
    for (int j = 0; ; j = k << 31)
    {
      k = paramArrayOfInt[i];
      paramArrayOfInt[i] = (j | k >>> 1);
      i++;
      if (i == 4)
        return;
    }
  }

  static void xor(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    for (int i = 15; ; i--)
    {
      if (i < 0)
        return;
      paramArrayOfByte1[i] = ((byte)(paramArrayOfByte1[i] ^ paramArrayOfByte2[i]));
    }
  }

  static void xor(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
  {
    for (int i = 3; ; i--)
    {
      if (i < 0)
        return;
      paramArrayOfInt1[i] ^= paramArrayOfInt2[i];
    }
  }
}