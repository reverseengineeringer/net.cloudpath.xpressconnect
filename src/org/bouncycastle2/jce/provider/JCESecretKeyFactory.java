package org.bouncycastle2.jce.provider;

import java.lang.reflect.Constructor;
import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactorySpi;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.params.DESParameters;
import org.bouncycastle2.crypto.params.KeyParameter;
import org.bouncycastle2.crypto.params.ParametersWithIV;

public class JCESecretKeyFactory extends SecretKeyFactorySpi
  implements PBE
{
  protected String algName;
  protected DERObjectIdentifier algOid;

  protected JCESecretKeyFactory(String paramString, DERObjectIdentifier paramDERObjectIdentifier)
  {
    this.algName = paramString;
    this.algOid = paramDERObjectIdentifier;
  }

  protected SecretKey engineGenerateSecret(KeySpec paramKeySpec)
    throws InvalidKeySpecException
  {
    if ((paramKeySpec instanceof SecretKeySpec))
      return (SecretKey)paramKeySpec;
    throw new InvalidKeySpecException("Invalid KeySpec");
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
    try
    {
      Constructor localConstructor = paramClass.getConstructor(new Class[] { [B.class });
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = paramSecretKey.getEncoded();
      KeySpec localKeySpec = (KeySpec)localConstructor.newInstance(arrayOfObject);
      return localKeySpec;
    }
    catch (Exception localException)
    {
      throw new InvalidKeySpecException(localException.toString());
    }
  }

  protected SecretKey engineTranslateKey(SecretKey paramSecretKey)
    throws InvalidKeyException
  {
    if (paramSecretKey == null)
      throw new InvalidKeyException("key parameter is null");
    if (!paramSecretKey.getAlgorithm().equalsIgnoreCase(this.algName))
      throw new InvalidKeyException("Key not of type " + this.algName + ".");
    return new SecretKeySpec(paramSecretKey.getEncoded(), this.algName);
  }

  public static class DES extends JCESecretKeyFactory
  {
    public DES()
    {
      super(null);
    }

    protected SecretKey engineGenerateSecret(KeySpec paramKeySpec)
      throws InvalidKeySpecException
    {
      if ((paramKeySpec instanceof DESKeySpec))
        return new SecretKeySpec(((DESKeySpec)paramKeySpec).getKey(), "DES");
      return super.engineGenerateSecret(paramKeySpec);
    }
  }

  public static class DESPBEKeyFactory extends JCESecretKeyFactory
  {
    private int digest;
    private boolean forCipher;
    private int ivSize;
    private int keySize;
    private int scheme;

    public DESPBEKeyFactory(String paramString, DERObjectIdentifier paramDERObjectIdentifier, boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      super(paramDERObjectIdentifier);
      this.forCipher = paramBoolean;
      this.scheme = paramInt1;
      this.digest = paramInt2;
      this.keySize = paramInt3;
      this.ivSize = paramInt4;
    }

    protected SecretKey engineGenerateSecret(KeySpec paramKeySpec)
      throws InvalidKeySpecException
    {
      if ((paramKeySpec instanceof PBEKeySpec))
      {
        PBEKeySpec localPBEKeySpec = (PBEKeySpec)paramKeySpec;
        if (localPBEKeySpec.getSalt() == null)
          return new JCEPBEKey(this.algName, this.algOid, this.scheme, this.digest, this.keySize, this.ivSize, localPBEKeySpec, null);
        CipherParameters localCipherParameters;
        if (this.forCipher)
        {
          localCipherParameters = PBE.Util.makePBEParameters(localPBEKeySpec, this.scheme, this.digest, this.keySize, this.ivSize);
          if (!(localCipherParameters instanceof ParametersWithIV))
            break label162;
        }
        label162: for (KeyParameter localKeyParameter = (KeyParameter)((ParametersWithIV)localCipherParameters).getParameters(); ; localKeyParameter = (KeyParameter)localCipherParameters)
        {
          DESParameters.setOddParity(localKeyParameter.getKey());
          return new JCEPBEKey(this.algName, this.algOid, this.scheme, this.digest, this.keySize, this.ivSize, localPBEKeySpec, localCipherParameters);
          localCipherParameters = PBE.Util.makePBEMacParameters(localPBEKeySpec, this.scheme, this.digest, this.keySize);
          break;
        }
      }
      throw new InvalidKeySpecException("Invalid KeySpec");
    }
  }

  public static class PBEKeyFactory extends JCESecretKeyFactory
  {
    private int digest;
    private boolean forCipher;
    private int ivSize;
    private int keySize;
    private int scheme;

    public PBEKeyFactory(String paramString, DERObjectIdentifier paramDERObjectIdentifier, boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      super(paramDERObjectIdentifier);
      this.forCipher = paramBoolean;
      this.scheme = paramInt1;
      this.digest = paramInt2;
      this.keySize = paramInt3;
      this.ivSize = paramInt4;
    }

    protected SecretKey engineGenerateSecret(KeySpec paramKeySpec)
      throws InvalidKeySpecException
    {
      if ((paramKeySpec instanceof PBEKeySpec))
      {
        PBEKeySpec localPBEKeySpec = (PBEKeySpec)paramKeySpec;
        if (localPBEKeySpec.getSalt() == null)
          return new JCEPBEKey(this.algName, this.algOid, this.scheme, this.digest, this.keySize, this.ivSize, localPBEKeySpec, null);
        if (this.forCipher);
        for (CipherParameters localCipherParameters = PBE.Util.makePBEParameters(localPBEKeySpec, this.scheme, this.digest, this.keySize, this.ivSize); ; localCipherParameters = PBE.Util.makePBEMacParameters(localPBEKeySpec, this.scheme, this.digest, this.keySize))
          return new JCEPBEKey(this.algName, this.algOid, this.scheme, this.digest, this.keySize, this.ivSize, localPBEKeySpec, localCipherParameters);
      }
      throw new InvalidKeySpecException("Invalid KeySpec");
    }
  }

  public static class PBEWithMD2AndDES extends JCESecretKeyFactory.DESPBEKeyFactory
  {
    public PBEWithMD2AndDES()
    {
      super(PKCSObjectIdentifiers.pbeWithMD2AndDES_CBC, true, 0, 5, 64, 64);
    }
  }

  public static class PBEWithMD2AndRC2 extends JCESecretKeyFactory.PBEKeyFactory
  {
    public PBEWithMD2AndRC2()
    {
      super(PKCSObjectIdentifiers.pbeWithMD2AndRC2_CBC, true, 0, 5, 64, 64);
    }
  }

  public static class PBEWithMD5And128BitAESCBCOpenSSL extends JCESecretKeyFactory.PBEKeyFactory
  {
    public PBEWithMD5And128BitAESCBCOpenSSL()
    {
      super(null, true, 3, 0, 128, 128);
    }
  }

  public static class PBEWithMD5And192BitAESCBCOpenSSL extends JCESecretKeyFactory.PBEKeyFactory
  {
    public PBEWithMD5And192BitAESCBCOpenSSL()
    {
      super(null, true, 3, 0, 192, 128);
    }
  }

  public static class PBEWithMD5And256BitAESCBCOpenSSL extends JCESecretKeyFactory.PBEKeyFactory
  {
    public PBEWithMD5And256BitAESCBCOpenSSL()
    {
      super(null, true, 3, 0, 256, 128);
    }
  }

  public static class PBEWithMD5AndDES extends JCESecretKeyFactory.DESPBEKeyFactory
  {
    public PBEWithMD5AndDES()
    {
      super(PKCSObjectIdentifiers.pbeWithMD5AndDES_CBC, true, 0, 0, 64, 64);
    }
  }

  public static class PBEWithMD5AndRC2 extends JCESecretKeyFactory.PBEKeyFactory
  {
    public PBEWithMD5AndRC2()
    {
      super(PKCSObjectIdentifiers.pbeWithMD5AndRC2_CBC, true, 0, 0, 64, 64);
    }
  }

  public static class PBEWithRIPEMD160 extends JCESecretKeyFactory.PBEKeyFactory
  {
    public PBEWithRIPEMD160()
    {
      super(null, false, 2, 2, 160, 0);
    }
  }

  public static class PBEWithSHA extends JCESecretKeyFactory.PBEKeyFactory
  {
    public PBEWithSHA()
    {
      super(null, false, 2, 1, 160, 0);
    }
  }

  public static class PBEWithSHA1AndDES extends JCESecretKeyFactory.DESPBEKeyFactory
  {
    public PBEWithSHA1AndDES()
    {
      super(PKCSObjectIdentifiers.pbeWithSHA1AndDES_CBC, true, 0, 1, 64, 64);
    }
  }

  public static class PBEWithSHA1AndRC2 extends JCESecretKeyFactory.PBEKeyFactory
  {
    public PBEWithSHA1AndRC2()
    {
      super(PKCSObjectIdentifiers.pbeWithSHA1AndRC2_CBC, true, 0, 1, 64, 64);
    }
  }

  public static class PBEWithSHA256And128BitAESBC extends JCESecretKeyFactory.PBEKeyFactory
  {
    public PBEWithSHA256And128BitAESBC()
    {
      super(null, true, 2, 4, 128, 128);
    }
  }

  public static class PBEWithSHA256And192BitAESBC extends JCESecretKeyFactory.PBEKeyFactory
  {
    public PBEWithSHA256And192BitAESBC()
    {
      super(null, true, 2, 4, 192, 128);
    }
  }

  public static class PBEWithSHA256And256BitAESBC extends JCESecretKeyFactory.PBEKeyFactory
  {
    public PBEWithSHA256And256BitAESBC()
    {
      super(null, true, 2, 4, 256, 128);
    }
  }

  public static class PBEWithSHAAnd128BitAESBC extends JCESecretKeyFactory.PBEKeyFactory
  {
    public PBEWithSHAAnd128BitAESBC()
    {
      super(null, true, 2, 1, 128, 128);
    }
  }

  public static class PBEWithSHAAnd128BitRC2 extends JCESecretKeyFactory.PBEKeyFactory
  {
    public PBEWithSHAAnd128BitRC2()
    {
      super(PKCSObjectIdentifiers.pbeWithSHAAnd128BitRC2_CBC, true, 2, 1, 128, 64);
    }
  }

  public static class PBEWithSHAAnd128BitRC4 extends JCESecretKeyFactory.PBEKeyFactory
  {
    public PBEWithSHAAnd128BitRC4()
    {
      super(PKCSObjectIdentifiers.pbeWithSHAAnd128BitRC4, true, 2, 1, 128, 0);
    }
  }

  public static class PBEWithSHAAnd192BitAESBC extends JCESecretKeyFactory.PBEKeyFactory
  {
    public PBEWithSHAAnd192BitAESBC()
    {
      super(null, true, 2, 1, 192, 128);
    }
  }

  public static class PBEWithSHAAnd256BitAESBC extends JCESecretKeyFactory.PBEKeyFactory
  {
    public PBEWithSHAAnd256BitAESBC()
    {
      super(null, true, 2, 1, 256, 128);
    }
  }

  public static class PBEWithSHAAnd40BitRC2 extends JCESecretKeyFactory.PBEKeyFactory
  {
    public PBEWithSHAAnd40BitRC2()
    {
      super(PKCSObjectIdentifiers.pbewithSHAAnd40BitRC2_CBC, true, 2, 1, 40, 64);
    }
  }

  public static class PBEWithSHAAnd40BitRC4 extends JCESecretKeyFactory.PBEKeyFactory
  {
    public PBEWithSHAAnd40BitRC4()
    {
      super(PKCSObjectIdentifiers.pbeWithSHAAnd128BitRC4, true, 2, 1, 40, 0);
    }
  }

  public static class PBEWithSHAAndDES2Key extends JCESecretKeyFactory.DESPBEKeyFactory
  {
    public PBEWithSHAAndDES2Key()
    {
      super(PKCSObjectIdentifiers.pbeWithSHAAnd2_KeyTripleDES_CBC, true, 2, 1, 128, 64);
    }
  }

  public static class PBEWithSHAAndDES3Key extends JCESecretKeyFactory.DESPBEKeyFactory
  {
    public PBEWithSHAAndDES3Key()
    {
      super(PKCSObjectIdentifiers.pbeWithSHAAnd3_KeyTripleDES_CBC, true, 2, 1, 192, 64);
    }
  }

  public static class PBEWithSHAAndTwofish extends JCESecretKeyFactory.PBEKeyFactory
  {
    public PBEWithSHAAndTwofish()
    {
      super(null, true, 2, 1, 256, 128);
    }
  }

  public static class PBEWithTiger extends JCESecretKeyFactory.PBEKeyFactory
  {
    public PBEWithTiger()
    {
      super(null, false, 2, 3, 192, 0);
    }
  }
}