package org.bouncycastle2.crypto.engines;

import org.bouncycastle2.crypto.BlockCipher;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.DataLengthException;
import org.bouncycastle2.crypto.params.KeyParameter;

public class NoekeonEngine
  implements BlockCipher
{
  private static final int genericSize = 16;
  private static final int[] nullVector = new int[4];
  private static final int[] roundConstants = { 128, 27, 54, 108, 216, 171, 77, 154, 47, 94, 188, 99, 198, 151, 53, 106, 212 };
  private boolean _forEncryption;
  private boolean _initialised = false;
  private int[] decryptKeys = new int[4];
  private int[] state = new int[4];
  private int[] subKeys = new int[4];

  private int bytesToIntBig(byte[] paramArrayOfByte, int paramInt)
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
    this.state[0] = bytesToIntBig(paramArrayOfByte1, paramInt1);
    this.state[1] = bytesToIntBig(paramArrayOfByte1, paramInt1 + 4);
    this.state[2] = bytesToIntBig(paramArrayOfByte1, paramInt1 + 8);
    this.state[3] = bytesToIntBig(paramArrayOfByte1, paramInt1 + 12);
    System.arraycopy(this.subKeys, 0, this.decryptKeys, 0, this.subKeys.length);
    theta(this.decryptKeys, nullVector);
    for (int i = 16; ; i--)
    {
      if (i <= 0)
      {
        theta(this.state, this.decryptKeys);
        int[] arrayOfInt2 = this.state;
        arrayOfInt2[0] ^= roundConstants[i];
        intToBytesBig(this.state[0], paramArrayOfByte2, paramInt2);
        intToBytesBig(this.state[1], paramArrayOfByte2, paramInt2 + 4);
        intToBytesBig(this.state[2], paramArrayOfByte2, paramInt2 + 8);
        intToBytesBig(this.state[3], paramArrayOfByte2, paramInt2 + 12);
        return 16;
      }
      theta(this.state, this.decryptKeys);
      int[] arrayOfInt1 = this.state;
      arrayOfInt1[0] ^= roundConstants[i];
      pi1(this.state);
      gamma(this.state);
      pi2(this.state);
    }
  }

  private int encryptBlock(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
  {
    this.state[0] = bytesToIntBig(paramArrayOfByte1, paramInt1);
    this.state[1] = bytesToIntBig(paramArrayOfByte1, paramInt1 + 4);
    this.state[2] = bytesToIntBig(paramArrayOfByte1, paramInt1 + 8);
    this.state[3] = bytesToIntBig(paramArrayOfByte1, paramInt1 + 12);
    for (int i = 0; ; i++)
    {
      if (i >= 16)
      {
        int[] arrayOfInt2 = this.state;
        arrayOfInt2[0] ^= roundConstants[i];
        theta(this.state, this.subKeys);
        intToBytesBig(this.state[0], paramArrayOfByte2, paramInt2);
        intToBytesBig(this.state[1], paramArrayOfByte2, paramInt2 + 4);
        intToBytesBig(this.state[2], paramArrayOfByte2, paramInt2 + 8);
        intToBytesBig(this.state[3], paramArrayOfByte2, paramInt2 + 12);
        return 16;
      }
      int[] arrayOfInt1 = this.state;
      arrayOfInt1[0] ^= roundConstants[i];
      theta(this.state, this.subKeys);
      pi1(this.state);
      gamma(this.state);
      pi2(this.state);
    }
  }

  private void gamma(int[] paramArrayOfInt)
  {
    paramArrayOfInt[1] ^= (0xFFFFFFFF ^ paramArrayOfInt[3]) & (0xFFFFFFFF ^ paramArrayOfInt[2]);
    paramArrayOfInt[0] ^= paramArrayOfInt[2] & paramArrayOfInt[1];
    int i = paramArrayOfInt[3];
    paramArrayOfInt[3] = paramArrayOfInt[0];
    paramArrayOfInt[0] = i;
    paramArrayOfInt[2] ^= paramArrayOfInt[0] ^ paramArrayOfInt[1] ^ paramArrayOfInt[3];
    paramArrayOfInt[1] ^= (0xFFFFFFFF ^ paramArrayOfInt[3]) & (0xFFFFFFFF ^ paramArrayOfInt[2]);
    paramArrayOfInt[0] ^= paramArrayOfInt[2] & paramArrayOfInt[1];
  }

  private void intToBytesBig(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
  {
    int i = paramInt2 + 1;
    paramArrayOfByte[paramInt2] = ((byte)(paramInt1 >>> 24));
    int j = i + 1;
    paramArrayOfByte[i] = ((byte)(paramInt1 >>> 16));
    int k = j + 1;
    paramArrayOfByte[j] = ((byte)(paramInt1 >>> 8));
    paramArrayOfByte[k] = ((byte)paramInt1);
  }

  private void pi1(int[] paramArrayOfInt)
  {
    paramArrayOfInt[1] = rotl(paramArrayOfInt[1], 1);
    paramArrayOfInt[2] = rotl(paramArrayOfInt[2], 5);
    paramArrayOfInt[3] = rotl(paramArrayOfInt[3], 2);
  }

  private void pi2(int[] paramArrayOfInt)
  {
    paramArrayOfInt[1] = rotl(paramArrayOfInt[1], 31);
    paramArrayOfInt[2] = rotl(paramArrayOfInt[2], 27);
    paramArrayOfInt[3] = rotl(paramArrayOfInt[3], 30);
  }

  private int rotl(int paramInt1, int paramInt2)
  {
    return paramInt1 << paramInt2 | paramInt1 >>> 32 - paramInt2;
  }

  private void setKey(byte[] paramArrayOfByte)
  {
    this.subKeys[0] = bytesToIntBig(paramArrayOfByte, 0);
    this.subKeys[1] = bytesToIntBig(paramArrayOfByte, 4);
    this.subKeys[2] = bytesToIntBig(paramArrayOfByte, 8);
    this.subKeys[3] = bytesToIntBig(paramArrayOfByte, 12);
  }

  private void theta(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
  {
    int i = paramArrayOfInt1[0] ^ paramArrayOfInt1[2];
    int j = i ^ (rotl(i, 8) ^ rotl(i, 24));
    paramArrayOfInt1[1] = (j ^ paramArrayOfInt1[1]);
    paramArrayOfInt1[3] = (j ^ paramArrayOfInt1[3]);
    for (int k = 0; ; k++)
    {
      if (k >= 4)
      {
        int m = paramArrayOfInt1[1] ^ paramArrayOfInt1[3];
        int n = m ^ (rotl(m, 8) ^ rotl(m, 24));
        paramArrayOfInt1[0] = (n ^ paramArrayOfInt1[0]);
        paramArrayOfInt1[2] = (n ^ paramArrayOfInt1[2]);
        return;
      }
      paramArrayOfInt1[k] ^= paramArrayOfInt2[k];
    }
  }

  public String getAlgorithmName()
  {
    return "Noekeon";
  }

  public int getBlockSize()
  {
    return 16;
  }

  public void init(boolean paramBoolean, CipherParameters paramCipherParameters)
  {
    if (!(paramCipherParameters instanceof KeyParameter))
      throw new IllegalArgumentException("invalid parameter passed to Noekeon init - " + paramCipherParameters.getClass().getName());
    this._forEncryption = paramBoolean;
    this._initialised = true;
    setKey(((KeyParameter)paramCipherParameters).getKey());
  }

  public int processBlock(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
  {
    if (!this._initialised)
      throw new IllegalStateException(getAlgorithmName() + " not initialised");
    if (paramInt1 + 16 > paramArrayOfByte1.length)
      throw new DataLengthException("input buffer too short");
    if (paramInt2 + 16 > paramArrayOfByte2.length)
      throw new DataLengthException("output buffer too short");
    if (this._forEncryption)
      return encryptBlock(paramArrayOfByte1, paramInt1, paramArrayOfByte2, paramInt2);
    return decryptBlock(paramArrayOfByte1, paramInt1, paramArrayOfByte2, paramInt2);
  }

  public void reset()
  {
  }
}