package org.bouncycastle2.crypto.engines;

import org.bouncycastle2.crypto.BlockCipher;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.DataLengthException;
import org.bouncycastle2.crypto.params.KeyParameter;

public class XTEAEngine
  implements BlockCipher
{
  private static final int block_size = 8;
  private static final int delta = -1640531527;
  private static final int rounds = 32;
  private int[] _S = new int[4];
  private boolean _forEncryption;
  private boolean _initialised = false;
  private int[] _sum0 = new int[32];
  private int[] _sum1 = new int[32];

  private int bytesToInt(byte[] paramArrayOfByte, int paramInt)
  {
    int i = paramInt + 1;
    int j = paramArrayOfByte[paramInt] << 24;
    int k = i + 1;
    int m = j | (0xFF & paramArrayOfByte[i]) << 16;
    int n = k + 1;
    return m | (0xFF & paramArrayOfByte[k]) << 8 | 0xFF & paramArrayOfByte[n];
  }

  private int decryptBlock(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
  {
    int i = bytesToInt(paramArrayOfByte1, paramInt1);
    int j = bytesToInt(paramArrayOfByte1, paramInt1 + 4);
    for (int k = 31; ; k--)
    {
      if (k < 0)
      {
        unpackInt(i, paramArrayOfByte2, paramInt2);
        unpackInt(j, paramArrayOfByte2, paramInt2 + 4);
        return 8;
      }
      j -= (i + (i << 4 ^ i >>> 5) ^ this._sum1[k]);
      i -= (j + (j << 4 ^ j >>> 5) ^ this._sum0[k]);
    }
  }

  private int encryptBlock(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
  {
    int i = bytesToInt(paramArrayOfByte1, paramInt1);
    int j = bytesToInt(paramArrayOfByte1, paramInt1 + 4);
    for (int k = 0; ; k++)
    {
      if (k >= 32)
      {
        unpackInt(i, paramArrayOfByte2, paramInt2);
        unpackInt(j, paramArrayOfByte2, paramInt2 + 4);
        return 8;
      }
      i += (j + (j << 4 ^ j >>> 5) ^ this._sum0[k]);
      j += (i + (i << 4 ^ i >>> 5) ^ this._sum1[k]);
    }
  }

  private void setKey(byte[] paramArrayOfByte)
  {
    int i = 0;
    int j = 0;
    int k;
    if (j >= 4)
      k = 0;
    for (int m = 0; ; m++)
    {
      if (m >= 32)
      {
        return;
        this._S[j] = bytesToInt(paramArrayOfByte, i);
        j++;
        i += 4;
        break;
      }
      this._sum0[m] = (k + this._S[(k & 0x3)]);
      k -= 1640531527;
      this._sum1[m] = (k + this._S[(0x3 & k >>> 11)]);
    }
  }

  private void unpackInt(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
  {
    int i = paramInt2 + 1;
    paramArrayOfByte[paramInt2] = ((byte)(paramInt1 >>> 24));
    int j = i + 1;
    paramArrayOfByte[i] = ((byte)(paramInt1 >>> 16));
    int k = j + 1;
    paramArrayOfByte[j] = ((byte)(paramInt1 >>> 8));
    paramArrayOfByte[k] = ((byte)paramInt1);
  }

  public String getAlgorithmName()
  {
    return "XTEA";
  }

  public int getBlockSize()
  {
    return 8;
  }

  public void init(boolean paramBoolean, CipherParameters paramCipherParameters)
  {
    if (!(paramCipherParameters instanceof KeyParameter))
      throw new IllegalArgumentException("invalid parameter passed to TEA init - " + paramCipherParameters.getClass().getName());
    this._forEncryption = paramBoolean;
    this._initialised = true;
    setKey(((KeyParameter)paramCipherParameters).getKey());
  }

  public int processBlock(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
  {
    if (!this._initialised)
      throw new IllegalStateException(getAlgorithmName() + " not initialised");
    if (paramInt1 + 8 > paramArrayOfByte1.length)
      throw new DataLengthException("input buffer too short");
    if (paramInt2 + 8 > paramArrayOfByte2.length)
      throw new DataLengthException("output buffer too short");
    if (this._forEncryption)
      return encryptBlock(paramArrayOfByte1, paramInt1, paramArrayOfByte2, paramInt2);
    return decryptBlock(paramArrayOfByte1, paramInt1, paramArrayOfByte2, paramInt2);
  }

  public void reset()
  {
  }
}