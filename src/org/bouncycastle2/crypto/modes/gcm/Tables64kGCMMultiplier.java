package org.bouncycastle2.crypto.modes.gcm;

import java.lang.reflect.Array;
import org.bouncycastle2.crypto.util.Pack;

public class Tables64kGCMMultiplier
  implements GCMMultiplier
{
  private final int[][][] M = (int[][][])Array.newInstance([I.class, new int[] { 16, 256 });

  public void init(byte[] paramArrayOfByte)
  {
    this.M[0][0] = new int[4];
    this.M[0][''] = GCMUtil.asInts(paramArrayOfByte);
    int i = 64;
    int j;
    if (i < 1)
      j = 0;
    while (true)
    {
      int k = 2;
      if (k >= 256)
      {
        j++;
        if (j != 16)
          break label178;
        return;
        int[] arrayOfInt1 = new int[4];
        System.arraycopy(this.M[0][(i + i)], 0, arrayOfInt1, 0, 4);
        GCMUtil.multiplyP(arrayOfInt1);
        this.M[0][i] = arrayOfInt1;
        i >>= 1;
        break;
      }
      for (int m = 1; ; m++)
      {
        if (m >= k)
        {
          k += k;
          break;
        }
        int[] arrayOfInt2 = new int[4];
        System.arraycopy(this.M[j][k], 0, arrayOfInt2, 0, 4);
        GCMUtil.xor(arrayOfInt2, this.M[j][m]);
        this.M[j][(k + m)] = arrayOfInt2;
      }
      label178: this.M[j][0] = new int[4];
      int n = 128;
      while (n > 0)
      {
        int[] arrayOfInt3 = new int[4];
        System.arraycopy(this.M[(j - 1)][n], 0, arrayOfInt3, 0, 4);
        GCMUtil.multiplyP8(arrayOfInt3);
        this.M[j][n] = arrayOfInt3;
        n >>= 1;
      }
    }
  }

  public void multiplyH(byte[] paramArrayOfByte)
  {
    int[] arrayOfInt1 = new int[4];
    for (int i = 15; ; i--)
    {
      if (i < 0)
      {
        Pack.intToBigEndian(arrayOfInt1[0], paramArrayOfByte, 0);
        Pack.intToBigEndian(arrayOfInt1[1], paramArrayOfByte, 4);
        Pack.intToBigEndian(arrayOfInt1[2], paramArrayOfByte, 8);
        Pack.intToBigEndian(arrayOfInt1[3], paramArrayOfByte, 12);
        return;
      }
      int[] arrayOfInt2 = this.M[i][(0xFF & paramArrayOfByte[i])];
      arrayOfInt1[0] ^= arrayOfInt2[0];
      arrayOfInt1[1] ^= arrayOfInt2[1];
      arrayOfInt1[2] ^= arrayOfInt2[2];
      arrayOfInt1[3] ^= arrayOfInt2[3];
    }
  }
}