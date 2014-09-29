package org.bouncycastle2.crypto.modes.gcm;

import java.lang.reflect.Array;
import org.bouncycastle2.crypto.util.Pack;

public class Tables8kGCMMultiplier
  implements GCMMultiplier
{
  private final int[][][] M = (int[][][])Array.newInstance([I.class, new int[] { 32, 16 });

  public void init(byte[] paramArrayOfByte)
  {
    this.M[0][0] = new int[4];
    this.M[1][0] = new int[4];
    this.M[1][8] = GCMUtil.asInts(paramArrayOfByte);
    int i = 4;
    int j;
    label82: int k;
    if (i < 1)
    {
      int[] arrayOfInt2 = new int[4];
      System.arraycopy(this.M[1][1], 0, arrayOfInt2, 0, 4);
      GCMUtil.multiplyP(arrayOfInt2);
      this.M[0][8] = arrayOfInt2;
      j = 4;
      if (j >= 1)
        break label153;
      k = 0;
    }
    while (true)
    {
      int m = 2;
      if (m >= 16)
      {
        k++;
        if (k != 32)
          break label282;
        return;
        int[] arrayOfInt1 = new int[4];
        System.arraycopy(this.M[1][(i + i)], 0, arrayOfInt1, 0, 4);
        GCMUtil.multiplyP(arrayOfInt1);
        this.M[1][i] = arrayOfInt1;
        i >>= 1;
        break;
        label153: int[] arrayOfInt3 = new int[4];
        System.arraycopy(this.M[0][(j + j)], 0, arrayOfInt3, 0, 4);
        GCMUtil.multiplyP(arrayOfInt3);
        this.M[0][j] = arrayOfInt3;
        j >>= 1;
        break label82;
      }
      for (int n = 1; ; n++)
      {
        if (n >= m)
        {
          m += m;
          break;
        }
        int[] arrayOfInt4 = new int[4];
        System.arraycopy(this.M[k][m], 0, arrayOfInt4, 0, 4);
        GCMUtil.xor(arrayOfInt4, this.M[k][n]);
        this.M[k][(m + n)] = arrayOfInt4;
      }
      label282: if (k > 1)
      {
        this.M[k][0] = new int[4];
        int i1 = 8;
        while (i1 > 0)
        {
          int[] arrayOfInt5 = new int[4];
          System.arraycopy(this.M[(k - 2)][i1], 0, arrayOfInt5, 0, 4);
          GCMUtil.multiplyP8(arrayOfInt5);
          this.M[k][i1] = arrayOfInt5;
          i1 >>= 1;
        }
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
      int[] arrayOfInt2 = this.M[(i + i)][(0xF & paramArrayOfByte[i])];
      arrayOfInt1[0] ^= arrayOfInt2[0];
      arrayOfInt1[1] ^= arrayOfInt2[1];
      arrayOfInt1[2] ^= arrayOfInt2[2];
      arrayOfInt1[3] ^= arrayOfInt2[3];
      int[] arrayOfInt3 = this.M[(1 + (i + i))][((0xF0 & paramArrayOfByte[i]) >>> 4)];
      arrayOfInt1[0] ^= arrayOfInt3[0];
      arrayOfInt1[1] ^= arrayOfInt3[1];
      arrayOfInt1[2] ^= arrayOfInt3[2];
      arrayOfInt1[3] ^= arrayOfInt3[3];
    }
  }
}