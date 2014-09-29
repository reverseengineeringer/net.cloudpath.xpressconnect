package org.bouncycastle2.jce.interfaces;

import java.security.PublicKey;

public abstract interface MQVPublicKey extends PublicKey
{
  public abstract PublicKey getEphemeralKey();

  public abstract PublicKey getStaticKey();
}