package org.bouncycastle2.jce.provider.symmetric;

import java.util.HashMap;
import org.bouncycastle2.crypto.CipherKeyGenerator;
import org.bouncycastle2.crypto.engines.Grainv1Engine;
import org.bouncycastle2.jce.provider.JCEKeyGenerator;
import org.bouncycastle2.jce.provider.JCEStreamCipher;

public final class Grainv1
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
      super(80, new CipherKeyGenerator());
    }
  }

  public static class Mappings extends HashMap
  {
    public Mappings()
    {
      put("Cipher.Grainv1", "org.bouncycastle2.jce.provider.symmetric.Grainv1$Base");
      put("KeyGenerator.Grainv1", "org.bouncycastle2.jce.provider.symmetric.Grainv1$KeyGen");
    }
  }
}