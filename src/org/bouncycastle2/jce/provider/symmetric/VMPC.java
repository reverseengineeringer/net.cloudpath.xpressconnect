package org.bouncycastle2.jce.provider.symmetric;

import java.util.HashMap;
import org.bouncycastle2.crypto.CipherKeyGenerator;
import org.bouncycastle2.crypto.engines.VMPCEngine;
import org.bouncycastle2.crypto.macs.VMPCMac;
import org.bouncycastle2.jce.provider.JCEKeyGenerator;
import org.bouncycastle2.jce.provider.JCEMac;
import org.bouncycastle2.jce.provider.JCEStreamCipher;

public final class VMPC
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

  public static class Mac extends JCEMac
  {
    public Mac()
    {
      super();
    }
  }

  public static class Mappings extends HashMap
  {
    public Mappings()
    {
      put("Cipher.VMPC", "org.bouncycastle2.jce.provider.symmetric.VMPC$Base");
      put("KeyGenerator.VMPC", "org.bouncycastle2.jce.provider.symmetric.VMPC$KeyGen");
      put("Mac.VMPCMAC", "org.bouncycastle2.jce.provider.symmetric.VMPC$Mac");
      put("Alg.Alias.Mac.VMPC", "VMPCMAC");
      put("Alg.Alias.Mac.VMPC-MAC", "VMPCMAC");
    }
  }
}