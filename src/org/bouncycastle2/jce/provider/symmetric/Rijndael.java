package org.bouncycastle2.jce.provider.symmetric;

import java.util.HashMap;
import org.bouncycastle2.crypto.CipherKeyGenerator;
import org.bouncycastle2.crypto.engines.RijndaelEngine;
import org.bouncycastle2.jce.provider.JCEBlockCipher;
import org.bouncycastle2.jce.provider.JCEKeyGenerator;
import org.bouncycastle2.jce.provider.JDKAlgorithmParameters.IVAlgorithmParameters;

public final class Rijndael
{
  public static class AlgParams extends JDKAlgorithmParameters.IVAlgorithmParameters
  {
    protected String engineToString()
    {
      return "Rijndael IV";
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
      put("Cipher.RIJNDAEL", "org.bouncycastle2.jce.provider.symmetric.Rijndael$ECB");
      put("KeyGenerator.RIJNDAEL", "org.bouncycastle2.jce.provider.symmetric.Rijndael$KeyGen");
      put("AlgorithmParameters.RIJNDAEL", "org.bouncycastle2.jce.provider.symmetric.Rijndael$AlgParams");
    }
  }
}