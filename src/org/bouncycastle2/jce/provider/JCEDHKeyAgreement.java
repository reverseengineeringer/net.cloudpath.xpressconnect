package org.bouncycastle2.jce.provider;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Hashtable;
import javax.crypto.KeyAgreementSpi;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle2.crypto.params.DESParameters;
import org.bouncycastle2.util.Strings;

public class JCEDHKeyAgreement extends KeyAgreementSpi
{
  private static final Hashtable algorithms = new Hashtable();
  private BigInteger g;
  private BigInteger p;
  private BigInteger result;
  private BigInteger x;

  static
  {
    Integer localInteger1 = new Integer(64);
    Integer localInteger2 = new Integer(192);
    Integer localInteger3 = new Integer(128);
    Integer localInteger4 = new Integer(256);
    algorithms.put("DES", localInteger1);
    algorithms.put("DESEDE", localInteger2);
    algorithms.put("BLOWFISH", localInteger3);
    algorithms.put("AES", localInteger4);
  }

  private byte[] bigIntToBytes(BigInteger paramBigInteger)
  {
    byte[] arrayOfByte1 = paramBigInteger.toByteArray();
    if (arrayOfByte1[0] == 0)
    {
      byte[] arrayOfByte2 = new byte[-1 + arrayOfByte1.length];
      System.arraycopy(arrayOfByte1, 1, arrayOfByte2, 0, arrayOfByte2.length);
      return arrayOfByte2;
    }
    return arrayOfByte1;
  }

  protected Key engineDoPhase(Key paramKey, boolean paramBoolean)
    throws InvalidKeyException, IllegalStateException
  {
    if (this.x == null)
      throw new IllegalStateException("Diffie-Hellman not initialised.");
    if (!(paramKey instanceof DHPublicKey))
      throw new InvalidKeyException("DHKeyAgreement doPhase requires DHPublicKey");
    DHPublicKey localDHPublicKey = (DHPublicKey)paramKey;
    if ((!localDHPublicKey.getParams().getG().equals(this.g)) || (!localDHPublicKey.getParams().getP().equals(this.p)))
      throw new InvalidKeyException("DHPublicKey not for this KeyAgreement!");
    if (paramBoolean)
    {
      this.result = ((DHPublicKey)paramKey).getY().modPow(this.x, this.p);
      return null;
    }
    this.result = ((DHPublicKey)paramKey).getY().modPow(this.x, this.p);
    return new JCEDHPublicKey(this.result, localDHPublicKey.getParams());
  }

  protected int engineGenerateSecret(byte[] paramArrayOfByte, int paramInt)
    throws IllegalStateException, ShortBufferException
  {
    if (this.x == null)
      throw new IllegalStateException("Diffie-Hellman not initialised.");
    byte[] arrayOfByte = bigIntToBytes(this.result);
    if (paramArrayOfByte.length - paramInt < arrayOfByte.length)
      throw new ShortBufferException("DHKeyAgreement - buffer too short");
    System.arraycopy(arrayOfByte, 0, paramArrayOfByte, paramInt, arrayOfByte.length);
    return arrayOfByte.length;
  }

  protected SecretKey engineGenerateSecret(String paramString)
  {
    if (this.x == null)
      throw new IllegalStateException("Diffie-Hellman not initialised.");
    String str = Strings.toUpperCase(paramString);
    byte[] arrayOfByte1 = bigIntToBytes(this.result);
    if (algorithms.containsKey(str))
    {
      byte[] arrayOfByte2 = new byte[((Integer)algorithms.get(str)).intValue() / 8];
      System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, arrayOfByte2.length);
      if (str.startsWith("DES"))
        DESParameters.setOddParity(arrayOfByte2);
      return new SecretKeySpec(arrayOfByte2, paramString);
    }
    return new SecretKeySpec(arrayOfByte1, paramString);
  }

  protected byte[] engineGenerateSecret()
    throws IllegalStateException
  {
    if (this.x == null)
      throw new IllegalStateException("Diffie-Hellman not initialised.");
    return bigIntToBytes(this.result);
  }

  protected void engineInit(Key paramKey, SecureRandom paramSecureRandom)
    throws InvalidKeyException
  {
    if (!(paramKey instanceof DHPrivateKey))
      throw new InvalidKeyException("DHKeyAgreement requires DHPrivateKey");
    DHPrivateKey localDHPrivateKey = (DHPrivateKey)paramKey;
    this.p = localDHPrivateKey.getParams().getP();
    this.g = localDHPrivateKey.getParams().getG();
    BigInteger localBigInteger = localDHPrivateKey.getX();
    this.result = localBigInteger;
    this.x = localBigInteger;
  }

  protected void engineInit(Key paramKey, AlgorithmParameterSpec paramAlgorithmParameterSpec, SecureRandom paramSecureRandom)
    throws InvalidKeyException, InvalidAlgorithmParameterException
  {
    if (!(paramKey instanceof DHPrivateKey))
      throw new InvalidKeyException("DHKeyAgreement requires DHPrivateKey for initialisation");
    DHPrivateKey localDHPrivateKey = (DHPrivateKey)paramKey;
    DHParameterSpec localDHParameterSpec;
    if (paramAlgorithmParameterSpec != null)
    {
      if (!(paramAlgorithmParameterSpec instanceof DHParameterSpec))
        throw new InvalidAlgorithmParameterException("DHKeyAgreement only accepts DHParameterSpec");
      localDHParameterSpec = (DHParameterSpec)paramAlgorithmParameterSpec;
      this.p = localDHParameterSpec.getP();
    }
    for (this.g = localDHParameterSpec.getG(); ; this.g = localDHPrivateKey.getParams().getG())
    {
      BigInteger localBigInteger = localDHPrivateKey.getX();
      this.result = localBigInteger;
      this.x = localBigInteger;
      return;
      this.p = localDHPrivateKey.getParams().getP();
    }
  }
}