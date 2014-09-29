package org.bouncycastle2.crypto.paddings;

import java.security.SecureRandom;
import org.bouncycastle2.crypto.InvalidCipherTextException;

public class ZeroBytePadding
  implements BlockCipherPadding
{
  public int addPadding(byte[] paramArrayOfByte, int paramInt)
  {
    int i = paramArrayOfByte.length - paramInt;
    while (true)
    {
      if (paramInt >= paramArrayOfByte.length)
        return i;
      paramArrayOfByte[paramInt] = 0;
      paramInt++;
    }
  }

  public String getPaddingName()
  {
    return "ZeroByte";
  }

  public void init(SecureRandom paramSecureRandom)
    throws IllegalArgumentException
  {
  }

  public int padCount(byte[] paramArrayOfByte)
    throws InvalidCipherTextException
  {
    for (int i = paramArrayOfByte.length; ; i--)
    {
      if (i <= 0);
      while (paramArrayOfByte[(i - 1)] != 0)
        return paramArrayOfByte.length - i;
    }
  }
}