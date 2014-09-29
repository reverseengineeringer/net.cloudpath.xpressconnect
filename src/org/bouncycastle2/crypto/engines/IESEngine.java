package org.bouncycastle2.crypto.engines;

import java.math.BigInteger;
import org.bouncycastle2.crypto.BasicAgreement;
import org.bouncycastle2.crypto.BufferedBlockCipher;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.DerivationFunction;
import org.bouncycastle2.crypto.InvalidCipherTextException;
import org.bouncycastle2.crypto.Mac;
import org.bouncycastle2.crypto.params.IESParameters;
import org.bouncycastle2.crypto.params.IESWithCipherParameters;
import org.bouncycastle2.crypto.params.KDFParameters;
import org.bouncycastle2.crypto.params.KeyParameter;

public class IESEngine
{
  BasicAgreement agree;
  BufferedBlockCipher cipher;
  boolean forEncryption;
  DerivationFunction kdf;
  Mac mac;
  byte[] macBuf;
  IESParameters param;
  CipherParameters privParam;
  CipherParameters pubParam;

  public IESEngine(BasicAgreement paramBasicAgreement, DerivationFunction paramDerivationFunction, Mac paramMac)
  {
    this.agree = paramBasicAgreement;
    this.kdf = paramDerivationFunction;
    this.mac = paramMac;
    this.macBuf = new byte[paramMac.getMacSize()];
    this.cipher = null;
  }

  public IESEngine(BasicAgreement paramBasicAgreement, DerivationFunction paramDerivationFunction, Mac paramMac, BufferedBlockCipher paramBufferedBlockCipher)
  {
    this.agree = paramBasicAgreement;
    this.kdf = paramDerivationFunction;
    this.mac = paramMac;
    this.macBuf = new byte[paramMac.getMacSize()];
    this.cipher = paramBufferedBlockCipher;
  }

  private byte[] decryptBlock(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2)
    throws InvalidCipherTextException
  {
    ((byte[])null);
    KDFParameters localKDFParameters = new KDFParameters(paramArrayOfByte2, this.param.getDerivationV());
    int i = this.param.getMacKeySize();
    this.kdf.init(localKDFParameters);
    int j = paramInt2 - this.mac.getMacSize();
    byte[] arrayOfByte5;
    byte[] arrayOfByte3;
    int i6;
    KeyParameter localKeyParameter;
    label117: int i3;
    if (this.cipher == null)
    {
      arrayOfByte5 = generateKdfBytes(localKDFParameters, j + i / 8);
      arrayOfByte3 = new byte[j];
      i6 = 0;
      if (i6 == j)
      {
        int i7 = i / 8;
        localKeyParameter = new KeyParameter(arrayOfByte5, j, i7);
        byte[] arrayOfByte4 = this.param.getEncodingV();
        this.mac.init(localKeyParameter);
        this.mac.update(paramArrayOfByte1, paramInt1, j);
        this.mac.update(arrayOfByte4, 0, arrayOfByte4.length);
        this.mac.doFinal(this.macBuf, 0);
        i3 = paramInt1 + j;
      }
    }
    for (int i4 = 0; ; i4++)
    {
      int i5 = this.macBuf.length;
      if (i4 >= i5)
      {
        return arrayOfByte3;
        arrayOfByte3[i6] = ((byte)(paramArrayOfByte1[(paramInt1 + i6)] ^ arrayOfByte5[i6]));
        i6++;
        break;
        int k = ((IESWithCipherParameters)this.param).getCipherKeySize();
        byte[] arrayOfByte1 = generateKdfBytes(localKDFParameters, k / 8 + i / 8);
        this.cipher.init(false, new KeyParameter(arrayOfByte1, 0, k / 8));
        byte[] arrayOfByte2 = new byte[this.cipher.getOutputSize(j)];
        int m = this.cipher.processBytes(paramArrayOfByte1, paramInt1, j, arrayOfByte2, 0);
        int n = m + this.cipher.doFinal(arrayOfByte2, m);
        arrayOfByte3 = new byte[n];
        System.arraycopy(arrayOfByte2, 0, arrayOfByte3, 0, n);
        int i1 = k / 8;
        int i2 = i / 8;
        localKeyParameter = new KeyParameter(arrayOfByte1, i1, i2);
        break label117;
      }
      if (this.macBuf[i4] != paramArrayOfByte1[(i3 + i4)])
        throw new InvalidCipherTextException("Mac codes failed to equal.");
    }
  }

  private byte[] encryptBlock(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2)
    throws InvalidCipherTextException
  {
    ((byte[])null);
    KDFParameters localKDFParameters = new KDFParameters(paramArrayOfByte2, this.param.getDerivationV());
    int i = this.param.getMacKeySize();
    byte[] arrayOfByte5;
    byte[] arrayOfByte3;
    int n;
    int i1;
    if (this.cipher == null)
    {
      arrayOfByte5 = generateKdfBytes(localKDFParameters, paramInt2 + i / 8);
      arrayOfByte3 = new byte[paramInt2 + this.mac.getMacSize()];
      n = paramInt2;
      i1 = 0;
      if (i1 != paramInt2);
    }
    int j;
    byte[] arrayOfByte1;
    for (KeyParameter localKeyParameter = new KeyParameter(arrayOfByte5, paramInt2, i / 8); ; localKeyParameter = new KeyParameter(arrayOfByte1, j / 8, i / 8))
    {
      byte[] arrayOfByte4 = this.param.getEncodingV();
      this.mac.init(localKeyParameter);
      this.mac.update(arrayOfByte3, 0, n);
      this.mac.update(arrayOfByte4, 0, arrayOfByte4.length);
      this.mac.doFinal(arrayOfByte3, n);
      return arrayOfByte3;
      arrayOfByte3[i1] = ((byte)(paramArrayOfByte1[(paramInt1 + i1)] ^ arrayOfByte5[i1]));
      i1++;
      break;
      j = ((IESWithCipherParameters)this.param).getCipherKeySize();
      arrayOfByte1 = generateKdfBytes(localKDFParameters, j / 8 + i / 8);
      this.cipher.init(true, new KeyParameter(arrayOfByte1, 0, j / 8));
      byte[] arrayOfByte2 = new byte[this.cipher.getOutputSize(paramInt2)];
      int k = this.cipher.processBytes(paramArrayOfByte1, paramInt1, paramInt2, arrayOfByte2, 0);
      int m = k + this.cipher.doFinal(arrayOfByte2, k);
      arrayOfByte3 = new byte[m + this.mac.getMacSize()];
      n = m;
      System.arraycopy(arrayOfByte2, 0, arrayOfByte3, 0, m);
    }
  }

  private byte[] generateKdfBytes(KDFParameters paramKDFParameters, int paramInt)
  {
    byte[] arrayOfByte = new byte[paramInt];
    this.kdf.init(paramKDFParameters);
    this.kdf.generateBytes(arrayOfByte, 0, arrayOfByte.length);
    return arrayOfByte;
  }

  public void init(boolean paramBoolean, CipherParameters paramCipherParameters1, CipherParameters paramCipherParameters2, CipherParameters paramCipherParameters3)
  {
    this.forEncryption = paramBoolean;
    this.privParam = paramCipherParameters1;
    this.pubParam = paramCipherParameters2;
    this.param = ((IESParameters)paramCipherParameters3);
  }

  public byte[] processBlock(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws InvalidCipherTextException
  {
    this.agree.init(this.privParam);
    BigInteger localBigInteger = this.agree.calculateAgreement(this.pubParam);
    if (this.forEncryption)
      return encryptBlock(paramArrayOfByte, paramInt1, paramInt2, localBigInteger.toByteArray());
    return decryptBlock(paramArrayOfByte, paramInt1, paramInt2, localBigInteger.toByteArray());
  }
}