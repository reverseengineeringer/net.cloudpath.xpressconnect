package org.bouncycastle2.crypto.generators;

import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.Digest;
import org.bouncycastle2.crypto.PBEParametersGenerator;
import org.bouncycastle2.crypto.params.KeyParameter;
import org.bouncycastle2.crypto.params.ParametersWithIV;

public class PKCS5S1ParametersGenerator extends PBEParametersGenerator
{
  private Digest digest;

  public PKCS5S1ParametersGenerator(Digest paramDigest)
  {
    this.digest = paramDigest;
  }

  private byte[] generateDerivedKey()
  {
    byte[] arrayOfByte = new byte[this.digest.getDigestSize()];
    this.digest.update(this.password, 0, this.password.length);
    this.digest.update(this.salt, 0, this.salt.length);
    this.digest.doFinal(arrayOfByte, 0);
    for (int i = 1; ; i++)
    {
      if (i >= this.iterationCount)
        return arrayOfByte;
      this.digest.update(arrayOfByte, 0, arrayOfByte.length);
      this.digest.doFinal(arrayOfByte, 0);
    }
  }

  public CipherParameters generateDerivedMacParameters(int paramInt)
  {
    return generateDerivedParameters(paramInt);
  }

  public CipherParameters generateDerivedParameters(int paramInt)
  {
    int i = paramInt / 8;
    if (i > this.digest.getDigestSize())
      throw new IllegalArgumentException("Can't generate a derived key " + i + " bytes long.");
    return new KeyParameter(generateDerivedKey(), 0, i);
  }

  public CipherParameters generateDerivedParameters(int paramInt1, int paramInt2)
  {
    int i = paramInt1 / 8;
    int j = paramInt2 / 8;
    if (i + j > this.digest.getDigestSize())
      throw new IllegalArgumentException("Can't generate a derived key " + (i + j) + " bytes long.");
    byte[] arrayOfByte = generateDerivedKey();
    return new ParametersWithIV(new KeyParameter(arrayOfByte, 0, i), arrayOfByte, i, j);
  }
}