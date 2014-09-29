package org.bouncycastle2.crypto.modes;

import org.bouncycastle2.crypto.BlockCipher;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.DataLengthException;
import org.bouncycastle2.crypto.params.ParametersWithIV;

public class GOFBBlockCipher
  implements BlockCipher
{
  static final int C1 = 16843012;
  static final int C2 = 16843009;
  private byte[] IV;
  int N3;
  int N4;
  private final int blockSize;
  private final BlockCipher cipher;
  boolean firstStep = true;
  private byte[] ofbOutV;
  private byte[] ofbV;

  public GOFBBlockCipher(BlockCipher paramBlockCipher)
  {
    this.cipher = paramBlockCipher;
    this.blockSize = paramBlockCipher.getBlockSize();
    if (this.blockSize != 8)
      throw new IllegalArgumentException("GCTR only for 64 bit block ciphers");
    this.IV = new byte[paramBlockCipher.getBlockSize()];
    this.ofbV = new byte[paramBlockCipher.getBlockSize()];
    this.ofbOutV = new byte[paramBlockCipher.getBlockSize()];
  }

  private int bytesToint(byte[] paramArrayOfByte, int paramInt)
  {
    return (0xFF000000 & paramArrayOfByte[(paramInt + 3)] << 24) + (0xFF0000 & paramArrayOfByte[(paramInt + 2)] << 16) + (0xFF00 & paramArrayOfByte[(paramInt + 1)] << 8) + (0xFF & paramArrayOfByte[paramInt]);
  }

  private void intTobytes(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
  {
    paramArrayOfByte[(paramInt2 + 3)] = ((byte)(paramInt1 >>> 24));
    paramArrayOfByte[(paramInt2 + 2)] = ((byte)(paramInt1 >>> 16));
    paramArrayOfByte[(paramInt2 + 1)] = ((byte)(paramInt1 >>> 8));
    paramArrayOfByte[paramInt2] = ((byte)paramInt1);
  }

  public String getAlgorithmName()
  {
    return this.cipher.getAlgorithmName() + "/GCTR";
  }

  public int getBlockSize()
  {
    return this.blockSize;
  }

  public BlockCipher getUnderlyingCipher()
  {
    return this.cipher;
  }

  public void init(boolean paramBoolean, CipherParameters paramCipherParameters)
    throws IllegalArgumentException
  {
    this.firstStep = true;
    this.N3 = 0;
    this.N4 = 0;
    if ((paramCipherParameters instanceof ParametersWithIV))
    {
      ParametersWithIV localParametersWithIV = (ParametersWithIV)paramCipherParameters;
      byte[] arrayOfByte = localParametersWithIV.getIV();
      int i;
      if (arrayOfByte.length < this.IV.length)
      {
        System.arraycopy(arrayOfByte, 0, this.IV, this.IV.length - arrayOfByte.length, arrayOfByte.length);
        i = 0;
        if (i < this.IV.length - arrayOfByte.length);
      }
      while (true)
      {
        reset();
        this.cipher.init(true, localParametersWithIV.getParameters());
        return;
        this.IV[i] = 0;
        i++;
        break;
        System.arraycopy(arrayOfByte, 0, this.IV, 0, this.IV.length);
      }
    }
    reset();
    this.cipher.init(true, paramCipherParameters);
  }

  public int processBlock(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
    throws DataLengthException, IllegalStateException
  {
    if (paramInt1 + this.blockSize > paramArrayOfByte1.length)
      throw new DataLengthException("input buffer too short");
    if (paramInt2 + this.blockSize > paramArrayOfByte2.length)
      throw new DataLengthException("output buffer too short");
    if (this.firstStep)
    {
      this.firstStep = false;
      this.cipher.processBlock(this.ofbV, 0, this.ofbOutV, 0);
      this.N3 = bytesToint(this.ofbOutV, 0);
      this.N4 = bytesToint(this.ofbOutV, 4);
    }
    this.N3 = (16843009 + this.N3);
    this.N4 = (16843012 + this.N4);
    intTobytes(this.N3, this.ofbV, 0);
    intTobytes(this.N4, this.ofbV, 4);
    this.cipher.processBlock(this.ofbV, 0, this.ofbOutV, 0);
    for (int i = 0; ; i++)
    {
      if (i >= this.blockSize)
      {
        System.arraycopy(this.ofbV, this.blockSize, this.ofbV, 0, this.ofbV.length - this.blockSize);
        System.arraycopy(this.ofbOutV, 0, this.ofbV, this.ofbV.length - this.blockSize, this.blockSize);
        return this.blockSize;
      }
      paramArrayOfByte2[(paramInt2 + i)] = ((byte)(this.ofbOutV[i] ^ paramArrayOfByte1[(paramInt1 + i)]));
    }
  }

  public void reset()
  {
    System.arraycopy(this.IV, 0, this.ofbV, 0, this.IV.length);
    this.cipher.reset();
  }
}