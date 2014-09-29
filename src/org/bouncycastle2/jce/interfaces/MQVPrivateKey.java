package org.bouncycastle2.jce.interfaces;

import java.security.PrivateKey;
import java.security.PublicKey;

public abstract interface MQVPrivateKey extends PrivateKey
{
  public abstract PrivateKey getEphemeralPrivateKey();

  public abstract PublicKey getEphemeralPublicKey();

  public abstract PrivateKey getStaticPrivateKey();
}