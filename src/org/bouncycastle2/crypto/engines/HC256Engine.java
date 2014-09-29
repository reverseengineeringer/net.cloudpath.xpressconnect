package org.bouncycastle2.crypto.engines;

import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.DataLengthException;
import org.bouncycastle2.crypto.StreamCipher;
import org.bouncycastle2.crypto.params.KeyParameter;
import org.bouncycastle2.crypto.params.ParametersWithIV;

public class HC256Engine
  implements StreamCipher
{
  private byte[] buf = new byte[4];
  private int cnt = 0;
  private int idx = 0;
  private boolean initialised;
  private byte[] iv;
  private byte[] key;
  private int[] p = new int[1024];
  private int[] q = new int[1024];

  private byte getByte()
  {
    if (this.idx == 0)
    {
      int i = step();
      this.buf[0] = ((byte)(i & 0xFF));
      int j = i >> 8;
      this.buf[1] = ((byte)(j & 0xFF));
      int k = j >> 8;
      this.buf[2] = ((byte)(k & 0xFF));
      int m = k >> 8;
      this.buf[3] = ((byte)(m & 0xFF));
    }
    byte b = this.buf[this.idx];
    this.idx = (0x3 & 1 + this.idx);
    return b;
  }

  private void init()
  {
    if ((this.key.length != 32) && (this.key.length != 16))
      throw new IllegalArgumentException("The key must be 128/256 bits long");
    if (this.iv.length < 16)
      throw new IllegalArgumentException("The IV must be at least 128 bits long");
    if (this.key.length != 32)
    {
      byte[] arrayOfByte2 = new byte[32];
      System.arraycopy(this.key, 0, arrayOfByte2, 0, this.key.length);
      System.arraycopy(this.key, 0, arrayOfByte2, 16, this.key.length);
      this.key = arrayOfByte2;
    }
    if (this.iv.length < 32)
    {
      byte[] arrayOfByte1 = new byte[32];
      System.arraycopy(this.iv, 0, arrayOfByte1, 0, this.iv.length);
      System.arraycopy(this.iv, 0, arrayOfByte1, this.iv.length, arrayOfByte1.length - this.iv.length);
      this.iv = arrayOfByte1;
    }
    this.cnt = 0;
    int[] arrayOfInt = new int[2560];
    int i = 0;
    int k;
    label189: int n;
    if (i >= 32)
    {
      k = 0;
      if (k < 32)
        break label289;
      n = 16;
      label200: if (n < 2560)
        break label332;
      System.arraycopy(arrayOfInt, 512, this.p, 0, 1024);
      System.arraycopy(arrayOfInt, 1536, this.q, 0, 1024);
    }
    for (int i3 = 0; ; i3++)
    {
      if (i3 >= 4096)
      {
        this.cnt = 0;
        return;
        int j = i >> 2;
        arrayOfInt[j] |= (0xFF & this.key[i]) << 8 * (i & 0x3);
        i++;
        break;
        label289: int m = 8 + (k >> 2);
        arrayOfInt[m] |= (0xFF & this.iv[k]) << 8 * (k & 0x3);
        k++;
        break label189;
        label332: int i1 = arrayOfInt[(n - 2)];
        int i2 = arrayOfInt[(n - 15)];
        arrayOfInt[n] = (n + ((rotateRight(i1, 17) ^ rotateRight(i1, 19) ^ i1 >>> 10) + arrayOfInt[(n - 7)] + (rotateRight(i2, 7) ^ rotateRight(i2, 18) ^ i2 >>> 3) + arrayOfInt[(n - 16)]));
        n++;
        break label200;
      }
      step();
    }
  }

  private static int rotateRight(int paramInt1, int paramInt2)
  {
    return paramInt1 >>> paramInt2 | paramInt1 << -paramInt2;
  }

  private int step()
  {
    int i = 0x3FF & this.cnt;
    int i3;
    if (this.cnt < 1024)
    {
      int i1 = this.p[(0x3FF & i - 3)];
      int i2 = this.p[(0x3FF & i - 1023)];
      int[] arrayOfInt2 = this.p;
      arrayOfInt2[i] += this.p[(0x3FF & i - 10)] + (rotateRight(i1, 10) ^ rotateRight(i2, 23)) + this.q[(0x3FF & (i1 ^ i2))];
      i3 = this.p[(0x3FF & i - 12)];
    }
    int m;
    for (int n = this.q[(i3 & 0xFF)] + this.q[(256 + (0xFF & i3 >> 8))] + this.q[(512 + (0xFF & i3 >> 16))] + this.q[(768 + (0xFF & i3 >> 24))] ^ this.p[i]; ; n = this.p[(m & 0xFF)] + this.p[(256 + (0xFF & m >> 8))] + this.p[(512 + (0xFF & m >> 16))] + this.p[(768 + (0xFF & m >> 24))] ^ this.q[i])
    {
      this.cnt = (0x7FF & 1 + this.cnt);
      return n;
      int j = this.q[(0x3FF & i - 3)];
      int k = this.q[(0x3FF & i - 1023)];
      int[] arrayOfInt1 = this.q;
      arrayOfInt1[i] += this.q[(0x3FF & i - 10)] + (rotateRight(j, 10) ^ rotateRight(k, 23)) + this.p[(0x3FF & (j ^ k))];
      m = this.q[(0x3FF & i - 12)];
    }
  }

  public String getAlgorithmName()
  {
    return "HC-256";
  }

  public void init(boolean paramBoolean, CipherParameters paramCipherParameters)
    throws IllegalArgumentException
  {
    CipherParameters localCipherParameters = paramCipherParameters;
    if ((paramCipherParameters instanceof ParametersWithIV))
    {
      this.iv = ((ParametersWithIV)paramCipherParameters).getIV();
      localCipherParameters = ((ParametersWithIV)paramCipherParameters).getParameters();
    }
    while ((localCipherParameters instanceof KeyParameter))
    {
      this.key = ((KeyParameter)localCipherParameters).getKey();
      init();
      this.initialised = true;
      return;
      this.iv = new byte[0];
    }
    throw new IllegalArgumentException("Invalid parameter passed to HC256 init - " + paramCipherParameters.getClass().getName());
  }

  public void processBytes(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3)
    throws DataLengthException
  {
    if (!this.initialised)
      throw new IllegalStateException(getAlgorithmName() + " not initialised");
    if (paramInt1 + paramInt2 > paramArrayOfByte1.length)
      throw new DataLengthException("input buffer too short");
    if (paramInt3 + paramInt2 > paramArrayOfByte2.length)
      throw new DataLengthException("output buffer too short");
    for (int i = 0; ; i++)
    {
      if (i >= paramInt2)
        return;
      paramArrayOfByte2[(paramInt3 + i)] = ((byte)(paramArrayOfByte1[(paramInt1 + i)] ^ getByte()));
    }
  }

  public void reset()
  {
    this.idx = 0;
    init();
  }

  public byte returnByte(byte paramByte)
  {
    return (byte)(paramByte ^ getByte());
  }
}