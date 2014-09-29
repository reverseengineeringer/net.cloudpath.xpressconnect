package org.bouncycastle2.crypto.engines;

import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.DataLengthException;
import org.bouncycastle2.crypto.MaxBytesExceededException;
import org.bouncycastle2.crypto.StreamCipher;
import org.bouncycastle2.crypto.params.KeyParameter;
import org.bouncycastle2.crypto.params.ParametersWithIV;
import org.bouncycastle2.util.Strings;

public class Salsa20Engine
  implements StreamCipher
{
  private static final byte[] sigma = Strings.toByteArray("expand 32-byte k");
  private static final int stateSize = 16;
  private static final byte[] tau = Strings.toByteArray("expand 16-byte k");
  private int cW0;
  private int cW1;
  private int cW2;
  private int[] engineState = new int[16];
  private int index = 0;
  private boolean initialised = false;
  private byte[] keyStream = new byte[64];
  private byte[] workingIV = null;
  private byte[] workingKey = null;
  private int[] x = new int[16];

  private int byteToIntLittle(byte[] paramArrayOfByte, int paramInt)
  {
    return 0xFF & paramArrayOfByte[paramInt] | (0xFF & paramArrayOfByte[(paramInt + 1)]) << 8 | (0xFF & paramArrayOfByte[(paramInt + 2)]) << 16 | paramArrayOfByte[(paramInt + 3)] << 24;
  }

  private byte[] intToByteLittle(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
  {
    paramArrayOfByte[paramInt2] = ((byte)paramInt1);
    paramArrayOfByte[(paramInt2 + 1)] = ((byte)(paramInt1 >>> 8));
    paramArrayOfByte[(paramInt2 + 2)] = ((byte)(paramInt1 >>> 16));
    paramArrayOfByte[(paramInt2 + 3)] = ((byte)(paramInt1 >>> 24));
    return paramArrayOfByte;
  }

  private boolean limitExceeded()
  {
    this.cW0 = (1 + this.cW0);
    int i = this.cW0;
    boolean bool = false;
    if (i == 0)
    {
      this.cW1 = (1 + this.cW1);
      int j = this.cW1;
      bool = false;
      if (j == 0)
      {
        this.cW2 = (1 + this.cW2);
        int k = 0x20 & this.cW2;
        bool = false;
        if (k != 0)
          bool = true;
      }
    }
    return bool;
  }

  private boolean limitExceeded(int paramInt)
  {
    if (this.cW0 >= 0)
      this.cW0 = (paramInt + this.cW0);
    do
    {
      do
      {
        do
        {
          return false;
          this.cW0 = (paramInt + this.cW0);
        }
        while (this.cW0 < 0);
        this.cW1 = (1 + this.cW1);
      }
      while (this.cW1 != 0);
      this.cW2 = (1 + this.cW2);
    }
    while ((0x20 & this.cW2) == 0);
    return true;
  }

  private void resetCounter()
  {
    this.cW0 = 0;
    this.cW1 = 0;
    this.cW2 = 0;
  }

  private int rotl(int paramInt1, int paramInt2)
  {
    return paramInt1 << paramInt2 | paramInt1 >>> -paramInt2;
  }

  private void salsa20WordToByte(int[] paramArrayOfInt, byte[] paramArrayOfByte)
  {
    System.arraycopy(paramArrayOfInt, 0, this.x, 0, paramArrayOfInt.length);
    int i = 0;
    int j;
    int k;
    if (i >= 10)
    {
      j = 0;
      k = 0;
      label26: if (k < 16)
        break label1222;
    }
    for (int m = 16; ; m++)
    {
      if (m >= this.x.length)
      {
        return;
        int[] arrayOfInt1 = this.x;
        arrayOfInt1[4] ^= rotl(this.x[0] + this.x[12], 7);
        int[] arrayOfInt2 = this.x;
        arrayOfInt2[8] ^= rotl(this.x[4] + this.x[0], 9);
        int[] arrayOfInt3 = this.x;
        arrayOfInt3[12] ^= rotl(this.x[8] + this.x[4], 13);
        int[] arrayOfInt4 = this.x;
        arrayOfInt4[0] ^= rotl(this.x[12] + this.x[8], 18);
        int[] arrayOfInt5 = this.x;
        arrayOfInt5[9] ^= rotl(this.x[5] + this.x[1], 7);
        int[] arrayOfInt6 = this.x;
        arrayOfInt6[13] ^= rotl(this.x[9] + this.x[5], 9);
        int[] arrayOfInt7 = this.x;
        arrayOfInt7[1] ^= rotl(this.x[13] + this.x[9], 13);
        int[] arrayOfInt8 = this.x;
        arrayOfInt8[5] ^= rotl(this.x[1] + this.x[13], 18);
        int[] arrayOfInt9 = this.x;
        arrayOfInt9[14] ^= rotl(this.x[10] + this.x[6], 7);
        int[] arrayOfInt10 = this.x;
        arrayOfInt10[2] ^= rotl(this.x[14] + this.x[10], 9);
        int[] arrayOfInt11 = this.x;
        arrayOfInt11[6] ^= rotl(this.x[2] + this.x[14], 13);
        int[] arrayOfInt12 = this.x;
        arrayOfInt12[10] ^= rotl(this.x[6] + this.x[2], 18);
        int[] arrayOfInt13 = this.x;
        arrayOfInt13[3] ^= rotl(this.x[15] + this.x[11], 7);
        int[] arrayOfInt14 = this.x;
        arrayOfInt14[7] ^= rotl(this.x[3] + this.x[15], 9);
        int[] arrayOfInt15 = this.x;
        arrayOfInt15[11] ^= rotl(this.x[7] + this.x[3], 13);
        int[] arrayOfInt16 = this.x;
        arrayOfInt16[15] ^= rotl(this.x[11] + this.x[7], 18);
        int[] arrayOfInt17 = this.x;
        arrayOfInt17[1] ^= rotl(this.x[0] + this.x[3], 7);
        int[] arrayOfInt18 = this.x;
        arrayOfInt18[2] ^= rotl(this.x[1] + this.x[0], 9);
        int[] arrayOfInt19 = this.x;
        arrayOfInt19[3] ^= rotl(this.x[2] + this.x[1], 13);
        int[] arrayOfInt20 = this.x;
        arrayOfInt20[0] ^= rotl(this.x[3] + this.x[2], 18);
        int[] arrayOfInt21 = this.x;
        arrayOfInt21[6] ^= rotl(this.x[5] + this.x[4], 7);
        int[] arrayOfInt22 = this.x;
        arrayOfInt22[7] ^= rotl(this.x[6] + this.x[5], 9);
        int[] arrayOfInt23 = this.x;
        arrayOfInt23[4] ^= rotl(this.x[7] + this.x[6], 13);
        int[] arrayOfInt24 = this.x;
        arrayOfInt24[5] ^= rotl(this.x[4] + this.x[7], 18);
        int[] arrayOfInt25 = this.x;
        arrayOfInt25[11] ^= rotl(this.x[10] + this.x[9], 7);
        int[] arrayOfInt26 = this.x;
        arrayOfInt26[8] ^= rotl(this.x[11] + this.x[10], 9);
        int[] arrayOfInt27 = this.x;
        arrayOfInt27[9] ^= rotl(this.x[8] + this.x[11], 13);
        int[] arrayOfInt28 = this.x;
        arrayOfInt28[10] ^= rotl(this.x[9] + this.x[8], 18);
        int[] arrayOfInt29 = this.x;
        arrayOfInt29[12] ^= rotl(this.x[15] + this.x[14], 7);
        int[] arrayOfInt30 = this.x;
        arrayOfInt30[13] ^= rotl(this.x[12] + this.x[15], 9);
        int[] arrayOfInt31 = this.x;
        arrayOfInt31[14] ^= rotl(this.x[13] + this.x[12], 13);
        int[] arrayOfInt32 = this.x;
        arrayOfInt32[15] ^= rotl(this.x[14] + this.x[13], 18);
        i++;
        break;
        label1222: intToByteLittle(this.x[k] + paramArrayOfInt[k], paramArrayOfByte, j);
        j += 4;
        k++;
        break label26;
      }
      intToByteLittle(this.x[m], paramArrayOfByte, j);
      j += 4;
    }
  }

  private void setKey(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    this.workingKey = paramArrayOfByte1;
    this.workingIV = paramArrayOfByte2;
    this.index = 0;
    resetCounter();
    this.engineState[1] = byteToIntLittle(this.workingKey, 0);
    this.engineState[2] = byteToIntLittle(this.workingKey, 4);
    this.engineState[3] = byteToIntLittle(this.workingKey, 8);
    this.engineState[4] = byteToIntLittle(this.workingKey, 12);
    byte[] arrayOfByte;
    if (this.workingKey.length == 32)
      arrayOfByte = sigma;
    for (int i = 16; ; i = 0)
    {
      this.engineState[11] = byteToIntLittle(this.workingKey, i);
      this.engineState[12] = byteToIntLittle(this.workingKey, i + 4);
      this.engineState[13] = byteToIntLittle(this.workingKey, i + 8);
      this.engineState[14] = byteToIntLittle(this.workingKey, i + 12);
      this.engineState[0] = byteToIntLittle(arrayOfByte, 0);
      this.engineState[5] = byteToIntLittle(arrayOfByte, 4);
      this.engineState[10] = byteToIntLittle(arrayOfByte, 8);
      this.engineState[15] = byteToIntLittle(arrayOfByte, 12);
      this.engineState[6] = byteToIntLittle(this.workingIV, 0);
      this.engineState[7] = byteToIntLittle(this.workingIV, 4);
      int[] arrayOfInt = this.engineState;
      this.engineState[9] = 0;
      arrayOfInt[8] = 0;
      this.initialised = true;
      return;
      arrayOfByte = tau;
    }
  }

  public String getAlgorithmName()
  {
    return "Salsa20";
  }

  public void init(boolean paramBoolean, CipherParameters paramCipherParameters)
  {
    if (!(paramCipherParameters instanceof ParametersWithIV))
      throw new IllegalArgumentException("Salsa20 Init parameters must include an IV");
    ParametersWithIV localParametersWithIV = (ParametersWithIV)paramCipherParameters;
    byte[] arrayOfByte = localParametersWithIV.getIV();
    if ((arrayOfByte == null) || (arrayOfByte.length != 8))
      throw new IllegalArgumentException("Salsa20 requires exactly 8 bytes of IV");
    if (!(localParametersWithIV.getParameters() instanceof KeyParameter))
      throw new IllegalArgumentException("Salsa20 Init parameters must include a key");
    this.workingKey = ((KeyParameter)localParametersWithIV.getParameters()).getKey();
    this.workingIV = arrayOfByte;
    setKey(this.workingKey, this.workingIV);
  }

  public void processBytes(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3)
  {
    if (!this.initialised)
      throw new IllegalStateException(getAlgorithmName() + " not initialised");
    if (paramInt1 + paramInt2 > paramArrayOfByte1.length)
      throw new DataLengthException("input buffer too short");
    if (paramInt3 + paramInt2 > paramArrayOfByte2.length)
      throw new DataLengthException("output buffer too short");
    if (limitExceeded(paramInt2))
      throw new MaxBytesExceededException("2^70 byte limit per IV would be exceeded; Change IV");
    for (int i = 0; ; i++)
    {
      if (i >= paramInt2)
        return;
      if (this.index == 0)
      {
        salsa20WordToByte(this.engineState, this.keyStream);
        int[] arrayOfInt1 = this.engineState;
        arrayOfInt1[8] = (1 + arrayOfInt1[8]);
        if (this.engineState[8] == 0)
        {
          int[] arrayOfInt2 = this.engineState;
          arrayOfInt2[9] = (1 + arrayOfInt2[9]);
        }
      }
      paramArrayOfByte2[(i + paramInt3)] = ((byte)(this.keyStream[this.index] ^ paramArrayOfByte1[(i + paramInt1)]));
      this.index = (0x3F & 1 + this.index);
    }
  }

  public void reset()
  {
    setKey(this.workingKey, this.workingIV);
  }

  public byte returnByte(byte paramByte)
  {
    if (limitExceeded())
      throw new MaxBytesExceededException("2^70 byte limit per IV; Change IV");
    if (this.index == 0)
    {
      salsa20WordToByte(this.engineState, this.keyStream);
      int[] arrayOfInt1 = this.engineState;
      arrayOfInt1[8] = (1 + arrayOfInt1[8]);
      if (this.engineState[8] == 0)
      {
        int[] arrayOfInt2 = this.engineState;
        arrayOfInt2[9] = (1 + arrayOfInt2[9]);
      }
    }
    byte b = (byte)(paramByte ^ this.keyStream[this.index]);
    this.index = (0x3F & 1 + this.index);
    return b;
  }
}