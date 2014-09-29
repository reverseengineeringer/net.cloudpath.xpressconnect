package org.bouncycastle2.crypto.engines;

import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.DataLengthException;
import org.bouncycastle2.crypto.StreamCipher;
import org.bouncycastle2.crypto.params.KeyParameter;
import org.bouncycastle2.crypto.params.ParametersWithIV;

public class VMPCEngine
  implements StreamCipher
{
  protected byte[] P = null;
  protected byte n = 0;
  protected byte s = 0;
  protected byte[] workingIV;
  protected byte[] workingKey;

  public String getAlgorithmName()
  {
    return "VMPC";
  }

  public void init(boolean paramBoolean, CipherParameters paramCipherParameters)
  {
    if (!(paramCipherParameters instanceof ParametersWithIV))
      throw new IllegalArgumentException("VMPC init parameters must include an IV");
    ParametersWithIV localParametersWithIV = (ParametersWithIV)paramCipherParameters;
    KeyParameter localKeyParameter = (KeyParameter)localParametersWithIV.getParameters();
    if (!(localParametersWithIV.getParameters() instanceof KeyParameter))
      throw new IllegalArgumentException("VMPC init parameters must include a key");
    this.workingIV = localParametersWithIV.getIV();
    if ((this.workingIV == null) || (this.workingIV.length < 1) || (this.workingIV.length > 768))
      throw new IllegalArgumentException("VMPC requires 1 to 768 bytes of IV");
    this.workingKey = localKeyParameter.getKey();
    initKey(this.workingKey, this.workingIV);
  }

  protected void initKey(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    this.s = 0;
    this.P = new byte[256];
    int i = 0;
    int j;
    if (i >= 256)
    {
      j = 0;
      label26: if (j < 768)
        break label65;
    }
    for (int m = 0; ; m++)
    {
      if (m >= 768)
      {
        this.n = 0;
        return;
        this.P[i] = ((byte)i);
        i++;
        break;
        label65: this.s = this.P[(0xFF & this.s + this.P[(j & 0xFF)] + paramArrayOfByte1[(j % paramArrayOfByte1.length)])];
        int k = this.P[(j & 0xFF)];
        this.P[(j & 0xFF)] = this.P[(0xFF & this.s)];
        this.P[(0xFF & this.s)] = k;
        j++;
        break label26;
      }
      this.s = this.P[(0xFF & this.s + this.P[(m & 0xFF)] + paramArrayOfByte2[(m % paramArrayOfByte2.length)])];
      int i1 = this.P[(m & 0xFF)];
      this.P[(m & 0xFF)] = this.P[(0xFF & this.s)];
      this.P[(0xFF & this.s)] = i1;
    }
  }

  public void processBytes(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3)
  {
    if (paramInt1 + paramInt2 > paramArrayOfByte1.length)
      throw new DataLengthException("input buffer too short");
    if (paramInt3 + paramInt2 > paramArrayOfByte2.length)
      throw new DataLengthException("output buffer too short");
    for (int i = 0; ; i++)
    {
      if (i >= paramInt2)
        return;
      this.s = this.P[(0xFF & this.s + this.P[(0xFF & this.n)])];
      int j = this.P[(0xFF & 1 + this.P[(0xFF & this.P[(0xFF & this.s)])])];
      int k = this.P[(0xFF & this.n)];
      this.P[(0xFF & this.n)] = this.P[(0xFF & this.s)];
      this.P[(0xFF & this.s)] = k;
      this.n = ((byte)(0xFF & 1 + this.n));
      paramArrayOfByte2[(i + paramInt3)] = ((byte)(j ^ paramArrayOfByte1[(i + paramInt1)]));
    }
  }

  public void reset()
  {
    initKey(this.workingKey, this.workingIV);
  }

  public byte returnByte(byte paramByte)
  {
    this.s = this.P[(0xFF & this.s + this.P[(0xFF & this.n)])];
    byte b = this.P[(0xFF & 1 + this.P[(0xFF & this.P[(0xFF & this.s)])])];
    int i = this.P[(0xFF & this.n)];
    this.P[(0xFF & this.n)] = this.P[(0xFF & this.s)];
    this.P[(0xFF & this.s)] = i;
    this.n = ((byte)(0xFF & 1 + this.n));
    return (byte)(paramByte ^ b);
  }
}