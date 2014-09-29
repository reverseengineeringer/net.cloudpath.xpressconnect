package org.bouncycastle2.jce.interfaces;

import java.math.BigInteger;
import java.security.PublicKey;

public abstract interface ElGamalPublicKey extends ElGamalKey, PublicKey
{
  public abstract BigInteger getY();
}