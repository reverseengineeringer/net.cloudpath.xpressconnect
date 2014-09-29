package org.bouncycastle2.jce.provider.symmetric;

import java.util.HashMap;
import org.bouncycastle2.crypto.CipherKeyGenerator;
import org.bouncycastle2.crypto.engines.TwofishEngine;
import org.bouncycastle2.jce.provider.JCEBlockCipher;
import org.bouncycastle2.jce.provider.JCEKeyGenerator;
import org.bouncycastle2.jce.provider.JDKAlgorithmParameters.IVAlgorithmParameters;

public final class Twofish
{
  public static class AlgParams extends JDKAlgorithmParameters.IVAlgorithmParameters
  {
    protected String engineToString()
    {
      return "Twofish IV";
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
      put("Cipher.Twofish", "org.bouncycastle2.jce.provider.symmetric.Twofish$ECB");
      put("KeyGenerator.Twofish", "org.bouncycastle2.jce.provider.symmetric.Twofish$KeyGen");
      put("AlgorithmParameters.Twofish", "org.bouncycastle2.jce.provider.symmetric.Twofish$AlgParams");
    }
  }
}