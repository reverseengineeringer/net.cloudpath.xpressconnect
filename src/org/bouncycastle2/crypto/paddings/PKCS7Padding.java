package org.bouncycastle2.crypto.paddings;

import java.security.SecureRandom;
import org.bouncycastle2.crypto.InvalidCipherTextException;

public class PKCS7Padding
  implements BlockCipherPadding
{
  public int addPadding(byte[] paramArrayOfByte, int paramInt)
  {
    int i = (byte)(paramArrayOfByte.length - paramInt);
    while (true)
    {
      if (paramInt >= paramArrayOfByte.length)
        return i;
      paramArrayOfByte[paramInt] = i;
      paramInt++;
    }
  }

  public String getPaddingName()
  {
    return "PKCS7";
  }

  public void init(SecureRandom paramSecureRandom)
    throws IllegalArgumentException
  {
  }

  public int padCount(byte[] paramArrayOfByte)
    throws InvalidCipherTextException
  {
    int i = 0xFF & paramArrayOfByte[(-1 + paramArrayOfByte.length)];
    if ((i > paramArrayOfByte.length) || (i == 0))
      throw new InvalidCipherTextException("pad block corrupted");
    for (int j = 1; ; j++)
    {
      if (j > i)
        return i;
      if (paramArrayOfByte[(paramArrayOfByte.length - j)] != i)
        throw new InvalidCipherTextException("pad block corrupted");
    }
  }
}