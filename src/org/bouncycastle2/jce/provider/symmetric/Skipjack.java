package org.bouncycastle2.jce.provider.symmetric;

import java.util.HashMap;
import org.bouncycastle2.crypto.CipherKeyGenerator;
import org.bouncycastle2.crypto.engines.SkipjackEngine;
import org.bouncycastle2.crypto.macs.CBCBlockCipherMac;
import org.bouncycastle2.crypto.macs.CFBBlockCipherMac;
import org.bouncycastle2.jce.provider.JCEBlockCipher;
import org.bouncycastle2.jce.provider.JCEKeyGenerator;
import org.bouncycastle2.jce.provider.JCEMac;
import org.bouncycastle2.jce.provider.JDKAlgorithmParameters.IVAlgorithmParameters;

public final class Skipjack
{
  public static class AlgParams extends JDKAlgorithmParameters.IVAlgorithmParameters
  {
    protected String engineToString()
    {
      return "Skipjack IV";
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
      super(80, new CipherKeyGenerator());
    }
  }

  public static class Mac extends JCEMac
  {
    public Mac()
    {
      super();
    }
  }

  public static class MacCFB8 extends JCEMac
  {
    public MacCFB8()
    {
      super();
    }
  }

  public static class Mappings extends HashMap
  {
    public Mappings()
    {
      put("Cipher.SKIPJACK", "org.bouncycastle2.jce.provider.symmetric.Skipjack$ECB");
      put("KeyGenerator.SKIPJACK", "org.bouncycastle2.jce.provider.symmetric.Skipjack$KeyGen");
      put("AlgorithmParameters.SKIPJACK", "org.bouncycastle2.jce.provider.symmetric.Skipjack$AlgParams");
      put("Mac.SKIPJACKMAC", "org.bouncycastle2.jce.provider.symmetric.Skipjack$Mac");
      put("Alg.Alias.Mac.SKIPJACK", "SKIPJACKMAC");
      put("Mac.SKIPJACKMAC/CFB8", "org.bouncycastle2.jce.provider.symmetric.Skipjack$MacCFB8");
      put("Alg.Alias.Mac.SKIPJACK/CFB8", "SKIPJACKMAC/CFB8");
    }
  }
}