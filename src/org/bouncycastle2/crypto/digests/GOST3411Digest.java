package org.bouncycastle2.crypto.digests;

import java.lang.reflect.Array;
import org.bouncycastle2.crypto.BlockCipher;
import org.bouncycastle2.crypto.ExtendedDigest;
import org.bouncycastle2.crypto.engines.GOST28147Engine;
import org.bouncycastle2.crypto.params.KeyParameter;
import org.bouncycastle2.crypto.params.ParametersWithSBox;
import org.bouncycastle2.crypto.util.Pack;
import org.bouncycastle2.util.Arrays;

public class GOST3411Digest
  implements ExtendedDigest
{
  private static final byte[] C2 = arrayOfByte;
  private static final int DIGEST_LENGTH = 32;
  private byte[][] C;
  private byte[] H = new byte[32];
  private byte[] K;
  private byte[] L = new byte[32];
  private byte[] M = new byte[32];
  byte[] S;
  private byte[] Sum = new byte[32];
  byte[] U;
  byte[] V;
  byte[] W;
  byte[] a;
  private long byteCount;
  private BlockCipher cipher;
  private byte[] sBox;
  short[] wS;
  short[] w_S;
  private byte[] xBuf;
  private int xBufOff;

  static
  {
    byte[] arrayOfByte = new byte[32];
    arrayOfByte[1] = -1;
    arrayOfByte[3] = -1;
    arrayOfByte[5] = -1;
    arrayOfByte[7] = -1;
    arrayOfByte[8] = -1;
    arrayOfByte[10] = -1;
    arrayOfByte[12] = -1;
    arrayOfByte[14] = -1;
    arrayOfByte[17] = -1;
    arrayOfByte[18] = -1;
    arrayOfByte[20] = -1;
    arrayOfByte[23] = -1;
    arrayOfByte[24] = -1;
    arrayOfByte[28] = -1;
    arrayOfByte[29] = -1;
    arrayOfByte[31] = -1;
  }

  public GOST3411Digest()
  {
    int[] arrayOfInt = { 4, 32 };
    this.C = ((byte[][])Array.newInstance(Byte.TYPE, arrayOfInt));
    this.xBuf = new byte[32];
    this.cipher = new GOST28147Engine();
    this.K = new byte[32];
    this.a = new byte[8];
    this.wS = new short[16];
    this.w_S = new short[16];
    this.S = new byte[32];
    this.U = new byte[32];
    this.V = new byte[32];
    this.W = new byte[32];
    this.sBox = GOST28147Engine.getSBox("D-A");
    this.cipher.init(true, new ParametersWithSBox(null, this.sBox));
    reset();
  }

  public GOST3411Digest(GOST3411Digest paramGOST3411Digest)
  {
    int[] arrayOfInt = { 4, 32 };
    this.C = ((byte[][])Array.newInstance(Byte.TYPE, arrayOfInt));
    this.xBuf = new byte[32];
    this.cipher = new GOST28147Engine();
    this.K = new byte[32];
    this.a = new byte[8];
    this.wS = new short[16];
    this.w_S = new short[16];
    this.S = new byte[32];
    this.U = new byte[32];
    this.V = new byte[32];
    this.W = new byte[32];
    this.sBox = paramGOST3411Digest.sBox;
    this.cipher.init(true, new ParametersWithSBox(null, this.sBox));
    reset();
    System.arraycopy(paramGOST3411Digest.H, 0, this.H, 0, paramGOST3411Digest.H.length);
    System.arraycopy(paramGOST3411Digest.L, 0, this.L, 0, paramGOST3411Digest.L.length);
    System.arraycopy(paramGOST3411Digest.M, 0, this.M, 0, paramGOST3411Digest.M.length);
    System.arraycopy(paramGOST3411Digest.Sum, 0, this.Sum, 0, paramGOST3411Digest.Sum.length);
    System.arraycopy(paramGOST3411Digest.C[1], 0, this.C[1], 0, paramGOST3411Digest.C[1].length);
    System.arraycopy(paramGOST3411Digest.C[2], 0, this.C[2], 0, paramGOST3411Digest.C[2].length);
    System.arraycopy(paramGOST3411Digest.C[3], 0, this.C[3], 0, paramGOST3411Digest.C[3].length);
    System.arraycopy(paramGOST3411Digest.xBuf, 0, this.xBuf, 0, paramGOST3411Digest.xBuf.length);
    this.xBufOff = paramGOST3411Digest.xBufOff;
    this.byteCount = paramGOST3411Digest.byteCount;
  }

  public GOST3411Digest(byte[] paramArrayOfByte)
  {
    int[] arrayOfInt = { 4, 32 };
    this.C = ((byte[][])Array.newInstance(Byte.TYPE, arrayOfInt));
    this.xBuf = new byte[32];
    this.cipher = new GOST28147Engine();
    this.K = new byte[32];
    this.a = new byte[8];
    this.wS = new short[16];
    this.w_S = new short[16];
    this.S = new byte[32];
    this.U = new byte[32];
    this.V = new byte[32];
    this.W = new byte[32];
    this.sBox = Arrays.clone(paramArrayOfByte);
    this.cipher.init(true, new ParametersWithSBox(null, this.sBox));
    reset();
  }

  private byte[] A(byte[] paramArrayOfByte)
  {
    for (int i = 0; ; i++)
    {
      if (i >= 8)
      {
        System.arraycopy(paramArrayOfByte, 8, paramArrayOfByte, 0, 24);
        System.arraycopy(this.a, 0, paramArrayOfByte, 24, 8);
        return paramArrayOfByte;
      }
      this.a[i] = ((byte)(paramArrayOfByte[i] ^ paramArrayOfByte[(i + 8)]));
    }
  }

  private void E(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt1, byte[] paramArrayOfByte3, int paramInt2)
  {
    this.cipher.init(true, new KeyParameter(paramArrayOfByte1));
    this.cipher.processBlock(paramArrayOfByte3, paramInt2, paramArrayOfByte2, paramInt1);
  }

  private byte[] P(byte[] paramArrayOfByte)
  {
    for (int i = 0; ; i++)
    {
      if (i >= 8)
        return this.K;
      this.K[(i * 4)] = paramArrayOfByte[i];
      this.K[(1 + i * 4)] = paramArrayOfByte[(i + 8)];
      this.K[(2 + i * 4)] = paramArrayOfByte[(i + 16)];
      this.K[(3 + i * 4)] = paramArrayOfByte[(i + 24)];
    }
  }

  private void cpyBytesToShort(byte[] paramArrayOfByte, short[] paramArrayOfShort)
  {
    for (int i = 0; ; i++)
    {
      if (i >= paramArrayOfByte.length / 2)
        return;
      paramArrayOfShort[i] = ((short)(0xFF00 & paramArrayOfByte[(1 + i * 2)] << 8 | 0xFF & paramArrayOfByte[(i * 2)]));
    }
  }

  private void cpyShortToBytes(short[] paramArrayOfShort, byte[] paramArrayOfByte)
  {
    for (int i = 0; ; i++)
    {
      if (i >= paramArrayOfByte.length / 2)
        return;
      paramArrayOfByte[(1 + i * 2)] = ((byte)(paramArrayOfShort[i] >> 8));
      paramArrayOfByte[(i * 2)] = ((byte)paramArrayOfShort[i]);
    }
  }

  private void finish()
  {
    Pack.longToLittleEndian(8L * this.byteCount, this.L, 0);
    while (true)
    {
      if (this.xBufOff == 0)
      {
        processBlock(this.L, 0);
        processBlock(this.Sum, 0);
        return;
      }
      update((byte)0);
    }
  }

  private void fw(byte[] paramArrayOfByte)
  {
    cpyBytesToShort(paramArrayOfByte, this.wS);
    this.w_S[15] = ((short)(this.wS[0] ^ this.wS[1] ^ this.wS[2] ^ this.wS[3] ^ this.wS[12] ^ this.wS[15]));
    System.arraycopy(this.wS, 1, this.w_S, 0, 15);
    cpyShortToBytes(this.w_S, paramArrayOfByte);
  }

  private void sumByteArray(byte[] paramArrayOfByte)
  {
    int i = 0;
    for (int j = 0; ; j++)
    {
      if (j == this.Sum.length)
        return;
      int k = i + ((0xFF & this.Sum[j]) + (0xFF & paramArrayOfByte[j]));
      this.Sum[j] = ((byte)k);
      i = k >>> 8;
    }
  }

  public int doFinal(byte[] paramArrayOfByte, int paramInt)
  {
    finish();
    System.arraycopy(this.H, 0, paramArrayOfByte, paramInt, this.H.length);
    reset();
    return 32;
  }

  public String getAlgorithmName()
  {
    return "GOST3411";
  }

  public int getByteLength()
  {
    return 32;
  }

  public int getDigestSize()
  {
    return 32;
  }

  protected void processBlock(byte[] paramArrayOfByte, int paramInt)
  {
    System.arraycopy(paramArrayOfByte, paramInt, this.M, 0, 32);
    System.arraycopy(this.H, 0, this.U, 0, 32);
    System.arraycopy(this.M, 0, this.V, 0, 32);
    int i = 0;
    int j;
    int n;
    label84: int i1;
    label94: int i2;
    if (i >= 32)
    {
      E(P(this.W), this.S, 0, this.H, 0);
      j = 1;
      if (j < 4)
        break label174;
      n = 0;
      if (n < 12)
        break label315;
      i1 = 0;
      if (i1 < 32)
        break label329;
      fw(this.S);
      i2 = 0;
      label112: if (i2 < 32)
        break label358;
    }
    for (int i3 = 0; ; i3++)
    {
      if (i3 >= 61)
      {
        System.arraycopy(this.S, 0, this.H, 0, this.H.length);
        return;
        this.W[i] = ((byte)(this.U[i] ^ this.V[i]));
        i++;
        break;
        label174: byte[] arrayOfByte = A(this.U);
        int k = 0;
        label187: if (k >= 32)
          this.V = A(A(this.V));
        for (int m = 0; ; m++)
        {
          if (m >= 32)
          {
            E(P(this.W), this.S, j * 8, this.H, j * 8);
            j++;
            break;
            this.U[k] = ((byte)(arrayOfByte[k] ^ this.C[j][k]));
            k++;
            break label187;
          }
          this.W[m] = ((byte)(this.U[m] ^ this.V[m]));
        }
        label315: fw(this.S);
        n++;
        break label84;
        label329: this.S[i1] = ((byte)(this.S[i1] ^ this.M[i1]));
        i1++;
        break label94;
        label358: this.S[i2] = ((byte)(this.H[i2] ^ this.S[i2]));
        i2++;
        break label112;
      }
      fw(this.S);
    }
  }

  public void reset()
  {
    this.byteCount = 0L;
    this.xBufOff = 0;
    int i = 0;
    int j;
    label23: int k;
    label34: int m;
    label46: int n;
    label61: int i1;
    if (i >= this.H.length)
    {
      j = 0;
      if (j < this.L.length)
        break label131;
      k = 0;
      if (k < this.M.length)
        break label144;
      m = 0;
      if (m < this.C[1].length)
        break label157;
      n = 0;
      if (n < this.C[3].length)
        break label173;
      i1 = 0;
      label76: if (i1 < this.Sum.length)
        break label189;
    }
    for (int i2 = 0; ; i2++)
    {
      if (i2 >= this.xBuf.length)
      {
        System.arraycopy(C2, 0, this.C[2], 0, C2.length);
        return;
        this.H[i] = 0;
        i++;
        break;
        label131: this.L[j] = 0;
        j++;
        break label23;
        label144: this.M[k] = 0;
        k++;
        break label34;
        label157: this.C[1][m] = 0;
        m++;
        break label46;
        label173: this.C[3][n] = 0;
        n++;
        break label61;
        label189: this.Sum[i1] = 0;
        i1++;
        break label76;
      }
      this.xBuf[i2] = 0;
    }
  }

  public void update(byte paramByte)
  {
    byte[] arrayOfByte = this.xBuf;
    int i = this.xBufOff;
    this.xBufOff = (i + 1);
    arrayOfByte[i] = paramByte;
    if (this.xBufOff == this.xBuf.length)
    {
      sumByteArray(this.xBuf);
      processBlock(this.xBuf, 0);
      this.xBufOff = 0;
    }
    this.byteCount = (1L + this.byteCount);
  }

  public void update(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    if ((this.xBufOff == 0) || (paramInt2 <= 0))
      label11: if (paramInt2 > this.xBuf.length)
        break label41;
    while (true)
    {
      if (paramInt2 <= 0)
      {
        return;
        update(paramArrayOfByte[paramInt1]);
        paramInt1++;
        paramInt2--;
        break;
        label41: System.arraycopy(paramArrayOfByte, paramInt1, this.xBuf, 0, this.xBuf.length);
        sumByteArray(this.xBuf);
        processBlock(this.xBuf, 0);
        paramInt1 += this.xBuf.length;
        paramInt2 -= this.xBuf.length;
        this.byteCount += this.xBuf.length;
        break label11;
      }
      update(paramArrayOfByte[paramInt1]);
      paramInt1++;
      paramInt2--;
    }
  }
}