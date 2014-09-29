package org.bouncycastle2.crypto.engines;

public class SEEDWrapEngine extends RFC3394WrapEngine
{
  public SEEDWrapEngine()
  {
    super(new SEEDEngine());
  }
}