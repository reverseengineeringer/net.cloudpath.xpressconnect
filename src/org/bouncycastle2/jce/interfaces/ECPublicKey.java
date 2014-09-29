package org.bouncycastle2.jce.interfaces;

import java.security.PublicKey;
import org.bouncycastle2.math.ec.ECPoint;

public abstract interface ECPublicKey extends ECKey, PublicKey
{
  public abstract ECPoint getQ();
}