package org.bouncycastle2.crypto.modes.gcm;

import org.bouncycastle2.util.Arrays;

public class Tables1kGCMExponentiator
  implements GCMExponentiator
{
  byte[][] lookupPowX2 = new byte[64][];

  public void exponentiateX(long paramLong, byte[] paramArrayOfByte)
  {
    byte[] arrayOfByte = GCMUtil.oneAsBytes();
    int i = 1;
    while (true)
    {
      if (paramLong <= 0L)
      {
        System.arraycopy(arrayOfByte, 0, paramArrayOfByte, 0, 16);
        return;
      }
      if ((1L & paramLong) != 0L)
        GCMUtil.multiply(arrayOfByte, this.lookupPowX2[i]);
      i++;
      paramLong >>>= 1;
    }
  }

  public void init(byte[] paramArrayOfByte)
  {
    this.lookupPowX2[0] = new byte[16];
    this.lookupPowX2[0][0] = -128;
    this.lookupPowX2[1] = Arrays.clone(paramArrayOfByte);
    for (int i = 2; ; i++)
    {
      if (i == 64)
        return;
      byte[] arrayOfByte = Arrays.clone(this.lookupPowX2[(i - 1)]);
      GCMUtil.multiply(arrayOfByte, arrayOfByte);
      this.lookupPowX2[i] = arrayOfByte;
    }
  }
}