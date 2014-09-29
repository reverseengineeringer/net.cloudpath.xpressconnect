package org.bouncycastle2.jce.provider.symmetric;

import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.HashMap;
import javax.crypto.spec.IvParameterSpec;
import org.bouncycastle2.crypto.CipherKeyGenerator;
import org.bouncycastle2.crypto.engines.RC532Engine;
import org.bouncycastle2.crypto.engines.RC564Engine;
import org.bouncycastle2.crypto.macs.CBCBlockCipherMac;
import org.bouncycastle2.crypto.macs.CFBBlockCipherMac;
import org.bouncycastle2.crypto.modes.CBCBlockCipher;
import org.bouncycastle2.jce.provider.BouncyCastleProvider;
import org.bouncycastle2.jce.provider.JCEBlockCipher;
import org.bouncycastle2.jce.provider.JCEKeyGenerator;
import org.bouncycastle2.jce.provider.JCEMac;
import org.bouncycastle2.jce.provider.JDKAlgorithmParameterGenerator;
import org.bouncycastle2.jce.provider.JDKAlgorithmParameters.IVAlgorithmParameters;

public final class RC5
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
        AlgorithmParameters localAlgorithmParameters = AlgorithmParameters.getInstance("RC5", BouncyCastleProvider.PROVIDER_NAME);
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
      throw new InvalidAlgorithmParameterException("No supported AlgorithmParameterSpec for RC5 parameter generation.");
    }
  }

  public static class AlgParams extends JDKAlgorithmParameters.IVAlgorithmParameters
  {
    protected String engineToString()
    {
      return "RC5 IV";
    }
  }

  public static class CBC32 extends JCEBlockCipher
  {
    public CBC32()
    {
      super(64);
    }
  }

  public static class CFB8Mac32 extends JCEMac
  {
    public CFB8Mac32()
    {
      super();
    }
  }

  public static class ECB32 extends JCEBlockCipher
  {
    public ECB32()
    {
      super();
    }
  }

  public static class ECB64 extends JCEBlockCipher
  {
    public ECB64()
    {
      super();
    }
  }

  public static class KeyGen32 extends JCEKeyGenerator
  {
    public KeyGen32()
    {
      super(128, new CipherKeyGenerator());
    }
  }

  public static class KeyGen64 extends JCEKeyGenerator
  {
    public KeyGen64()
    {
      super(256, new CipherKeyGenerator());
    }
  }

  public static class Mac32 extends JCEMac
  {
    public Mac32()
    {
      super();
    }
  }

  public static class Mappings extends HashMap
  {
    public Mappings()
    {
      put("Cipher.RC5", "org.bouncycastle2.jce.provider.symmetric.RC5$ECB32");
      put("Alg.Alias.Cipher.RC5-32", "RC5");
      put("Cipher.RC5-64", "org.bouncycastle2.jce.provider.symmetric.RC5$ECB64");
      put("KeyGenerator.RC5", "org.bouncycastle2.jce.provider.symmetric.RC5$KeyGen32");
      put("Alg.Alias.KeyGenerator.RC5-32", "RC5");
      put("KeyGenerator.RC5-64", "org.bouncycastle2.jce.provider.symmetric.RC5$KeyGen64");
      put("AlgorithmParameters.RC5", "org.bouncycastle2.jce.provider.symmetric.RC5$AlgParams");
      put("AlgorithmParameters.RC5-64", "org.bouncycastle2.jce.provider.symmetric.RC5$AlgParams");
      put("Mac.RC5MAC", "org.bouncycastle2.jce.provider.symmetric.RC5$Mac32");
      put("Alg.Alias.Mac.RC5", "RC5MAC");
      put("Mac.RC5MAC/CFB8", "org.bouncycastle2.jce.provider.symmetric.RC5$CFB8Mac32");
      put("Alg.Alias.Mac.RC5/CFB8", "RC5MAC/CFB8");
    }
  }
}