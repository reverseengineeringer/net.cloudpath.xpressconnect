package org.bouncycastle2.jce.provider.symmetric;

import java.io.IOException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import java.util.HashMap;
import javax.crypto.spec.IvParameterSpec;
import org.bouncycastle2.asn1.ASN1InputStream;
import org.bouncycastle2.asn1.misc.CAST5CBCParameters;
import org.bouncycastle2.crypto.CipherKeyGenerator;
import org.bouncycastle2.crypto.engines.CAST5Engine;
import org.bouncycastle2.crypto.modes.CBCBlockCipher;
import org.bouncycastle2.jce.provider.BouncyCastleProvider;
import org.bouncycastle2.jce.provider.JCEBlockCipher;
import org.bouncycastle2.jce.provider.JCEKeyGenerator;
import org.bouncycastle2.jce.provider.JDKAlgorithmParameterGenerator;
import org.bouncycastle2.jce.provider.JDKAlgorithmParameters;

public final class CAST5
{
  public static class AlgParamGen extends JDKAlgorithmParameterGenerator
  {
    protected AlgorithmParameters engineGenerateParameters()
    {
      byte[] arrayOfByte = new byte[8];
      if (this.random == null)
        this.random = new SecureRandom();
      this.random.nextBytes(arrayOfByte);
      try
      {
        AlgorithmParameters localAlgorithmParameters = AlgorithmParameters.getInstance("CAST5", BouncyCastleProvider.PROVIDER_NAME);
        localAlgorithmParameters.init(new IvParameterSpec(arrayOfByte));
        return localAlgorithmParameters;
      }
      catch (Exception localException)
      {
        throw new RuntimeException(localException.getMessage());
      }
    }

    protected void engineInit(AlgorithmParameterSpec paramAlgorithmParameterSpec, SecureRandom paramSecureRandom)
      throws InvalidAlgorithmParameterException
    {
      throw new InvalidAlgorithmParameterException("No supported AlgorithmParameterSpec for CAST5 parameter generation.");
    }
  }

  public static class AlgParams extends JDKAlgorithmParameters
  {
    private byte[] iv;
    private int keyLength = 128;

    protected byte[] engineGetEncoded()
    {
      byte[] arrayOfByte = new byte[this.iv.length];
      System.arraycopy(this.iv, 0, arrayOfByte, 0, this.iv.length);
      return arrayOfByte;
    }

    protected byte[] engineGetEncoded(String paramString)
      throws IOException
    {
      if (isASN1FormatString(paramString))
        return new CAST5CBCParameters(engineGetEncoded(), this.keyLength).getEncoded();
      if (paramString.equals("RAW"))
        return engineGetEncoded();
      return null;
    }

    protected void engineInit(AlgorithmParameterSpec paramAlgorithmParameterSpec)
      throws InvalidParameterSpecException
    {
      if ((paramAlgorithmParameterSpec instanceof IvParameterSpec))
      {
        this.iv = ((IvParameterSpec)paramAlgorithmParameterSpec).getIV();
        return;
      }
      throw new InvalidParameterSpecException("IvParameterSpec required to initialise a CAST5 parameters algorithm parameters object");
    }

    protected void engineInit(byte[] paramArrayOfByte)
      throws IOException
    {
      this.iv = new byte[paramArrayOfByte.length];
      System.arraycopy(paramArrayOfByte, 0, this.iv, 0, this.iv.length);
    }

    protected void engineInit(byte[] paramArrayOfByte, String paramString)
      throws IOException
    {
      if (isASN1FormatString(paramString))
      {
        CAST5CBCParameters localCAST5CBCParameters = CAST5CBCParameters.getInstance(new ASN1InputStream(paramArrayOfByte).readObject());
        this.keyLength = localCAST5CBCParameters.getKeyLength();
        this.iv = localCAST5CBCParameters.getIV();
        return;
      }
      if (paramString.equals("RAW"))
      {
        engineInit(paramArrayOfByte);
        return;
      }
      throw new IOException("Unknown parameters format in IV parameters object");
    }

    protected String engineToString()
    {
      return "CAST5 Parameters";
    }

    protected AlgorithmParameterSpec localEngineGetParameterSpec(Class paramClass)
      throws InvalidParameterSpecException
    {
      if (paramClass == IvParameterSpec.class)
        return new IvParameterSpec(this.iv);
      throw new InvalidParameterSpecException("unknown parameter spec passed to CAST5 parameters object.");
    }
  }

  public static class CBC extends JCEBlockCipher
  {
    public CBC()
    {
      super(64);
    }
  }

  public static class ECB extends JCEBlockCipher
  {
    public ECB()
    {
      super();
    }
  }

  public static class KeyGen extends JCEKeyGenerator
  {
    public KeyGen()
    {
      super(128, new CipherKeyGenerator());
    }
  }

  public static class Mappings extends HashMap
  {
    public Mappings()
    {
      put("AlgorithmParameters.CAST5", "org.bouncycastle2.jce.provider.symmetric.CAST5$AlgParams");
      put("Alg.Alias.AlgorithmParameters.1.2.840.113533.7.66.10", "CAST5");
      put("AlgorithmParameterGenerator.CAST5", "org.bouncycastle2.jce.provider.symmetric.CAST5$AlgParamGen");
      put("Alg.Alias.AlgorithmParameterGenerator.1.2.840.113533.7.66.10", "CAST5");
      put("Cipher.CAST5", "org.bouncycastle2.jce.provider.symmetric.CAST5$ECB");
      put("Cipher.1.2.840.113533.7.66.10", "org.bouncycastle2.jce.provider.symmetric.CAST5$CBC");
      put("KeyGenerator.CAST5", "org.bouncycastle2.jce.provider.symmetric.CAST5$KeyGen");
      put("Alg.Alias.KeyGenerator.1.2.840.113533.7.66.10", "CAST5");
    }
  }
}