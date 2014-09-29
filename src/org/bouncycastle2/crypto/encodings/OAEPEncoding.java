package org.bouncycastle2.crypto.encodings;

import java.security.SecureRandom;
import org.bouncycastle2.crypto.AsymmetricBlockCipher;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.Digest;
import org.bouncycastle2.crypto.InvalidCipherTextException;
import org.bouncycastle2.crypto.digests.SHA1Digest;
import org.bouncycastle2.crypto.params.ParametersWithRandom;

public class OAEPEncoding
  implements AsymmetricBlockCipher
{
  private byte[] defHash;
  private AsymmetricBlockCipher engine;
  private boolean forEncryption;
  private Digest hash;
  private Digest mgf1Hash;
  private SecureRandom random;

  public OAEPEncoding(AsymmetricBlockCipher paramAsymmetricBlockCipher)
  {
    this(paramAsymmetricBlockCipher, new SHA1Digest(), null);
  }

  public OAEPEncoding(AsymmetricBlockCipher paramAsymmetricBlockCipher, Digest paramDigest)
  {
    this(paramAsymmetricBlockCipher, paramDigest, null);
  }

  public OAEPEncoding(AsymmetricBlockCipher paramAsymmetricBlockCipher, Digest paramDigest1, Digest paramDigest2, byte[] paramArrayOfByte)
  {
    this.engine = paramAsymmetricBlockCipher;
    this.hash = paramDigest1;
    this.mgf1Hash = paramDigest2;
    this.defHash = new byte[paramDigest1.getDigestSize()];
    if (paramArrayOfByte != null)
      paramDigest1.update(paramArrayOfByte, 0, paramArrayOfByte.length);
    paramDigest1.doFinal(this.defHash, 0);
  }

  public OAEPEncoding(AsymmetricBlockCipher paramAsymmetricBlockCipher, Digest paramDigest, byte[] paramArrayOfByte)
  {
    this(paramAsymmetricBlockCipher, paramDigest, paramDigest, paramArrayOfByte);
  }

  private void ItoOSP(int paramInt, byte[] paramArrayOfByte)
  {
    paramArrayOfByte[0] = ((byte)(paramInt >>> 24));
    paramArrayOfByte[1] = ((byte)(paramInt >>> 16));
    paramArrayOfByte[2] = ((byte)(paramInt >>> 8));
    paramArrayOfByte[3] = ((byte)(paramInt >>> 0));
  }

  private byte[] maskGeneratorFunction1(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3)
  {
    byte[] arrayOfByte1 = new byte[paramInt3];
    byte[] arrayOfByte2 = new byte[this.mgf1Hash.getDigestSize()];
    byte[] arrayOfByte3 = new byte[4];
    int i = 0;
    this.hash.reset();
    do
    {
      ItoOSP(i, arrayOfByte3);
      this.mgf1Hash.update(paramArrayOfByte, paramInt1, paramInt2);
      this.mgf1Hash.update(arrayOfByte3, 0, arrayOfByte3.length);
      this.mgf1Hash.doFinal(arrayOfByte2, 0);
      System.arraycopy(arrayOfByte2, 0, arrayOfByte1, i * arrayOfByte2.length, arrayOfByte2.length);
      i++;
    }
    while (i < paramInt3 / arrayOfByte2.length);
    if (i * arrayOfByte2.length < paramInt3)
    {
      ItoOSP(i, arrayOfByte3);
      this.mgf1Hash.update(paramArrayOfByte, paramInt1, paramInt2);
      this.mgf1Hash.update(arrayOfByte3, 0, arrayOfByte3.length);
      this.mgf1Hash.doFinal(arrayOfByte2, 0);
      System.arraycopy(arrayOfByte2, 0, arrayOfByte1, i * arrayOfByte2.length, arrayOfByte1.length - i * arrayOfByte2.length);
    }
    return arrayOfByte1;
  }

  public byte[] decodeBlock(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws InvalidCipherTextException
  {
    byte[] arrayOfByte1 = this.engine.processBlock(paramArrayOfByte, paramInt1, paramInt2);
    byte[] arrayOfByte2;
    if (arrayOfByte1.length < this.engine.getOutputBlockSize())
    {
      arrayOfByte2 = new byte[this.engine.getOutputBlockSize()];
      System.arraycopy(arrayOfByte1, 0, arrayOfByte2, arrayOfByte2.length - arrayOfByte1.length, arrayOfByte1.length);
    }
    while (arrayOfByte2.length < 1 + 2 * this.defHash.length)
    {
      throw new InvalidCipherTextException("data too short");
      arrayOfByte2 = arrayOfByte1;
    }
    byte[] arrayOfByte3 = maskGeneratorFunction1(arrayOfByte2, this.defHash.length, arrayOfByte2.length - this.defHash.length, this.defHash.length);
    int i = 0;
    byte[] arrayOfByte4;
    int j;
    label162: int k;
    if (i == this.defHash.length)
    {
      arrayOfByte4 = maskGeneratorFunction1(arrayOfByte2, 0, this.defHash.length, arrayOfByte2.length - this.defHash.length);
      j = this.defHash.length;
      if (j != arrayOfByte2.length)
        break label267;
      k = 0;
      label173: if (k != this.defHash.length)
        break label296;
    }
    for (int m = 2 * this.defHash.length; ; m++)
    {
      if (m == arrayOfByte2.length);
      label267: label296: 
      while (arrayOfByte2[m] != 0)
      {
        if ((m < -1 + arrayOfByte2.length) && (arrayOfByte2[m] == 1))
          break label347;
        throw new InvalidCipherTextException("data start wrong " + m);
        arrayOfByte2[i] = ((byte)(arrayOfByte2[i] ^ arrayOfByte3[i]));
        i++;
        break;
        arrayOfByte2[j] = ((byte)(arrayOfByte2[j] ^ arrayOfByte4[(j - this.defHash.length)]));
        j++;
        break label162;
        if (this.defHash[k] != arrayOfByte2[(k + this.defHash.length)])
          throw new InvalidCipherTextException("data hash wrong");
        k++;
        break label173;
      }
    }
    label347: int n = m + 1;
    byte[] arrayOfByte5 = new byte[arrayOfByte2.length - n];
    System.arraycopy(arrayOfByte2, n, arrayOfByte5, 0, arrayOfByte5.length);
    return arrayOfByte5;
  }

  public byte[] encodeBlock(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws InvalidCipherTextException
  {
    byte[] arrayOfByte1 = new byte[1 + getInputBlockSize() + 2 * this.defHash.length];
    System.arraycopy(paramArrayOfByte, paramInt1, arrayOfByte1, arrayOfByte1.length - paramInt2, paramInt2);
    arrayOfByte1[(-1 + (arrayOfByte1.length - paramInt2))] = 1;
    System.arraycopy(this.defHash, 0, arrayOfByte1, this.defHash.length, this.defHash.length);
    byte[] arrayOfByte2 = new byte[this.defHash.length];
    this.random.nextBytes(arrayOfByte2);
    byte[] arrayOfByte3 = maskGeneratorFunction1(arrayOfByte2, 0, arrayOfByte2.length, arrayOfByte1.length - this.defHash.length);
    int i = this.defHash.length;
    byte[] arrayOfByte4;
    if (i == arrayOfByte1.length)
    {
      System.arraycopy(arrayOfByte2, 0, arrayOfByte1, 0, this.defHash.length);
      arrayOfByte4 = maskGeneratorFunction1(arrayOfByte1, this.defHash.length, arrayOfByte1.length - this.defHash.length, this.defHash.length);
    }
    for (int j = 0; ; j++)
    {
      if (j == this.defHash.length)
      {
        return this.engine.processBlock(arrayOfByte1, 0, arrayOfByte1.length);
        arrayOfByte1[i] = ((byte)(arrayOfByte1[i] ^ arrayOfByte3[(i - this.defHash.length)]));
        i++;
        break;
      }
      arrayOfByte1[j] = ((byte)(arrayOfByte1[j] ^ arrayOfByte4[j]));
    }
  }

  public int getInputBlockSize()
  {
    int i = this.engine.getInputBlockSize();
    if (this.forEncryption)
      i = i - 1 - 2 * this.defHash.length;
    return i;
  }

  public int getOutputBlockSize()
  {
    int i = this.engine.getOutputBlockSize();
    if (this.forEncryption)
      return i;
    return i - 1 - 2 * this.defHash.length;
  }

  public AsymmetricBlockCipher getUnderlyingCipher()
  {
    return this.engine;
  }

  public void init(boolean paramBoolean, CipherParameters paramCipherParameters)
  {
    if ((paramCipherParameters instanceof ParametersWithRandom));
    for (this.random = ((ParametersWithRandom)paramCipherParameters).getRandom(); ; this.random = new SecureRandom())
    {
      this.engine.init(paramBoolean, paramCipherParameters);
      this.forEncryption = paramBoolean;
      return;
    }
  }

  public byte[] processBlock(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws InvalidCipherTextException
  {
    if (this.forEncryption)
      return encodeBlock(paramArrayOfByte, paramInt1, paramInt2);
    return decodeBlock(paramArrayOfByte, paramInt1, paramInt2);
  }
}