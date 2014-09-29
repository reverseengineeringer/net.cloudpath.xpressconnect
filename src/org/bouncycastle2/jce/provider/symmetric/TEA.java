package org.bouncycastle2.jce.provider.symmetric;

import java.util.HashMap;
import org.bouncycastle2.crypto.CipherKeyGenerator;
import org.bouncycastle2.crypto.engines.TEAEngine;
import org.bouncycastle2.jce.provider.JCEBlockCipher;
import org.bouncycastle2.jce.provider.JCEKeyGenerator;
import org.bouncycastle2.jce.provider.JDKAlgorithmParameters.IVAlgorithmParameters;

public final class TEA
{
  public static class AlgParams extends JDKAlgorithmParameters.IVAlgorithmParameters
  {
    protected String engineToString()
    {
      return "TEA IV";
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
      put("Cipher.TEA", "org.bouncycastle2.jce.provider.symmetric.TEA$ECB");
      put("KeyGenerator.TEA", "org.bouncycastle2.jce.provider.symmetric.TEA$KeyGen");
      put("AlgorithmParameters.TEA", "org.bouncycastle2.jce.provider.symmetric.TEA$AlgParams");
    }
  }
}