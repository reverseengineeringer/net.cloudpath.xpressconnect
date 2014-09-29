package org.bouncycastle2.jce.provider.symmetric;

import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.HashMap;
import javax.crypto.spec.IvParameterSpec;
import org.bouncycastle2.asn1.ntt.NTTObjectIdentifiers;
import org.bouncycastle2.crypto.CipherKeyGenerator;
import org.bouncycastle2.crypto.engines.CamelliaEngine;
import org.bouncycastle2.crypto.engines.CamelliaWrapEngine;
import org.bouncycastle2.crypto.engines.RFC3211WrapEngine;
import org.bouncycastle2.crypto.modes.CBCBlockCipher;
import org.bouncycastle2.jce.provider.BouncyCastleProvider;
import org.bouncycastle2.jce.provider.JCEBlockCipher;
import org.bouncycastle2.jce.provider.JCEKeyGenerator;
import org.bouncycastle2.jce.provider.JDKAlgorithmParameterGenerator;
import org.bouncycastle2.jce.provider.JDKAlgorithmParameters.IVAlgorithmParameters;
import org.bouncycastle2.jce.provider.WrapCipherSpi;

public final class Camellia
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
        AlgorithmParameters localAlgorithmParameters = AlgorithmParameters.getInstance("Camellia", BouncyCastleProvider.PROVIDER_NAME);
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
      throw new InvalidAlgorithmParameterException("No supported AlgorithmParameterSpec for Camellia parameter generation.");
    }
  }

  public static class AlgParams extends JDKAlgorithmParameters.IVAlgorithmParameters
  {
    protected String engineToString()
    {
      return "Camellia IV";
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
      this(256);
    }

    public KeyGen(int paramInt)
    {
      super(paramInt, new CipherKeyGenerator());
    }
  }

  public static class KeyGen128 extends Camellia.KeyGen
  {
    public KeyGen128()
    {
      super();
    }
  }

  public static class KeyGen192 extends Camellia.KeyGen
  {
    public KeyGen192()
    {
      super();
    }
  }

  public static class KeyGen256 extends Camellia.KeyGen
  {
    public KeyGen256()
    {
      super();
    }
  }

  public static class Mappings extends HashMap
  {
    public Mappings()
    {
      put("AlgorithmParameters.CAMELLIA", "org.bouncycastle2.jce.provider.symmetric.Camellia$AlgParams");
      put("Alg.Alias.AlgorithmParameters." + NTTObjectIdentifiers.id_camellia128_cbc, "CAMELLIA");
      put("Alg.Alias.AlgorithmParameters." + NTTObjectIdentifiers.id_camellia192_cbc, "CAMELLIA");
      put("Alg.Alias.AlgorithmParameters." + NTTObjectIdentifiers.id_camellia256_cbc, "CAMELLIA");
      put("AlgorithmParameterGenerator.CAMELLIA", "org.bouncycastle2.jce.provider.symmetric.Camellia$AlgParamGen");
      put("Alg.Alias.AlgorithmParameterGenerator." + NTTObjectIdentifiers.id_camellia128_cbc, "CAMELLIA");
      put("Alg.Alias.AlgorithmParameterGenerator." + NTTObjectIdentifiers.id_camellia192_cbc, "CAMELLIA");
      put("Alg.Alias.AlgorithmParameterGenerator." + NTTObjectIdentifiers.id_camellia256_cbc, "CAMELLIA");
      put("Cipher.CAMELLIA", "org.bouncycastle2.jce.provider.symmetric.Camellia$ECB");
      put("Cipher." + NTTObjectIdentifiers.id_camellia128_cbc, "org.bouncycastle2.jce.provider.symmetric.Camellia$CBC");
      put("Cipher." + NTTObjectIdentifiers.id_camellia192_cbc, "org.bouncycastle2.jce.provider.symmetric.Camellia$CBC");
      put("Cipher." + NTTObjectIdentifiers.id_camellia256_cbc, "org.bouncycastle2.jce.provider.symmetric.Camellia$CBC");
      put("Cipher.CAMELLIARFC3211WRAP", "org.bouncycastle2.jce.provider.symmetric.Camellia$RFC3211Wrap");
      put("Cipher.CAMELLIAWRAP", "org.bouncycastle2.jce.provider.symmetric.Camellia$Wrap");
      put("Alg.Alias.Cipher." + NTTObjectIdentifiers.id_camellia128_wrap, "CAMELLIAWRAP");
      put("Alg.Alias.Cipher." + NTTObjectIdentifiers.id_camellia192_wrap, "CAMELLIAWRAP");
      put("Alg.Alias.Cipher." + NTTObjectIdentifiers.id_camellia256_wrap, "CAMELLIAWRAP");
      put("KeyGenerator.CAMELLIA", "org.bouncycastle2.jce.provider.symmetric.Camellia$KeyGen");
      put("KeyGenerator." + NTTObjectIdentifiers.id_camellia128_wrap, "org.bouncycastle2.jce.provider.symmetric.Camellia$KeyGen128");
      put("KeyGenerator." + NTTObjectIdentifiers.id_camellia192_wrap, "org.bouncycastle2.jce.provider.symmetric.Camellia$KeyGen192");
      put("KeyGenerator." + NTTObjectIdentifiers.id_camellia256_wrap, "org.bouncycastle2.jce.provider.symmetric.Camellia$KeyGen256");
      put("KeyGenerator." + NTTObjectIdentifiers.id_camellia128_cbc, "org.bouncycastle2.jce.provider.symmetric.Camellia$KeyGen128");
      put("KeyGenerator." + NTTObjectIdentifiers.id_camellia192_cbc, "org.bouncycastle2.jce.provider.symmetric.Camellia$KeyGen192");
      put("KeyGenerator." + NTTObjectIdentifiers.id_camellia256_cbc, "org.bouncycastle2.jce.provider.symmetric.Camellia$KeyGen256");
    }
  }

  public static class RFC3211Wrap extends WrapCipherSpi
  {
    public RFC3211Wrap()
    {
      super(16);
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