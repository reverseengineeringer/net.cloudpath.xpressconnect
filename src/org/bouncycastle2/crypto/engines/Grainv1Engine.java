package org.bouncycastle2.crypto.engines;

import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.DataLengthException;
import org.bouncycastle2.crypto.StreamCipher;
import org.bouncycastle2.crypto.params.KeyParameter;
import org.bouncycastle2.crypto.params.ParametersWithIV;

public class Grainv1Engine
  implements StreamCipher
{
  private static final int STATE_SIZE = 5;
  private int index = 2;
  private boolean initialised = false;
  private int[] lfsr;
  private int[] nfsr;
  private byte[] out;
  private int output;
  private byte[] workingIV;
  private byte[] workingKey;

  private byte getKeyStream()
  {
    if (this.index > 1)
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
    int i = this.nfsr[0] >>> 1 | this.nfsr[1] << 15;
    int j = this.nfsr[0] >>> 2 | this.nfsr[1] << 14;
    int k = this.nfsr[0] >>> 4 | this.nfsr[1] << 12;
    int m = this.nfsr[0] >>> 10 | this.nfsr[1] << 6;
    int n = this.nfsr[1] >>> 15 | this.nfsr[2] << 1;
    int i1 = this.nfsr[2] >>> 11 | this.nfsr[3] << 5;
    int i2 = this.nfsr[3] >>> 8 | this.nfsr[4] << 8;
    int i3 = this.nfsr[3] >>> 15 | this.nfsr[4] << 1;
    int i4 = this.lfsr[0] >>> 3 | this.lfsr[1] << 13;
    int i5 = this.lfsr[1] >>> 9 | this.lfsr[2] << 7;
    int i6 = this.lfsr[2] >>> 14 | this.lfsr[3] << 2;
    int i7 = this.lfsr[4];
    return 0xFFFF & (i2 ^ (i1 ^ (n ^ (m ^ (k ^ (j ^ (i ^ (i5 ^ i3 ^ i4 & i7 ^ i6 & i7 ^ i7 & i3 ^ i6 & (i4 & i5) ^ i7 & (i4 & i6) ^ i3 & (i4 & i6) ^ i3 & (i5 & i6) ^ i3 & (i6 & i7)))))))));
  }

  private int getOutputLFSR()
  {
    int i = this.lfsr[0];
    int j = this.lfsr[0] >>> 13 | this.lfsr[1] << 3;
    int k = this.lfsr[1] >>> 7 | this.lfsr[2] << 9;
    int m = this.lfsr[2] >>> 6 | this.lfsr[3] << 10;
    int n = this.lfsr[3] >>> 3 | this.lfsr[4] << 13;
    return 0xFFFF & ((this.lfsr[3] >>> 14 | this.lfsr[4] << 2) ^ (n ^ (m ^ (k ^ (i ^ j)))));
  }

  private int getOutputNFSR()
  {
    int i = this.nfsr[0];
    int j = this.nfsr[0] >>> 9 | this.nfsr[1] << 7;
    int k = this.nfsr[0] >>> 14 | this.nfsr[1] << 2;
    int m = this.nfsr[0] >>> 15 | this.nfsr[1] << 1;
    int n = this.nfsr[1] >>> 5 | this.nfsr[2] << 11;
    int i1 = this.nfsr[1] >>> 12 | this.nfsr[2] << 4;
    int i2 = this.nfsr[2] >>> 1 | this.nfsr[3] << 15;
    int i3 = this.nfsr[2] >>> 5 | this.nfsr[3] << 11;
    int i4 = this.nfsr[2] >>> 13 | this.nfsr[3] << 3;
    int i5 = this.nfsr[3] >>> 4 | this.nfsr[4] << 12;
    int i6 = this.nfsr[3] >>> 12 | this.nfsr[4] << 4;
    int i7 = this.nfsr[3] >>> 14 | this.nfsr[4] << 2;
    int i8 = this.nfsr[3] >>> 15 | this.nfsr[4] << 1;
    return 0xFFFF & (i ^ (j ^ (k ^ (n ^ (i1 ^ (i2 ^ (i3 ^ (i4 ^ (i5 ^ (i7 ^ i6))))))))) ^ i8 & i6 ^ i3 & i2 ^ m & j ^ i4 & (i6 & i5) ^ n & (i2 & i1) ^ j & (i1 & (i8 & i4)) ^ i2 & (i3 & (i6 & i5)) ^ m & (n & (i8 & i6)) ^ i3 & (i4 & (i5 & (i8 & i6))) ^ j & (m & (n & (i2 & i1))) ^ n & (i1 & (i2 & (i3 & (i5 & i4)))));
  }

  private void initGrain()
  {
    for (int i = 0; ; i++)
    {
      if (i >= 10)
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
    this.nfsr = shift(this.nfsr, getOutputNFSR() ^ this.lfsr[0]);
    this.lfsr = shift(this.lfsr, getOutputLFSR());
  }

  private void setKey(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    paramArrayOfByte2[8] = -1;
    paramArrayOfByte2[9] = -1;
    this.workingKey = paramArrayOfByte1;
    this.workingIV = paramArrayOfByte2;
    int i = 0;
    for (int j = 0; ; j++)
    {
      if (j >= this.nfsr.length)
        return;
      this.nfsr[j] = (0xFFFF & (this.workingKey[(i + 1)] << 8 | 0xFF & this.workingKey[i]));
      this.lfsr[j] = (0xFFFF & (this.workingIV[(i + 1)] << 8 | 0xFF & this.workingIV[i]));
      i += 2;
    }
  }

  private int[] shift(int[] paramArrayOfInt, int paramInt)
  {
    paramArrayOfInt[0] = paramArrayOfInt[1];
    paramArrayOfInt[1] = paramArrayOfInt[2];
    paramArrayOfInt[2] = paramArrayOfInt[3];
    paramArrayOfInt[3] = paramArrayOfInt[4];
    paramArrayOfInt[4] = paramInt;
    return paramArrayOfInt;
  }

  public String getAlgorithmName()
  {
    return "Grain v1";
  }

  public void init(boolean paramBoolean, CipherParameters paramCipherParameters)
    throws IllegalArgumentException
  {
    if (!(paramCipherParameters instanceof ParametersWithIV))
      throw new IllegalArgumentException("Grain v1 Init parameters must include an IV");
    ParametersWithIV localParametersWithIV = (ParametersWithIV)paramCipherParameters;
    byte[] arrayOfByte = localParametersWithIV.getIV();
    if ((arrayOfByte == null) || (arrayOfByte.length != 8))
      throw new IllegalArgumentException("Grain v1 requires exactly 8 bytes of IV");
    if (!(localParametersWithIV.getParameters() instanceof KeyParameter))
      throw new IllegalArgumentException("Grain v1 Init parameters must include a key");
    KeyParameter localKeyParameter = (KeyParameter)localParametersWithIV.getParameters();
    this.workingIV = new byte[localKeyParameter.getKey().length];
    this.workingKey = new byte[localKeyParameter.getKey().length];
    this.lfsr = new int[5];
    this.nfsr = new int[5];
    this.out = new byte[2];
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
    this.index = 2;
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