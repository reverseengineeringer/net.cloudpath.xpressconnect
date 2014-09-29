package org.bouncycastle2.jce.provider.symmetric;

import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.HashMap;
import javax.crypto.spec.IvParameterSpec;
import org.bouncycastle2.asn1.kisa.KISAObjectIdentifiers;
import org.bouncycastle2.crypto.CipherKeyGenerator;
import org.bouncycastle2.crypto.engines.SEEDEngine;
import org.bouncycastle2.crypto.engines.SEEDWrapEngine;
import org.bouncycastle2.crypto.modes.CBCBlockCipher;
import org.bouncycastle2.jce.provider.BouncyCastleProvider;
import org.bouncycastle2.jce.provider.JCEBlockCipher;
import org.bouncycastle2.jce.provider.JCEKeyGenerator;
import org.bouncycastle2.jce.provider.JDKAlgorithmParameterGenerator;
import org.bouncycastle2.jce.provider.JDKAlgorithmParameters.IVAlgorithmParameters;
import org.bouncycastle2.jce.provider.WrapCipherSpi;

public final class SEED
{
  public static class AlgParamGen extends JDKAlgorithmParameterGenerator
  {
    protected AlgorithmParameters engineGenerateParameters()
    {
      byte[] arrayOfByte = new byte[16];
      if (this.random == null)
        this.random = new SecureRandom();
      this.random.nextBytes(arrayOfByte);
      try
      {
        AlgorithmParameters localAlgorithmParameters = AlgorithmParameters.getInstance("SEED", BouncyCastleProvider.PROVIDER_NAME);
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
      throw new InvalidAlgorithmParameterException("No supported AlgorithmParameterSpec for SEED parameter generation.");
    }
  }

  public static class AlgParams extends JDKAlgorithmParameters.IVAlgorithmParameters
  {
    protected String engineToString()
    {
      return "SEED IV";
    }
  }

  public static class CBC extends JCEBlockCipher
  {
    public CBC()
    {
      super(128);
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
      put("AlgorithmParameters.SEED", "org.bouncycastle2.jce.provider.symmetric.SEED$AlgParams");
      put("Alg.Alias.AlgorithmParameters." + KISAObjectIdentifiers.id_seedCBC, "SEED");
      put("AlgorithmParameterGenerator.SEED", "org.bouncycastle2.jce.provider.symmetric.SEED$AlgParamGen");
      put("Alg.Alias.AlgorithmParameterGenerator." + KISAObjectIdentifiers.id_seedCBC, "SEED");
      put("Cipher.SEED", "org.bouncycastle2.jce.provider.symmetric.SEED$ECB");
      put("Cipher." + KISAObjectIdentifiers.id_seedCBC, "org.bouncycastle2.jce.provider.symmetric.SEED$CBC");
      put("Cipher.SEEDWRAP", "org.bouncycastle2.jce.provider.symmetric.SEED$Wrap");
      put("Alg.Alias.Cipher." + KISAObjectIdentifiers.id_npki_app_cmsSeed_wrap, "SEEDWRAP");
      put("KeyGenerator.SEED", "org.bouncycastle2.jce.provider.symmetric.SEED$KeyGen");
      put("KeyGenerator." + KISAObjectIdentifiers.id_seedCBC, "org.bouncycastle2.jce.provider.symmetric.SEED$KeyGen");
      put("KeyGenerator." + KISAObjectIdentifiers.id_npki_app_cmsSeed_wrap, "org.bouncycastle2.jce.provider.symmetric.SEED$KeyGen");
    }
  }

  public static class Wrap extends WrapCipherSpi
  {
    public Wrap()
    {
      super();
    }
  }
}