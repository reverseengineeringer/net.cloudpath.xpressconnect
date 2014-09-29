package org.bouncycastle2.crypto.engines;

import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.DataLengthException;
import org.bouncycastle2.crypto.StreamCipher;
import org.bouncycastle2.crypto.params.KeyParameter;
import org.bouncycastle2.crypto.params.ParametersWithIV;

public class Grain128Engine
  implements StreamCipher
{
  private static final int STATE_SIZE = 4;
  private int index = 4;
  private boolean initialised = false;
  private int[] lfsr;
  private int[] nfsr;
  private byte[] out;
  private int output;
  private byte[] workingIV;
  private byte[] workingKey;

  private byte getKeyStream()
  {
    if (this.index > 3)
    {
      oneRound();
      this.index = 0;
    }
    byte[] arrayOfByte = this.out;
    int i = this.index;
    this.index = (i + 1);
    return arrayOfByte[i];
  }

  private int getOutput()
  {
    int i = this.nfsr[0] >>> 2 | this.nfsr[1] << 30;
    int j = this.nfsr[0] >>> 12 | this.nfsr[1] << 20;
    int k = this.nfsr[0] >>> 15 | this.nfsr[1] << 17;
    int m = this.nfsr[1] >>> 4 | this.nfsr[2] << 28;
    int n = this.nfsr[1] >>> 13 | this.nfsr[2] << 19;
    int i1 = this.nfsr[2];
    int i2 = this.nfsr[2] >>> 9 | this.nfsr[3] << 23;
    int i3 = this.nfsr[2] >>> 25 | this.nfsr[3] << 7;
    int i4 = this.nfsr[2] >>> 31 | this.nfsr[3] << 1;
    int i5 = this.lfsr[0] >>> 8 | this.lfsr[1] << 24;
    int i6 = this.lfsr[0] >>> 13 | this.lfsr[1] << 19;
    int i7 = this.lfsr[0] >>> 20 | this.lfsr[1] << 12;
    int i8 = this.lfsr[1] >>> 10 | this.lfsr[2] << 22;
    int i9 = this.lfsr[1] >>> 28 | this.lfsr[2] << 4;
    int i10 = this.lfsr[2] >>> 15 | this.lfsr[3] << 17;
    int i11 = this.lfsr[2] >>> 29 | this.lfsr[3] << 3;
    int i12 = this.lfsr[2] >>> 31 | this.lfsr[3] << 1;
    return i3 ^ (i2 ^ (i1 ^ (n ^ (m ^ (k ^ (i ^ (i11 ^ (j & i5 ^ i6 & i7 ^ i4 & i8 ^ i9 & i10 ^ i12 & (j & i4)))))))));
  }

  private int getOutputLFSR()
  {
    int i = this.lfsr[0];
    int j = this.lfsr[0] >>> 7 | this.lfsr[1] << 25;
    int k = this.lfsr[1] >>> 6 | this.lfsr[2] << 26;
    int m = this.lfsr[2] >>> 6 | this.lfsr[3] << 26;
    int n = this.lfsr[2] >>> 17 | this.lfsr[3] << 15;
    return this.lfsr[3] ^ (n ^ (m ^ (k ^ (i ^ j))));
  }

  private int getOutputNFSR()
  {
    int i = this.nfsr[0];
    int j = this.nfsr[0] >>> 3 | this.nfsr[1] << 29;
    int k = this.nfsr[0] >>> 11 | this.nfsr[1] << 21;
    int m = this.nfsr[0] >>> 13 | this.nfsr[1] << 19;
    int n = this.nfsr[0] >>> 17 | this.nfsr[1] << 15;
    int i1 = this.nfsr[0] >>> 18 | this.nfsr[1] << 14;
    int i2 = this.nfsr[0] >>> 26 | this.nfsr[1] << 6;
    int i3 = this.nfsr[0] >>> 27 | this.nfsr[1] << 5;
    int i4 = this.nfsr[1] >>> 8 | this.nfsr[2] << 24;
    int i5 = this.nfsr[1] >>> 16 | this.nfsr[2] << 16;
    int i6 = this.nfsr[1] >>> 24 | this.nfsr[2] << 8;
    int i7 = this.nfsr[1] >>> 27 | this.nfsr[2] << 5;
    int i8 = this.nfsr[1] >>> 29 | this.nfsr[2] << 3;
    int i9 = this.nfsr[2] >>> 1 | this.nfsr[3] << 31;
    int i10 = this.nfsr[2] >>> 3 | this.nfsr[3] << 29;
    int i11 = this.nfsr[2] >>> 4 | this.nfsr[3] << 28;
    int i12 = this.nfsr[2] >>> 20 | this.nfsr[3] << 12;
    int i13 = this.nfsr[2] >>> 27 | this.nfsr[3] << 5;
    return this.nfsr[3] ^ (i13 ^ (i6 ^ (i ^ i2))) ^ j & i10 ^ k & m ^ n & i1 ^ i3 & i7 ^ i4 & i5 ^ i8 & i9 ^ i11 & i12;
  }

  private void initGrain()
  {
    for (int i = 0; ; i++)
    {
      if (i >= 8)
      {
        this.initialised = true;
        return;
      }
      this.output = getOutput();
      this.nfsr = shift(this.nfsr, getOutputNFSR() ^ this.lfsr[0] ^ this.output);
      this.lfsr = shift(this.lfsr, getOutputLFSR() ^ this.output);
    }
  }

  private void oneRound()
  {
    this.output = getOutput();
    this.out[0] = ((byte)this.output);
    this.out[1] = ((byte)(this.output >> 8));
    this.out[2] = ((byte)(this.output >> 16));
    this.out[3] = ((byte)(this.output >> 24));
    this.nfsr = shift(this.nfsr, getOutputNFSR() ^ this.lfsr[0]);
    this.lfsr = shift(this.lfsr, getOutputLFSR());
  }

  private void setKey(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    paramArrayOfByte2[12] = -1;
    paramArrayOfByte2[13] = -1;
    paramArrayOfByte2[14] = -1;
    paramArrayOfByte2[15] = -1;
    this.workingKey = paramArrayOfByte1;
    this.workingIV = paramArrayOfByte2;
    int i = 0;
    for (int j = 0; ; j++)
    {
      if (j >= this.nfsr.length)
        return;
      this.nfsr[j] = (this.workingKey[(i + 3)] << 24 | 0xFF0000 & this.workingKey[(i + 2)] << 16 | 0xFF00 & this.workingKey[(i + 1)] << 8 | 0xFF & this.workingKey[i]);
      this.lfsr[j] = (this.workingIV[(i + 3)] << 24 | 0xFF0000 & this.workingIV[(i + 2)] << 16 | 0xFF00 & this.workingIV[(i + 1)] << 8 | 0xFF & this.workingIV[i]);
      i += 4;
    }
  }

  private int[] shift(int[] paramArrayOfInt, int paramInt)
  {
    paramArrayOfInt[0] = paramArrayOfInt[1];
    paramArrayOfInt[1] = paramArrayOfInt[2];
    paramArrayOfInt[2] = paramArrayOfInt[3];
    paramArrayOfInt[3] = paramInt;
    return paramArrayOfInt;
  }

  public String getAlgorithmName()
  {
    return "Grain-128";
  }

  public void init(boolean paramBoolean, CipherParameters paramCipherParameters)
    throws IllegalArgumentException
  {
    if (!(paramCipherParameters instanceof ParametersWithIV))
      throw new IllegalArgumentException("Grain-128 Init parameters must include an IV");
    ParametersWithIV localParametersWithIV = (ParametersWithIV)paramCipherParameters;
    byte[] arrayOfByte = localParametersWithIV.getIV();
    if ((arrayOfByte == null) || (arrayOfByte.length != 12))
      throw new IllegalArgumentException("Grain-128  requires exactly 12 bytes of IV");
    if (!(localParametersWithIV.getParameters() instanceof KeyParameter))
      throw new IllegalArgumentException("Grain-128 Init parameters must include a key");
    KeyParameter localKeyParameter = (KeyParameter)localParametersWithIV.getParameters();
    this.workingIV = new byte[localKeyParameter.getKey().length];
    this.workingKey = new byte[localKeyParameter.getKey().length];
    this.lfsr = new int[4];
    this.nfsr = new int[4];
    this.out = new byte[4];
    System.arraycopy(arrayOfByte, 0, this.workingIV, 0, arrayOfByte.length);
    System.arraycopy(localKeyParameter.getKey(), 0, this.workingKey, 0, localKeyParameter.getKey().length);
    setKey(this.workingKey, this.workingIV);
    initGrain();
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
      paramArrayOfByte2[(paramInt3 + i)] = ((byte)(paramArrayOfByte1[(paramInt1 + i)] ^ getKeyStream()));
    }
  }

  public void reset()
  {
    this.index = 4;
    setKey(this.workingKey, this.workingIV);
    initGrain();
  }

  public byte returnByte(byte paramByte)
  {
    if (!this.initialised)
      throw new IllegalStateException(getAlgorithmName() + " not initialised");
    return (byte)(paramByte ^ getKeyStream());
  }
}