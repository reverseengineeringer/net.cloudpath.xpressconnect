package org.bouncycastle2.crypto.paddings;

import java.security.SecureRandom;
import org.bouncycastle2.crypto.InvalidCipherTextException;

public class X923Padding
  implements BlockCipherPadding
{
  SecureRandom random = null;

  public int addPadding(byte[] paramArrayOfByte, int paramInt)
  {
    int i = (byte)(paramArrayOfByte.length - paramInt);
    if (paramInt >= -1 + paramArrayOfByte.length)
    {
      paramArrayOfByte[paramInt] = i;
      return i;
    }
    if (this.random == null)
      paramArrayOfByte[paramInt] = 0;
    while (true)
    {
      paramInt++;
      break;
      paramArrayOfByte[paramInt] = ((byte)this.random.nextInt());
    }
  }

  public String getPaddingName()
  {
    return "X9.23";
  }

  public void init(SecureRandom paramSecureRandom)
    throws IllegalArgumentException
  {
    this.random = paramSecureRandom;
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