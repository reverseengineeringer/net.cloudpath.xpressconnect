package org.bouncycastle2.jce.provider.symmetric;

import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.HashMap;
import javax.crypto.spec.IvParameterSpec;
import org.bouncycastle2.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle2.crypto.BufferedBlockCipher;
import org.bouncycastle2.crypto.CipherKeyGenerator;
import org.bouncycastle2.crypto.engines.AESFastEngine;
import org.bouncycastle2.crypto.engines.AESWrapEngine;
import org.bouncycastle2.crypto.engines.RFC3211WrapEngine;
import org.bouncycastle2.crypto.macs.CMac;
import org.bouncycastle2.crypto.modes.CBCBlockCipher;
import org.bouncycastle2.crypto.modes.CFBBlockCipher;
import org.bouncycastle2.crypto.modes.OFBBlockCipher;
import org.bouncycastle2.jce.provider.BouncyCastleProvider;
import org.bouncycastle2.jce.provider.JCEBlockCipher;
import org.bouncycastle2.jce.provider.JCEKeyGenerator;
import org.bouncycastle2.jce.provider.JCEMac;
import org.bouncycastle2.jce.provider.JDKAlgorithmParameterGenerator;
import org.bouncycastle2.jce.provider.JDKAlgorithmParameters.IVAlgorithmParameters;
import org.bouncycastle2.jce.provider.WrapCipherSpi;

public final class AES
{
  public static class AESCMAC extends JCEMac
  {
    public AESCMAC()
    {
      super();
    }
  }

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
        AlgorithmParameters localAlgorithmParameters = AlgorithmParameters.getInstance("AES", BouncyCastleProvider.PROVIDER_NAME);
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
      throw new InvalidAlgorithmParameterException("No supported AlgorithmParameterSpec for AES parameter generation.");
    }
  }

  public static class AlgParams extends JDKAlgorithmParameters.IVAlgorithmParameters
  {
    protected String engineToString()
    {
      return "AES IV";
    }
  }

  public static class CBC extends JCEBlockCipher
  {
    public CBC()
    {
      super(128);
    }
  }

  public static class CFB extends JCEBlockCipher
  {
    public CFB()
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
      this(192);
    }

    public KeyGen(int paramInt)
    {
      super(paramInt, new CipherKeyGenerator());
    }
  }

  public static class KeyGen128 extends AES.KeyGen
  {
    public KeyGen128()
    {
      super();
    }
  }

  public static class KeyGen192 extends AES.KeyGen
  {
    public KeyGen192()
    {
      super();
    }
  }

  public static class KeyGen256 extends AES.KeyGen
  {
    public KeyGen256()
    {
      super();
    }
  }

  public static class Mappings extends HashMap
  {
    private static final String wrongAES128 = "2.16.840.1.101.3.4.2";
    private static final String wrongAES192 = "2.16.840.1.101.3.4.22";
    private static final String wrongAES256 = "2.16.840.1.101.3.4.42";

    public Mappings()
    {
      put("AlgorithmParameters.AES", "org.bouncycastle2.jce.provider.symmetric.AES$AlgParams");
      put("Alg.Alias.AlgorithmParameters.2.16.840.1.101.3.4.2", "AES");
      put("Alg.Alias.AlgorithmParameters.2.16.840.1.101.3.4.22", "AES");
      put("Alg.Alias.AlgorithmParameters.2.16.840.1.101.3.4.42", "AES");
      put("Alg.Alias.AlgorithmParameters." + NISTObjectIdentifiers.id_aes128_CBC, "AES");
      put("Alg.Alias.AlgorithmParameters." + NISTObjectIdentifiers.id_aes192_CBC, "AES");
      put("Alg.Alias.AlgorithmParameters." + NISTObjectIdentifiers.id_aes256_CBC, "AES");
      put("AlgorithmParameterGenerator.AES", "org.bouncycastle2.jce.provider.symmetric.AES$AlgParamGen");
      put("Alg.Alias.AlgorithmParameterGenerator.2.16.840.1.101.3.4.2", "AES");
      put("Alg.Alias.AlgorithmParameterGenerator.2.16.840.1.101.3.4.22", "AES");
      put("Alg.Alias.AlgorithmParameterGenerator.2.16.840.1.101.3.4.42", "AES");
      put("Alg.Alias.AlgorithmParameterGenerator." + NISTObjectIdentifiers.id_aes128_CBC, "AES");
      put("Alg.Alias.AlgorithmParameterGenerator." + NISTObjectIdentifiers.id_aes192_CBC, "AES");
      put("Alg.Alias.AlgorithmParameterGenerator." + NISTObjectIdentifiers.id_aes256_CBC, "AES");
      put("Cipher.AES", "org.bouncycastle2.jce.provider.symmetric.AES$ECB");
      put("Alg.Alias.Cipher.2.16.840.1.101.3.4.2", "AES");
      put("Alg.Alias.Cipher.2.16.840.1.101.3.4.22", "AES");
      put("Alg.Alias.Cipher.2.16.840.1.101.3.4.42", "AES");
      put("Cipher." + NISTObjectIdentifiers.id_aes128_ECB, "org.bouncycastle2.jce.provider.symmetric.AES$ECB");
      put("Cipher." + NISTObjectIdentifiers.id_aes192_ECB, "org.bouncycastle2.jce.provider.symmetric.AES$ECB");
      put("Cipher." + NISTObjectIdentifiers.id_aes256_ECB, "org.bouncycastle2.jce.provider.symmetric.AES$ECB");
      put("Cipher." + NISTObjectIdentifiers.id_aes128_CBC, "org.bouncycastle2.jce.provider.symmetric.AES$CBC");
      put("Cipher." + NISTObjectIdentifiers.id_aes192_CBC, "org.bouncycastle2.jce.provider.symmetric.AES$CBC");
      put("Cipher." + NISTObjectIdentifiers.id_aes256_CBC, "org.bouncycastle2.jce.provider.symmetric.AES$CBC");
      put("Cipher." + NISTObjectIdentifiers.id_aes128_OFB, "org.bouncycastle2.jce.provider.symmetric.AES$OFB");
      put("Cipher." + NISTObjectIdentifiers.id_aes192_OFB, "org.bouncycastle2.jce.provider.symmetric.AES$OFB");
      put("Cipher." + NISTObjectIdentifiers.id_aes256_OFB, "org.bouncycastle2.jce.provider.symmetric.AES$OFB");
      put("Cipher." + NISTObjectIdentifiers.id_aes128_CFB, "org.bouncycastle2.jce.provider.symmetric.AES$CFB");
      put("Cipher." + NISTObjectIdentifiers.id_aes192_CFB, "org.bouncycastle2.jce.provider.symmetric.AES$CFB");
      put("Cipher." + NISTObjectIdentifiers.id_aes256_CFB, "org.bouncycastle2.jce.provider.symmetric.AES$CFB");
      put("Cipher.AESWRAP", "org.bouncycastle2.jce.provider.symmetric.AES$Wrap");
      put("Alg.Alias.Cipher." + NISTObjectIdentifiers.id_aes128_wrap, "AESWRAP");
      put("Alg.Alias.Cipher." + NISTObjectIdentifiers.id_aes192_wrap, "AESWRAP");
      put("Alg.Alias.Cipher." + NISTObjectIdentifiers.id_aes256_wrap, "AESWRAP");
      put("Cipher.AESRFC3211WRAP", "org.bouncycastle2.jce.provider.symmetric.AES$RFC3211Wrap");
      put("KeyGenerator.AES", "org.bouncycastle2.jce.provider.symmetric.AES$KeyGen");
      put("KeyGenerator.2.16.840.1.101.3.4.2", "org.bouncycastle2.jce.provider.symmetric.AES$KeyGen128");
      put("KeyGenerator.2.16.840.1.101.3.4.22", "org.bouncycastle2.jce.provider.symmetric.AES$KeyGen192");
      put("KeyGenerator.2.16.840.1.101.3.4.42", "org.bouncycastle2.jce.provider.symmetric.AES$KeyGen256");
      put("KeyGenerator." + NISTObjectIdentifiers.id_aes128_ECB, "org.bouncycastle2.jce.provider.symmetric.AES$KeyGen128");
      put("KeyGenerator." + NISTObjectIdentifiers.id_aes128_CBC, "org.bouncycastle2.jce.provider.symmetric.AES$KeyGen128");
      put("KeyGenerator." + NISTObjectIdentifiers.id_aes128_OFB, "org.bouncycastle2.jce.provider.symmetric.AES$KeyGen128");
      put("KeyGenerator." + NISTObjectIdentifiers.id_aes128_CFB, "org.bouncycastle2.jce.provider.symmetric.AES$KeyGen128");
      put("KeyGenerator." + NISTObjectIdentifiers.id_aes192_ECB, "org.bouncycastle2.jce.provider.symmetric.AES$KeyGen192");
      put("KeyGenerator." + NISTObjectIdentifiers.id_aes192_CBC, "org.bouncycastle2.jce.provider.symmetric.AES$KeyGen192");
      put("KeyGenerator." + NISTObjectIdentifiers.id_aes192_OFB, "org.bouncycastle2.jce.provider.symmetric.AES$KeyGen192");
      put("KeyGenerator." + NISTObjectIdentifiers.id_aes192_CFB, "org.bouncycastle2.jce.provider.symmetric.AES$KeyGen192");
      put("KeyGenerator." + NISTObjectIdentifiers.id_aes256_ECB, "org.bouncycastle2.jce.provider.symmetric.AES$KeyGen256");
      put("KeyGenerator." + NISTObjectIdentifiers.id_aes256_CBC, "org.bouncycastle2.jce.provider.symmetric.AES$KeyGen256");
      put("KeyGenerator." + NISTObjectIdentifiers.id_aes256_OFB, "org.bouncycastle2.jce.provider.symmetric.AES$KeyGen256");
      put("KeyGenerator." + NISTObjectIdentifiers.id_aes256_CFB, "org.bouncycastle2.jce.provider.symmetric.AES$KeyGen256");
      put("KeyGenerator.AESWRAP", "org.bouncycastle2.jce.provider.symmetric.AES$KeyGen");
      put("KeyGenerator." + NISTObjectIdentifiers.id_aes128_wrap, "org.bouncycastle2.jce.provider.symmetric.AES$KeyGen128");
      put("KeyGenerator." + NISTObjectIdentifiers.id_aes192_wrap, "org.bouncycastle2.jce.provider.symmetric.AES$KeyGen192");
      put("KeyGenerator." + NISTObjectIdentifiers.id_aes256_wrap, "org.bouncycastle2.jce.provider.symmetric.AES$KeyGen256");
      put("Mac.AESCMAC", "org.bouncycastle2.jce.provider.symmetric.AES$AESCMAC");
    }
  }

  public static class OFB extends JCEBlockCipher
  {
    public OFB()
    {
      super(128);
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