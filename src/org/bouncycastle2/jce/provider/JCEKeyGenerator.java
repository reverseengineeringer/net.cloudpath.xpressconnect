package org.bouncycastle2.jce.provider;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidParameterException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.KeyGeneratorSpi;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle2.crypto.CipherKeyGenerator;
import org.bouncycastle2.crypto.KeyGenerationParameters;
import org.bouncycastle2.crypto.generators.DESKeyGenerator;

public class JCEKeyGenerator extends KeyGeneratorSpi
{
  protected String algName;
  protected int defaultKeySize;
  protected CipherKeyGenerator engine;
  protected int keySize;
  protected boolean uninitialised = true;

  protected JCEKeyGenerator(String paramString, int paramInt, CipherKeyGenerator paramCipherKeyGenerator)
  {
    this.algName = paramString;
    this.defaultKeySize = paramInt;
    this.keySize = paramInt;
    this.engine = paramCipherKeyGenerator;
  }

  protected SecretKey engineGenerateKey()
  {
    if (this.uninitialised)
    {
      this.engine.init(new KeyGenerationParameters(new SecureRandom(), this.defaultKeySize));
      this.uninitialised = false;
    }
    return new SecretKeySpec(this.engine.generateKey(), this.algName);
  }

  protected void engineInit(int paramInt, SecureRandom paramSecureRandom)
  {
    try
    {
      this.engine.init(new KeyGenerationParameters(paramSecureRandom, paramInt));
      this.uninitialised = false;
      return;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      throw new InvalidParameterException(localIllegalArgumentException.getMessage());
    }
  }

  protected void engineInit(SecureRandom paramSecureRandom)
  {
    if (paramSecureRandom != null)
    {
      this.engine.init(new KeyGenerationParameters(paramSecureRandom, this.defaultKeySize));
      this.uninitialised = false;
    }
  }

  protected void engineInit(AlgorithmParameterSpec paramAlgorithmParameterSpec, SecureRandom paramSecureRandom)
    throws InvalidAlgorithmParameterException
  {
    throw new InvalidAlgorithmParameterException("Not Implemented");
  }

  public static class DES extends JCEKeyGenerator
  {
    public DES()
    {
      super(64, new DESKeyGenerator());
    }
  }

  public static class GOST28147 extends JCEKeyGenerator
  {
    public GOST28147()
    {
      super(256, new CipherKeyGenerator());
    }
  }

  public static class HMACSHA1 extends JCEKeyGenerator
  {
    public HMACSHA1()
    {
      super(160, new CipherKeyGenerator());
    }
  }

  public static class HMACSHA224 extends JCEKeyGenerator
  {
    public HMACSHA224()
    {
      super(224, new CipherKeyGenerator());
    }
  }

  public static class HMACSHA256 extends JCEKeyGenerator
  {
    public HMACSHA256()
    {
      super(256, new CipherKeyGenerator());
    }
  }

  public static class HMACSHA384 extends JCEKeyGenerator
  {
    public HMACSHA384()
    {
      super(384, new CipherKeyGenerator());
    }
  }

  public static class HMACSHA512 extends JCEKeyGenerator
  {
    public HMACSHA512()
    {
      super(512, new CipherKeyGenerator());
    }
  }

  public static class HMACTIGER extends JCEKeyGenerator
  {
    public HMACTIGER()
    {
      super(192, new CipherKeyGenerator());
    }
  }

  public static class MD2HMAC extends JCEKeyGenerator
  {
    public MD2HMAC()
    {
      super(128, new CipherKeyGenerator());
    }
  }

  public static class MD4HMAC extends JCEKeyGenerator
  {
    public MD4HMAC()
    {
      super(128, new CipherKeyGenerator());
    }
  }

  public static class MD5HMAC extends JCEKeyGenerator
  {
    public MD5HMAC()
    {
      super(128, new CipherKeyGenerator());
    }
  }

  public static class RC2 extends JCEKeyGenerator
  {
    public RC2()
    {
      super(128, new CipherKeyGenerator());
    }
  }

  public static class RIPEMD128HMAC extends JCEKeyGenerator
  {
    public RIPEMD128HMAC()
    {
      super(128, new CipherKeyGenerator());
    }
  }

  public static class RIPEMD160HMAC extends JCEKeyGenerator
  {
    public RIPEMD160HMAC()
    {
      super(160, new CipherKeyGenerator());
    }
  }
}