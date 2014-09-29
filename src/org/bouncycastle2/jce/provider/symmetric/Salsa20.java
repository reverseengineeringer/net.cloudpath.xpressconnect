package org.bouncycastle2.jce.provider.symmetric;

import java.util.HashMap;
import org.bouncycastle2.crypto.CipherKeyGenerator;
import org.bouncycastle2.crypto.engines.Salsa20Engine;
import org.bouncycastle2.jce.provider.JCEKeyGenerator;
import org.bouncycastle2.jce.provider.JCEStreamCipher;

public final class Salsa20
{
  public static class Base extends JCEStreamCipher
  {
    public Base()
    {
      super(8);
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
      put("Cipher.SALSA20", "org.bouncycastle2.jce.provider.symmetric.Salsa20$Base");
      put("KeyGenerator.SALSA20", "org.bouncycastle2.jce.provider.symmetric.Salsa20$KeyGen");
    }
  }
}