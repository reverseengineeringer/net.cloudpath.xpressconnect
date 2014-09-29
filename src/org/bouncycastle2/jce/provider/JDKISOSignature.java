package org.bouncycastle2.jce.provider;

import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.SignatureSpi;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import org.bouncycastle2.crypto.AsymmetricBlockCipher;
import org.bouncycastle2.crypto.Digest;
import org.bouncycastle2.crypto.digests.MD5Digest;
import org.bouncycastle2.crypto.digests.RIPEMD160Digest;
import org.bouncycastle2.crypto.digests.SHA1Digest;
import org.bouncycastle2.crypto.engines.RSABlindedEngine;
import org.bouncycastle2.crypto.params.RSAKeyParameters;
import org.bouncycastle2.crypto.signers.ISO9796d2Signer;

public class JDKISOSignature extends SignatureSpi
{
  private ISO9796d2Signer signer;

  protected JDKISOSignature(Digest paramDigest, AsymmetricBlockCipher paramAsymmetricBlockCipher)
  {
    this.signer = new ISO9796d2Signer(paramAsymmetricBlockCipher, paramDigest, true);
  }

  protected Object engineGetParameter(String paramString)
  {
    throw new UnsupportedOperationException("engineSetParameter unsupported");
  }

  protected void engineInitSign(PrivateKey paramPrivateKey)
    throws InvalidKeyException
  {
    RSAKeyParameters localRSAKeyParameters = RSAUtil.generatePrivateKeyParameter((RSAPrivateKey)paramPrivateKey);
    this.signer.init(true, localRSAKeyParameters);
  }

  protected void engineInitVerify(PublicKey paramPublicKey)
    throws InvalidKeyException
  {
    RSAKeyParameters localRSAKeyParameters = RSAUtil.generatePublicKeyParameter((RSAPublicKey)paramPublicKey);
    this.signer.init(false, localRSAKeyParameters);
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
    try
    {
      byte[] arrayOfByte = this.signer.generateSignature();
      return arrayOfByte;
    }
    catch (Exception localException)
    {
      throw new SignatureException(localException.toString());
    }
  }

  protected void engineUpdate(byte paramByte)
    throws SignatureException
  {
    this.signer.update(paramByte);
  }

  protected void engineUpdate(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws SignatureException
  {
    this.signer.update(paramArrayOfByte, paramInt1, paramInt2);
  }

  protected boolean engineVerify(byte[] paramArrayOfByte)
    throws SignatureException
  {
    return this.signer.verifySignature(paramArrayOfByte);
  }

  public static class MD5WithRSAEncryption extends JDKISOSignature
  {
    public MD5WithRSAEncryption()
    {
      super(new RSABlindedEngine());
    }
  }

  public static class RIPEMD160WithRSAEncryption extends JDKISOSignature
  {
    public RIPEMD160WithRSAEncryption()
    {
      super(new RSABlindedEngine());
    }
  }

  public static class SHA1WithRSAEncryption extends JDKISOSignature
  {
    public SHA1WithRSAEncryption()
    {
      super(new RSABlindedEngine());
    }
  }
}