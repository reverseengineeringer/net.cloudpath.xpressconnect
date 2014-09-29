package org.bouncycastle2.jce.provider.symmetric;

import java.util.HashMap;
import org.bouncycastle2.crypto.CipherKeyGenerator;
import org.bouncycastle2.crypto.engines.VMPCKSA3Engine;
import org.bouncycastle2.jce.provider.JCEKeyGenerator;
import org.bouncycastle2.jce.provider.JCEStreamCipher;

public final class VMPCKSA3
{
  public static class Base extends JCEStreamCipher
  {
    public Base()
    {
      super(16);
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
      put("Cipher.VMPC-KSA3", "org.bouncycastle2.jce.provider.symmetric.VMPCKSA3$Base");
      put("KeyGenerator.VMPC-KSA3", "org.bouncycastle2.jce.provider.symmetric.VMPCKSA3$KeyGen");
    }
  }
}