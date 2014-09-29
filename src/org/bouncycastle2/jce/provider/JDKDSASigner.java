package org.bouncycastle2.jce.provider;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.SignatureSpi;
import java.security.interfaces.DSAKey;
import java.security.spec.AlgorithmParameterSpec;
import org.bouncycastle2.asn1.ASN1Object;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle2.asn1.x509.X509ObjectIdentifiers;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.DSA;
import org.bouncycastle2.crypto.Digest;
import org.bouncycastle2.crypto.digests.NullDigest;
import org.bouncycastle2.crypto.digests.SHA1Digest;
import org.bouncycastle2.crypto.digests.SHA224Digest;
import org.bouncycastle2.crypto.digests.SHA256Digest;
import org.bouncycastle2.crypto.digests.SHA384Digest;
import org.bouncycastle2.crypto.digests.SHA512Digest;
import org.bouncycastle2.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle2.crypto.params.ParametersWithRandom;
import org.bouncycastle2.crypto.signers.DSASigner;
import org.bouncycastle2.jce.interfaces.GOST3410Key;

public class JDKDSASigner extends SignatureSpi
  implements PKCSObjectIdentifiers, X509ObjectIdentifiers
{
  private Digest digest;
  private SecureRandom random;
  private DSA signer;

  protected JDKDSASigner(Digest paramDigest, DSA paramDSA)
  {
    this.digest = paramDigest;
    this.signer = paramDSA;
  }

  private BigInteger[] derDecode(byte[] paramArrayOfByte)
    throws IOException
  {
    ASN1Sequence localASN1Sequence = (ASN1Sequence)ASN1Object.fromByteArray(paramArrayOfByte);
    BigInteger[] arrayOfBigInteger = new BigInteger[2];
    arrayOfBigInteger[0] = ((DERInteger)localASN1Sequence.getObjectAt(0)).getValue();
    arrayOfBigInteger[1] = ((DERInteger)localASN1Sequence.getObjectAt(1)).getValue();
    return arrayOfBigInteger;
  }

  private byte[] derEncode(BigInteger paramBigInteger1, BigInteger paramBigInteger2)
    throws IOException
  {
    DERInteger[] arrayOfDERInteger = new DERInteger[2];
    arrayOfDERInteger[0] = new DERInteger(paramBigInteger1);
    arrayOfDERInteger[1] = new DERInteger(paramBigInteger2);
    return new DERSequence(arrayOfDERInteger).getEncoded("DER");
  }

  protected Object engineGetParameter(String paramString)
  {
    throw new UnsupportedOperationException("engineSetParameter unsupported");
  }

  protected void engineInitSign(PrivateKey paramPrivateKey)
    throws InvalidKeyException
  {
    if ((paramPrivateKey instanceof GOST3410Key));
    for (Object localObject = GOST3410Util.generatePrivateKeyParameter(paramPrivateKey); ; localObject = DSAUtil.generatePrivateKeyParameter(paramPrivateKey))
    {
      if (this.random != null)
        localObject = new ParametersWithRandom((CipherParameters)localObject, this.random);
      this.digest.reset();
      this.signer.init(true, (CipherParameters)localObject);
      return;
    }
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
    if ((paramPublicKey instanceof GOST3410Key))
      localAsymmetricKeyParameter = GOST3410Util.generatePublicKeyParameter(paramPublicKey);
    while (true)
    {
      this.digest.reset();
      this.signer.init(false, localAsymmetricKeyParameter);
      return;
      if ((paramPublicKey instanceof DSAKey))
        localAsymmetricKeyParameter = DSAUtil.generatePublicKeyParameter(paramPublicKey);
      else
        try
        {
          PublicKey localPublicKey = JDKKeyFactory.createPublicKeyFromDERStream(paramPublicKey.getEncoded());
          if ((localPublicKey instanceof DSAKey))
            localAsymmetricKeyParameter = DSAUtil.generatePublicKeyParameter(localPublicKey);
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
    try
    {
      BigInteger[] arrayOfBigInteger = this.signer.generateSignature(arrayOfByte1);
      byte[] arrayOfByte2 = derEncode(arrayOfBigInteger[0], arrayOfBigInteger[1]);
      return arrayOfByte2;
    }
    catch (Exception localException)
    {
      throw new SignatureException(localException.toString());
    }
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
    byte[] arrayOfByte = new byte[this.digest.getDigestSize()];
    this.digest.doFinal(arrayOfByte, 0);
    try
    {
      BigInteger[] arrayOfBigInteger = derDecode(paramArrayOfByte);
      return this.signer.verifySignature(arrayOfByte, arrayOfBigInteger[0], arrayOfBigInteger[1]);
    }
    catch (Exception localException)
    {
    }
    throw new SignatureException("error decoding signature bytes.");
  }

  public static class dsa224 extends JDKDSASigner
  {
    public dsa224()
    {
      super(new DSASigner());
    }
  }

  public static class dsa256 extends JDKDSASigner
  {
    public dsa256()
    {
      super(new DSASigner());
    }
  }

  public static class dsa384 extends JDKDSASigner
  {
    public dsa384()
    {
      super(new DSASigner());
    }
  }

  public static class dsa512 extends JDKDSASigner
  {
    public dsa512()
    {
      super(new DSASigner());
    }
  }

  public static class noneDSA extends JDKDSASigner
  {
    public noneDSA()
    {
      super(new DSASigner());
    }
  }

  public static class stdDSA extends JDKDSASigner
  {
    public stdDSA()
    {
      super(new DSASigner());
    }
  }
}