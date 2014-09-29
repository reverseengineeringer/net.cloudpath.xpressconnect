package org.bouncycastle2.crypto.generators;

import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.Digest;
import org.bouncycastle2.crypto.ExtendedDigest;
import org.bouncycastle2.crypto.PBEParametersGenerator;
import org.bouncycastle2.crypto.params.KeyParameter;
import org.bouncycastle2.crypto.params.ParametersWithIV;

public class PKCS12ParametersGenerator extends PBEParametersGenerator
{
  public static final int IV_MATERIAL = 2;
  public static final int KEY_MATERIAL = 1;
  public static final int MAC_MATERIAL = 3;
  private Digest digest;
  private int u;
  private int v;

  public PKCS12ParametersGenerator(Digest paramDigest)
  {
    this.digest = paramDigest;
    if ((paramDigest instanceof ExtendedDigest))
    {
      this.u = paramDigest.getDigestSize();
      this.v = ((ExtendedDigest)paramDigest).getByteLength();
      return;
    }
    throw new IllegalArgumentException("Digest " + paramDigest.getAlgorithmName() + " unsupported");
  }

  private void adjust(byte[] paramArrayOfByte1, int paramInt, byte[] paramArrayOfByte2)
  {
    int i = 1 + ((0xFF & paramArrayOfByte2[(-1 + paramArrayOfByte2.length)]) + (0xFF & paramArrayOfByte1[(-1 + (paramInt + paramArrayOfByte2.length))]));
    paramArrayOfByte1[(-1 + (paramInt + paramArrayOfByte2.length))] = ((byte)i);
    int j = i >>> 8;
    for (int k = -2 + paramArrayOfByte2.length; ; k--)
    {
      if (k < 0)
        return;
      int m = j + ((0xFF & paramArrayOfByte2[k]) + (0xFF & paramArrayOfByte1[(paramInt + k)]));
      paramArrayOfByte1[(paramInt + k)] = ((byte)m);
      j = m >>> 8;
    }
  }

  private byte[] generateDerivedKey(int paramInt1, int paramInt2)
  {
    byte[] arrayOfByte1 = new byte[this.v];
    byte[] arrayOfByte2 = new byte[paramInt2];
    int i = 0;
    byte[] arrayOfByte3;
    int i3;
    label66: label74: byte[] arrayOfByte4;
    int i2;
    if (i == arrayOfByte1.length)
    {
      if ((this.salt == null) || (this.salt.length == 0))
        break label235;
      arrayOfByte3 = new byte[this.v * ((-1 + (this.salt.length + this.v)) / this.v)];
      i3 = 0;
      if (i3 != arrayOfByte3.length)
        break label211;
      if ((this.password == null) || (this.password.length == 0))
        break label267;
      arrayOfByte4 = new byte[this.v * ((-1 + (this.password.length + this.v)) / this.v)];
      i2 = 0;
      label118: if (i2 != arrayOfByte4.length)
        break label243;
    }
    byte[] arrayOfByte5;
    byte[] arrayOfByte6;
    int j;
    int k;
    while (true)
    {
      arrayOfByte5 = new byte[arrayOfByte3.length + arrayOfByte4.length];
      System.arraycopy(arrayOfByte3, 0, arrayOfByte5, 0, arrayOfByte3.length);
      System.arraycopy(arrayOfByte4, 0, arrayOfByte5, arrayOfByte3.length, arrayOfByte4.length);
      arrayOfByte6 = new byte[this.v];
      j = (-1 + (paramInt2 + this.u)) / this.u;
      k = 1;
      if (k <= j)
        break label275;
      return arrayOfByte2;
      arrayOfByte1[i] = ((byte)paramInt1);
      i++;
      break;
      label211: arrayOfByte3[i3] = this.salt[(i3 % this.salt.length)];
      i3++;
      break label66;
      label235: arrayOfByte3 = new byte[0];
      break label74;
      label243: arrayOfByte4[i2] = this.password[(i2 % this.password.length)];
      i2++;
      break label118;
      label267: arrayOfByte4 = new byte[0];
    }
    label275: byte[] arrayOfByte7 = new byte[this.u];
    this.digest.update(arrayOfByte1, 0, arrayOfByte1.length);
    this.digest.update(arrayOfByte5, 0, arrayOfByte5.length);
    this.digest.doFinal(arrayOfByte7, 0);
    int m = 1;
    label327: int n;
    label339: int i1;
    if (m == this.iterationCount)
    {
      n = 0;
      if (n != arrayOfByte6.length)
        break label440;
      i1 = 0;
      label350: if (i1 != arrayOfByte5.length / this.v)
        break label460;
      if (k != j)
        break label481;
      System.arraycopy(arrayOfByte7, 0, arrayOfByte2, (k - 1) * this.u, arrayOfByte2.length - (k - 1) * this.u);
    }
    while (true)
    {
      k++;
      break;
      this.digest.update(arrayOfByte7, 0, arrayOfByte7.length);
      this.digest.doFinal(arrayOfByte7, 0);
      m++;
      break label327;
      label440: arrayOfByte6[n] = arrayOfByte7[(n % arrayOfByte7.length)];
      n++;
      break label339;
      label460: adjust(arrayOfByte5, i1 * this.v, arrayOfByte6);
      i1++;
      break label350;
      label481: System.arraycopy(arrayOfByte7, 0, arrayOfByte2, (k - 1) * this.u, arrayOfByte7.length);
    }
  }

  public CipherParameters generateDerivedMacParameters(int paramInt)
  {
    int i = paramInt / 8;
    return new KeyParameter(generateDerivedKey(3, i), 0, i);
  }

  public CipherParameters generateDerivedParameters(int paramInt)
  {
    int i = paramInt / 8;
    return new KeyParameter(generateDerivedKey(1, i), 0, i);
  }

  public CipherParameters generateDerivedParameters(int paramInt1, int paramInt2)
  {
    int i = paramInt1 / 8;
    int j = paramInt2 / 8;
    byte[] arrayOfByte1 = generateDerivedKey(1, i);
    byte[] arrayOfByte2 = generateDerivedKey(2, j);
    return new ParametersWithIV(new KeyParameter(arrayOfByte1, 0, i), arrayOfByte2, 0, j);
  }
}