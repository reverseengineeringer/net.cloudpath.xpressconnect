package org.bouncycastle2.crypto.engines;

import org.bouncycastle2.crypto.BlockCipher;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.params.RC5Parameters;

public class RC564Engine
  implements BlockCipher
{
  private static final long P64 = -5196783011329398165L;
  private static final long Q64 = -7046029254386353131L;
  private static final int bytesPerWord = 8;
  private static final int wordSize = 64;
  private long[] _S = null;
  private int _noRounds = 12;
  private boolean forEncryption;

  private long bytesToWord(byte[] paramArrayOfByte, int paramInt)
  {
    long l = 0L;
    for (int i = 7; ; i--)
    {
      if (i < 0)
        return l;
      l = (l << 8) + (0xFF & paramArrayOfByte[(i + paramInt)]);
    }
  }

  private int decryptBlock(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
  {
    long l1 = bytesToWord(paramArrayOfByte1, paramInt1);
    long l2 = bytesToWord(paramArrayOfByte1, paramInt1 + 8);
    for (int i = this._noRounds; ; i--)
    {
      if (i < 1)
      {
        wordToBytes(l1 - this._S[0], paramArrayOfByte2, paramInt2);
        wordToBytes(l2 - this._S[1], paramArrayOfByte2, paramInt2 + 8);
        return 16;
      }
      l2 = l1 ^ rotateRight(l2 - this._S[(1 + i * 2)], l1);
      l1 = l2 ^ rotateRight(l1 - this._S[(i * 2)], l2);
    }
  }

  private int encryptBlock(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
  {
    long l1 = bytesToWord(paramArrayOfByte1, paramInt1) + this._S[0];
    long l2 = bytesToWord(paramArrayOfByte1, paramInt1 + 8) + this._S[1];
    for (int i = 1; ; i++)
    {
      if (i > this._noRounds)
      {
        wordToBytes(l1, paramArrayOfByte2, paramInt2);
        wordToBytes(l2, paramArrayOfByte2, paramInt2 + 8);
        return 16;
      }
      l1 = rotateLeft(l1 ^ l2, l2) + this._S[(i * 2)];
      l2 = rotateLeft(l2 ^ l1, l1) + this._S[(1 + i * 2)];
    }
  }

  private long rotateLeft(long paramLong1, long paramLong2)
  {
    return paramLong1 << (int)(paramLong2 & 0x3F) | paramLong1 >>> (int)(64L - (0x3F & paramLong2));
  }

  private long rotateRight(long paramLong1, long paramLong2)
  {
    return paramLong1 >>> (int)(paramLong2 & 0x3F) | paramLong1 << (int)(64L - (0x3F & paramLong2));
  }

  private void setKey(byte[] paramArrayOfByte)
  {
    long[] arrayOfLong1 = new long[(7 + paramArrayOfByte.length) / 8];
    int i = 0;
    int k;
    label45: int m;
    label71: long l1;
    long l2;
    int n;
    int i1;
    if (i == paramArrayOfByte.length)
    {
      this._S = new long[2 * (1 + this._noRounds)];
      this._S[0] = -5196783011329398165L;
      k = 1;
      if (k < this._S.length)
        break label131;
      if (arrayOfLong1.length <= this._S.length)
        break label157;
      m = 3 * arrayOfLong1.length;
      l1 = 0L;
      l2 = 0L;
      n = 0;
      i1 = 0;
    }
    for (int i2 = 0; ; i2++)
    {
      if (i2 >= m)
      {
        return;
        int j = i / 8;
        arrayOfLong1[j] += ((0xFF & paramArrayOfByte[i]) << 8 * (i % 8));
        i++;
        break;
        label131: this._S[k] = (-7046029254386353131L + this._S[(k - 1)]);
        k++;
        break label45;
        label157: m = 3 * this._S.length;
        break label71;
      }
      long[] arrayOfLong2 = this._S;
      l1 = rotateLeft(l2 + (l1 + this._S[n]), 3L);
      arrayOfLong2[n] = l1;
      l2 = rotateLeft(l2 + (l1 + arrayOfLong1[i1]), l1 + l2);
      arrayOfLong1[i1] = l2;
      n = (n + 1) % this._S.length;
      i1 = (i1 + 1) % arrayOfLong1.length;
    }
  }

  private void wordToBytes(long paramLong, byte[] paramArrayOfByte, int paramInt)
  {
    for (int i = 0; ; i++)
    {
      if (i >= 8)
        return;
      paramArrayOfByte[(i + paramInt)] = ((byte)(int)paramLong);
      paramLong >>>= 8;
    }
  }

  public String getAlgorithmName()
  {
    return "RC5-64";
  }

  public int getBlockSize()
  {
    return 16;
  }

  public void init(boolean paramBoolean, CipherParameters paramCipherParameters)
  {
    if (!(paramCipherParameters instanceof RC5Parameters))
      throw new IllegalArgumentException("invalid parameter passed to RC564 init - " + paramCipherParameters.getClass().getName());
    RC5Parameters localRC5Parameters = (RC5Parameters)paramCipherParameters;
    this.forEncryption = paramBoolean;
    this._noRounds = localRC5Parameters.getRounds();
    setKey(localRC5Parameters.getKey());
  }

  public int processBlock(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
  {
    if (this.forEncryption)
      return encryptBlock(paramArrayOfByte1, paramInt1, paramArrayOfByte2, paramInt2);
    return decryptBlock(paramArrayOfByte1, paramInt1, paramArrayOfByte2, paramInt2);
  }

  public void reset()
  {
  }
}