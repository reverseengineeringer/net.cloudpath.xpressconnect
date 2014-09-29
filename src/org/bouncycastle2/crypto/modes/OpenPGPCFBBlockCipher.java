package org.bouncycastle2.crypto.modes;

import org.bouncycastle2.crypto.BlockCipher;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.DataLengthException;

public class OpenPGPCFBBlockCipher
  implements BlockCipher
{
  private byte[] FR;
  private byte[] FRE;
  private byte[] IV;
  private int blockSize;
  private BlockCipher cipher;
  private int count;
  private boolean forEncryption;

  public OpenPGPCFBBlockCipher(BlockCipher paramBlockCipher)
  {
    this.cipher = paramBlockCipher;
    this.blockSize = paramBlockCipher.getBlockSize();
    this.IV = new byte[this.blockSize];
    this.FR = new byte[this.blockSize];
    this.FRE = new byte[this.blockSize];
  }

  private int decryptBlock(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
    throws DataLengthException, IllegalStateException
  {
    if (paramInt1 + this.blockSize > paramArrayOfByte1.length)
      throw new DataLengthException("input buffer too short");
    if (paramInt2 + this.blockSize > paramArrayOfByte2.length)
      throw new DataLengthException("output buffer too short");
    int k;
    if (this.count > this.blockSize)
    {
      byte b4 = paramArrayOfByte1[paramInt1];
      this.FR[(-2 + this.blockSize)] = b4;
      paramArrayOfByte2[paramInt2] = encryptByte(b4, -2 + this.blockSize);
      byte b5 = paramArrayOfByte1[(paramInt1 + 1)];
      this.FR[(-1 + this.blockSize)] = b5;
      paramArrayOfByte2[(paramInt2 + 1)] = encryptByte(b5, -1 + this.blockSize);
      this.cipher.processBlock(this.FR, 0, this.FRE, 0);
      k = 2;
      if (k < this.blockSize);
    }
    do
    {
      return this.blockSize;
      byte b6 = paramArrayOfByte1[(paramInt1 + k)];
      this.FR[(k - 2)] = b6;
      paramArrayOfByte2[(paramInt2 + k)] = encryptByte(b6, k - 2);
      k++;
      break;
      if (this.count == 0)
      {
        this.cipher.processBlock(this.FR, 0, this.FRE, 0);
        for (int j = 0; ; j++)
        {
          if (j >= this.blockSize)
          {
            this.count += this.blockSize;
            break;
          }
          this.FR[j] = paramArrayOfByte1[(paramInt1 + j)];
          paramArrayOfByte2[j] = encryptByte(paramArrayOfByte1[(paramInt1 + j)], j);
        }
      }
    }
    while (this.count != this.blockSize);
    this.cipher.processBlock(this.FR, 0, this.FRE, 0);
    byte b1 = paramArrayOfByte1[paramInt1];
    byte b2 = paramArrayOfByte1[(paramInt1 + 1)];
    paramArrayOfByte2[paramInt2] = encryptByte(b1, 0);
    paramArrayOfByte2[(paramInt2 + 1)] = encryptByte(b2, 1);
    System.arraycopy(this.FR, 2, this.FR, 0, -2 + this.blockSize);
    this.FR[(-2 + this.blockSize)] = b1;
    this.FR[(-1 + this.blockSize)] = b2;
    this.cipher.processBlock(this.FR, 0, this.FRE, 0);
    for (int i = 2; ; i++)
    {
      if (i >= this.blockSize)
      {
        this.count += this.blockSize;
        break;
      }
      byte b3 = paramArrayOfByte1[(paramInt1 + i)];
      this.FR[(i - 2)] = b3;
      paramArrayOfByte2[(paramInt2 + i)] = encryptByte(b3, i - 2);
    }
  }

  private int encryptBlock(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
    throws DataLengthException, IllegalStateException
  {
    if (paramInt1 + this.blockSize > paramArrayOfByte1.length)
      throw new DataLengthException("input buffer too short");
    if (paramInt2 + this.blockSize > paramArrayOfByte2.length)
      throw new DataLengthException("output buffer too short");
    int i8;
    if (this.count > this.blockSize)
    {
      byte[] arrayOfByte3 = this.FR;
      int i3 = -2 + this.blockSize;
      int i4 = encryptByte(paramArrayOfByte1[paramInt1], -2 + this.blockSize);
      paramArrayOfByte2[paramInt2] = i4;
      arrayOfByte3[i3] = i4;
      byte[] arrayOfByte4 = this.FR;
      int i5 = -1 + this.blockSize;
      int i6 = paramInt2 + 1;
      int i7 = encryptByte(paramArrayOfByte1[(paramInt1 + 1)], -1 + this.blockSize);
      paramArrayOfByte2[i6] = i7;
      arrayOfByte4[i5] = i7;
      this.cipher.processBlock(this.FR, 0, this.FRE, 0);
      i8 = 2;
      if (i8 < this.blockSize);
    }
    do
    {
      return this.blockSize;
      byte[] arrayOfByte5 = this.FR;
      int i9 = i8 - 2;
      int i10 = paramInt2 + i8;
      int i11 = encryptByte(paramArrayOfByte1[(paramInt1 + i8)], i8 - 2);
      paramArrayOfByte2[i10] = i11;
      arrayOfByte5[i9] = i11;
      i8++;
      break;
      if (this.count == 0)
      {
        this.cipher.processBlock(this.FR, 0, this.FRE, 0);
        for (int n = 0; ; n++)
        {
          if (n >= this.blockSize)
          {
            this.count += this.blockSize;
            break;
          }
          byte[] arrayOfByte2 = this.FR;
          int i1 = paramInt2 + n;
          int i2 = encryptByte(paramArrayOfByte1[(paramInt1 + n)], n);
          paramArrayOfByte2[i1] = i2;
          arrayOfByte2[n] = i2;
        }
      }
    }
    while (this.count != this.blockSize);
    this.cipher.processBlock(this.FR, 0, this.FRE, 0);
    paramArrayOfByte2[paramInt2] = encryptByte(paramArrayOfByte1[paramInt1], 0);
    paramArrayOfByte2[(paramInt2 + 1)] = encryptByte(paramArrayOfByte1[(paramInt1 + 1)], 1);
    System.arraycopy(this.FR, 2, this.FR, 0, -2 + this.blockSize);
    System.arraycopy(paramArrayOfByte2, paramInt2, this.FR, -2 + this.blockSize, 2);
    this.cipher.processBlock(this.FR, 0, this.FRE, 0);
    for (int i = 2; ; i++)
    {
      if (i >= this.blockSize)
      {
        this.count += this.blockSize;
        break;
      }
      byte[] arrayOfByte1 = this.FR;
      int j = i - 2;
      int k = paramInt2 + i;
      int m = encryptByte(paramArrayOfByte1[(paramInt1 + i)], i - 2);
      paramArrayOfByte2[k] = m;
      arrayOfByte1[j] = m;
    }
  }

  private byte encryptByte(byte paramByte, int paramInt)
  {
    return (byte)(paramByte ^ this.FRE[paramInt]);
  }

  public String getAlgorithmName()
  {
    return this.cipher.getAlgorithmName() + "/OpenPGPCFB";
  }

  public int getBlockSize()
  {
    return this.cipher.getBlockSize();
  }

  public BlockCipher getUnderlyingCipher()
  {
    return this.cipher;
  }

  public void init(boolean paramBoolean, CipherParameters paramCipherParameters)
    throws IllegalArgumentException
  {
    this.forEncryption = paramBoolean;
    reset();
    this.cipher.init(true, paramCipherParameters);
  }

  public int processBlock(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
    throws DataLengthException, IllegalStateException
  {
    if (this.forEncryption)
      return encryptBlock(paramArrayOfByte1, paramInt1, paramArrayOfByte2, paramInt2);
    return decryptBlock(paramArrayOfByte1, paramInt1, paramArrayOfByte2, paramInt2);
  }

  public void reset()
  {
    this.count = 0;
    System.arraycopy(this.IV, 0, this.FR, 0, this.FR.length);
    this.cipher.reset();
  }
}