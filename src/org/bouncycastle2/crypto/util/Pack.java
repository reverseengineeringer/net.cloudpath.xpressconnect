package org.bouncycastle2.crypto.util;

public abstract class Pack
{
  public static int bigEndianToInt(byte[] paramArrayOfByte, int paramInt)
  {
    int i = paramArrayOfByte[paramInt] << 24;
    int j = paramInt + 1;
    int k = i | (0xFF & paramArrayOfByte[j]) << 16;
    int m = j + 1;
    return k | (0xFF & paramArrayOfByte[m]) << 8 | 0xFF & paramArrayOfByte[(m + 1)];
  }

  public static long bigEndianToLong(byte[] paramArrayOfByte, int paramInt)
  {
    int i = bigEndianToInt(paramArrayOfByte, paramInt);
    int j = bigEndianToInt(paramArrayOfByte, paramInt + 4);
    return (0xFFFFFFFF & i) << 32 | 0xFFFFFFFF & j;
  }

  public static void intToBigEndian(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
  {
    paramArrayOfByte[paramInt2] = ((byte)(paramInt1 >>> 24));
    int i = paramInt2 + 1;
    paramArrayOfByte[i] = ((byte)(paramInt1 >>> 16));
    int j = i + 1;
    paramArrayOfByte[j] = ((byte)(paramInt1 >>> 8));
    paramArrayOfByte[(j + 1)] = ((byte)paramInt1);
  }

  public static void intToLittleEndian(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
  {
    paramArrayOfByte[paramInt2] = ((byte)paramInt1);
    int i = paramInt2 + 1;
    paramArrayOfByte[i] = ((byte)(paramInt1 >>> 8));
    int j = i + 1;
    paramArrayOfByte[j] = ((byte)(paramInt1 >>> 16));
    paramArrayOfByte[(j + 1)] = ((byte)(paramInt1 >>> 24));
  }

  public static int littleEndianToInt(byte[] paramArrayOfByte, int paramInt)
  {
    int i = paramArrayOfByte[paramInt];
    int j = paramInt + 1;
    int k = i | (0xFF & paramArrayOfByte[j]) << 8;
    int m = j + 1;
    return k | (0xFF & paramArrayOfByte[m]) << 16 | (0xFF & paramArrayOfByte[(m + 1)]) << 24;
  }

  public static long littleEndianToLong(byte[] paramArrayOfByte, int paramInt)
  {
    int i = littleEndianToInt(paramArrayOfByte, paramInt);
    return (0xFFFFFFFF & littleEndianToInt(paramArrayOfByte, paramInt + 4)) << 32 | 0xFFFFFFFF & i;
  }

  public static void longToBigEndian(long paramLong, byte[] paramArrayOfByte, int paramInt)
  {
    intToBigEndian((int)(paramLong >>> 32), paramArrayOfByte, paramInt);
    intToBigEndian((int)(0xFFFFFFFF & paramLong), paramArrayOfByte, paramInt + 4);
  }

  public static void longToLittleEndian(long paramLong, byte[] paramArrayOfByte, int paramInt)
  {
    intToLittleEndian((int)(0xFFFFFFFF & paramLong), paramArrayOfByte, paramInt);
    intToLittleEndian((int)(paramLong >>> 32), paramArrayOfByte, paramInt + 4);
  }
}