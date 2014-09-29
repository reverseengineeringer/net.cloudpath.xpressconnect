package org.bouncycastle2.crypto.engines;

import java.security.SecureRandom;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.Digest;
import org.bouncycastle2.crypto.InvalidCipherTextException;
import org.bouncycastle2.crypto.Wrapper;
import org.bouncycastle2.crypto.digests.SHA1Digest;
import org.bouncycastle2.crypto.modes.CBCBlockCipher;
import org.bouncycastle2.crypto.params.ParametersWithIV;
import org.bouncycastle2.crypto.params.ParametersWithRandom;
import org.bouncycastle2.util.Arrays;

public class RC2WrapEngine
  implements Wrapper
{
  private static final byte[] IV2 = { 74, -35, -94, 44, 121, -24, 33, 5 };
  byte[] digest = new byte[20];
  private CBCBlockCipher engine;
  private boolean forWrapping;
  private byte[] iv;
  private CipherParameters param;
  private ParametersWithIV paramPlusIV;
  Digest sha1 = new SHA1Digest();
  private SecureRandom sr;

  private byte[] calculateCMSKeyChecksum(byte[] paramArrayOfByte)
  {
    byte[] arrayOfByte = new byte[8];
    this.sha1.update(paramArrayOfByte, 0, paramArrayOfByte.length);
    this.sha1.doFinal(this.digest, 0);
    System.arraycopy(this.digest, 0, arrayOfByte, 0, 8);
    return arrayOfByte;
  }

  private boolean checkCMSKeyChecksum(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    return Arrays.constantTimeAreEqual(calculateCMSKeyChecksum(paramArrayOfByte1), paramArrayOfByte2);
  }

  public String getAlgorithmName()
  {
    return "RC2";
  }

  public void init(boolean paramBoolean, CipherParameters paramCipherParameters)
  {
    this.forWrapping = paramBoolean;
    this.engine = new CBCBlockCipher(new RC2Engine());
    if ((paramCipherParameters instanceof ParametersWithRandom))
    {
      ParametersWithRandom localParametersWithRandom = (ParametersWithRandom)paramCipherParameters;
      this.sr = localParametersWithRandom.getRandom();
      paramCipherParameters = localParametersWithRandom.getParameters();
    }
    while ((paramCipherParameters instanceof ParametersWithIV))
    {
      this.paramPlusIV = ((ParametersWithIV)paramCipherParameters);
      this.iv = this.paramPlusIV.getIV();
      this.param = this.paramPlusIV.getParameters();
      if (this.forWrapping)
      {
        if ((this.iv != null) && (this.iv.length == 8))
          return;
        throw new IllegalArgumentException("IV is not 8 octets");
        this.sr = new SecureRandom();
      }
      else
      {
        throw new IllegalArgumentException("You should not supply an IV for unwrapping");
      }
    }
    this.param = paramCipherParameters;
    if (this.forWrapping)
    {
      this.iv = new byte[8];
      this.sr.nextBytes(this.iv);
      this.paramPlusIV = new ParametersWithIV(this.param, this.iv);
    }
  }

  public byte[] unwrap(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws InvalidCipherTextException
  {
    if (this.forWrapping)
      throw new IllegalStateException("Not set for unwrapping");
    if (paramArrayOfByte == null)
      throw new InvalidCipherTextException("Null pointer as ciphertext");
    if (paramInt2 % this.engine.getBlockSize() != 0)
      throw new InvalidCipherTextException("Ciphertext not multiple of " + this.engine.getBlockSize());
    ParametersWithIV localParametersWithIV = new ParametersWithIV(this.param, IV2);
    this.engine.init(false, localParametersWithIV);
    byte[] arrayOfByte1 = new byte[paramInt2];
    System.arraycopy(paramArrayOfByte, paramInt1, arrayOfByte1, 0, paramInt2);
    int i = 0;
    byte[] arrayOfByte2;
    int k;
    label142: byte[] arrayOfByte4;
    if (i >= arrayOfByte1.length / this.engine.getBlockSize())
    {
      arrayOfByte2 = new byte[arrayOfByte1.length];
      k = 0;
      if (k < arrayOfByte1.length)
        break label368;
      this.iv = new byte[8];
      byte[] arrayOfByte3 = new byte[-8 + arrayOfByte2.length];
      System.arraycopy(arrayOfByte2, 0, this.iv, 0, 8);
      System.arraycopy(arrayOfByte2, 8, arrayOfByte3, 0, -8 + arrayOfByte2.length);
      this.paramPlusIV = new ParametersWithIV(this.param, this.iv);
      this.engine.init(false, this.paramPlusIV);
      arrayOfByte4 = new byte[arrayOfByte3.length];
      System.arraycopy(arrayOfByte3, 0, arrayOfByte4, 0, arrayOfByte3.length);
    }
    byte[] arrayOfByte5;
    for (int m = 0; ; m++)
    {
      if (m >= arrayOfByte4.length / this.engine.getBlockSize())
      {
        arrayOfByte5 = new byte[-8 + arrayOfByte4.length];
        byte[] arrayOfByte6 = new byte[8];
        System.arraycopy(arrayOfByte4, 0, arrayOfByte5, 0, -8 + arrayOfByte4.length);
        System.arraycopy(arrayOfByte4, -8 + arrayOfByte4.length, arrayOfByte6, 0, 8);
        if (checkCMSKeyChecksum(arrayOfByte5, arrayOfByte6))
          break label424;
        throw new InvalidCipherTextException("Checksum inside ciphertext is corrupted");
        int j = i * this.engine.getBlockSize();
        this.engine.processBlock(arrayOfByte1, j, arrayOfByte1, j);
        i++;
        break;
        label368: arrayOfByte2[k] = arrayOfByte1[(arrayOfByte1.length - (k + 1))];
        k++;
        break label142;
      }
      int n = m * this.engine.getBlockSize();
      this.engine.processBlock(arrayOfByte4, n, arrayOfByte4, n);
    }
    label424: if (arrayOfByte5.length - (1 + (0xFF & arrayOfByte5[0])) > 7)
      throw new InvalidCipherTextException("too many pad bytes (" + (arrayOfByte5.length - (1 + (0xFF & arrayOfByte5[0]))) + ")");
    byte[] arrayOfByte7 = new byte[arrayOfByte5[0]];
    System.arraycopy(arrayOfByte5, 1, arrayOfByte7, 0, arrayOfByte7.length);
    return arrayOfByte7;
  }

  public byte[] wrap(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    if (!this.forWrapping)
      throw new IllegalStateException("Not initialized for wrapping");
    int i = paramInt2 + 1;
    if (i % 8 != 0)
      i += 8 - i % 8;
    byte[] arrayOfByte1 = new byte[i];
    arrayOfByte1[0] = ((byte)paramInt2);
    System.arraycopy(paramArrayOfByte, paramInt1, arrayOfByte1, 1, paramInt2);
    byte[] arrayOfByte2 = new byte[-1 + (arrayOfByte1.length - paramInt2)];
    if (arrayOfByte2.length > 0)
    {
      this.sr.nextBytes(arrayOfByte2);
      System.arraycopy(arrayOfByte2, 0, arrayOfByte1, paramInt2 + 1, arrayOfByte2.length);
    }
    byte[] arrayOfByte3 = calculateCMSKeyChecksum(arrayOfByte1);
    byte[] arrayOfByte4 = new byte[arrayOfByte1.length + arrayOfByte3.length];
    System.arraycopy(arrayOfByte1, 0, arrayOfByte4, 0, arrayOfByte1.length);
    System.arraycopy(arrayOfByte3, 0, arrayOfByte4, arrayOfByte1.length, arrayOfByte3.length);
    byte[] arrayOfByte5 = new byte[arrayOfByte4.length];
    System.arraycopy(arrayOfByte4, 0, arrayOfByte5, 0, arrayOfByte4.length);
    int j = arrayOfByte4.length / this.engine.getBlockSize();
    if (arrayOfByte4.length % this.engine.getBlockSize() != 0)
      throw new IllegalStateException("Not multiple of block length");
    this.engine.init(true, this.paramPlusIV);
    int k = 0;
    byte[] arrayOfByte6;
    byte[] arrayOfByte7;
    int n;
    if (k >= j)
    {
      arrayOfByte6 = new byte[this.iv.length + arrayOfByte5.length];
      System.arraycopy(this.iv, 0, arrayOfByte6, 0, this.iv.length);
      System.arraycopy(arrayOfByte5, 0, arrayOfByte6, this.iv.length, arrayOfByte5.length);
      arrayOfByte7 = new byte[arrayOfByte6.length];
      n = 0;
      label282: if (n < arrayOfByte6.length)
        break label365;
      ParametersWithIV localParametersWithIV = new ParametersWithIV(this.param, IV2);
      this.engine.init(true, localParametersWithIV);
    }
    for (int i1 = 0; ; i1++)
    {
      if (i1 >= j + 1)
      {
        return arrayOfByte7;
        int m = k * this.engine.getBlockSize();
        this.engine.processBlock(arrayOfByte5, m, arrayOfByte5, m);
        k++;
        break;
        label365: arrayOfByte7[n] = arrayOfByte6[(arrayOfByte6.length - (n + 1))];
        n++;
        break label282;
      }
      int i2 = i1 * this.engine.getBlockSize();
      this.engine.processBlock(arrayOfByte7, i2, arrayOfByte7, i2);
    }
  }
}