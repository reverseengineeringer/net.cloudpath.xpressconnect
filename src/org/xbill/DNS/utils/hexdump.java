package org.xbill.DNS.utils;

public class hexdump
{
  private static final char[] hex = "0123456789ABCDEF".toCharArray();

  public static String dump(String paramString, byte[] paramArrayOfByte)
  {
    return dump(paramString, paramArrayOfByte, 0, paramArrayOfByte.length);
  }

  public static String dump(String paramString, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append(paramInt2 + "b");
    if (paramString != null)
      localStringBuffer.append(" (" + paramString + ")");
    localStringBuffer.append(':');
    int i = 0xFFFFFFF8 & 8 + localStringBuffer.toString().length();
    localStringBuffer.append('\t');
    int j = (80 - i) / 3;
    for (int k = 0; k < paramInt2; k++)
    {
      if ((k != 0) && (k % j == 0))
      {
        localStringBuffer.append('\n');
        for (int n = 0; n < i / 8; n++)
          localStringBuffer.append('\t');
      }
      int m = 0xFF & paramArrayOfByte[(k + paramInt1)];
      localStringBuffer.append(hex[(m >> 4)]);
      localStringBuffer.append(hex[(m & 0xF)]);
      localStringBuffer.append(' ');
    }
    localStringBuffer.append('\n');
    return localStringBuffer.toString();
  }
}