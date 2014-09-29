package org.bouncycastle2.crypto.engines;

import java.security.SecureRandom;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.Digest;
import org.bouncycastle2.crypto.InvalidCipherTextException;
import org.bouncycastle2.crypto.Wrapper;
import org.bouncycastle2.crypto.digests.SHA1Digest;
import org.bouncycastle2.crypto.modes.CBCBlockCipher;
import org.bouncycastle2.crypto.params.KeyParameter;
import org.bouncycastle2.crypto.params.ParametersWithIV;
import org.bouncycastle2.crypto.params.ParametersWithRandom;
import org.bouncycastle2.util.Arrays;

public class DESedeWrapEngine
  implements Wrapper
{
  private static final byte[] IV2 = { 74, -35, -94, 44, 121, -24, 33, 5 };
  byte[] digest = new byte[20];
  private CBCBlockCipher engine;
  private boolean forWrapping;
  private byte[] iv;
  private KeyParameter param;
  private ParametersWithIV paramPlusIV;
  Digest sha1 = new SHA1Digest();

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

  private static byte[] reverse(byte[] paramArrayOfByte)
  {
    byte[] arrayOfByte = new byte[paramArrayOfByte.length];
    for (int i = 0; ; i++)
    {
      if (i >= paramArrayOfByte.length)
        return arrayOfByte;
      arrayOfByte[i] = paramArrayOfByte[(paramArrayOfByte.length - (i + 1))];
    }
  }

  public String getAlgorithmName()
  {
    return "DESede";
  }

  public void init(boolean paramBoolean, CipherParameters paramCipherParameters)
  {
    this.forWrapping = paramBoolean;
    this.engine = new CBCBlockCipher(new DESedeEngine());
    SecureRandom localSecureRandom;
    if ((paramCipherParameters instanceof ParametersWithRandom))
    {
      ParametersWithRandom localParametersWithRandom = (ParametersWithRandom)paramCipherParameters;
      paramCipherParameters = localParametersWithRandom.getParameters();
      localSecureRandom = localParametersWithRandom.getRandom();
      if (!(paramCipherParameters instanceof KeyParameter))
        break label117;
      this.param = ((KeyParameter)paramCipherParameters);
      if (this.forWrapping)
      {
        this.iv = new byte[8];
        localSecureRandom.nextBytes(this.iv);
        this.paramPlusIV = new ParametersWithIV(this.param, this.iv);
      }
    }
    label117: 
    do
    {
      do
      {
        return;
        localSecureRandom = new SecureRandom();
        break;
      }
      while (!(paramCipherParameters instanceof ParametersWithIV));
      this.paramPlusIV = ((ParametersWithIV)paramCipherParameters);
      this.iv = this.paramPlusIV.getIV();
      this.param = ((KeyParameter)this.paramPlusIV.getParameters());
      if (!this.forWrapping)
        break label191;
    }
    while ((this.iv != null) && (this.iv.length == 8));
    throw new IllegalArgumentException("IV is not 8 octets");
    label191: throw new IllegalArgumentException("You should not supply an IV for unwrapping");
  }

  public byte[] unwrap(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws InvalidCipherTextException
  {
    if (this.forWrapping)
      throw new IllegalStateException("Not set for unwrapping");
    if (paramArrayOfByte == null)
      throw new InvalidCipherTextException("Null pointer as ciphertext");
    int i = this.engine.getBlockSize();
    if (paramInt2 % i != 0)
      throw new InvalidCipherTextException("Ciphertext not multiple of " + i);
    ParametersWithIV localParametersWithIV = new ParametersWithIV(this.param, IV2);
    this.engine.init(false, localParametersWithIV);
    byte[] arrayOfByte1 = new byte[paramInt2];
    int j = 0;
    byte[] arrayOfByte3;
    byte[] arrayOfByte4;
    int k;
    if (j == paramInt2)
    {
      byte[] arrayOfByte2 = reverse(arrayOfByte1);
      this.iv = new byte[8];
      arrayOfByte3 = new byte[-8 + arrayOfByte2.length];
      System.arraycopy(arrayOfByte2, 0, this.iv, 0, 8);
      System.arraycopy(arrayOfByte2, 8, arrayOfByte3, 0, -8 + arrayOfByte2.length);
      this.paramPlusIV = new ParametersWithIV(this.param, this.iv);
      this.engine.init(false, this.paramPlusIV);
      arrayOfByte4 = new byte[arrayOfByte3.length];
      k = 0;
    }
    byte[] arrayOfByte5;
    while (true)
    {
      if (k == arrayOfByte4.length)
      {
        arrayOfByte5 = new byte[-8 + arrayOfByte4.length];
        byte[] arrayOfByte6 = new byte[8];
        System.arraycopy(arrayOfByte4, 0, arrayOfByte5, 0, -8 + arrayOfByte4.length);
        System.arraycopy(arrayOfByte4, -8 + arrayOfByte4.length, arrayOfByte6, 0, 8);
        if (checkCMSKeyChecksum(arrayOfByte5, arrayOfByte6))
          break label336;
        throw new InvalidCipherTextException("Checksum inside ciphertext is corrupted");
        this.engine.processBlock(paramArrayOfByte, paramInt1 + j, arrayOfByte1, j);
        j += i;
        break;
      }
      this.engine.processBlock(arrayOfByte3, k, arrayOfByte4, k);
      k += i;
    }
    label336: return arrayOfByte5;
  }

  public byte[] wrap(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    if (!this.forWrapping)
      throw new IllegalStateException("Not initialized for wrapping");
    byte[] arrayOfByte1 = new byte[paramInt2];
    System.arraycopy(paramArrayOfByte, paramInt1, arrayOfByte1, 0, paramInt2);
    byte[] arrayOfByte2 = calculateCMSKeyChecksum(arrayOfByte1);
    byte[] arrayOfByte3 = new byte[arrayOfByte1.length + arrayOfByte2.length];
    System.arraycopy(arrayOfByte1, 0, arrayOfByte3, 0, arrayOfByte1.length);
    System.arraycopy(arrayOfByte2, 0, arrayOfByte3, arrayOfByte1.length, arrayOfByte2.length);
    int i = this.engine.getBlockSize();
    if (arrayOfByte3.length % i != 0)
      throw new IllegalStateException("Not multiple of block length");
    this.engine.init(true, this.paramPlusIV);
    byte[] arrayOfByte4 = new byte[arrayOfByte3.length];
    int j = 0;
    byte[] arrayOfByte6;
    int k;
    if (j == arrayOfByte3.length)
    {
      byte[] arrayOfByte5 = new byte[this.iv.length + arrayOfByte4.length];
      System.arraycopy(this.iv, 0, arrayOfByte5, 0, this.iv.length);
      System.arraycopy(arrayOfByte4, 0, arrayOfByte5, this.iv.length, arrayOfByte4.length);
      arrayOfByte6 = reverse(arrayOfByte5);
      ParametersWithIV localParametersWithIV = new ParametersWithIV(this.param, IV2);
      this.engine.init(true, localParametersWithIV);
      k = 0;
    }
    while (true)
    {
      if (k == arrayOfByte6.length)
      {
        return arrayOfByte6;
        this.engine.processBlock(arrayOfByte3, j, arrayOfByte4, j);
        j += i;
        break;
      }
      this.engine.processBlock(arrayOfByte6, k, arrayOfByte6, k);
      k += i;
    }
  }
}