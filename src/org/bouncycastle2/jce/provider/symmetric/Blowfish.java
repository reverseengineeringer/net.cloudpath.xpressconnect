package org.bouncycastle2.jce.provider.symmetric;

import java.util.HashMap;
import org.bouncycastle2.crypto.CipherKeyGenerator;
import org.bouncycastle2.crypto.engines.BlowfishEngine;
import org.bouncycastle2.crypto.modes.CBCBlockCipher;
import org.bouncycastle2.jce.provider.JCEBlockCipher;
import org.bouncycastle2.jce.provider.JCEKeyGenerator;
import org.bouncycastle2.jce.provider.JDKAlgorithmParameters.IVAlgorithmParameters;

public final class Blowfish
{
  public static class AlgParams extends JDKAlgorithmParameters.IVAlgorithmParameters
  {
    protected String engineToString()
    {
      return "Blowfish IV";
    }
  }

  public static class CBC extends JCEBlockCipher
  {
    public CBC()
    {
      super(64);
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
      put("Cipher.BLOWFISH", "org.bouncycastle2.jce.provider.symmetric.Blowfish$ECB");
      put("Cipher.1.3.6.1.4.1.3029.1.2", "org.bouncycastle2.jce.provider.symmetric.Blowfish$CBC");
      put("KeyGenerator.BLOWFISH", "org.bouncycastle2.jce.provider.symmetric.Blowfish$KeyGen");
      put("Alg.Alias.KeyGenerator.1.3.6.1.4.1.3029.1.2", "BLOWFISH");
      put("AlgorithmParameters.BLOWFISH", "org.bouncycastle2.jce.provider.symmetric.Blowfish$AlgParams");
      put("Alg.Alias.AlgorithmParameters.1.3.6.1.4.1.3029.1.2", "BLOWFISH");
    }
  }
}