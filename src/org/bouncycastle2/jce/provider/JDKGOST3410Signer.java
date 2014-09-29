package org.bouncycastle2.jce.provider;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.SignatureSpi;
import java.security.spec.AlgorithmParameterSpec;
import org.bouncycastle2.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle2.asn1.x509.X509ObjectIdentifiers;
import org.bouncycastle2.crypto.DSA;
import org.bouncycastle2.crypto.Digest;
import org.bouncycastle2.crypto.digests.GOST3411Digest;
import org.bouncycastle2.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle2.crypto.params.ParametersWithRandom;
import org.bouncycastle2.crypto.signers.ECGOST3410Signer;
import org.bouncycastle2.crypto.signers.GOST3410Signer;
import org.bouncycastle2.jce.interfaces.ECKey;
import org.bouncycastle2.jce.interfaces.ECPublicKey;
import org.bouncycastle2.jce.interfaces.GOST3410Key;
import org.bouncycastle2.jce.provider.asymmetric.ec.ECUtil;

public class JDKGOST3410Signer extends SignatureSpi
  implements PKCSObjectIdentifiers, X509ObjectIdentifiers
{
  private Digest digest;
  private SecureRandom random;
  private DSA signer;

  protected JDKGOST3410Signer(Digest paramDigest, DSA paramDSA)
  {
    this.digest = paramDigest;
    this.signer = paramDSA;
  }

  protected Object engineGetParameter(String paramString)
  {
    throw new UnsupportedOperationException("engineSetParameter unsupported");
  }

  protected void engineInitSign(PrivateKey paramPrivateKey)
    throws InvalidKeyException
  {
    if ((paramPrivateKey instanceof ECKey));
    for (AsymmetricKeyParameter localAsymmetricKeyParameter = ECUtil.generatePrivateKeyParameter(paramPrivateKey); ; localAsymmetricKeyParameter = GOST3410Util.generatePrivateKeyParameter(paramPrivateKey))
    {
      this.digest.reset();
      if (this.random == null)
        break;
      this.signer.init(true, new ParametersWithRandom(localAsymmetricKeyParameter, this.random));
      return;
    }
    this.signer.init(true, localAsymmetricKeyParameter);
  }

  protected void engineInitSign(PrivateKey paramPrivateKey, SecureRandom paramSecureRandom)
    throws InvalidKeyException
  {
    this.random = paramSecureRandom;
    engineInitSign(paramPrivateKey);
  }

  protected void engineInitVerify(PublicKey paramPublicKey)
    throws InvalidKeyException
  {
    AsymmetricKeyParameter localAsymmetricKeyParameter;
    if ((paramPublicKey instanceof ECPublicKey))
      localAsymmetricKeyParameter = ECUtil.generatePublicKeyParameter(paramPublicKey);
    while (true)
    {
      this.digest.reset();
      this.signer.init(false, localAsymmetricKeyParameter);
      return;
      if ((paramPublicKey instanceof GOST3410Key))
        localAsymmetricKeyParameter = GOST3410Util.generatePublicKeyParameter(paramPublicKey);
      else
        try
        {
          PublicKey localPublicKey = JDKKeyFactory.createPublicKeyFromDERStream(paramPublicKey.getEncoded());
          if ((localPublicKey instanceof ECPublicKey))
            localAsymmetricKeyParameter = ECUtil.generatePublicKeyParameter(localPublicKey);
          else
            throw new InvalidKeyException("can't recognise key type in DSA based signer");
        }
        catch (Exception localException)
        {
        }
    }
    throw new InvalidKeyException("can't recognise key type in DSA based signer");
  }

  protected void engineSetParameter(String paramString, Object paramObject)
  {
    throw new UnsupportedOperationException("engineSetParameter unsupported");
  }

  protected void engineSetParameter(AlgorithmParameterSpec paramAlgorithmParameterSpec)
  {
    throw new UnsupportedOperationException("engineSetParameter unsupported");
  }

  protected byte[] engineSign()
    throws SignatureException
  {
    byte[] arrayOfByte1 = new byte[this.digest.getDigestSize()];
    this.digest.doFinal(arrayOfByte1, 0);
    byte[] arrayOfByte2;
    byte[] arrayOfByte3;
    try
    {
      arrayOfByte2 = new byte[64];
      BigInteger[] arrayOfBigInteger = this.signer.generateSignature(arrayOfByte1);
      arrayOfByte3 = arrayOfBigInteger[0].toByteArray();
      byte[] arrayOfByte4 = arrayOfBigInteger[1].toByteArray();
      if (arrayOfByte4[0] != 0)
        System.arraycopy(arrayOfByte4, 0, arrayOfByte2, 32 - arrayOfByte4.length, arrayOfByte4.length);
      while (arrayOfByte3[0] != 0)
      {
        System.arraycopy(arrayOfByte3, 0, arrayOfByte2, 64 - arrayOfByte3.length, arrayOfByte3.length);
        return arrayOfByte2;
        System.arraycopy(arrayOfByte4, 1, arrayOfByte2, 32 - (-1 + arrayOfByte4.length), -1 + arrayOfByte4.length);
      }
    }
    catch (Exception localException)
    {
      throw new SignatureException(localException.toString());
    }
    System.arraycopy(arrayOfByte3, 1, arrayOfByte2, 64 - (-1 + arrayOfByte3.length), -1 + arrayOfByte3.length);
    return arrayOfByte2;
  }

  protected void engineUpdate(byte paramByte)
    throws SignatureException
  {
    this.digest.update(paramByte);
  }

  protected void engineUpdate(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws SignatureException
  {
    this.digest.update(paramArrayOfByte, paramInt1, paramInt2);
  }

  protected boolean engineVerify(byte[] paramArrayOfByte)
    throws SignatureException
  {
    byte[] arrayOfByte1 = new byte[this.digest.getDigestSize()];
    this.digest.doFinal(arrayOfByte1, 0);
    try
    {
      byte[] arrayOfByte2 = new byte[32];
      byte[] arrayOfByte3 = new byte[32];
      System.arraycopy(paramArrayOfByte, 0, arrayOfByte3, 0, 32);
      System.arraycopy(paramArrayOfByte, 32, arrayOfByte2, 0, 32);
      BigInteger[] arrayOfBigInteger = new BigInteger[2];
      arrayOfBigInteger[0] = new BigInteger(1, arrayOfByte2);
      arrayOfBigInteger[1] = new BigInteger(1, arrayOfByte3);
      return this.signer.verifySignature(arrayOfByte1, arrayOfBigInteger[0], arrayOfBigInteger[1]);
    }
    catch (Exception localException)
    {
    }
    throw new SignatureException("error decoding signature bytes.");
  }

  public static class ecgost3410 extends JDKGOST3410Signer
  {
    public ecgost3410()
    {
      super(new ECGOST3410Signer());
    }
  }

  public static class gost3410 extends JDKGOST3410Signer
  {
    public gost3410()
    {
      super(new GOST3410Signer());
    }
  }
}