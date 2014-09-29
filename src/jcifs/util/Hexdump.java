package jcifs.util;

import java.io.PrintStream;

public class Hexdump
{
  public static final char[] HEX_DIGITS = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70 };
  private static final String NL = System.getProperty("line.separator");
  private static final int NL_LENGTH = NL.length();
  private static final char[] SPACE_CHARS = { 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32 };

  public static void hexdump(PrintStream paramPrintStream, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    if (paramInt2 == 0)
      return;
    int i = paramInt2 % 16;
    int j;
    char[] arrayOfChar1;
    char[] arrayOfChar2;
    int k;
    int m;
    label47: int i1;
    int i4;
    if (i == 0)
    {
      j = paramInt2 / 16;
      arrayOfChar1 = new char[j * (74 + NL_LENGTH)];
      arrayOfChar2 = new char[16];
      k = 0;
      m = 0;
      toHexChars(k, arrayOfChar1, m, 5);
      int n = m + 5;
      i1 = n + 1;
      arrayOfChar1[n] = ':';
      if (k != paramInt2)
        break label243;
      int i10 = 16 - i;
      System.arraycopy(SPACE_CHARS, 0, arrayOfChar1, i1, i10 * 3);
      i4 = i1 + i10 * 3;
      System.arraycopy(SPACE_CHARS, 0, arrayOfChar2, i, i10);
    }
    label338: 
    while (true)
    {
      int i5 = i4 + 1;
      arrayOfChar1[i4] = ' ';
      int i6 = i5 + 1;
      arrayOfChar1[i5] = ' ';
      int i7 = i6 + 1;
      arrayOfChar1[i6] = '|';
      System.arraycopy(arrayOfChar2, 0, arrayOfChar1, i7, 16);
      int i8 = i7 + 16;
      int i9 = i8 + 1;
      arrayOfChar1[i8] = '|';
      NL.getChars(0, NL_LENGTH, arrayOfChar1, i9);
      m = i9 + NL_LENGTH;
      if (k < paramInt2)
        break label47;
      paramPrintStream.println(arrayOfChar1);
      return;
      j = 1 + paramInt2 / 16;
      break;
      label243: int i2 = i1 + 1;
      arrayOfChar1[i1] = ' ';
      int i3 = 0xFF & paramArrayOfByte[(paramInt1 + k)];
      toHexChars(i3, arrayOfChar1, i2, 2);
      i4 = i2 + 2;
      if ((i3 < 0) || (Character.isISOControl((char)i3)))
        arrayOfChar2[(k % 16)] = '.';
      while (true)
      {
        k++;
        if (k % 16 == 0)
          break label338;
        i1 = i4;
        break;
        arrayOfChar2[(k % 16)] = ((char)i3);
      }
    }
  }

  public static void toHexChars(int paramInt1, char[] paramArrayOfChar, int paramInt2, int paramInt3)
  {
    while (paramInt3 > 0)
    {
      int i = -1 + (paramInt2 + paramInt3);
      if (i < paramArrayOfChar.length)
        paramArrayOfChar[i] = HEX_DIGITS[(paramInt1 & 0xF)];
      if (paramInt1 != 0)
        paramInt1 >>>= 4;
      paramInt3--;
    }
  }

  public static void toHexChars(long paramLong, char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    while (paramInt2 > 0)
    {
      paramArrayOfChar[(-1 + (paramInt1 + paramInt2))] = HEX_DIGITS[((int)(0xF & paramLong))];
      if (paramLong != 0L)
        paramLong >>>= 4;
      paramInt2--;
    }
  }

  public static String toHexString(int paramInt1, int paramInt2)
  {
    char[] arrayOfChar = new char[paramInt2];
    toHexChars(paramInt1, arrayOfChar, 0, paramInt2);
    return new String(arrayOfChar);
  }

  public static String toHexString(long paramLong, int paramInt)
  {
    char[] arrayOfChar = new char[paramInt];
    toHexChars(paramLong, arrayOfChar, 0, paramInt);
    return new String(arrayOfChar);
  }

  public static String toHexString(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    char[] arrayOfChar = new char[paramInt2];
    int i;
    int j;
    int k;
    label21: int m;
    if (paramInt2 % 2 == 0)
    {
      i = paramInt2 / 2;
      j = 0;
      k = 0;
      if (j >= i)
        break label104;
      m = k + 1;
      arrayOfChar[k] = HEX_DIGITS[(0xF & paramArrayOfByte[j] >> 4)];
      if (m != arrayOfChar.length)
        break label77;
    }
    label77: label104: 
    while (true)
    {
      return new String(arrayOfChar);
      i = 1 + paramInt2 / 2;
      break;
      k = m + 1;
      arrayOfChar[m] = HEX_DIGITS[(0xF & paramArrayOfByte[j])];
      j++;
      break label21;
    }
  }
}