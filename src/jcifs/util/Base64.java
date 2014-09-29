package jcifs.util;

public class Base64
{
  private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

  public static byte[] decode(String paramString)
  {
    int i = paramString.length();
    byte[] arrayOfByte;
    if (i == 0)
    {
      arrayOfByte = new byte[0];
      return arrayOfByte;
    }
    int k;
    label30: int m;
    int n;
    int i1;
    if (paramString.charAt(i - 2) == '=')
    {
      k = 2;
      m = i * 3 / 4 - k;
      arrayOfByte = new byte[m];
      n = 0;
      i1 = 0;
    }
    label264: label269: 
    while (true)
    {
      label51: int i8;
      int i9;
      if (i1 < i)
      {
        int i2 = i1 + 1;
        int i3 = (0xFF & "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".indexOf(paramString.charAt(i1))) << 18;
        int i4 = i2 + 1;
        int i5 = i3 | (0xFF & "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".indexOf(paramString.charAt(i2))) << 12;
        int i6 = i4 + 1;
        int i7 = i5 | (0xFF & "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".indexOf(paramString.charAt(i4))) << 6;
        i1 = i6 + 1;
        i8 = i7 | 0xFF & "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".indexOf(paramString.charAt(i6));
        i9 = n + 1;
        arrayOfByte[n] = ((byte)(i8 >>> 16));
        if (i9 >= m)
          break label264;
        n = i9 + 1;
        arrayOfByte[i9] = ((byte)(0xFF & i8 >>> 8));
      }
      while (true)
      {
        if (n >= m)
          break label269;
        int i10 = n + 1;
        arrayOfByte[n] = ((byte)(i8 & 0xFF));
        n = i10;
        break label51;
        break;
        int j = paramString.charAt(i - 1);
        k = 0;
        if (j != 61)
          break label30;
        k = 1;
        break label30;
        n = i9;
      }
    }
  }

  public static String encode(byte[] paramArrayOfByte)
  {
    int i = paramArrayOfByte.length;
    if (i == 0)
      return "";
    StringBuffer localStringBuffer = new StringBuffer(4 * (int)Math.ceil(i / 3.0D));
    int j = i % 3;
    int k = i - j;
    int i7;
    for (int m = 0; m < k; m = i7)
    {
      int i3 = m + 1;
      int i4 = (0xFF & paramArrayOfByte[m]) << 16;
      int i5 = i3 + 1;
      int i6 = i4 | (0xFF & paramArrayOfByte[i3]) << 8;
      i7 = i5 + 1;
      int i8 = i6 | 0xFF & paramArrayOfByte[i5];
      localStringBuffer.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt(i8 >>> 18));
      localStringBuffer.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt(0x3F & i8 >>> 12));
      localStringBuffer.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt(0x3F & i8 >>> 6));
      localStringBuffer.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt(i8 & 0x3F));
    }
    if (j == 0)
      return localStringBuffer.toString();
    if (j == 1)
    {
      int i2 = (0xFF & paramArrayOfByte[m]) << 4;
      localStringBuffer.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt(i2 >>> 6));
      localStringBuffer.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt(i2 & 0x3F));
      localStringBuffer.append("==");
      return localStringBuffer.toString();
    }
    int n = m + 1;
    int i1 = ((0xFF & paramArrayOfByte[m]) << 8 | 0xFF & paramArrayOfByte[n]) << 2;
    localStringBuffer.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt(i1 >>> 12));
    localStringBuffer.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt(0x3F & i1 >>> 6));
    localStringBuffer.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt(i1 & 0x3F));
    localStringBuffer.append("=");
    return localStringBuffer.toString();
  }
}