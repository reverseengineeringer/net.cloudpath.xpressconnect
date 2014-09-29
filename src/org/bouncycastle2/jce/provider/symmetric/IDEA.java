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
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.misc.IDEACBCPar;
import org.bouncycastle2.crypto.CipherKeyGenerator;
import org.bouncycastle2.crypto.engines.IDEAEngine;
import org.bouncycastle2.crypto.macs.CBCBlockCipherMac;
import org.bouncycastle2.crypto.macs.CFBBlockCipherMac;
import org.bouncycastle2.crypto.modes.CBCBlockCipher;
import org.bouncycastle2.jce.provider.BouncyCastleProvider;
import org.bouncycastle2.jce.provider.JCEBlockCipher;
import org.bouncycastle2.jce.provider.JCEKeyGenerator;
import org.bouncycastle2.jce.provider.JCEMac;
import org.bouncycastle2.jce.provider.JCESecretKeyFactory.PBEKeyFactory;
import org.bouncycastle2.jce.provider.JDKAlgorithmParameterGenerator;
import org.bouncycastle2.jce.provider.JDKAlgorithmParameters;

public final class IDEA
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
        AlgorithmParameters localAlgorithmParameters = AlgorithmParameters.getInstance("IDEA", BouncyCastleProvider.PROVIDER_NAME);
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
      throw new InvalidAlgorithmParameterException("No supported AlgorithmParameterSpec for IDEA parameter generation.");
    }
  }

  public static class AlgParams extends JDKAlgorithmParameters
  {
    private byte[] iv;

    protected byte[] engineGetEncoded()
      throws IOException
    {
      return engineGetEncoded("ASN.1");
    }

    protected byte[] engineGetEncoded(String paramString)
      throws IOException
    {
      if (isASN1FormatString(paramString))
        return new IDEACBCPar(engineGetEncoded("RAW")).getEncoded();
      if (paramString.equals("RAW"))
      {
        byte[] arrayOfByte = new byte[this.iv.length];
        System.arraycopy(this.iv, 0, arrayOfByte, 0, this.iv.length);
        return arrayOfByte;
      }
      return null;
    }

    protected void engineInit(AlgorithmParameterSpec paramAlgorithmParameterSpec)
      throws InvalidParameterSpecException
    {
      if (!(paramAlgorithmParameterSpec instanceof IvParameterSpec))
        throw new InvalidParameterSpecException("IvParameterSpec required to initialise a IV parameters algorithm parameters object");
      this.iv = ((IvParameterSpec)paramAlgorithmParameterSpec).getIV();
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
      if (paramString.equals("RAW"))
      {
        engineInit(paramArrayOfByte);
        return;
      }
      if (paramString.equals("ASN.1"))
      {
        engineInit(new IDEACBCPar((ASN1Sequence)new ASN1InputStream(paramArrayOfByte).readObject()).getIV());
        return;
      }
      throw new IOException("Unknown parameters format in IV parameters object");
    }

    protected String engineToString()
    {
      return "IDEA Parameters";
    }

    protected AlgorithmParameterSpec localEngineGetParameterSpec(Class paramClass)
      throws InvalidParameterSpecException
    {
      if (paramClass == IvParameterSpec.class)
        return new IvParameterSpec(this.iv);
      throw new InvalidParameterSpecException("unknown parameter spec passed to IV parameters object.");
    }
  }

  public static class CBC extends JCEBlockCipher
  {
    public CBC()
    {
      super(64);
    }
  }

  public static class CFB8Mac extends JCEMac
  {
    public CFB8Mac()
    {
      super();
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

  public static class Mac extends JCEMac
  {
    public Mac()
    {
      super();
    }
  }

  public static class Mappings extends HashMap
  {
    public Mappings()
    {
      put("AlgorithmParameterGenerator.IDEA", "org.bouncycastle2.jce.provider.symmetric.IDEA$AlgParamGen");
      put("AlgorithmParameterGenerator.1.3.6.1.4.1.188.7.1.1.2", "org.bouncycastle2.jce.provider.symmetric.IDEA$AlgParamGen");
      put("AlgorithmParameters.IDEA", "org.bouncycastle2.jce.provider.symmetric.IDEA$AlgParams");
      put("AlgorithmParameters.1.3.6.1.4.1.188.7.1.1.2", "org.bouncycastle2.jce.provider.symmetric.IDEA$AlgParams");
      put("Alg.Alias.AlgorithmParameters.PBEWITHSHAANDIDEA", "PKCS12PBE");
      put("Alg.Alias.AlgorithmParameters.PBEWITHSHAANDIDEA", "PKCS12PBE");
      put("Alg.Alias.AlgorithmParameters.PBEWITHSHAANDIDEA-CBC", "PKCS12PBE");
      put("Cipher.IDEA", "org.bouncycastle2.jce.provider.symmetric.IDEA$ECB");
      put("Cipher.1.3.6.1.4.1.188.7.1.1.2", "org.bouncycastle2.jce.provider.symmetric.IDEA$CBC");
      put("Cipher.PBEWITHSHAANDIDEA-CBC", "org.bouncycastle2.jce.provider.symmetric.IDEA$PBEWithSHAAndIDEA");
      put("KeyGenerator.IDEA", "org.bouncycastle2.jce.provider.symmetric.IDEA$KeyGen");
      put("KeyGenerator.1.3.6.1.4.1.188.7.1.1.2", "org.bouncycastle2.jce.provider.symmetric.IDEA$KeyGen");
      put("SecretKeyFactory.PBEWITHSHAANDIDEA-CBC", "org.bouncycastle2.jce.provider.symmetric.IDEA$PBEWithSHAAndIDEAKeyGen");
      put("Mac.IDEAMAC", "org.bouncycastle2.jce.provider.symmetric.IDEA$Mac");
      put("Alg.Alias.Mac.IDEA", "IDEAMAC");
      put("Mac.IDEAMAC/CFB8", "org.bouncycastle2.jce.provider.symmetric.IDEA$CFB8Mac");
      put("Alg.Alias.Mac.IDEA/CFB8", "IDEAMAC/CFB8");
    }
  }

  public static class PBEWithSHAAndIDEA extends JCEBlockCipher
  {
    public PBEWithSHAAndIDEA()
    {
      super();
    }
  }

  public static class PBEWithSHAAndIDEAKeyGen extends JCESecretKeyFactory.PBEKeyFactory
  {
    public PBEWithSHAAndIDEAKeyGen()
    {
      super(null, true, 2, 1, 128, 64);
    }
  }
}