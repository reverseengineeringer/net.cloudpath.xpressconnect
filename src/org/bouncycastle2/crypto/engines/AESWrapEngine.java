package org.bouncycastle2.crypto.engines;

public class AESWrapEngine extends RFC3394WrapEngine
{
  public AESWrapEngine()
  {
    super(new AESEngine());
  }
}