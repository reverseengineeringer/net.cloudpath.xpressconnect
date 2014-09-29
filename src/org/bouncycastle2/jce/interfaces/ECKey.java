package org.bouncycastle2.jce.interfaces;

import org.bouncycastle2.jce.spec.ECParameterSpec;

public abstract interface ECKey
{
  public abstract ECParameterSpec getParameters();
}