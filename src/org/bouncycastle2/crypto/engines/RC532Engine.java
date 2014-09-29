package org.bouncycastle2.crypto.engines;

import org.bouncycastle2.crypto.BlockCipher;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.params.KeyParameter;
import org.bouncycastle2.crypto.params.RC5Parameters;

public class RC532Engine
  implements BlockCipher
{
  private static final int P32 = -1209970333;
  private static final int Q32 = -1640531527;
  private int[] _S = null;
  private int _noRounds = 12;
  private boolean forEncryption;

  private int bytesToWord(byte[] paramArrayOfByte, int paramInt)
  {
    return 0xFF & paramArrayOfByte[paramInt] | (0xFF & paramArrayOfByte[(paramInt + 1)]) << 8 | (0xFF & paramArrayOfByte[(paramInt + 2)]) << 16 | (0xFF & paramArrayOfByte[(paramInt + 3)]) << 24;
  }

  private int decryptBlock(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
  {
    int i = bytesToWord(paramArrayOfByte1, paramInt1);
    int j = bytesToWord(paramArrayOfByte1, paramInt1 + 4);
    for (int k = this._noRounds; ; k--)
    {
      if (k < 1)
      {
        wordToBytes(i - this._S[0], paramArrayOfByte2, paramInt2);
        wordToBytes(j - this._S[1], paramArrayOfByte2, paramInt2 + 4);
        return 8;
      }
      j = i ^ rotateRight(j - this._S[(1 + k * 2)], i);
      i = j ^ rotateRight(i - this._S[(k * 2)], j);
    }
  }

  private int encryptBlock(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
  {
    int i = bytesToWord(paramArrayOfByte1, paramInt1) + this._S[0];
    int j = bytesToWord(paramArrayOfByte1, paramInt1 + 4) + this._S[1];
    for (int k = 1; ; k++)
    {
      if (k > this._noRounds)
      {
        wordToBytes(i, paramArrayOfByte2, paramInt2);
        wordToBytes(j, paramArrayOfByte2, paramInt2 + 4);
        return 8;
      }
      i = rotateLeft(i ^ j, j) + this._S[(k * 2)];
      j = rotateLeft(j ^ i, i) + this._S[(1 + k * 2)];
    }
  }

  private int rotateLeft(int paramInt1, int paramInt2)
  {
    return paramInt1 << (paramInt2 & 0x1F) | paramInt1 >>> 32 - (paramInt2 & 0x1F);
  }

  private int rotateRight(int paramInt1, int paramInt2)
  {
    return paramInt1 >>> (paramInt2 & 0x1F) | paramInt1 << 32 - (paramInt2 & 0x1F);
  }

  private void setKey(byte[] paramArrayOfByte)
  {
    int[] arrayOfInt1 = new int[(3 + paramArrayOfByte.length) / 4];
    int i = 0;
    int k;
    label42: int m;
    label68: int n;
    int i1;
    int i2;
    int i3;
    if (i == paramArrayOfByte.length)
    {
      this._S = new int[2 * (1 + this._noRounds)];
      this._S[0] = -1209970333;
      k = 1;
      if (k < this._S.length)
        break label125;
      if (arrayOfInt1.length <= this._S.length)
        break label150;
      m = 3 * arrayOfInt1.length;
      n = 0;
      i1 = 0;
      i2 = 0;
      i3 = 0;
    }
    for (int i4 = 0; ; i4++)
    {
      if (i4 >= m)
      {
        return;
        int j = i / 4;
        arrayOfInt1[j] += ((0xFF & paramArrayOfByte[i]) << 8 * (i % 4));
        i++;
        break;
        label125: this._S[k] = (-1640531527 + this._S[(k - 1)]);
        k++;
        break label42;
        label150: m = 3 * this._S.length;
        break label68;
      }
      int[] arrayOfInt2 = this._S;
      n = rotateLeft(i1 + (n + this._S[i2]), 3);
      arrayOfInt2[i2] = n;
      i1 = rotateLeft(i1 + (n + arrayOfInt1[i3]), n + i1);
      arrayOfInt1[i3] = i1;
      i2 = (i2 + 1) % this._S.length;
      i3 = (i3 + 1) % arrayOfInt1.length;
    }
  }

  private void wordToBytes(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
  {
    paramArrayOfByte[paramInt2] = ((byte)paramInt1);
    paramArrayOfByte[(paramInt2 + 1)] = ((byte)(paramInt1 >> 8));
    paramArrayOfByte[(paramInt2 + 2)] = ((byte)(paramInt1 >> 16));
    paramArrayOfByte[(paramInt2 + 3)] = ((byte)(paramInt1 >> 24));
  }

  public String getAlgorithmName()
  {
    return "RC5-32";
  }

  public int getBlockSize()
  {
    return 8;
  }

  public void init(boolean paramBoolean, CipherParameters paramCipherParameters)
  {
    if ((paramCipherParameters instanceof RC5Parameters))
    {
      RC5Parameters localRC5Parameters = (RC5Parameters)paramCipherParameters;
      this._noRounds = localRC5Parameters.getRounds();
      setKey(localRC5Parameters.getKey());
    }
    while (true)
    {
      this.forEncryption = paramBoolean;
      return;
      if (!(paramCipherParameters instanceof KeyParameter))
        break;
      setKey(((KeyParameter)paramCipherParameters).getKey());
    }
    throw new IllegalArgumentException("invalid parameter passed to RC532 init - " + paramCipherParameters.getClass().getName());
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