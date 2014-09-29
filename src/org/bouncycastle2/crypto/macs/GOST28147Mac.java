package org.bouncycastle2.crypto.macs;

import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.DataLengthException;
import org.bouncycastle2.crypto.Mac;
import org.bouncycastle2.crypto.params.KeyParameter;
import org.bouncycastle2.crypto.params.ParametersWithSBox;

public class GOST28147Mac
  implements Mac
{
  private byte[] S;
  private int blockSize = 8;
  private byte[] buf;
  private int bufOff;
  private boolean firstStep = true;
  private byte[] mac;
  private int macSize = 4;
  private int[] workingKey = null;

  public GOST28147Mac()
  {
    byte[] arrayOfByte = new byte[''];
    arrayOfByte[0] = 9;
    arrayOfByte[1] = 6;
    arrayOfByte[2] = 3;
    arrayOfByte[3] = 2;
    arrayOfByte[4] = 8;
    arrayOfByte[5] = 11;
    arrayOfByte[6] = 1;
    arrayOfByte[7] = 7;
    arrayOfByte[8] = 10;
    arrayOfByte[9] = 4;
    arrayOfByte[10] = 14;
    arrayOfByte[11] = 15;
    arrayOfByte[12] = 12;
    arrayOfByte[14] = 13;
    arrayOfByte[15] = 5;
    arrayOfByte[16] = 3;
    arrayOfByte[17] = 7;
    arrayOfByte[18] = 14;
    arrayOfByte[19] = 9;
    arrayOfByte[20] = 8;
    arrayOfByte[21] = 10;
    arrayOfByte[22] = 15;
    arrayOfByte[24] = 5;
    arrayOfByte[25] = 2;
    arrayOfByte[26] = 6;
    arrayOfByte[27] = 12;
    arrayOfByte[28] = 11;
    arrayOfByte[29] = 4;
    arrayOfByte[30] = 13;
    arrayOfByte[31] = 1;
    arrayOfByte[32] = 14;
    arrayOfByte[33] = 4;
    arrayOfByte[34] = 6;
    arrayOfByte[35] = 2;
    arrayOfByte[36] = 11;
    arrayOfByte[37] = 3;
    arrayOfByte[38] = 13;
    arrayOfByte[39] = 8;
    arrayOfByte[40] = 12;
    arrayOfByte[41] = 15;
    arrayOfByte[42] = 5;
    arrayOfByte[43] = 10;
    arrayOfByte[45] = 7;
    arrayOfByte[46] = 1;
    arrayOfByte[47] = 9;
    arrayOfByte[48] = 14;
    arrayOfByte[49] = 7;
    arrayOfByte[50] = 10;
    arrayOfByte[51] = 12;
    arrayOfByte[52] = 13;
    arrayOfByte[53] = 1;
    arrayOfByte[54] = 3;
    arrayOfByte[55] = 9;
    arrayOfByte[57] = 2;
    arrayOfByte[58] = 11;
    arrayOfByte[59] = 4;
    arrayOfByte[60] = 15;
    arrayOfByte[61] = 8;
    arrayOfByte[62] = 5;
    arrayOfByte[63] = 6;
    arrayOfByte[64] = 11;
    arrayOfByte[65] = 5;
    arrayOfByte[66] = 1;
    arrayOfByte[67] = 9;
    arrayOfByte[68] = 8;
    arrayOfByte[69] = 13;
    arrayOfByte[70] = 15;
    arrayOfByte[72] = 14;
    arrayOfByte[73] = 4;
    arrayOfByte[74] = 2;
    arrayOfByte[75] = 3;
    arrayOfByte[76] = 12;
    arrayOfByte[77] = 7;
    arrayOfByte[78] = 10;
    arrayOfByte[79] = 6;
    arrayOfByte[80] = 3;
    arrayOfByte[81] = 10;
    arrayOfByte[82] = 13;
    arrayOfByte[83] = 12;
    arrayOfByte[84] = 1;
    arrayOfByte[85] = 2;
    arrayOfByte[87] = 11;
    arrayOfByte[88] = 7;
    arrayOfByte[89] = 5;
    arrayOfByte[90] = 9;
    arrayOfByte[91] = 4;
    arrayOfByte[92] = 8;
    arrayOfByte[93] = 15;
    arrayOfByte[94] = 14;
    arrayOfByte[95] = 6;
    arrayOfByte[96] = 1;
    arrayOfByte[97] = 13;
    arrayOfByte[98] = 2;
    arrayOfByte[99] = 9;
    arrayOfByte[100] = 7;
    arrayOfByte[101] = 10;
    arrayOfByte[102] = 6;
    arrayOfByte[104] = 8;
    arrayOfByte[105] = 12;
    arrayOfByte[106] = 4;
    arrayOfByte[107] = 5;
    arrayOfByte[108] = 15;
    arrayOfByte[109] = 3;
    arrayOfByte[110] = 11;
    arrayOfByte[111] = 14;
    arrayOfByte[112] = 11;
    arrayOfByte[113] = 10;
    arrayOfByte[114] = 15;
    arrayOfByte[115] = 5;
    arrayOfByte[117] = 12;
    arrayOfByte[118] = 14;
    arrayOfByte[119] = 8;
    arrayOfByte[120] = 6;
    arrayOfByte[121] = 2;
    arrayOfByte[122] = 3;
    arrayOfByte[123] = 9;
    arrayOfByte[124] = 1;
    arrayOfByte[125] = 7;
    arrayOfByte[126] = 13;
    arrayOfByte[127] = 4;
    this.S = arrayOfByte;
    this.mac = new byte[this.blockSize];
    this.buf = new byte[this.blockSize];
    this.bufOff = 0;
  }

  private byte[] CM5func(byte[] paramArrayOfByte1, int paramInt, byte[] paramArrayOfByte2)
  {
    byte[] arrayOfByte = new byte[paramArrayOfByte1.length - paramInt];
    System.arraycopy(paramArrayOfByte1, paramInt, arrayOfByte, 0, paramArrayOfByte2.length);
    for (int i = 0; ; i++)
    {
      if (i == paramArrayOfByte2.length)
        return arrayOfByte;
      arrayOfByte[i] = ((byte)(arrayOfByte[i] ^ paramArrayOfByte2[i]));
    }
  }

  private int bytesToint(byte[] paramArrayOfByte, int paramInt)
  {
    return (0xFF000000 & paramArrayOfByte[(paramInt + 3)] << 24) + (0xFF0000 & paramArrayOfByte[(paramInt + 2)] << 16) + (0xFF00 & paramArrayOfByte[(paramInt + 1)] << 8) + (0xFF & paramArrayOfByte[paramInt]);
  }

  private int[] generateWorkingKey(byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte.length != 32)
      throw new IllegalArgumentException("Key length invalid. Key needs to be 32 byte - 256 bit!!!");
    int[] arrayOfInt = new int[8];
    for (int i = 0; ; i++)
    {
      if (i == 8)
        return arrayOfInt;
      arrayOfInt[i] = bytesToint(paramArrayOfByte, i * 4);
    }
  }

  private void gost28147MacFunc(int[] paramArrayOfInt, byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
  {
    int i = bytesToint(paramArrayOfByte1, paramInt1);
    int j = bytesToint(paramArrayOfByte1, paramInt1 + 4);
    int k = 0;
    if (k >= 2)
    {
      intTobytes(i, paramArrayOfByte2, paramInt2);
      intTobytes(j, paramArrayOfByte2, paramInt2 + 4);
      return;
    }
    for (int m = 0; ; m++)
    {
      if (m >= 8)
      {
        k++;
        break;
      }
      int n = i;
      i = j ^ gost28147_mainStep(i, paramArrayOfInt[m]);
      j = n;
    }
  }

  private int gost28147_mainStep(int paramInt1, int paramInt2)
  {
    int i = paramInt2 + paramInt1;
    int j = (this.S[(0 + (0xF & i >> 0))] << 0) + (this.S[(16 + (0xF & i >> 4))] << 4) + (this.S[(32 + (0xF & i >> 8))] << 8) + (this.S[(48 + (0xF & i >> 12))] << 12) + (this.S[(64 + (0xF & i >> 16))] << 16) + (this.S[(80 + (0xF & i >> 20))] << 20) + (this.S[(96 + (0xF & i >> 24))] << 24) + (this.S[(112 + (0xF & i >> 28))] << 28);
    return j << 11 | j >>> 21;
  }

  private void intTobytes(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
  {
    paramArrayOfByte[(paramInt2 + 3)] = ((byte)(paramInt1 >>> 24));
    paramArrayOfByte[(paramInt2 + 2)] = ((byte)(paramInt1 >>> 16));
    paramArrayOfByte[(paramInt2 + 1)] = ((byte)(paramInt1 >>> 8));
    paramArrayOfByte[paramInt2] = ((byte)paramInt1);
  }

  public int doFinal(byte[] paramArrayOfByte, int paramInt)
    throws DataLengthException, IllegalStateException
  {
    byte[] arrayOfByte;
    if (this.bufOff >= this.blockSize)
    {
      arrayOfByte = new byte[this.buf.length];
      System.arraycopy(this.buf, 0, arrayOfByte, 0, this.mac.length);
      if (!this.firstStep)
        break label118;
      this.firstStep = false;
    }
    while (true)
    {
      gost28147MacFunc(this.workingKey, arrayOfByte, 0, this.mac, 0);
      System.arraycopy(this.mac, this.mac.length / 2 - this.macSize, paramArrayOfByte, paramInt, this.macSize);
      reset();
      return this.macSize;
      this.buf[this.bufOff] = 0;
      this.bufOff = (1 + this.bufOff);
      break;
      label118: arrayOfByte = CM5func(this.buf, 0, this.mac);
    }
  }

  public String getAlgorithmName()
  {
    return "GOST28147Mac";
  }

  public int getMacSize()
  {
    return this.macSize;
  }

  public void init(CipherParameters paramCipherParameters)
    throws IllegalArgumentException
  {
    reset();
    this.buf = new byte[this.blockSize];
    if ((paramCipherParameters instanceof ParametersWithSBox))
    {
      ParametersWithSBox localParametersWithSBox = (ParametersWithSBox)paramCipherParameters;
      System.arraycopy(localParametersWithSBox.getSBox(), 0, this.S, 0, localParametersWithSBox.getSBox().length);
      if (localParametersWithSBox.getParameters() != null)
        this.workingKey = generateWorkingKey(((KeyParameter)localParametersWithSBox.getParameters()).getKey());
      return;
    }
    if ((paramCipherParameters instanceof KeyParameter))
    {
      this.workingKey = generateWorkingKey(((KeyParameter)paramCipherParameters).getKey());
      return;
    }
    throw new IllegalArgumentException("invalid parameter passed to GOST28147 init - " + paramCipherParameters.getClass().getName());
  }

  public void reset()
  {
    for (int i = 0; ; i++)
    {
      if (i >= this.buf.length)
      {
        this.bufOff = 0;
        this.firstStep = true;
        return;
      }
      this.buf[i] = 0;
    }
  }

  public void update(byte paramByte)
    throws IllegalStateException
  {
    byte[] arrayOfByte2;
    if (this.bufOff == this.buf.length)
    {
      arrayOfByte2 = new byte[this.buf.length];
      System.arraycopy(this.buf, 0, arrayOfByte2, 0, this.mac.length);
      if (!this.firstStep)
        break label92;
      this.firstStep = false;
    }
    while (true)
    {
      gost28147MacFunc(this.workingKey, arrayOfByte2, 0, this.mac, 0);
      this.bufOff = 0;
      byte[] arrayOfByte1 = this.buf;
      int i = this.bufOff;
      this.bufOff = (i + 1);
      arrayOfByte1[i] = paramByte;
      return;
      label92: arrayOfByte2 = CM5func(this.buf, 0, this.mac);
    }
  }

  public void update(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws DataLengthException, IllegalStateException
  {
    if (paramInt2 < 0)
      throw new IllegalArgumentException("Can't have a negative input length!");
    int i = this.blockSize - this.bufOff;
    byte[] arrayOfByte1;
    if (paramInt2 > i)
    {
      System.arraycopy(paramArrayOfByte, paramInt1, this.buf, this.bufOff, i);
      arrayOfByte1 = new byte[this.buf.length];
      System.arraycopy(this.buf, 0, arrayOfByte1, 0, this.mac.length);
      if (!this.firstStep)
        break label147;
      this.firstStep = false;
      gost28147MacFunc(this.workingKey, arrayOfByte1, 0, this.mac, 0);
      this.bufOff = 0;
      paramInt2 -= i;
      paramInt1 += i;
    }
    while (true)
    {
      if (paramInt2 <= this.blockSize)
      {
        System.arraycopy(paramArrayOfByte, paramInt1, this.buf, this.bufOff, paramInt2);
        this.bufOff = (paramInt2 + this.bufOff);
        return;
        label147: arrayOfByte1 = CM5func(this.buf, 0, this.mac);
        break;
      }
      byte[] arrayOfByte2 = CM5func(paramArrayOfByte, paramInt1, this.mac);
      gost28147MacFunc(this.workingKey, arrayOfByte2, 0, this.mac, 0);
      paramInt2 -= this.blockSize;
      paramInt1 += this.blockSize;
    }
  }
}