package org.bouncycastle2.crypto.paddings;

import java.security.SecureRandom;
import org.bouncycastle2.crypto.InvalidCipherTextException;

public class ISO10126d2Padding
  implements BlockCipherPadding
{
  SecureRandom random;

  public int addPadding(byte[] paramArrayOfByte, int paramInt)
  {
    int i = (byte)(paramArrayOfByte.length - paramInt);
    while (true)
    {
      if (paramInt >= -1 + paramArrayOfByte.length)
      {
        paramArrayOfByte[paramInt] = i;
        return i;
      }
      paramArrayOfByte[paramInt] = ((byte)this.random.nextInt());
      paramInt++;
    }
  }

  public String getPaddingName()
  {
    return "ISO10126-2";
  }

  public void init(SecureRandom paramSecureRandom)
    throws IllegalArgumentException
  {
    if (paramSecureRandom != null)
    {
      this.random = paramSecureRandom;
      return;
    }
    this.random = new SecureRandom();
  }

  public int padCount(byte[] paramArrayOfByte)
    throws InvalidCipherTextException
  {
    int i = 0xFF & paramArrayOfByte[(-1 + paramArrayOfByte.length)];
    if (i > paramArrayOfByte.length)
      throw new InvalidCipherTextException("pad block corrupted");
    return i;
  }
}