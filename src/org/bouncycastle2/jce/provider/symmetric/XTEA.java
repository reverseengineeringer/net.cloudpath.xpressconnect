package org.bouncycastle2.jce.provider.symmetric;

import java.util.HashMap;
import org.bouncycastle2.crypto.CipherKeyGenerator;
import org.bouncycastle2.crypto.engines.XTEAEngine;
import org.bouncycastle2.jce.provider.JCEBlockCipher;
import org.bouncycastle2.jce.provider.JCEKeyGenerator;
import org.bouncycastle2.jce.provider.JDKAlgorithmParameters.IVAlgorithmParameters;

public final class XTEA
{
  public static class AlgParams extends JDKAlgorithmParameters.IVAlgorithmParameters
  {
    protected String engineToString()
    {
      return "XTEA IV";
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
      put("Cipher.XTEA", "org.bouncycastle2.jce.provider.symmetric.XTEA$ECB");
      put("KeyGenerator.XTEA", "org.bouncycastle2.jce.provider.symmetric.XTEA$KeyGen");
      put("AlgorithmParameters.XTEA", "org.bouncycastle2.jce.provider.symmetric.XTEA$AlgParams");
    }
  }
}