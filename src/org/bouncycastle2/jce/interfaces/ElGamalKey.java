package org.bouncycastle2.jce.interfaces;

import org.bouncycastle2.jce.spec.ElGamalParameterSpec;

public abstract interface ElGamalKey
{
  public abstract ElGamalParameterSpec getParameters();
}