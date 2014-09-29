package org.bouncycastle2.jce.provider.symmetric;

import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.HashMap;
import javax.crypto.spec.IvParameterSpec;
import org.bouncycastle2.crypto.BufferedBlockCipher;
import org.bouncycastle2.crypto.CipherKeyGenerator;
import org.bouncycastle2.crypto.engines.RC6Engine;
import org.bouncycastle2.crypto.modes.CBCBlockCipher;
import org.bouncycastle2.crypto.modes.CFBBlockCipher;
import org.bouncycastle2.crypto.modes.OFBBlockCipher;
import org.bouncycastle2.jce.provider.BouncyCastleProvider;
import org.bouncycastle2.jce.provider.JCEBlockCipher;
import org.bouncycastle2.jce.provider.JCEKeyGenerator;
import org.bouncycastle2.jce.provider.JDKAlgorithmParameterGenerator;
import org.bouncycastle2.jce.provider.JDKAlgorithmParameters.IVAlgorithmParameters;

public final class RC6
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
        AlgorithmParameters localAlgorithmParameters = AlgorithmParameters.getInstance("RC6", BouncyCastleProvider.PROVIDER_NAME);
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
      throw new InvalidAlgorithmParameterException("No supported AlgorithmParameterSpec for RC6 parameter generation.");
    }
  }

  public static class AlgParams extends JDKAlgorithmParameters.IVAlgorithmParameters
  {
    protected String engineToString()
    {
      return "RC6 IV";
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
      super(256, new CipherKeyGenerator());
    }
  }

  public static class Mappings extends HashMap
  {
    public Mappings()
    {
      put("Cipher.RC6", "org.bouncycastle2.jce.provider.symmetric.RC6$ECB");
      put("KeyGenerator.RC6", "org.bouncycastle2.jce.provider.symmetric.RC6$KeyGen");
      put("AlgorithmParameters.RC6", "org.bouncycastle2.jce.provider.symmetric.RC6$AlgParams");
    }
  }

  public static class OFB extends JCEBlockCipher
  {
    public OFB()
    {
      super(128);
    }
  }
}