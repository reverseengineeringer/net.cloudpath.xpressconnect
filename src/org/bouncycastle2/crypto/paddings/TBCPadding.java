package org.bouncycastle2.crypto.paddings;

import java.security.SecureRandom;
import org.bouncycastle2.crypto.InvalidCipherTextException;

public class TBCPadding
  implements BlockCipherPadding
{
  public int addPadding(byte[] paramArrayOfByte, int paramInt)
  {
    int i = 255;
    int j = paramArrayOfByte.length - paramInt;
    int k;
    if (paramInt > 0)
      if ((0x1 & paramArrayOfByte[(paramInt - 1)]) == 0)
        k = (byte)i;
    while (true)
    {
      if (paramInt >= paramArrayOfByte.length)
      {
        return j;
        i = 0;
        break;
        if ((0x1 & paramArrayOfByte[(-1 + paramArrayOfByte.length)]) == 0);
        while (true)
        {
          k = (byte)i;
          break;
          i = 0;
        }
      }
      paramArrayOfByte[paramInt] = k;
      paramInt++;
    }
  }

  public String getPaddingName()
  {
    return "TBC";
  }

  public void init(SecureRandom paramSecureRandom)
    throws IllegalArgumentException
  {
  }

  public int padCount(byte[] paramArrayOfByte)
    throws InvalidCipherTextException
  {
    int i = paramArrayOfByte[(-1 + paramArrayOfByte.length)];
    for (int j = -1 + paramArrayOfByte.length; ; j--)
      if ((j <= 0) || (paramArrayOfByte[(j - 1)] != i))
        return paramArrayOfByte.length - j;
  }
}