package org.bouncycastle2.jce.provider;

import java.io.ByteArrayOutputStream;
import java.security.AlgorithmParameters;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.SignatureSpi;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;
import org.bouncycastle2.asn1.ASN1ObjectIdentifier;
import org.bouncycastle2.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle2.crypto.AsymmetricBlockCipher;
import org.bouncycastle2.crypto.CryptoException;
import org.bouncycastle2.crypto.Digest;
import org.bouncycastle2.crypto.engines.RSABlindedEngine;
import org.bouncycastle2.crypto.params.ParametersWithRandom;
import org.bouncycastle2.crypto.signers.PSSSigner;

public class JDKPSSSigner extends SignatureSpi
{
  private Digest contentDigest;
  private AlgorithmParameters engineParams;
  private boolean isRaw;
  private Digest mgfDigest;
  private PSSParameterSpec originalSpec;
  private PSSParameterSpec paramSpec;
  private PSSSigner pss;
  private int saltLength;
  private AsymmetricBlockCipher signer;
  private byte trailer;

  protected JDKPSSSigner(AsymmetricBlockCipher paramAsymmetricBlockCipher, PSSParameterSpec paramPSSParameterSpec)
  {
    this(paramAsymmetricBlockCipher, paramPSSParameterSpec, false);
  }

  protected JDKPSSSigner(AsymmetricBlockCipher paramAsymmetricBlockCipher, PSSParameterSpec paramPSSParameterSpec, boolean paramBoolean)
  {
    this.signer = paramAsymmetricBlockCipher;
    this.originalSpec = paramPSSParameterSpec;
    if (paramPSSParameterSpec == null);
    for (this.paramSpec = PSSParameterSpec.DEFAULT; ; this.paramSpec = paramPSSParameterSpec)
    {
      this.mgfDigest = JCEDigestUtil.getDigest(this.paramSpec.getDigestAlgorithm());
      this.saltLength = this.paramSpec.getSaltLength();
      this.trailer = getTrailer(this.paramSpec.getTrailerField());
      this.isRaw = paramBoolean;
      setupContentDigest();
      return;
    }
  }

  private byte getTrailer(int paramInt)
  {
    if (paramInt == 1)
      return -68;
    throw new IllegalArgumentException("unknown trailer field");
  }

  private void setupContentDigest()
  {
    if (this.isRaw)
    {
      this.contentDigest = new NullPssDigest(this.mgfDigest);
      return;
    }
    this.contentDigest = this.mgfDigest;
  }

  protected Object engineGetParameter(String paramString)
  {
    throw new UnsupportedOperationException("engineGetParameter unsupported");
  }

  protected AlgorithmParameters engineGetParameters()
  {
    if ((this.engineParams == null) && (this.paramSpec != null));
    try
    {
      this.engineParams = AlgorithmParameters.getInstance("PSS", BouncyCastleProvider.PROVIDER_NAME);
      this.engineParams.init(this.paramSpec);
      return this.engineParams;
    }
    catch (Exception localException)
    {
      throw new RuntimeException(localException.toString());
    }
  }

  protected void engineInitSign(PrivateKey paramPrivateKey)
    throws InvalidKeyException
  {
    if (!(paramPrivateKey instanceof RSAPrivateKey))
      throw new InvalidKeyException("Supplied key is not a RSAPrivateKey instance");
    this.pss = new PSSSigner(this.signer, this.contentDigest, this.mgfDigest, this.saltLength, this.trailer);
    this.pss.init(true, RSAUtil.generatePrivateKeyParameter((RSAPrivateKey)paramPrivateKey));
  }

  protected void engineInitSign(PrivateKey paramPrivateKey, SecureRandom paramSecureRandom)
    throws InvalidKeyException
  {
    if (!(paramPrivateKey instanceof RSAPrivateKey))
      throw new InvalidKeyException("Supplied key is not a RSAPrivateKey instance");
    this.pss = new PSSSigner(this.signer, this.contentDigest, this.mgfDigest, this.saltLength, this.trailer);
    this.pss.init(true, new ParametersWithRandom(RSAUtil.generatePrivateKeyParameter((RSAPrivateKey)paramPrivateKey), paramSecureRandom));
  }

  protected void engineInitVerify(PublicKey paramPublicKey)
    throws InvalidKeyException
  {
    if (!(paramPublicKey instanceof RSAPublicKey))
      throw new InvalidKeyException("Supplied key is not a RSAPublicKey instance");
    this.pss = new PSSSigner(this.signer, this.contentDigest, this.mgfDigest, this.saltLength, this.trailer);
    this.pss.init(false, RSAUtil.generatePublicKeyParameter((RSAPublicKey)paramPublicKey));
  }

  protected void engineSetParameter(String paramString, Object paramObject)
  {
    throw new UnsupportedOperationException("engineSetParameter unsupported");
  }

  protected void engineSetParameter(AlgorithmParameterSpec paramAlgorithmParameterSpec)
    throws InvalidParameterException
  {
    if ((paramAlgorithmParameterSpec instanceof PSSParameterSpec))
    {
      PSSParameterSpec localPSSParameterSpec = (PSSParameterSpec)paramAlgorithmParameterSpec;
      if ((this.originalSpec != null) && (!JCEDigestUtil.isSameDigest(this.originalSpec.getDigestAlgorithm(), localPSSParameterSpec.getDigestAlgorithm())))
        throw new InvalidParameterException("parameter must be using " + this.originalSpec.getDigestAlgorithm());
      if ((!localPSSParameterSpec.getMGFAlgorithm().equalsIgnoreCase("MGF1")) && (!localPSSParameterSpec.getMGFAlgorithm().equals(PKCSObjectIdentifiers.id_mgf1.getId())))
        throw new InvalidParameterException("unknown mask generation function specified");
      if (!(localPSSParameterSpec.getMGFParameters() instanceof MGF1ParameterSpec))
        throw new InvalidParameterException("unkown MGF parameters");
      MGF1ParameterSpec localMGF1ParameterSpec = (MGF1ParameterSpec)localPSSParameterSpec.getMGFParameters();
      if (!JCEDigestUtil.isSameDigest(localMGF1ParameterSpec.getDigestAlgorithm(), localPSSParameterSpec.getDigestAlgorithm()))
        throw new InvalidParameterException("digest algorithm for MGF should be the same as for PSS parameters.");
      Digest localDigest = JCEDigestUtil.getDigest(localMGF1ParameterSpec.getDigestAlgorithm());
      if (localDigest == null)
        throw new InvalidParameterException("no match on MGF digest algorithm: " + localMGF1ParameterSpec.getDigestAlgorithm());
      this.engineParams = null;
      this.paramSpec = localPSSParameterSpec;
      this.mgfDigest = localDigest;
      this.saltLength = this.paramSpec.getSaltLength();
      this.trailer = getTrailer(this.paramSpec.getTrailerField());
      setupContentDigest();
      return;
    }
    throw new InvalidParameterException("Only PSSParameterSpec supported");
  }

  protected byte[] engineSign()
    throws SignatureException
  {
    try
    {
      byte[] arrayOfByte = this.pss.generateSignature();
      return arrayOfByte;
    }
    catch (CryptoException localCryptoException)
    {
      throw new SignatureException(localCryptoException.getMessage());
    }
  }

  protected void engineUpdate(byte paramByte)
    throws SignatureException
  {
    this.pss.update(paramByte);
  }

  protected void engineUpdate(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws SignatureException
  {
    this.pss.update(paramArrayOfByte, paramInt1, paramInt2);
  }

  protected boolean engineVerify(byte[] paramArrayOfByte)
    throws SignatureException
  {
    return this.pss.verifySignature(paramArrayOfByte);
  }

  private class NullPssDigest
    implements Digest
  {
    private ByteArrayOutputStream bOut = new ByteArrayOutputStream();
    private Digest baseDigest;
    private boolean oddTime = true;

    public NullPssDigest(Digest arg2)
    {
      Object localObject;
      this.baseDigest = localObject;
    }

    public int doFinal(byte[] paramArrayOfByte, int paramInt)
    {
      byte[] arrayOfByte = this.bOut.toByteArray();
      boolean bool2;
      if (this.oddTime)
      {
        System.arraycopy(arrayOfByte, 0, paramArrayOfByte, paramInt, arrayOfByte.length);
        reset();
        boolean bool1 = this.oddTime;
        bool2 = false;
        if (!bool1)
          break label79;
      }
      while (true)
      {
        this.oddTime = bool2;
        return arrayOfByte.length;
        this.baseDigest.update(arrayOfByte, 0, arrayOfByte.length);
        this.baseDigest.doFinal(paramArrayOfByte, paramInt);
        break;
        label79: bool2 = true;
      }
    }

    public String getAlgorithmName()
    {
      return "NULL";
    }

    public int getDigestSize()
    {
      return this.baseDigest.getDigestSize();
    }

    public void reset()
    {
      this.bOut.reset();
      this.baseDigest.reset();
    }

    public void update(byte paramByte)
    {
      this.bOut.write(paramByte);
    }

    public void update(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    {
      this.bOut.write(paramArrayOfByte, paramInt1, paramInt2);
    }
  }

  public static class PSSwithRSA extends JDKPSSSigner
  {
    public PSSwithRSA()
    {
      super(null);
    }
  }

  public static class SHA1withRSA extends JDKPSSSigner
  {
    public SHA1withRSA()
    {
      super(PSSParameterSpec.DEFAULT);
    }
  }

  public static class SHA224withRSA extends JDKPSSSigner
  {
    public SHA224withRSA()
    {
      super(new PSSParameterSpec("SHA-224", "MGF1", new MGF1ParameterSpec("SHA-224"), 28, 1));
    }
  }

  public static class SHA256withRSA extends JDKPSSSigner
  {
    public SHA256withRSA()
    {
      super(new PSSParameterSpec("SHA-256", "MGF1", new MGF1ParameterSpec("SHA-256"), 32, 1));
    }
  }

  public static class SHA384withRSA extends JDKPSSSigner
  {
    public SHA384withRSA()
    {
      super(new PSSParameterSpec("SHA-384", "MGF1", new MGF1ParameterSpec("SHA-384"), 48, 1));
    }
  }

  public static class SHA512withRSA extends JDKPSSSigner
  {
    public SHA512withRSA()
    {
      super(new PSSParameterSpec("SHA-512", "MGF1", new MGF1ParameterSpec("SHA-512"), 64, 1));
    }
  }

  public static class nonePSS extends JDKPSSSigner
  {
    public nonePSS()
    {
      super(null, true);
    }
  }
}