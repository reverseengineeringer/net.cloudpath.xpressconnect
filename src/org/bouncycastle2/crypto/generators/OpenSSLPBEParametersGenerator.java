package org.bouncycastle2.crypto.generators;

import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.Digest;
import org.bouncycastle2.crypto.PBEParametersGenerator;
import org.bouncycastle2.crypto.digests.MD5Digest;
import org.bouncycastle2.crypto.params.KeyParameter;
import org.bouncycastle2.crypto.params.ParametersWithIV;

public class OpenSSLPBEParametersGenerator extends PBEParametersGenerator
{
  private Digest digest = new MD5Digest();

  private byte[] generateDerivedKey(int paramInt)
  {
    byte[] arrayOfByte1 = new byte[this.digest.getDigestSize()];
    byte[] arrayOfByte2 = new byte[paramInt];
    int i = 0;
    while (true)
    {
      this.digest.update(this.password, 0, this.password.length);
      this.digest.update(this.salt, 0, this.salt.length);
      this.digest.doFinal(arrayOfByte1, 0);
      if (paramInt > arrayOfByte1.length);
      for (int j = arrayOfByte1.length; ; j = paramInt)
      {
        System.arraycopy(arrayOfByte1, 0, arrayOfByte2, i, j);
        i += j;
        paramInt -= j;
        if (paramInt != 0)
          break;
        return arrayOfByte2;
      }
      this.digest.reset();
      this.digest.update(arrayOfByte1, 0, arrayOfByte1.length);
    }
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

  public void init(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    super.init(paramArrayOfByte1, paramArrayOfByte2, 1);
  }
}