package org.bouncycastle2.jce.provider.symmetric;

import java.util.HashMap;
import org.bouncycastle2.crypto.CipherKeyGenerator;
import org.bouncycastle2.crypto.engines.SerpentEngine;
import org.bouncycastle2.jce.provider.JCEBlockCipher;
import org.bouncycastle2.jce.provider.JCEKeyGenerator;
import org.bouncycastle2.jce.provider.JDKAlgorithmParameters.IVAlgorithmParameters;

public final class Serpent
{
  public static class AlgParams extends JDKAlgorithmParameters.IVAlgorithmParameters
  {
    protected String engineToString()
    {
      return "Serpent IV";
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
      super(192, new CipherKeyGenerator());
    }
  }

  public static class Mappings extends HashMap
  {
    public Mappings()
    {
      put("Cipher.Serpent", "org.bouncycastle2.jce.provider.symmetric.Serpent$ECB");
      put("KeyGenerator.Serpent", "org.bouncycastle2.jce.provider.symmetric.Serpent$KeyGen");
      put("AlgorithmParameters.Serpent", "org.bouncycastle2.jce.provider.symmetric.Serpent$AlgParams");
    }
  }
}