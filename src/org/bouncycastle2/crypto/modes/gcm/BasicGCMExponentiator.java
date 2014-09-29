package org.bouncycastle2.crypto.modes.gcm;

import org.bouncycastle2.util.Arrays;

public class BasicGCMExponentiator
  implements GCMExponentiator
{
  private byte[] x;

  public void exponentiateX(long paramLong, byte[] paramArrayOfByte)
  {
    byte[] arrayOfByte1 = GCMUtil.oneAsBytes();
    if (paramLong > 0L)
    {
      byte[] arrayOfByte2 = Arrays.clone(this.x);
      do
      {
        if ((1L & paramLong) != 0L)
          GCMUtil.multiply(arrayOfByte1, arrayOfByte2);
        GCMUtil.multiply(arrayOfByte2, arrayOfByte2);
        paramLong >>>= 1;
      }
      while (paramLong > 0L);
    }
    System.arraycopy(arrayOfByte1, 0, paramArrayOfByte, 0, 16);
  }

  public void init(byte[] paramArrayOfByte)
  {
    this.x = Arrays.clone(paramArrayOfByte);
  }
}