package org.bouncycastle2.jce.provider;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.security.SignatureSpi;
import java.security.spec.AlgorithmParameterSpec;
import org.bouncycastle2.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle2.asn1.x509.X509ObjectIdentifiers;
import org.bouncycastle2.crypto.DSA;
import org.bouncycastle2.crypto.Digest;

public abstract class DSABase extends SignatureSpi
  implements PKCSObjectIdentifiers, X509ObjectIdentifiers
{
  protected Digest digest;
  protected DSAEncoder encoder;
  protected DSA signer;

  protected DSABase(Digest paramDigest, DSA paramDSA, DSAEncoder paramDSAEncoder)
  {
    this.digest = paramDigest;
    this.signer = paramDSA;
    this.encoder = paramDSAEncoder;
  }

  protected Object engineGetParameter(String paramString)
  {
    throw new UnsupportedOperationException("engineSetParameter unsupported");
  }

  protected void engineInitSign(PrivateKey paramPrivateKey)
    throws InvalidKeyException
  {
    engineInitSign(paramPrivateKey, null);
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
      byte[] arrayOfByte2 = this.encoder.encode(arrayOfBigInteger[0], arrayOfBigInteger[1]);
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
      BigInteger[] arrayOfBigInteger = this.encoder.decode(paramArrayOfByte);
      return this.signer.verifySignature(arrayOfByte, arrayOfBigInteger[0], arrayOfBigInteger[1]);
    }
    catch (Exception localException)
    {
    }
    throw new SignatureException("error decoding signature bytes.");
  }
}