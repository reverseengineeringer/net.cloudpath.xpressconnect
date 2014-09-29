package org.bouncycastle2.crypto.paddings;

import java.security.SecureRandom;
import org.bouncycastle2.crypto.InvalidCipherTextException;

public class ISO7816d4Padding
  implements BlockCipherPadding
{
  public int addPadding(byte[] paramArrayOfByte, int paramInt)
  {
    int i = paramArrayOfByte.length - paramInt;
    paramArrayOfByte[paramInt] = -128;
    for (int j = paramInt + 1; ; j++)
    {
      if (j >= paramArrayOfByte.length)
        return i;
      paramArrayOfByte[j] = 0;
    }
  }

  public String getPaddingName()
  {
    return "ISO7816-4";
  }

  public void init(SecureRandom paramSecureRandom)
    throws IllegalArgumentException
  {
  }

  public int padCount(byte[] paramArrayOfByte)
    throws InvalidCipherTextException
  {
    for (int i = -1 + paramArrayOfByte.length; ; i--)
      if ((i <= 0) || (paramArrayOfByte[i] != 0))
      {
        if (paramArrayOfByte[i] == -128)
          break;
        throw new InvalidCipherTextException("pad block corrupted");
      }
    return paramArrayOfByte.length - i;
  }
}