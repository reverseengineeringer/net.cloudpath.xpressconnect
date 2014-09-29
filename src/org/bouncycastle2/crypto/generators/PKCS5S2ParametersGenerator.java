package org.bouncycastle2.crypto.generators;

import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.Mac;
import org.bouncycastle2.crypto.PBEParametersGenerator;
import org.bouncycastle2.crypto.digests.SHA1Digest;
import org.bouncycastle2.crypto.macs.HMac;
import org.bouncycastle2.crypto.params.KeyParameter;
import org.bouncycastle2.crypto.params.ParametersWithIV;

public class PKCS5S2ParametersGenerator extends PBEParametersGenerator
{
  private Mac hMac = new HMac(new SHA1Digest());

  private void F(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt1, byte[] paramArrayOfByte3, byte[] paramArrayOfByte4, int paramInt2)
  {
    byte[] arrayOfByte = new byte[this.hMac.getMacSize()];
    KeyParameter localKeyParameter = new KeyParameter(paramArrayOfByte1);
    this.hMac.init(localKeyParameter);
    if (paramArrayOfByte2 != null)
      this.hMac.update(paramArrayOfByte2, 0, paramArrayOfByte2.length);
    this.hMac.update(paramArrayOfByte3, 0, paramArrayOfByte3.length);
    this.hMac.doFinal(arrayOfByte, 0);
    System.arraycopy(arrayOfByte, 0, paramArrayOfByte4, paramInt2, arrayOfByte.length);
    if (paramInt1 == 0)
      throw new IllegalArgumentException("iteration count must be at least 1.");
    int i = 1;
    if (i >= paramInt1)
      return;
    this.hMac.init(localKeyParameter);
    this.hMac.update(arrayOfByte, 0, arrayOfByte.length);
    this.hMac.doFinal(arrayOfByte, 0);
    for (int j = 0; ; j++)
    {
      if (j == arrayOfByte.length)
      {
        i++;
        break;
      }
      int k = paramInt2 + j;
      paramArrayOfByte4[k] = ((byte)(paramArrayOfByte4[k] ^ arrayOfByte[j]));
    }
  }

  private byte[] generateDerivedKey(int paramInt)
  {
    int i = this.hMac.getMacSize();
    int j = (-1 + (paramInt + i)) / i;
    byte[] arrayOfByte1 = new byte[4];
    byte[] arrayOfByte2 = new byte[j * i];
    for (int k = 1; ; k++)
    {
      if (k > j)
        return arrayOfByte2;
      intToOctet(arrayOfByte1, k);
      F(this.password, this.salt, this.iterationCount, arrayOfByte1, arrayOfByte2, i * (k - 1));
    }
  }

  private void intToOctet(byte[] paramArrayOfByte, int paramInt)
  {
    paramArrayOfByte[0] = ((byte)(paramInt >>> 24));
    paramArrayOfByte[1] = ((byte)(paramInt >>> 16));
    paramArrayOfByte[2] = ((byte)(paramInt >>> 8));
    paramArrayOfByte[3] = ((byte)paramInt);
  }

  public CipherParameters generateDerivedMacParameters(int paramInt)
  {
    return generateDerivedParameters(paramInt);
  }

  public CipherParameters generateDerivedParameters(int paramInt)
  {
    int i = paramInt / 8;
    return new KeyParameter(generateDerivedKey(i), 0, i);
  }

  public CipherParameters generateDerivedParameters(int paramInt1, int paramInt2)
  {
    int i = paramInt1 / 8;
    int j = paramInt2 / 8;
    byte[] arrayOfByte = generateDerivedKey(i + j);
    return new ParametersWithIV(new KeyParameter(arrayOfByte, 0, i), arrayOfByte, i, j);
  }
}