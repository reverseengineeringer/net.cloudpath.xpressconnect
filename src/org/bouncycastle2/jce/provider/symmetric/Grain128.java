package org.bouncycastle2.jce.provider.symmetric;

import java.util.HashMap;
import org.bouncycastle2.crypto.CipherKeyGenerator;
import org.bouncycastle2.crypto.engines.Grain128Engine;
import org.bouncycastle2.jce.provider.JCEKeyGenerator;
import org.bouncycastle2.jce.provider.JCEStreamCipher;

public final class Grain128
{
  public static class Base extends JCEStreamCipher
  {
    public Base()
    {
      super(12);
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
      put("Cipher.Grain128", "org.bouncycastle2.jce.provider.symmetric.Grain128$Base");
      put("KeyGenerator.Grain128", "org.bouncycastle2.jce.provider.symmetric.Grain128$KeyGen");
    }
  }
}