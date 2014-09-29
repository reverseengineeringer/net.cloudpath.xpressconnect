package org.bouncycastle2.jce.provider.symmetric;

import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.HashMap;
import javax.crypto.spec.IvParameterSpec;
import org.bouncycastle2.crypto.CipherKeyGenerator;
import org.bouncycastle2.crypto.engines.NoekeonEngine;
import org.bouncycastle2.jce.provider.BouncyCastleProvider;
import org.bouncycastle2.jce.provider.JCEBlockCipher;
import org.bouncycastle2.jce.provider.JCEKeyGenerator;
import org.bouncycastle2.jce.provider.JDKAlgorithmParameterGenerator;
import org.bouncycastle2.jce.provider.JDKAlgorithmParameters.IVAlgorithmParameters;

public final class Noekeon
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
        AlgorithmParameters localAlgorithmParameters = AlgorithmParameters.getInstance("Noekeon", BouncyCastleProvider.PROVIDER_NAME);
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
      throw new InvalidAlgorithmParameterException("No supported AlgorithmParameterSpec for Noekeon parameter generation.");
    }
  }

  public static class AlgParams extends JDKAlgorithmParameters.IVAlgorithmParameters
  {
    protected String engineToString()
    {
      return "Noekeon IV";
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
      put("AlgorithmParameters.NOEKEON", "org.bouncycastle2.jce.provider.symmetric.Noekeon$AlgParams");
      put("AlgorithmParameterGenerator.NOEKEON", "org.bouncycastle2.jce.provider.symmetric.Noekeon$AlgParamGen");
      put("Cipher.NOEKEON", "org.bouncycastle2.jce.provider.symmetric.Noekeon$ECB");
      put("KeyGenerator.NOEKEON", "org.bouncycastle2.jce.provider.symmetric.Noekeon$KeyGen");
    }
  }
}