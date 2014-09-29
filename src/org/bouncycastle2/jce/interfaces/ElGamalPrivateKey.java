package org.bouncycastle2.jce.interfaces;

import java.math.BigInteger;
import java.security.PrivateKey;

public abstract interface ElGamalPrivateKey extends ElGamalKey, PrivateKey
{
  public abstract BigInteger getX();
}