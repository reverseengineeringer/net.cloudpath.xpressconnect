package org.bouncycastle2.jce.provider.symmetric;

import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.HashMap;
import javax.crypto.SecretKey;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle2.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle2.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle2.crypto.CipherKeyGenerator;
import org.bouncycastle2.crypto.KeyGenerationParameters;
import org.bouncycastle2.crypto.engines.DESedeEngine;
import org.bouncycastle2.crypto.engines.DESedeWrapEngine;
import org.bouncycastle2.crypto.engines.RFC3211WrapEngine;
import org.bouncycastle2.crypto.generators.DESedeKeyGenerator;
import org.bouncycastle2.crypto.macs.CBCBlockCipherMac;
import org.bouncycastle2.crypto.macs.CFBBlockCipherMac;
import org.bouncycastle2.crypto.macs.CMac;
import org.bouncycastle2.crypto.modes.CBCBlockCipher;
import org.bouncycastle2.crypto.paddings.ISO7816d4Padding;
import org.bouncycastle2.jce.provider.JCEBlockCipher;
import org.bouncycastle2.jce.provider.JCEKeyGenerator;
import org.bouncycastle2.jce.provider.JCEMac;
import org.bouncycastle2.jce.provider.JCESecretKeyFactory;
import org.bouncycastle2.jce.provider.WrapCipherSpi;

public final class DESede
{
  public static class CBC extends JCEBlockCipher
  {
    public CBC()
    {
      super(64);
    }
  }

  public static class CBCMAC extends JCEMac
  {
    public CBCMAC()
    {
      super();
    }
  }

  public static class CMAC extends JCEMac
  {
    public CMAC()
    {
      super();
    }
  }

  public static class DESede64 extends JCEMac
  {
    public DESede64()
    {
      super();
    }
  }

  public static class DESede64with7816d4 extends JCEMac
  {
    public DESede64with7816d4()
    {
      super();
    }
  }

  public static class DESedeCFB8 extends JCEMac
  {
    public DESedeCFB8()
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

  public static class KeyFactory extends JCESecretKeyFactory
  {
    public KeyFactory()
    {
      super(null);
    }

    protected SecretKey engineGenerateSecret(KeySpec paramKeySpec)
      throws InvalidKeySpecException
    {
      if ((paramKeySpec instanceof DESedeKeySpec))
        return new SecretKeySpec(((DESedeKeySpec)paramKeySpec).getKey(), "DESede");
      return super.engineGenerateSecret(paramKeySpec);
    }

    protected KeySpec engineGetKeySpec(SecretKey paramSecretKey, Class paramClass)
      throws InvalidKeySpecException
    {
      if (paramClass == null)
        throw new InvalidKeySpecException("keySpec parameter is null");
      if (paramSecretKey == null)
        throw new InvalidKeySpecException("key parameter is null");
      if (SecretKeySpec.class.isAssignableFrom(paramClass))
        return new SecretKeySpec(paramSecretKey.getEncoded(), this.algName);
      if (DESedeKeySpec.class.isAssignableFrom(paramClass))
      {
        byte[] arrayOfByte1 = paramSecretKey.getEncoded();
        try
        {
          if (arrayOfByte1.length == 16)
          {
            byte[] arrayOfByte2 = new byte[24];
            System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, 16);
            System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 16, 8);
            DESedeKeySpec localDESedeKeySpec2 = new DESedeKeySpec(arrayOfByte2);
            return localDESedeKeySpec2;
          }
        }
        catch (Exception localException)
        {
          throw new InvalidKeySpecException(localException.toString());
        }
        DESedeKeySpec localDESedeKeySpec1 = new DESedeKeySpec(arrayOfByte1);
        return localDESedeKeySpec1;
      }
      throw new InvalidKeySpecException("Invalid KeySpec");
    }
  }

  public static class KeyGenerator extends JCEKeyGenerator
  {
    private boolean keySizeSet = false;

    public KeyGenerator()
    {
      super(192, new DESedeKeyGenerator());
    }

    protected SecretKey engineGenerateKey()
    {
      if (this.uninitialised)
      {
        this.engine.init(new KeyGenerationParameters(new SecureRandom(), this.defaultKeySize));
        this.uninitialised = false;
      }
      if (!this.keySizeSet)
      {
        byte[] arrayOfByte = this.engine.generateKey();
        System.arraycopy(arrayOfByte, 0, arrayOfByte, 16, 8);
        return new SecretKeySpec(arrayOfByte, this.algName);
      }
      return new SecretKeySpec(this.engine.generateKey(), this.algName);
    }

    protected void engineInit(int paramInt, SecureRandom paramSecureRandom)
    {
      super.engineInit(paramInt, paramSecureRandom);
      this.keySizeSet = true;
    }
  }

  public static class KeyGenerator3 extends JCEKeyGenerator
  {
    public KeyGenerator3()
    {
      super(192, new DESedeKeyGenerator());
    }
  }

  public static class Mappings extends HashMap
  {
    public Mappings()
    {
      put("Cipher.DESEDE", "org.bouncycastle2.jce.provider.symmetric.DESede$ECB");
      put("Cipher." + PKCSObjectIdentifiers.des_EDE3_CBC, "org.bouncycastle2.jce.provider.symmetric.DESede$CBC");
      put("Cipher." + OIWObjectIdentifiers.desCBC, "org.bouncycastle2.jce.provider.symmetric.DESede$CBC");
      put("Cipher.DESEDEWRAP", "org.bouncycastle2.jce.provider.symmetric.DESede$Wrap");
      put("Cipher." + PKCSObjectIdentifiers.id_alg_CMS3DESwrap, "org.bouncycastle2.jce.provider.symmetric.DESede$Wrap");
      put("Cipher.DESEDERFC3211WRAP", "org.bouncycastle2.jce.provider.symmetric.DESede$RFC3211");
      put("KeyGenerator.DESEDE", "org.bouncycastle2.jce.provider.symmetric.DESede$KeyGenerator");
      put("KeyGenerator." + PKCSObjectIdentifiers.des_EDE3_CBC, "org.bouncycastle2.jce.provider.symmetric.DESede$KeyGenerator3");
      put("KeyGenerator.DESEDEWRAP", "org.bouncycastle2.jce.provider.symmetric.DESede$KeyGenerator");
      put("SecretKeyFactory.DESEDE", "org.bouncycastle2.jce.provider.symmetric.DESede$KeyFactory");
      put("Mac.DESEDECMAC", "org.bouncycastle2.jce.provider.symmetric.DESede$CMAC");
      put("Mac.DESEDEMAC", "org.bouncycastle2.jce.provider.symmetric.DESede$CBCMAC");
      put("Alg.Alias.Mac.DESEDE", "DESEDEMAC");
      put("Mac.DESEDEMAC/CFB8", "org.bouncycastle2.jce.provider.symmetric.DESede$DESedeCFB8");
      put("Alg.Alias.Mac.DESEDE/CFB8", "DESEDEMAC/CFB8");
      put("Mac.DESEDEMAC64", "org.bouncycastle2.jce.provider.symmetric.DESede$DESede64");
      put("Alg.Alias.Mac.DESEDE64", "DESEDEMAC64");
      put("Mac.DESEDEMAC64WITHISO7816-4PADDING", "org.bouncycastle2.jce.provider.symmetric.DESede$DESede64with7816d4");
      put("Alg.Alias.Mac.DESEDE64WITHISO7816-4PADDING", "DESEDEMAC64WITHISO7816-4PADDING");
      put("Alg.Alias.Mac.DESEDEISO9797ALG1MACWITHISO7816-4PADDING", "DESEDEMAC64WITHISO7816-4PADDING");
      put("Alg.Alias.Mac.DESEDEISO9797ALG1WITHISO7816-4PADDING", "DESEDEMAC64WITHISO7816-4PADDING");
    }
  }

  public static class RFC3211 extends WrapCipherSpi
  {
    public RFC3211()
    {
      super(8);
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