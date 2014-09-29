package org.bouncycastle2.crypto.signers;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle2.crypto.AsymmetricBlockCipher;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.CryptoException;
import org.bouncycastle2.crypto.DataLengthException;
import org.bouncycastle2.crypto.Digest;
import org.bouncycastle2.crypto.Signer;
import org.bouncycastle2.crypto.params.ParametersWithRandom;
import org.bouncycastle2.crypto.params.RSABlindingParameters;
import org.bouncycastle2.crypto.params.RSAKeyParameters;

public class PSSSigner
  implements Signer
{
  public static final byte TRAILER_IMPLICIT = -68;
  private byte[] block;
  private AsymmetricBlockCipher cipher;
  private Digest contentDigest;
  private int emBits;
  private int hLen;
  private byte[] mDash;
  private Digest mgfDigest;
  private int mgfhLen;
  private SecureRandom random;
  private int sLen;
  private byte[] salt;
  private byte trailer;

  public PSSSigner(AsymmetricBlockCipher paramAsymmetricBlockCipher, Digest paramDigest, int paramInt)
  {
    this(paramAsymmetricBlockCipher, paramDigest, paramInt, (byte)-68);
  }

  public PSSSigner(AsymmetricBlockCipher paramAsymmetricBlockCipher, Digest paramDigest, int paramInt, byte paramByte)
  {
    this(paramAsymmetricBlockCipher, paramDigest, paramDigest, paramInt, paramByte);
  }

  public PSSSigner(AsymmetricBlockCipher paramAsymmetricBlockCipher, Digest paramDigest1, Digest paramDigest2, int paramInt)
  {
    this(paramAsymmetricBlockCipher, paramDigest1, paramDigest2, paramInt, (byte)-68);
  }

  public PSSSigner(AsymmetricBlockCipher paramAsymmetricBlockCipher, Digest paramDigest1, Digest paramDigest2, int paramInt, byte paramByte)
  {
    this.cipher = paramAsymmetricBlockCipher;
    this.contentDigest = paramDigest1;
    this.mgfDigest = paramDigest2;
    this.hLen = paramDigest1.getDigestSize();
    this.mgfhLen = paramDigest2.getDigestSize();
    this.sLen = paramInt;
    this.salt = new byte[paramInt];
    this.mDash = new byte[paramInt + 8 + this.hLen];
    this.trailer = paramByte;
  }

  private void ItoOSP(int paramInt, byte[] paramArrayOfByte)
  {
    paramArrayOfByte[0] = ((byte)(paramInt >>> 24));
    paramArrayOfByte[1] = ((byte)(paramInt >>> 16));
    paramArrayOfByte[2] = ((byte)(paramInt >>> 8));
    paramArrayOfByte[3] = ((byte)(paramInt >>> 0));
  }

  private void clearBlock(byte[] paramArrayOfByte)
  {
    for (int i = 0; ; i++)
    {
      if (i == paramArrayOfByte.length)
        return;
      paramArrayOfByte[i] = 0;
    }
  }

  private byte[] maskGeneratorFunction1(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3)
  {
    byte[] arrayOfByte1 = new byte[paramInt3];
    byte[] arrayOfByte2 = new byte[this.mgfhLen];
    byte[] arrayOfByte3 = new byte[4];
    int i = 0;
    this.mgfDigest.reset();
    while (true)
    {
      if (i >= paramInt3 / this.mgfhLen)
      {
        if (i * this.mgfhLen < paramInt3)
        {
          ItoOSP(i, arrayOfByte3);
          this.mgfDigest.update(paramArrayOfByte, paramInt1, paramInt2);
          this.mgfDigest.update(arrayOfByte3, 0, arrayOfByte3.length);
          this.mgfDigest.doFinal(arrayOfByte2, 0);
          System.arraycopy(arrayOfByte2, 0, arrayOfByte1, i * this.mgfhLen, arrayOfByte1.length - i * this.mgfhLen);
        }
        return arrayOfByte1;
      }
      ItoOSP(i, arrayOfByte3);
      this.mgfDigest.update(paramArrayOfByte, paramInt1, paramInt2);
      this.mgfDigest.update(arrayOfByte3, 0, arrayOfByte3.length);
      this.mgfDigest.doFinal(arrayOfByte2, 0);
      System.arraycopy(arrayOfByte2, 0, arrayOfByte1, i * this.mgfhLen, this.mgfhLen);
      i++;
    }
  }

  public byte[] generateSignature()
    throws CryptoException, DataLengthException
  {
    this.contentDigest.doFinal(this.mDash, this.mDash.length - this.hLen - this.sLen);
    if (this.sLen != 0)
    {
      this.random.nextBytes(this.salt);
      System.arraycopy(this.salt, 0, this.mDash, this.mDash.length - this.sLen, this.sLen);
    }
    byte[] arrayOfByte1 = new byte[this.hLen];
    this.contentDigest.update(this.mDash, 0, this.mDash.length);
    this.contentDigest.doFinal(arrayOfByte1, 0);
    this.block[(-1 + (-1 + (this.block.length - this.sLen) - this.hLen))] = 1;
    System.arraycopy(this.salt, 0, this.block, -1 + (this.block.length - this.sLen - this.hLen), this.sLen);
    byte[] arrayOfByte2 = maskGeneratorFunction1(arrayOfByte1, 0, arrayOfByte1.length, -1 + (this.block.length - this.hLen));
    for (int i = 0; ; i++)
    {
      if (i == arrayOfByte2.length)
      {
        byte[] arrayOfByte4 = this.block;
        arrayOfByte4[0] = ((byte)(arrayOfByte4[0] & 255 >> 8 * this.block.length - this.emBits));
        System.arraycopy(arrayOfByte1, 0, this.block, -1 + (this.block.length - this.hLen), this.hLen);
        this.block[(-1 + this.block.length)] = this.trailer;
        byte[] arrayOfByte5 = this.cipher.processBlock(this.block, 0, this.block.length);
        clearBlock(this.block);
        return arrayOfByte5;
      }
      byte[] arrayOfByte3 = this.block;
      arrayOfByte3[i] = ((byte)(arrayOfByte3[i] ^ arrayOfByte2[i]));
    }
  }

  public void init(boolean paramBoolean, CipherParameters paramCipherParameters)
  {
    CipherParameters localCipherParameters;
    if ((paramCipherParameters instanceof ParametersWithRandom))
    {
      ParametersWithRandom localParametersWithRandom = (ParametersWithRandom)paramCipherParameters;
      localCipherParameters = localParametersWithRandom.getParameters();
      this.random = localParametersWithRandom.getRandom();
      this.cipher.init(paramBoolean, localCipherParameters);
      if (!(localCipherParameters instanceof RSABlindingParameters))
        break label124;
    }
    label124: for (RSAKeyParameters localRSAKeyParameters = ((RSABlindingParameters)localCipherParameters).getPublicKey(); ; localRSAKeyParameters = (RSAKeyParameters)localCipherParameters)
    {
      this.emBits = (-1 + localRSAKeyParameters.getModulus().bitLength());
      if (this.emBits >= 9 + (8 * this.hLen + 8 * this.sLen))
        break label133;
      throw new IllegalArgumentException("key too small for specified hash and salt lengths");
      localCipherParameters = paramCipherParameters;
      if (!paramBoolean)
        break;
      this.random = new SecureRandom();
      break;
    }
    label133: this.block = new byte[(7 + this.emBits) / 8];
    reset();
  }

  public void reset()
  {
    this.contentDigest.reset();
  }

  public void update(byte paramByte)
  {
    this.contentDigest.update(paramByte);
  }

  public void update(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    this.contentDigest.update(paramArrayOfByte, paramInt1, paramInt2);
  }

  public boolean verifySignature(byte[] paramArrayOfByte)
  {
    this.contentDigest.doFinal(this.mDash, this.mDash.length - this.hLen - this.sLen);
    try
    {
      byte[] arrayOfByte1 = this.cipher.processBlock(paramArrayOfByte, 0, paramArrayOfByte.length);
      System.arraycopy(arrayOfByte1, 0, this.block, this.block.length - arrayOfByte1.length, arrayOfByte1.length);
      if (this.block[(-1 + this.block.length)] != this.trailer)
      {
        clearBlock(this.block);
        return false;
      }
    }
    catch (Exception localException)
    {
      return false;
    }
    byte[] arrayOfByte2 = maskGeneratorFunction1(this.block, -1 + (this.block.length - this.hLen), this.hLen, -1 + (this.block.length - this.hLen));
    int i = 0;
    if (i == arrayOfByte2.length)
    {
      byte[] arrayOfByte4 = this.block;
      arrayOfByte4[0] = ((byte)(arrayOfByte4[0] & 255 >> 8 * this.block.length - this.emBits));
    }
    for (int j = 0; ; j++)
    {
      if (j == -2 + (this.block.length - this.hLen - this.sLen))
      {
        if (this.block[(-2 + (this.block.length - this.hLen - this.sLen))] == 1)
          break label298;
        clearBlock(this.block);
        return false;
        byte[] arrayOfByte3 = this.block;
        arrayOfByte3[i] = ((byte)(arrayOfByte3[i] ^ arrayOfByte2[i]));
        i++;
        break;
      }
      if (this.block[j] != 0)
      {
        clearBlock(this.block);
        return false;
      }
    }
    label298: System.arraycopy(this.block, -1 + (this.block.length - this.sLen - this.hLen), this.mDash, this.mDash.length - this.sLen, this.sLen);
    this.contentDigest.update(this.mDash, 0, this.mDash.length);
    this.contentDigest.doFinal(this.mDash, this.mDash.length - this.hLen);
    int k = -1 + (this.block.length - this.hLen);
    for (int m = this.mDash.length - this.hLen; ; m++)
    {
      if (m == this.mDash.length)
      {
        clearBlock(this.mDash);
        clearBlock(this.block);
        return true;
      }
      if ((this.block[k] ^ this.mDash[m]) != 0)
      {
        clearBlock(this.mDash);
        clearBlock(this.block);
        return false;
      }
      k++;
    }
  }
}