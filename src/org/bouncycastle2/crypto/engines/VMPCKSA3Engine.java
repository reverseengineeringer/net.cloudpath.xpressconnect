package org.bouncycastle2.crypto.engines;

public class VMPCKSA3Engine extends VMPCEngine
{
  public String getAlgorithmName()
  {
    return "VMPC-KSA3";
  }

  protected void initKey(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    this.s = 0;
    this.P = new byte[256];
    int i = 0;
    int j;
    label26: int m;
    if (i >= 256)
    {
      j = 0;
      if (j < 768)
        break label76;
      m = 0;
      label37: if (m < 768)
        break label171;
    }
    for (int i1 = 0; ; i1++)
    {
      if (i1 >= 768)
      {
        this.n = 0;
        return;
        this.P[i] = ((byte)i);
        i++;
        break;
        label76: this.s = this.P[(0xFF & this.s + this.P[(j & 0xFF)] + paramArrayOfByte1[(j % paramArrayOfByte1.length)])];
        int k = this.P[(j & 0xFF)];
        this.P[(j & 0xFF)] = this.P[(0xFF & this.s)];
        this.P[(0xFF & this.s)] = k;
        j++;
        break label26;
        label171: this.s = this.P[(0xFF & this.s + this.P[(m & 0xFF)] + paramArrayOfByte2[(m % paramArrayOfByte2.length)])];
        int n = this.P[(m & 0xFF)];
        this.P[(m & 0xFF)] = this.P[(0xFF & this.s)];
        this.P[(0xFF & this.s)] = n;
        m++;
        break label37;
      }
      this.s = this.P[(0xFF & this.s + this.P[(i1 & 0xFF)] + paramArrayOfByte1[(i1 % paramArrayOfByte1.length)])];
      int i2 = this.P[(i1 & 0xFF)];
      this.P[(i1 & 0xFF)] = this.P[(0xFF & this.s)];
      this.P[(0xFF & this.s)] = i2;
    }
  }
}