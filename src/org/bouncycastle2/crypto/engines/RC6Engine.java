package org.bouncycastle2.crypto.engines;

import org.bouncycastle2.crypto.BlockCipher;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.DataLengthException;
import org.bouncycastle2.crypto.params.KeyParameter;

public class RC6Engine
  implements BlockCipher
{
  private static final int LGW = 5;
  private static final int P32 = -1209970333;
  private static final int Q32 = -1640531527;
  private static final int _noRounds = 20;
  private static final int bytesPerWord = 4;
  private static final int wordSize = 32;
  private int[] _S = null;
  private boolean forEncryption;

  private int bytesToWord(byte[] paramArrayOfByte, int paramInt)
  {
    int i = 0;
    for (int j = 3; ; j--)
    {
      if (j < 0)
        return i;
      i = (i << 8) + (0xFF & paramArrayOfByte[(j + paramInt)]);
    }
  }

  private int decryptBlock(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
  {
    int i = bytesToWord(paramArrayOfByte1, paramInt1);
    int j = bytesToWord(paramArrayOfByte1, paramInt1 + 4);
    int k = bytesToWord(paramArrayOfByte1, paramInt1 + 8);
    int m = bytesToWord(paramArrayOfByte1, paramInt1 + 12);
    int n = k - this._S[43];
    int i1 = i - this._S[42];
    for (int i2 = 20; ; i2--)
    {
      if (i2 < 1)
      {
        int i7 = m - this._S[1];
        int i8 = j - this._S[0];
        wordToBytes(i1, paramArrayOfByte2, paramInt2);
        wordToBytes(i8, paramArrayOfByte2, paramInt2 + 4);
        wordToBytes(n, paramArrayOfByte2, paramInt2 + 8);
        wordToBytes(i7, paramArrayOfByte2, paramInt2 + 12);
        return 16;
      }
      int i3 = m;
      m = n;
      int i4 = j;
      j = i1;
      int i5 = rotateLeft(j * (1 + j * 2), 5);
      int i6 = rotateLeft(m * (1 + m * 2), 5);
      n = i6 ^ rotateRight(i4 - this._S[(1 + i2 * 2)], i5);
      i1 = i5 ^ rotateRight(i3 - this._S[(i2 * 2)], i6);
    }
  }

  private int encryptBlock(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
  {
    int i = bytesToWord(paramArrayOfByte1, paramInt1);
    int j = bytesToWord(paramArrayOfByte1, paramInt1 + 4);
    int k = bytesToWord(paramArrayOfByte1, paramInt1 + 8);
    int m = bytesToWord(paramArrayOfByte1, paramInt1 + 12);
    int n = j + this._S[0];
    int i1 = m + this._S[1];
    for (int i2 = 1; ; i2++)
    {
      if (i2 > 20)
      {
        int i7 = i + this._S[42];
        int i8 = k + this._S[43];
        wordToBytes(i7, paramArrayOfByte2, paramInt2);
        wordToBytes(n, paramArrayOfByte2, paramInt2 + 4);
        wordToBytes(i8, paramArrayOfByte2, paramInt2 + 8);
        wordToBytes(i1, paramArrayOfByte2, paramInt2 + 12);
        return 16;
      }
      int i3 = rotateLeft(n * (1 + n * 2), 5);
      int i4 = rotateLeft(i1 * (1 + i1 * 2), 5);
      int i5 = rotateLeft(i ^ i3, i4) + this._S[(i2 * 2)];
      int i6 = rotateLeft(k ^ i4, i3) + this._S[(1 + i2 * 2)];
      i = n;
      n = i6;
      k = i1;
      i1 = i5;
    }
  }

  private int rotateLeft(int paramInt1, int paramInt2)
  {
    return paramInt1 << paramInt2 | paramInt1 >>> -paramInt2;
  }

  private int rotateRight(int paramInt1, int paramInt2)
  {
    return paramInt1 >>> paramInt2 | paramInt1 << -paramInt2;
  }

  private void setKey(byte[] paramArrayOfByte)
  {
    if ((3 + paramArrayOfByte.length) / 4 == 0);
    int[] arrayOfInt1 = new int[(-1 + (4 + paramArrayOfByte.length)) / 4];
    int i = -1 + paramArrayOfByte.length;
    int j;
    label48: int k;
    label74: int m;
    int n;
    int i1;
    int i2;
    if (i < 0)
    {
      this._S = new int[44];
      this._S[0] = -1209970333;
      j = 1;
      if (j < this._S.length)
        break label124;
      if (arrayOfInt1.length <= this._S.length)
        break label149;
      k = 3 * arrayOfInt1.length;
      m = 0;
      n = 0;
      i1 = 0;
      i2 = 0;
    }
    for (int i3 = 0; ; i3++)
    {
      if (i3 >= k)
      {
        return;
        arrayOfInt1[(i / 4)] = ((arrayOfInt1[(i / 4)] << 8) + (0xFF & paramArrayOfByte[i]));
        i--;
        break;
        label124: this._S[j] = (-1640531527 + this._S[(j - 1)]);
        j++;
        break label48;
        label149: k = 3 * this._S.length;
        break label74;
      }
      int[] arrayOfInt2 = this._S;
      m = rotateLeft(n + (m + this._S[i1]), 3);
      arrayOfInt2[i1] = m;
      n = rotateLeft(n + (m + arrayOfInt1[i2]), m + n);
      arrayOfInt1[i2] = n;
      i1 = (i1 + 1) % this._S.length;
      i2 = (i2 + 1) % arrayOfInt1.length;
    }
  }

  private void wordToBytes(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
  {
    for (int i = 0; ; i++)
    {
      if (i >= 4)
        return;
      paramArrayOfByte[(i + paramInt2)] = ((byte)paramInt1);
      paramInt1 >>>= 8;
    }
  }

  public String getAlgorithmName()
  {
    return "RC6";
  }

  public int getBlockSize()
  {
    return 16;
  }

  public void init(boolean paramBoolean, CipherParameters paramCipherParameters)
  {
    if (!(paramCipherParameters instanceof KeyParameter))
      throw new IllegalArgumentException("invalid parameter passed to RC6 init - " + paramCipherParameters.getClass().getName());
    KeyParameter localKeyParameter = (KeyParameter)paramCipherParameters;
    this.forEncryption = paramBoolean;
    setKey(localKeyParameter.getKey());
  }

  public int processBlock(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
  {
    int i = getBlockSize();
    if (this._S == null)
      throw new IllegalStateException("RC6 engine not initialised");
    if (paramInt1 + i > paramArrayOfByte1.length)
      throw new DataLengthException("input buffer too short");
    if (paramInt2 + i > paramArrayOfByte2.length)
      throw new DataLengthException("output buffer too short");
    if (this.forEncryption)
      return encryptBlock(paramArrayOfByte1, paramInt1, paramArrayOfByte2, paramInt2);
    return decryptBlock(paramArrayOfByte1, paramInt1, paramArrayOfByte2, paramInt2);
  }

  public void reset()
  {
  }
}